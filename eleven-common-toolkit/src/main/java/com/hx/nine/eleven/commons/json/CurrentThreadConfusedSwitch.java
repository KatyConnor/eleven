//package com.hx.nine.eleven.commons.json;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//public class CurrentThreadConfusedSwitch {
//    private static Logger logger = LoggerFactory.getLogger(CurrentThreadConfusedSwitch.class);
//    private static final String SESSION_KEY = "_e_g_k";
//    private static ThreadLocal<Boolean> cached = new ThreadLocal();
//
//    public CurrentThreadConfusedSwitch() {
//    }
//
//
//    private static void updateSwitchStateToSession(boolean v, HttpServletRequest request) {
//        HttpSession session = request.getSession(true);
//        session.setAttribute("_e_g_k", v);
//    }
//
//    public static void turnOnConfusedSwitch(HttpServletRequest request) {
//        if (request == null) {
//            logger.error("request is null! could not turn on the switch!");
//        } else {
//            updateSwitchStateToSession(true, request);
//        }
//
//    }
//
//    public static void turnOffConfusedSwitch(HttpServletRequest request) {
//        if (request == null) {
//            logger.error("request is null! could not trun off the switch!");
//        } else {
//            updateSwitchStateToSession(false, request);
//        }
//
//    }
//
//    public static void holdSwitchState(HttpServletRequest request) {
//        boolean b = internal_getSwitchStateFromSession(request);
//        internal_setSwitchState(b);
//    }
//
//    public static boolean getSwitchState() {
//        return cached.get() != null && (Boolean)cached.get();
//    }
//
//    private static boolean internal_getSwitchStateFromSession(HttpServletRequest request) {
//        HttpSession session = request.getSession(true);
//        Boolean session_confused = (Boolean)session.getAttribute("_e_g_k");
//        return session_confused != null && session_confused;
//    }
//
//    private static void internal_setSwitchState(boolean value) {
//        cached.set(value);
//    }
//}
