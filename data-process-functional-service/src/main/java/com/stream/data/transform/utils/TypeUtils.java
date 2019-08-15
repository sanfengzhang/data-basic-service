package com.stream.data.transform.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author: Hanl
 * @date :2019/8/8
 * @desc:
 */
public class TypeUtils {

    public static final String STRING = "String";

    public static final String INT = "Integer";

    public static final String FLOAT = "Float";

    public static final String DOUBLE = "Double";

    public static final String LONG = "Long";

    public static final String SHORT = "Short";

    public static final String TIME = "time";

    public static final String TIME_FORMAT = "format";

    public static String string(Object o) {
        return o == null ? null : o.toString();
    }

    public static Object convert(String string, String targetType, String format) {
        switch (targetType) {
            case INT:
                return Integer.parseInt(string);
            case FLOAT:
                return Float.parseFloat(string);
            case DOUBLE:
                return Double.parseDouble(string);
            case LONG:
                return Long.parseLong(string);
            case SHORT:
                return Short.parseShort(string);
            case TIME:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                try {
                    return simpleDateFormat.parse(string);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
        }

        throw new NullPointerException();
    }

    public static Object convert(Object o) {
        if (null == o) {

            return null;
        }
        switch (o.getClass().getSimpleName()) {
            case INT:
                return Integer.parseInt(o.toString());
            case FLOAT:
                return Float.parseFloat(o.toString());
            case DOUBLE:
                return Double.parseDouble(o.toString());
            case LONG:
                return Long.parseLong(o.toString());
            case SHORT:
                return Short.parseShort(o.toString());
            case STRING:
                return o.toString();

        }

        throw new NullPointerException();
    }


}
