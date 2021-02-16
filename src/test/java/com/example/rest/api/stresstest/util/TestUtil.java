package com.example.rest.api.stresstest.util;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;


public class TestUtil {
    private static final Logger LOG = LoggerFactory.getLogger(TestUtil.class);

    private static ThreadLocal<StopWatch> clzStopWatch = new ThreadLocal<>();
    private static ThreadLocal<StopWatch> stopWatch = new ThreadLocal<>();
    private static final String YMD_PATTERN = "yyyy-MM-dd";
    public static final String TEST_MDC_KEY = "TestMtd";
    private static final Marker METHOD_STARTED = MarkerFactory.getMarker("MthStr");
    private static final Marker METHOD_END = MarkerFactory.getMarker("MthEnd");

    public static void logException(final Throwable e) {
        LOG.error("error:", e);
        if (e instanceof AssertionError) {
            throw (AssertionError) e;
        }
        throw new RuntimeException(e);
    }

    public static LocInfo printStart() {
        final LocInfo locInfo = getLocation();
        LOG.info(METHOD_STARTED, locInfo.toString() + " ====== starting ... ======");
        final StopWatch sw = StopWatch.createStarted();
        stopWatch.set(sw);
        return locInfo;
    }

    public static void printEnd() {
        final LocInfo locInfo = getLocation();
        final StopWatch sw = stopWatch.get();
        stopWatch.remove();
        sw.stop();
        LOG.info(METHOD_END, locInfo.toString() + " ====== Ended. Took [{}]     ======", sw);
    }

    public static LocInfo printClassStart() {
        final LocInfo locInfo = getLocation();
        LOG.info(locInfo.toString() + " ====== starting class... ======");
        final StopWatch sw = StopWatch.createStarted();
        clzStopWatch.set(sw);
        return locInfo;
    }

    public static void printClassEnd() {
        final LocInfo locInfo = getLocation();
        final StopWatch sw = clzStopWatch.get();
        clzStopWatch.remove();
        sw.stop();
        LOG.info(locInfo.toString() + " ====== Ended class. Took [{}]     ======", sw);
    }

    public static LocInfo getLocation() {
        final StackTraceElement[] steArr = Thread.currentThread().getStackTrace();
        LocInfo loc = null;
        for (StackTraceElement ste : steArr) {
            final String classFullName = ste.getClassName();
            if (TestUtil.class.getName().equals(classFullName) || Thread.class.getName().equals(classFullName)) {
                continue;
            }
            final String clsName = getClassSimpleName(classFullName);
            final String mtdName = ste.getMethodName();
            final int lineNum = ste.getLineNumber();
            loc = new LocInfo(clsName, mtdName, lineNum);
            // LOG.debug("{}.{}:{}", clsName, mtdName, lineNum);
            break;
        }
        return loc;
    }

    private static String getClassSimpleName(final String classFullName) {
        final String result;
        if (classFullName == null) {
            result = null;
        } else if (classFullName.trim().length() == 0) {
            result = "";
        } else {
            final int docIdx = classFullName.lastIndexOf(".");
            if (docIdx >= 0) {
                result = classFullName.substring(docIdx + 1);
            } else {
                result = classFullName;
            }
        }
        return result;
    }

    /*
     * *****************************************************************************
     */
    public static class LocInfo {
        private final String clsName;
        private final String mtdName;
        private final int lineNum;
        private boolean stop = false;

        public LocInfo(final String clsName, final String mtdName, final int lineNum) {
            super();
            this.clsName = clsName;
            this.mtdName = mtdName;
            this.lineNum = lineNum;
        }

        public String getClsName() {
            return clsName;
        }

        public String getMtdName() {
            return mtdName;
        }

        public int getLineNum() {
            return lineNum;
        }

        public String getClzMtdInfo() {
            return clsName + "." + mtdName;
        }

        /**
         * @return the stop
         */
        public boolean isStop() {
            return stop;
        }

        /**
         * @param stop the stop to set
         */
        protected LocInfo setStop(final boolean stop) {
            this.stop = stop;
            return this;
        }

        @Override
        public String toString() {
            final String s = String.format("LocInfo [clsName=%s, mtdName=%s, lineNum=%s, stop=%s]", clsName, mtdName,
                    lineNum, stop);
            return s;
        }

    }
}

