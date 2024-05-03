package com.example.spellingcheck.util;

public class Constants {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_DAY = "60";
    public static final String SORT_DIRECTION_ASC = "asc";
    public static final String SORT_DIRECTION_DESC = "desc";
    public static final String DEFAULT_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    public static final String PHONE_NUMBER_PATTERN = "(0[3|5|7|8|9])+([0-9]{8})\\b";
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@\" + \"[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$";

    public static final String ALLOWED_ORIGIN = "http://localhost:5173";
    public static final String ZEEK_CONFIG_PATH = "./zeek/myscript/MyConfig.zeek";

    public static final String[] ZEEK_START_COMMAND = {"bash", "-c", "cd zeek && zeek -i enp0s8 myscript"};

}
