package elektroGo.back.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomLogger {

    private static CustomLogger singletonObject;

    private CustomLogger() {
        logger = LoggerFactory.getLogger(CustomLogger.class);
    };

    public static CustomLogger getInstance() {
        if (singletonObject == null) {
            singletonObject = new CustomLogger();
        }
        return singletonObject;
    }


    private final Logger logger;

    public void log(String message, logType type) {
        switch (type) {
            case TRACE:
                logger.trace(message);
                break;
            case DEBUG:
                logger.debug(message);
                break;
            case INFO:
                logger.info(message);
                break;
            case WARN:
                logger.warn(message);
                break;
            case ERROR:
                logger.error(message);
                break;
        }

    }



}
