package com.artino.service.context;

public class RequestContext {
    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    /**
     *  set request info
     * @param requestInfo
     */
    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    /**
     * get request info
     * @return
     */
    public static RequestInfo get() {
        return threadLocal.get();
    }

    /**
     * remove request info
     */
    public static void remove() {
        threadLocal.remove();
    }
}
