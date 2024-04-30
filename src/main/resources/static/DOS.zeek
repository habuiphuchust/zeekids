@load base/frameworks/sumstats
@load MyConfig.zeek

module DOS;

export {
    redef enum Notice::Type += {
    	TCP_SYN_FLUSH,
    	PING_OF_DEATH,
    };
}


# This event is handled at a priority higher than zero so that if
# users modify this stream in another script, they can do so at the
# default priority of zero.
event zeek_init() &priority=1
    {
    # Create the reducer.
    # The reducer attaches to the "conn attempted" observation stream
    # and uses the summing calculation on the observations. Keep
    # in mind that there will be one result per key (connection originator).
    local r1 = SumStats::Reducer($stream="modbus conn attempted", 
                                 $apply=set(SumStats::SUM));
                                 
    local r2 = SumStats::Reducer($stream="ping too big", 
                                 $apply=set(SumStats::SUM));

    # Create the final sumstat.
    # This is slightly different from the last example since we're providing
    # a callback to calculate a value to check against the threshold with 
    # $threshold_val.  The actual threshold itself is provided with $threshold.
    # Another callback is provided for when a key crosses the threshold.
    SumStats::create([$name = "finding dos",
                      $epoch = MyConfig::TCP_SYN_FLUSH_EPO,
                      $reducers = set(r1),
                      # Provide a threshold.
                      $threshold = MyConfig::TCP_SYN_FLUSH_THREADSHOLD,
                      # Provide a callback to calculate a value from the result
                      # to check against the threshold field.
                      $threshold_val(key: SumStats::Key, result: SumStats::Result) =
                        {
                        return result["modbus conn attempted"]$sum;
                        },
                      # Provide a callback for when a key crosses the threshold.
                      $threshold_crossed(key: SumStats::Key, result: SumStats::Result) =
                        {
                        NOTICE([
                        	$note=TCP_SYN_FLUSH, 
                        	$dst=key$host,
                        	$p=	MyConfig::MODBUS_PORT,
                        	$ts=network_time(), 
                        	$sub="tcp syn flush",
                        	$msg=fmt("attempted %.0f or more connections during %s on %s:%s", 
                        		result["modbus conn attempted"]$sum, 
                        		MyConfig::TCP_SYN_FLUSH_EPO,
                        		MyConfig::MODBUS_SLAVE_IP,
                        		MyConfig::MODBUS_PORT),
                        	]);
                        }]);
                        
        SumStats::create([$name = "finding dos 2",
                      $epoch = MyConfig::PING_OF_DEATH_EPO,
                      $reducers = set(r2),
                      # Provide a threshold.
                      $threshold = MyConfig::PING_OF_DEATH_THREADSHOLD,
                      # Provide a callback to calculate a value from the result
                      # to check against the threshold field.
                      $threshold_val(key: SumStats::Key, result: SumStats::Result) =
                        {
                        return result["ping too big"]$sum;
                        },
                      # Provide a callback for when a key crosses the threshold.
                      $threshold_crossed(key: SumStats::Key, result: SumStats::Result) =
                        {
                        NOTICE([
                        	$note=PING_OF_DEATH, 
                        	$dst=key$host,
                        	$ts=network_time(), 
                        	$sub="ping of death",
                        	$msg=fmt("received %.0f or more icmp request with big length during %s on %s", 
                        		result["ping too big"]$sum, 
                        		MyConfig::PING_OF_DEATH_EPO,
                        		MyConfig::MODBUS_SLAVE_IP),
                        	]);
                        }]);

    }
    
event connection_attempt(c: connection)
    {
    # Make an observation!
    # This observation is about the host attempting the connection.
    # Each established connection counts as one so the observation is always 1.
    if (c$id$resp_h == MyConfig::MODBUS_SLAVE_IP && c$id$resp_p == MyConfig::MODBUS_PORT)
    SumStats::observe("modbus conn attempted", 
                      SumStats::Key($host=MyConfig::MODBUS_SLAVE_IP), 
                      SumStats::Observation($num=1));
    }
    
event icmp_echo_request(c: connection, info: icmp_info, id: count, seq: count, payload: string){
	if (info$len > MyConfig::PING_MAX_LENGTH && c$id$resp_h == MyConfig::MODBUS_SLAVE_IP)
	    SumStats::observe("ping too big", 
                      SumStats::Key($host=MyConfig::MODBUS_SLAVE_IP), 
                      SumStats::Observation($num=1));
}
