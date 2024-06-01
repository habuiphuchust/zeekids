# module detect scan port attack
@load ./ScanPort.zeek
# module detect scan to modbus port
@load ./ScanModbus.zeek
# module detect modbus injection
@load ./ModbusInjection.zeek
# module detect DOS attack
@load ./DOS.zeek
# module detect harmful function, ip address of modbus
@load ./ModbusDetection.zeek
# module detect convert channel
@load ./zeek-modbus-cc
#