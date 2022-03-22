/**
 * @file RestControllerAdvice.java
 * @author Daniel Pulido
 * @date 19/03/2022
 * @brief Implementacio del Rest Controller Advice per els codis d'error http.
 */

package elektroGo.back.controller;

import elektroGo.back.exceptions.*;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * @brief Classe del Rest Controller Advice la qual captura les excepcions i envia els codis d'error corresponents
 */
@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    /**
     * @brief Handler per capturar la excepcio DriverNotFound
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de la excepcio
     */
    @ExceptionHandler(DriverNotFound.class)
    public String handleDriverNotFound(DriverNotFound ex, HttpServletResponse response) {
        try {
            response.sendError(432, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex.getMessage();
    }

    /**
     * @brief Handler per capturar la excepcio DriverVehicleAlreadyExists
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de la excepcio
     */
    @ExceptionHandler(DriverVehicleAlreadyExists.class)
    public String handleDriverVehicleAlreadyExists(DriverVehicleAlreadyExists ex, HttpServletResponse response) {
        try {
            response.sendError(433, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex.getMessage();
    }

    /**
     * @brief Handler per capturar la excepcio DriverVehicleNotFound
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de la excepcio
     */
    @ExceptionHandler(DriverVehicleNotFound.class)
    public String handleDriverVehicleNotFound(DriverVehicleNotFound ex, HttpServletResponse response) {
        try {
            response.sendError(434, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex.getMessage();
    }

    /**
     * @brief Handler per capturar la excepcio VehicleAlreadyExists
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de la excepcio
     */
    @ExceptionHandler(VehicleAlreadyExists.class)
    public String handleVehicleAlreadyExists(VehicleAlreadyExists ex, HttpServletResponse response) {
        try {
            response.sendError(435, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex.getMessage();
    }

    /**
     * @brief Handler per capturar la excepcio VehicleNotFound
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de la excepcio
     */
    @ExceptionHandler(VehicleNotFound.class)
    public String handleVehicleNotFound(VehicleNotFound ex, HttpServletResponse response) {
        try {
            response.sendError(436, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex.getMessage();
    }

    @ExceptionHandler(UserNotFound.class)
    public String handleUserNotFound(UserNotFound ex, HttpServletResponse response) {
        try {
            response.sendError(437, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex.getMessage();
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public String handleUserExists(UserAlreadyExists ex, HttpServletResponse response) {
        try {
            response.sendError(438, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex.getMessage();
    }

    @ExceptionHandler(DriverAlreadyExists.class)
    public String handleDriverExists(DriverAlreadyExists ex, HttpServletResponse response) {
        try {
            response.sendError(439, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex.getMessage();
    }

}

