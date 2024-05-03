@load ./MyConfig.zeek
module ModbusInjection;

export {
    redef enum Notice::Type += {
    	Detect_Retransmission,
    };
}

event connection_EOF(c: connection, is_orig: bool) {
	if (c$id$resp_p == MyConfig::MODBUS_PORT && "T" in c$history && c$id$resp_h == MyConfig::MODBUS_SLAVE_IP) {
		NOTICE([$note=	Detect_Retransmission, $conn=c, $msg="suspicous packet injection", $sub=fmt("history: %s", c$history)]);
	}
}
