package elektroGo.back;

import elektroGo.back.data.Database;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RunAfterStartup {

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        System.out.println("\nRunning startup methods...");
        Database d = Database.getInstance();
        System.out.println("End of running startup methods\n");
    }
}
