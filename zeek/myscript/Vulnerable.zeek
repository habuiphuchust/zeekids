module Signature;

export {
    redef enum Notice::Type += {
    	RESET,
    };
}

event Modbus::log_modbus(rec: Modbus::Info){
if (rec$func == "RESET_COMM_LINK_884_U84")
	# print rec;
    NOTICE([$note=RESET, $src=rec$id$orig_h, $dst=rec$id$resp_h, $msg="detect reset function"]);
}