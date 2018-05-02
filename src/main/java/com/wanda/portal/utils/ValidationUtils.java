package com.wanda.portal.utils;

import java.util.Date;

public class ValidationUtils {

    public static Date validateDate(Date x) {
        if (x == null) {
            x = new Date();
        }
        return new Date(x.getTime());
    }
}
