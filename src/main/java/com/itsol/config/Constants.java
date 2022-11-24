package com.itsol.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";
    public static final String CODE_REGEX = "[A-Z0-9_-]{1,255}";
    public static final String NAME_REGEX = "^[a-zA-Z-0-9()_-_ÀÁÂÃÈÉÊẾÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêếìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\ ]+$";
    public static final String DAY_REGEX = "^(0?[1-9]|[12][0-9]|3[01])$";
    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String REGEX_CODE_AND_NAME = "^[0-9]+ - [a-zA-Z-0-9()_-_ÀÁÂÃÈÉÊẾÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêếìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\ ]+$";
    public static final String NUMBER_BIGGER_O = "^((?!(0))[0-9]{1,10})$";
    public static final String NUMBER = "[0-9]{1,10}$";

    private Constants() {
    }

    public static final class Api {
        public static class Path {
            public static final String PREFIX = "/api";
        }
    }
    public static final class MessValidate{
        public static final String ERR_001 = "Trường thông tin đã tồn tại";
        public static final String ERR_002 = "Trường bắt buộc nhập";
        public static final String ERR_003 = "Chỉ cho phép nhập ký tự in hoa A-Z, chữ số 0-9,ký tự đặc biệt gồm:-,_";
        public static final String ERR_005 = "Chỉ được nhập ký tự số,tối đa 10 ký tự";
        public static final String ERR_004 = "Chỉ cho phép nhập ký tự tiếng việt có dấu A-Z,a-z,0-9,ký tự đặc biệt gồm:(,)-,_";

    }

}
