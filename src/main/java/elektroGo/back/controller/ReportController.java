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
import java.util.List;

@RequestMapping("/reports")
@RestController
public class ReportController {
    private final CustomLogger logger = CustomLogger.getInstance();

    @GetMapping("")
    public List<GatewayReport> allReports() throws SQLException {
        logger.log("\nStarting allReports method...", logType.TRACE);
        FinderReport fR = FinderReport.getInstance();
        List<GatewayReport> l= fR.findAll();
        String log = "Returning this reports: \n";
        for (GatewayReport g : l) log += g.json() + "\n";
        logger.log(log + "End of method", logType.TRACE);
        return fR.findAll();
    }

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

    @GetMapping("/from/{username}")
    public List<GatewayReport> getReportsMadeUser(@PathVariable String username) throws SQLException {
        logger.log("\nStarting getReportsMadeUser method with username : '" + username + "'", logType.TRACE);
        FinderReport fR = FinderReport.getInstance();
        FinderUser fU = FinderUser.getInstance();
        if ( fU.findByUsername(username) == null) throw new UserNotFound(username);
        List<GatewayReport> l = fR.findByUserWhoReports(username);
        String log = "Returning reports made by user with username: '" + username + "' that are:" ;
        for (GatewayReport g : l) log += g.json() + "\n";
        logger.log(log + "End of method", logType.TRACE);
        return l;
    }

    @GetMapping("/to/{username}")
    public List<GatewayReport> getReportsReceivedUser(@PathVariable String username) throws SQLException {
        logger.log("\nStarting reportsReceivedUser method with username : '" + username + "'", logType.TRACE);
        FinderReport fR = FinderReport.getInstance();
        FinderUser fU = FinderUser.getInstance();
        if ( fU.findByUsername(username) == null) throw new UserNotFound(username);
        List<GatewayReport> l = fR.findByReportedUser(username);
        String log = "Returning reports received by user with username: '" + username + "' that are:" ;
        for (GatewayReport g : l) log += g.json() + "\n";
        logger.log(log + "End of method", logType.TRACE);
        return l;
    }

    @DeleteMapping("/from/{userFrom}/to/{userTo}")
    public void unreportUser(@PathVariable String userFrom, @PathVariable String userTo) throws SQLException {
        logger.log("\nStarting unreportUser method with userFrom: '" + userFrom + "' and userTo: '" + userTo + "'", logType.TRACE);
        FinderReport fR = FinderReport.getInstance();
        GatewayReport gR = fR.findByPrimaryKey(userFrom, userTo);
        if (gR == null) throw new ReportNotFound(userFrom, userTo);
        gR.remove();
        if (fR.findByPrimaryKey(userFrom, userTo) == null) logger.log("Report removed successfully, end of method", logType.TRACE);
        else logger.log("ERROR, couldn't delete the report", logType.ERROR);
    }
}
