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
     * @brief Metode que extreu el comportament comu de tots els handlers
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de l'excepcio
     */
    private String handleError(RuntimeException ex, HttpServletResponse response, int httpCode) {
        try {
            response.sendError(httpCode, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ex.getClass().getCanonicalName()+": " + ex.getMessage());
        return ex.getMessage();
    }

    /**
     * @brief Handler per capturar l'excepcio DriverNotFound
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de l'excepcio
     */
    @ExceptionHandler(DriverNotFound.class)
    public String handleDriverNotFound(DriverNotFound ex, HttpServletResponse response) {
        return handleError(ex,response,432);
    }

    /**
     * @brief Handler per capturar l'excepcio DriverVehicleAlreadyExists
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de l'excepcio
     */
    @ExceptionHandler(DriverVehicleAlreadyExists.class)
    public String handleDriverVehicleAlreadyExists(DriverVehicleAlreadyExists ex, HttpServletResponse response) {
        return handleError(ex,response,433);
    }

    /**
     * @brief Handler per capturar l'excepcio DriverVehicleNotFound
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de l'excepcio
     */
    @ExceptionHandler(DriverVehicleNotFound.class)
    public String handleDriverVehicleNotFound(DriverVehicleNotFound ex, HttpServletResponse response) {
        return handleError(ex,response,434);
    }

    /**
     * @brief Handler per capturar l'excepcio VehicleAlreadyExists
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de l'excepcio
     */
    @ExceptionHandler(VehicleAlreadyExists.class)
    public String handleVehicleAlreadyExists(VehicleAlreadyExists ex, HttpServletResponse response) {
        return handleError(ex,response,435);
    }

    /**
     * @brief Handler per capturar l'excepcio VehicleNotFound
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de l'excepcio
     */
    @ExceptionHandler(VehicleNotFound.class)
    public String handleVehicleNotFound(VehicleNotFound ex, HttpServletResponse response) {
        return handleError(ex,response,436);
    }

    /**
     * @brief Handler per capturar l'excepcio UserNotFound
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de l'excepcio
     */
    @ExceptionHandler(UserNotFound.class)
    public String handleUserNotFound(UserNotFound ex, HttpServletResponse response) {
        return handleError(ex, response, 437);
    }

    /**
     * @brief Handler per capturar l'excepcio UserAlreadyExists
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de l'excepcio
     */
    @ExceptionHandler(UserAlreadyExists.class)
    public String handleUserExists(UserAlreadyExists ex, HttpServletResponse response) {
        return handleError(ex,response,438);
    }

    /**
     * @brief Handler per capturar l'excepcio DriverAlreadyExists
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de l'excepcio
     */
    @ExceptionHandler(DriverAlreadyExists.class)
    public String handleDriverExists(DriverAlreadyExists ex, HttpServletResponse response) {
        return handleError(ex, response, 439);
    }

    /**
     * @brief Handler per capturar l'excepcio WrongVehicleInfo
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de l'excepcio
     */
    @ExceptionHandler(WrongVehicleInfo.class)
    public String handleWrongVehicleInfo(WrongVehicleInfo ex, HttpServletResponse response) {
        return handleError(ex, response, 440);
    }

    /**
     * @brief Handler per capturar l'excepcio DestinationNotReachable
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de l'excepcio
     */
    @ExceptionHandler(DestinationNotReachable.class)
    public String handleDestinationNotReachable(DestinationNotReachable ex, HttpServletResponse response) {
        return handleError(ex, response, 441);
    }

    /**
     * @brief Handler per capturar l'excepcio RatingNotFound
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de l'excepcio
     */
    @ExceptionHandler(RatingNotFound.class)
    public String handleRatingNotFound(RatingNotFound ex, HttpServletResponse response) {
        return handleError(ex, response, 442);
    }

    /**
     * @brief Handler per capturar l'excepcio ReportNotFound
     * @param ex Excepcio capturada
     * @param response Response http per setejar els codis d'error
     * @post Envia l'error http corresponent al client
     * @return Retorna l'error de l'excepcio
     */
    @ExceptionHandler(ReportNotFound.class)
    public String handleReportNotFound(ReportNotFound ex, HttpServletResponse response) {
        return handleError(ex, response, 445);
    }


}

