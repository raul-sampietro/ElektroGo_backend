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
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;

@RequestMapping("/reports")
@RestController
public class ReportController {

    @PostMapping("")
    public void reportUser(@RequestBody GatewayReport gR) throws SQLException {
        System.out.println("\nStarting reportUser method with report:");
        System.out.println(gR.json());
        FinderUser fU = FinderUser.getInstance();
        if ( fU.findByUsername(gR.getUserWhoReports()) == null) throw new UserNotFound(gR.getUserWhoReports());
        if ( fU.findByUsername(gR.getReportedUser()) == null) throw new UserNotFound(gR.getReportedUser());
        FinderReport fR = FinderReport.getInstance();
        //Report doesn't already exist
        if (fR.findByPrimaryKey(gR.getUserWhoReports(), gR.getReportedUser()) == null) {
            System.out.println("Report doesn't already exist, creating new report...");
            gR.insert();
            System.out.println("Report created, end of method");
        }
        //Report exists
        else {
            System.out.println("Report already exists, updating report...");
            gR.update();
            System.out.println("Report updated, end of method");
        }
    }

    @DeleteMapping("")
    public void unreportUser(@RequestParam String userWhoReports, @RequestParam String reportedUser) throws SQLException {
        System.out.println("\nStarting unreportUser method with userWhoReports: '" + userWhoReports + "' and reportedUser: '" + reportedUser + "'");
        FinderReport fR = FinderReport.getInstance();
        GatewayReport gR = fR.findByPrimaryKey(userWhoReports, reportedUser);
        if (gR == null) throw new ReportNotFound(userWhoReports, reportedUser);
        gR.remove();
        if (fR.findByPrimaryKey(userWhoReports, reportedUser) == null) System.out.println("Report removed successfully, end of method");
        else System.out.println("ERROR, couldn't delete the report");
    }
}
