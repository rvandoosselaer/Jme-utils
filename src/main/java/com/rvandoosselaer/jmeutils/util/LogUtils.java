package com.rvandoosselaer.jmeutils.util;

import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * A container for static utility methods related to logging.
 *
 * @author rvandoosselaer
 */
public class LogUtils {

    public static void forwardJULToSlf4j() {
        // enable all JUL levels
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.ALL);

        // disable all rootLogger handlers
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        // Adds a SLF4JBridgeHandler instance to jul's root logger
        SLF4JBridgeHandler.install();
    }

}
