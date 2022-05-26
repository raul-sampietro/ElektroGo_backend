package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.logs.CustomLogger;
import elektroGo.back.logs.logType;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.SQLOutput;

@Component
public class RunAfterStartup {

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        CustomLogger logger = CustomLogger.getInstance() ;
        logger.log("\nRunning startup methods...", logType.INFO);
        Database d = Database.getInstance();
        logger.log("End of running startup methods\n", logType.INFO);
    }
}
