@load base/frameworks/sumstats
@load ./MyConfig.zeek

module ScanPort;

export {
    redef enum Notice::Type += {
    	Scan_Port,
    };
    redef record SumStats::Key += {
           host2:    addr    &default=192.168.1.1;
    };
}


# This event is handled at a priority higher than zero so that if
# users modify this stream in another script, they can do so at the
# default priority of zero.
event zeek_init() &priority=5
    {
    print "load ScanPort";
    # Create the reducer.
    # The reducer attaches to the "conn attempted" observation stream
    # and uses the summing calculation on the observations. Keep
    # in mind that there will be one result per key (connection originator).
    local r1 = SumStats::Reducer($stream="conn attempted", 
                                 $apply=set(SumStats::SUM));

    # Create the final sumstat.
    # This is slightly different from the last example since we're providing
    # a callback to calculate a value to check against the threshold with 
    # $threshold_val.  The actual threshold itself is provided with $threshold.
    # Another callback is provided for when a key crosses the threshold.
    SumStats::create([$name = "finding scanners",
                      $epoch = MyConfig::SCANPORT_EPO,
                      $reducers = set(r1),
                      # Provide a threshold.
                      $threshold = MyConfig::SCANPORT_THREADSHOLD,
                      # Provide a callback to calculate a value from the result
                      # to check against the threshold field.
                      $threshold_val(key: SumStats::Key, result: SumStats::Result) =
                        {
                        return result["conn attempted"]$sum;
                        },
                      # Provide a callback for when a key crosses the threshold.
                      $threshold_crossed(key: SumStats::Key, result: SumStats::Result) =
                        {
                        NOTICE([
                        	$note=Scan_Port, 
                        	$src=key$host,
                        	$dst=key$host2,
                        	$ts=network_time(),
                        	$msg="scan port"
                        ]);
                        }]);
    }
    
event connection_attempt(c: connection)
    {
    # Make an observation!
    # This observation is about the host attempting the connection.
    # Each established connection counts as one so the observation is always 1.
    if (c$id$resp_h in MyConfig::MODBUS_SLAVE_IP)
    SumStats::observe("conn attempted", 
                      SumStats::Key($host=c$id$orig_h, $host2=c$id$resp_h),
                      SumStats::Observation($num=1));
    }
