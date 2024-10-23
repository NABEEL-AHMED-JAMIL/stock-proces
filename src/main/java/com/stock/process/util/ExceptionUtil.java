package com.stock.process.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nabeel Ahmed
 */
public class ExceptionUtil {

    public Logger logger = LogManager.getLogger(ExceptionUtil.class);

    /**
     * Method use to get the root cause
     * @param throwable
     * @return Throwable
     * */
    public static Throwable getRootCause(final Throwable throwable) {
        final List<Throwable> list = getThrowableList(throwable);
        Throwable rootCause = list.size() < 2 ? null : (Throwable) list.get(list.size() - 1);
        if (rootCause == null) {
            return throwable;
        }
        return rootCause;
    }

    /**
     * Method use to get root cause message
     * @param throwable
     * @return String
     * */
    public static String getRootCauseMessage(final Throwable throwable) {
        Throwable root = getRootCause(throwable);
        return root.toString();
    }

    /**
     * Method use to get throwable list
     * @param throwable
     * @return List<Throwable>
     * */
    private static List<Throwable> getThrowableList(Throwable throwable) {
        final List<Throwable> list = new ArrayList<Throwable>();
        while (throwable != null && !list.contains(throwable)) {
            list.add(throwable);
            throwable = throwable.getCause();
        }
        return list;
    }

}