/**
 * @file ReportController.java
 * @author Raül Sampietro
 * @date 15/5/2022
 * @brief Implementació de la classe ReportController
 */

package elektroGo.back.controller;

import elektroGo.back.data.finders.FinderReport;
import elektroGo.back.data.finders.FinderUser;
import elektroGo.back.data.gateways.GatewayReport;
import elektroGo.back.exceptions.ReportNotFound;
import elektroGo.back.exceptions.UserNotFound;
import elektroGo.back.logs.CustomLogger;
import elektroGo.back.logs.logType;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;

@RequestMapping("/reports")
@RestController
public class ReportController {
    private final CustomLogger logger = CustomLogger.getInstance();

    @PostMapping("")
    public void reportUser(@RequestBody GatewayReport gR) throws SQLException {
        logger.log("\nStarting reportUser method with report:\n" + gR.json() , logType.TRACE);
        FinderUser fU = FinderUser.getInstance();
        if ( fU.findByUsername(gR.getUserWhoReports()) == null) throw new UserNotFound(gR.getUserWhoReports());
        if ( fU.findByUsername(gR.getReportedUser()) == null) throw new UserNotFound(gR.getReportedUser());
        FinderReport fR = FinderReport.getInstance();
        //Report doesn't already exist
        if (fR.findByPrimaryKey(gR.getUserWhoReports(), gR.getReportedUser()) == null) {
            logger.log("Report doesn't already exist, creating new report...", logType.TRACE);
            gR.insert();
            logger.log("Report created, end of method", logType.TRACE);
        }
        //Report exists
        else {
            logger.log("Report already exists, updating report...", logType.TRACE);
            gR.update();
            logger.log("Report updated, end of method", logType.TRACE);
        }
    }

    @DeleteMapping("")
    public void unreportUser(@RequestParam String userWhoReports, @RequestParam String reportedUser) throws SQLException {
        logger.log("\nStarting unreportUser method with userWhoReports: '" + userWhoReports + "' and reportedUser: '" + reportedUser + "'", logType.TRACE);
        FinderReport fR = FinderReport.getInstance();
        GatewayReport gR = fR.findByPrimaryKey(userWhoReports, reportedUser);
        if (gR == null) throw new ReportNotFound(userWhoReports, reportedUser);
        gR.remove();
        if (fR.findByPrimaryKey(userWhoReports, reportedUser) == null) logger.log("Report removed successfully, end of method", logType.TRACE);
        else logger.log("ERROR, couldn't delete the report", logType.ERROR);
    }
}
