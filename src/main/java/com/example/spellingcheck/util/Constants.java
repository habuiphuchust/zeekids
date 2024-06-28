package com.example.spellingcheck.util;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_DAY = "60";
    public static final String SORT_DIRECTION_ASC = "asc";
    public static final String SORT_DIRECTION_DESC = "desc";
    public static final String DEFAULT_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PW_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    public static final String PHONE_NUMBER_PATTERN = "(0[3|5|7|8|9])+([0-9]{8})\\b";
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@\" + \"[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$";
    public static final String ZEEK_CONFIG_PATH = "./zeek/myscript/";

    public static final String[] ZEEK_START_COMMAND = {"bash", "-c", "cd zeek && zeek -i enp0s8 local myscript"};
    public static final String[] ZEEK_CHECK_STATUS = {"bash", "-c", "ps -ef | grep zeek | grep -i enp0s8 | grep -v grep"};
    public static final String[] ZEEK_STOP = {"bash", "-c", "/bin/kill -9 $(ps -ef | grep socat | grep -i enp0s8 |  awk '{print $2}')"};
    public static final String[] STORE_LOG = {"bash", "-c", "cp zeek/*.log $(mkdir zeek/$(date +%Y-%m-%d_%H-%M-%S) && echo zeek/$(date +%Y-%m-%d_%H-%M-%S))"};

}
