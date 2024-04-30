module MyConfig;

export {
	const MODBUS_SLAVE_IP = 192.168.56.1;
	const SCANPORT_EPO = 5min;
	const SCANPORT_THREADSHOLD = 20.0;
	const MODBUS_PORT = 502/tcp;
	const TCP_SYN_FLUSH_EPO = 5min;
	const TCP_SYN_FLUSH_THREADSHOLD = 2000.0;
	const PING_MAX_LENGTH = 32;
	const PING_OF_DEATH_EPO = 5min;
	const PING_OF_DEATH_THREADSHOLD = 200.0;
}
