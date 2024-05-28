@load ./MyConfig.zeek
module ScanModbus;

export {
    redef enum Notice::Type += {
    	Scan_Port_To_Modbus,
    };
}

event connection_state_remove(c: connection)
    {
    if (c$id$resp_p == MyConfig::MODBUS_PORT && !c$conn?$service && c$id$resp_h in MyConfig::MODBUS_SLAVE_IP && c$history != "S")
    	# Log::write(ScanModbus::LOG, [$ts=c$conn$ts, $id=c$id, $service="modbus"]);
    	# print c$id;
    	NOTICE([$note=Scan_Port_To_Modbus, $conn=c, $msg="scan modbus port"]);
    
    }
