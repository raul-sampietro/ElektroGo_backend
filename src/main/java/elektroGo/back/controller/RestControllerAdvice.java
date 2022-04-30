/**
 * @file RestControllerAdvice.java
 * @author Daniel Pulido i Gerard Castell
 * @date 19/03/2022
 * @brief Implementacio del Rest Controller Advice per els codis d'error http.
 */

package elektroGo.back.controller;

import elektroGo.back.exceptions.*;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * @brief La classe RestControllerAdvice captura les excepcions i envia els codis d'error corresponents
 */
@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Metode que extreu el comportament comu de tots els handlers
     * @post Envia l'error http corresponent al client
     */
    private String handleError(RuntimeException ex, HttpServletResponse response, int httpCode) {
        try {
            response.sendError(httpCode, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ex.getClass().getCanonicalName() + ": " + ex.getMessage());
        return ex.getMessage();
    }

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio DriverNotFound
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(DriverNotFound.class)
    public String handleDriverNotFound(DriverNotFound ex, HttpServletResponse response) {
        return handleError(ex, response, 432);
    }

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio DriverVehicleAlreadyExists
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(DriverVehicleAlreadyExists.class)
    public String handleDriverVehicleAlreadyExists(DriverVehicleAlreadyExists ex, HttpServletResponse response) {
        return handleError(ex, response, 433);
    }

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio DriverVehicleNotFound
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(DriverVehicleNotFound.class)
    public String handleDriverVehicleNotFound(DriverVehicleNotFound ex, HttpServletResponse response) {
        return handleError(ex, response, 434);
    }

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio VehicleAlreadyExists
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(VehicleAlreadyExists.class)
    public String handleVehicleAlreadyExists(VehicleAlreadyExists ex, HttpServletResponse response) {
        return handleError(ex, response, 435);
    }

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio VehicleNotFound
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(VehicleNotFound.class)
    public String handleVehicleNotFound(VehicleNotFound ex, HttpServletResponse response) {
        return handleError(ex, response, 436);
    }

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio UserNotFound
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(UserNotFound.class)
    public String handleUserNotFound(UserNotFound ex, HttpServletResponse response) {
        return handleError(ex, response, 437);
    }

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio UserAlreadyExists
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(UserAlreadyExists.class)
    public String handleUserExists(UserAlreadyExists ex, HttpServletResponse response) {
        return handleError(ex, response, 438);
    }

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio DriverAlreadyExists
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(DriverAlreadyExists.class)
    public String handleDriverExists(DriverAlreadyExists ex, HttpServletResponse response) {
        return handleError(ex, response, 439);
    }

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio WrongVehicleInfo
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(WrongVehicleInfo.class)
    public String handleWrongVehicleInfo(WrongVehicleInfo ex, HttpServletResponse response) {
        return handleError(ex, response, 440);
    }

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio DestinationNotReachable
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(DestinationNotReachable.class)
    public String handleDestinationNotReachable(DestinationNotReachable ex, HttpServletResponse response) {
        return handleError(ex, response, 441);
    }

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio TripNotFound
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(TripNotFound.class)
    public String handleTripNotFound(TripNotFound ex, HttpServletResponse response) {
        return handleError(ex, response, 443);
    }

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio TripAlreadyExists
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(TripAlreadyExists.class)
    public String handleTripAlreadyExists(TripAlreadyExists ex, HttpServletResponse response) {
        return handleError(ex, response, 444);
    }

    /**
     * @param ex       Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio InvKey
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(InvalidKey.class)
    public String handleInvalidKey(InvalidKey ex, HttpServletResponse response) {
        return handleError(ex, response, 446);

    }

    /**
     * @param ex UserTripNotFound Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio UserTripNotFound
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(UserTripNotFound.class)
    public String handleUserTripNotFound(UserTripNotFound ex, HttpServletResponse response) {
        return handleError(ex, response, 447);
    }


    /**
     * @param ex UserTripAlreadyExists Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @return Retorna l'error de l'excepcio
     * @brief Handler per capturar l'excepcio UserTripNotFound
     * @post Envia l'error http corresponent al client
     */
    @ExceptionHandler(UserTripAlreadyExists.class)
    public String handleUserTripAlreadyExists(UserTripAlreadyExists ex, HttpServletResponse response) {
        return handleError(ex, response, 448);
    }
}

