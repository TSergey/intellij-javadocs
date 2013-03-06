package com.github.setial.intellijjavadocs.template.logging;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.log4j.Level;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;

public class IntellijPluginLogSystem implements LogChute {

    public static final String RUNTIME_LOG_LEVEL_KEY = "runtime.log.logsystem.intellij.plugin.level";

    public static final String TRACE_LEVEL = "trace";
    public static final String DEBUG_LEVEL = "debug";
    public static final String INFO_LEVEL  = "info";
    public static final String WARN_LEVEL  = "warn";
    public static final String ERROR_LEVEL = "error";

    private Logger logger;
    private Level logLevel;

    @Override
    public void init(RuntimeServices rs) throws Exception {
        String level = (String) rs.getProperty(RUNTIME_LOG_LEVEL_KEY);
        logger = PluginManager.getLogger();
        logLevel = Level.toLevel(level);
        logger.setLevel(logLevel);
    }

    @Override
    public void log(int level, String message) {
        if (!isLevelEnabled(level)) {
            return;
        }
        switch (level) {
            case LogChute.WARN_ID:
                logger.warn(message);
                break;
            case LogChute.INFO_ID:
                logger.info(message);
                break;
            case LogChute.TRACE_ID:
                logger.debug(message);
                break;
            case LogChute.ERROR_ID:
                logger.error(message);
                break;
            case LogChute.DEBUG_ID:
            default:
                logger.debug(message);
                break;
        }
    }

    @Override
    public void log(int level, String message, Throwable t) {
        if (!isLevelEnabled(level)) {
            return;
        }
        switch (level) {
            case LogChute.WARN_ID:
                logger.warn(message, t);
                break;
            case LogChute.INFO_ID:
                logger.info(message, t);
                break;
            case LogChute.TRACE_ID:
                logger.debug(message, t);
                break;
            case LogChute.ERROR_ID:
                logger.error(message, t);
                break;
            case LogChute.DEBUG_ID:
            default:
                logger.debug(message, t);
                break;
        }
    }

    @Override
    public boolean isLevelEnabled(int level) {
        return logLevel.getSyslogEquivalent() == level;
    }

}
