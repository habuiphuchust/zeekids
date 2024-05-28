@load ./MyConfig.zeek
module ModbusDetection;

export {
    redef enum Notice::Type += {
    	FUNCTION,
    	IP_STRANGER,
    };
}

event Modbus::log_modbus(rec: Modbus::Info){
    switch rec$func {
        case "RESET_COMM_LINK_884_U84":
            NOTICE([$note=FUNCTION, $src=rec$id$orig_h, $dst=rec$id$resp_h, $msg="detect reset function"]);
            break;
        case "DIAGNOSTICS":
            NOTICE([$note=FUNCTION, $src=rec$id$orig_h, $dst=rec$id$resp_h, $msg="detect diagnostics function"]);
            break;
        case "REPORT_SLAVE_ID":
            NOTICE([$note=FUNCTION, $src=rec$id$orig_h, $dst=rec$id$resp_h, $msg="detect report slave id function"]);
            break;
        case "ENCAP_INTERFACE_TRANSPORT":
            NOTICE([$note=FUNCTION, $src=rec$id$orig_h, $dst=rec$id$resp_h, $msg="detect read device identification function"]);
            break;
        default:
            break;
    }
# ip stranger
  if (rec$id$orig_h !in MyConfig::MODBUS_MASTER_IP && rec$id$resp_h in MyConfig::MODBUS_SLAVE_IP)
     NOTICE([$note=IP_STRANGER, $src=rec$id$orig_h, $dst=rec$id$resp_h, $msg="detect strange ip address connecting to modbus"]);
}