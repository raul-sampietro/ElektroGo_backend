/**
 * @file RatingNotFound.java
 * @author Daniel Pulido
 * @date 12/04/2022
 * @brief Implementació de l'excepcio RatingNotFound
 */
package elektroGo.back.exceptions;

/**
 * @brief La classe RatingNotFound implementa una RuntimeException
 */
public class ReportNotFound extends RuntimeException {
    public ReportNotFound() {
        super();
    }
    public ReportNotFound(String userWhoReports, String reportedUser) {
        super("Report with userWhoReports: '" + userWhoReports +"' and reportedUser: '" + reportedUser + "' wasn't found");
    }
}
