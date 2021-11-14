package com.bitoasis.ticker.aop;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class LoggingHelperV2 {

    private static final String EQUALSTO = "=";
    private static final String SEPARATOR = ",";
    private static final String EXCEPTION = "Exception";
    private static final int OBJ_COUNT_TO_DISPLAY = 10;
    private static final int MAX_CHARS_IN_OBJ = 1000;

    private LoggingHelperV2() {

    }

    public static String logRegular(Map<String, Object> map) {
        return getLogMsg(map).toString();
    }


    public static String logRegular(Map<String, Object> map,
                                    Throwable t) {
        StringBuilder msg = getLogMsg(map);
        msg.append(SEPARATOR);
        msg.append(EXCEPTION);
        msg.append(EQUALSTO);
        msg.append(t);
        return msg.toString();

    }

    private static StringBuilder getLogMsg(Map<String, Object> map) {
        StringBuilder string = new StringBuilder();
        Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            string.append(entry.getKey());
            string.append(EQUALSTO);
            string.append(entry.getValue() == null ? "" : getString(entry.getValue()));
            string.append(SEPARATOR);
        }
        string.deleteCharAt(string.length() - 1);
        return string;

    }

    private static String getString(Object object) {
        StringBuilder sBuilder = new StringBuilder();
        if ((object instanceof String) || (object instanceof Number) || (object instanceof Boolean)) {
            return object.toString();
        } else if (object instanceof Collection) {
            appendCollection(object, sBuilder);
        } else if (object instanceof Map) {
            appendMap(object, sBuilder);
        } else if (object instanceof Exception) {
            sBuilder.append(((Exception) object).getMessage());
        } else if (null != object) {
            int logLength = object.toString().length() < MAX_CHARS_IN_OBJ ? object.toString().length() : MAX_CHARS_IN_OBJ;
            sBuilder.append(object.toString().substring(0, logLength));
        } else {
            sBuilder.append(object);
        }
        return sBuilder.toString();
    }


    private static void appendMap(Object object,
                                  StringBuilder sBuilder) {
        Iterator<?> i = ((Map<?, ?>) object).keySet().iterator();
        int size = ((Map<?, ?>) object).size();
        sBuilder.append("(");
        sBuilder.append("size");
        sBuilder.append(EQUALSTO);
        sBuilder.append(size);
        sBuilder.append(")");
        sBuilder.append("{");
        int count = 0;
        while (i.hasNext()) {
            count++;
            Object obj = i.next();
            sBuilder.append(getString(obj));
            sBuilder.append(EQUALSTO);
            sBuilder.append(getString(((Map<?, ?>) object).get(obj) == null ? "" : getString(((Map<?, ?>) object).get(obj))));
            sBuilder.append(SEPARATOR);
            if ((count >= OBJ_COUNT_TO_DISPLAY) || (sBuilder.length() > MAX_CHARS_IN_OBJ)) {
                sBuilder.append(" and more...");
                break;
            }
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("}");
    }


    private static void appendCollection(Object object,
                                         StringBuilder sBuilder) {
        Iterator<?> i = ((Collection<?>) object).iterator();
        int size = ((Collection<?>) object).size();
        sBuilder.append("(");
        sBuilder.append("size");
        sBuilder.append(EQUALSTO);
        sBuilder.append(size);
        sBuilder.append(")");
        sBuilder.append("{");
        int count = 0;
        while (i.hasNext()) {
            count++;
            sBuilder.append(getString(i.next()));
            sBuilder.append(SEPARATOR);
            if ((count >= OBJ_COUNT_TO_DISPLAY) || (sBuilder.length() >= MAX_CHARS_IN_OBJ)) {
                sBuilder.append(" and more...");
                break;
            }
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("}");
    }
}