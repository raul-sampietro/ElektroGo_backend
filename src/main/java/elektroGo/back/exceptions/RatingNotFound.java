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
public class RatingNotFound extends RuntimeException {
    public RatingNotFound() {
        super();
    }
    public RatingNotFound(String userWhoRates, String ratedUser) {
        super("Rating with userWhoRates: '" + userWhoRates +"' and ratedUser: '" + ratedUser + "' wasn't found");
    }
}
