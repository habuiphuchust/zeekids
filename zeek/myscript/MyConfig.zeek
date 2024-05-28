module MyConfig;

export {
# ip address of slaves
	const MODBUS_SLAVE_IP = set(192.168.56.1, 192.168.56.2);
# ip address of master
	const MODBUS_MASTER_IP = set(192.168.56.106);
# epo of scan port attack
	const SCANPORT_EPO = 5min;
# threshold of scan port attack
	const SCANPORT_THREADSHOLD = 20.0;
# port of modbus service
	const MODBUS_PORT = 502/tcp;
# epo of tcp syn flush attack
	const TCP_SYN_FLUSH_EPO = 5min;
# threshold of tcp syn flush attack
	const TCP_SYN_FLUSH_THREADSHOLD = 2000.0;
# max size of packet icmp
	const PING_MAX_LENGTH = 32;
# epo of ping of death attack
	const PING_OF_DEATH_EPO = 5min;
# threshold of ping of death attack
	const PING_OF_DEATH_THREADSHOLD = 200.0;
# threshold of ping of dns amplification attack
	const DNS_AMPLIFICATION_THREADSHOLD = 200.0;
# epo of ping of dns amplification attack
	const DNS_AMPLIFICATION_EPO = 5min;
}
