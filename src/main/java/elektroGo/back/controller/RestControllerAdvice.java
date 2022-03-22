package elektroGo.back.controller;

import elektroGo.back.exceptions.*;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(DriverNotFound.class)
    public String handleDriverNotFound(DriverNotFound ex, HttpServletResponse response) {
        try {
            response.sendError(432, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex.getMessage();
    }

    @ExceptionHandler(DriverVehicleAlreadyExists.class)
    public String handleDriverVehicleAlreadyExists(DriverVehicleAlreadyExists ex, HttpServletResponse response) {
        try {
            response.sendError(433, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex.getMessage();
    }

    @ExceptionHandler(DriverVehicleNotFound.class)
    public String handleDriverVehicleNotFound(DriverVehicleNotFound ex, HttpServletResponse response) {
        try {
            response.sendError(434, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex.getMessage();
    }

    @ExceptionHandler(VehicleAlreadyExists.class)
    public String handleVehicleAlreadyExists(VehicleAlreadyExists ex, HttpServletResponse response) {
        try {
            response.sendError(435, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ex.getMessage();
    }

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

}

