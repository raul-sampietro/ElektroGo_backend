/**
 * @file WrongVehicleInfo.java
 * @author Daniel Pulido
 * @date 12/03/2022
 * @brief Implementació de l'excepcio WrongVehicleInfo
 */

package elektroGo.back.exceptions;

/**
 * @brief La classe WrongVehicleInfo implementa una RuntimeException
 */
public class WrongVehicleInfo  extends RuntimeException{
    public WrongVehicleInfo(){
        super();
    }
    public WrongVehicleInfo(String  numberPlate) {
        super("Vehicle with number plate "+  numberPlate + " already exists but with other information");
    }
}
