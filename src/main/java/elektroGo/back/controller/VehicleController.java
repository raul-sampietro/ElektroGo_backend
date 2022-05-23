/**
 * @file VehicleController.java
 * @author Daniel Pulido
 * @date 15/03/2022
 * @brief Implementació del Rest Controller de Vehicle.
 */

package elektroGo.back.controller;

import elektroGo.back.data.finders.FinderDriver;
import elektroGo.back.data.finders.FinderDriverVehicle;
import elektroGo.back.data.finders.FinderVehicle;
import elektroGo.back.data.gateways.GatewayDriverVehicle;
import elektroGo.back.data.gateways.GatewayVehicle;
import elektroGo.back.exceptions.*;
import elektroGo.back.logs.CustomLogger;
import elektroGo.back.logs.logType;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @brief La classe VehicleController mapeja els diferents metodes http de la classe Vehicle.
 */
@RequestMapping("/vehicles")
@RestController
public class VehicleController {

    private final CustomLogger logger = CustomLogger.getInstance();

    @GetMapping("")
    public ArrayList<GatewayVehicle> getVehicles(@RequestParam(required = false) Boolean notVerified) throws SQLException {
        if (notVerified) {
            logger.log("\nStarting getNotVerified method...", logType.TRACE);
            FinderVehicle fV = FinderVehicle.getInstance();
            ArrayList<GatewayVehicle> aL = fV.findNotVerified();
            String log = "Returning this vehicles:\n";
            for (GatewayVehicle gV : aL) log += gV.json() + "\n";
            logger.log(log + "End of method", logType.TRACE);
            return aL;
        }
        else {
            logger.log("\nStarting getAllVehicles method...", logType.TRACE);
            FinderVehicle fV = FinderVehicle.getInstance();
            ArrayList<GatewayVehicle> aL = fV.findAll();
            String log = "Returning this vehicles:\n";
            for (GatewayVehicle gV : aL) log += gV.json() + "\n";
            logger.log(log + "End of method", logType.TRACE);
            return aL;
        }
    }

    /**
     * @brief Metode per llegir un vehicle.
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @pre numberPlate no es null
     * @return Retorna l'objecte GatewayVehicle identificat per numberPlate
     */
    @GetMapping("/{numberPlate}")
    public GatewayVehicle readVehicle(@PathVariable String numberPlate) throws SQLException {
        logger.log("\nStarting readVehicle method with numberPlate '" + numberPlate + "' ...", logType.TRACE);
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        logger.log("Returning the vehicle " + gV.json() +"\nEnd of method", logType.TRACE);
        return gV;
    }

    /**
     * @brief Metode per eliminar un vehicle.
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @pre numberPlate no es null
     * @post Elimina el Vehicle identificat per numberPlate i les associacions que tenia amb drivers
     */
    @DeleteMapping("/{numberPlate}")
    public void deleteVehicle(@PathVariable String numberPlate) {
        logger.log("\nStarting deleteVehicle method...", logType.TRACE);
        logger.log("Vehicle that will be deleted is identified by " + numberPlate, logType.TRACE);
        FinderVehicle fV = FinderVehicle.getInstance();
        try {
            GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
            if (gV != null) {
                FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
                ArrayList<GatewayDriverVehicle> aL = fDV.findByNumberPlateV(numberPlate);
                for (GatewayDriverVehicle gDV : aL) gDV.remove();
                logger.log("Deleting image of the vehicle...", logType.TRACE);
                File fileToDelete = new File("../Images/vehicle-images/" + gV.getImageId());
                boolean success = fileToDelete.delete();
                if (success) logger.log("File was removed successfully", logType.TRACE);
                else logger.log("WARNING: File couldn't be removed", logType.TRACE);
                if (success) logger.log("File was removed succesfully", logType.TRACE);
                else logger.log("WARNING: File couldn't be removed", logType.TRACE);
                gV.remove();
            }
            else throw new VehicleNotFound(numberPlate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.log("End of method deleteVehicle.", logType.TRACE);
    }

    /**
     * @brief Metode per rebre la imatge del Vehicle identificat per numberPlate.
     * @param response resposta http on es posa la imatge
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @pre numberPlate es son null.
     * @post La imatge demanada esta al response, per tant el client la rep
     */
    @GetMapping("/{numberPlate}/image")
    public void getImage(HttpServletResponse response, @PathVariable String numberPlate) throws IOException, SQLException {
        logger.log("\nStarting getImage method", logType.TRACE);
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        InputStream in = new BufferedInputStream(new FileInputStream("../Images/vehicle-images/" + gV.getImageId()));
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
        logger.log("getImage method ended.", logType.TRACE);
    }

    /**
     * @brief Metode per posar imatge al Vehicle identificat per numberPlate.
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @param file Arxiu que conte la imatge.
     * @pre numberPlate i file no son null.
     * @post El Vehicle identificat per numberPlate te la imatge continguda a file.
     */
    @PostMapping("/{numberPlate}/image")
    public void setImage(@PathVariable String numberPlate  ,@RequestParam("image") MultipartFile file) throws IOException, SQLException {
        logger.log("\nSetting image with original filename '" + file.getOriginalFilename() + "' with size "+ file.getSize() + " bytes", logType.TRACE);
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = numberPlate+"."+extension;
        logger.log("New filename = " + fileName, logType.TRACE);
        gV.setImageId(fileName);
        gV.update();
        logger.log("Filename settled in Database", logType.TRACE);
        Path uploadPath = Paths.get("../Images/vehicle-images/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        else logger.log("WARNING: file with same filename already exists", logType.TRACE);
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.log("File saved correctly", logType.TRACE);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
        logger.log("End setting image", logType.TRACE);

    }

    @GetMapping("/from/{username}")
    public List<GatewayVehicle> readVehicles(@PathVariable String username) throws SQLException {
        return readVehiclesUser(username);
    }

    /**
     * @brief Metode per crear un Vehicle donat un json amb la informacio del vehicle excepte la imatge.
     * @param gV Objecte amb la informacio del vehicle.
     * @param username Username del driver del vehicle.
     * @pre gV i username no son null.
     * @post Es crea un nou Vehicle amb la informacio de gV en cas que no existeixi el vehicle i es relaciona amb el driver identificat amb username.
     */
    @PostMapping("/from/{username}")
    public void createVehicle(@RequestBody GatewayVehicle gV, @PathVariable String username) throws SQLException {
        logger.log("\nCreating vehicle, vehicle arrived with this information:" + gV.json() + "\nAnd with username of driver " + "'" +username+"'", logType.TRACE);
        FinderDriver fD = FinderDriver.getInstance();
        FinderVehicle fV = FinderVehicle.getInstance();
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        if (fDV.findByNumberPlateDriver(username, gV.getNumberPlate()) != null) throw new DriverVehicleAlreadyExists(username, gV.getNumberPlate());
        if (fD.findByUserName(username) == null) throw new DriverNotFound(username);
        GatewayVehicle gVComp = fV.findByNumberPlate(gV.getNumberPlate());
        if (gVComp == null) {
            logger.log("Vehicle didn't exists ,creating new vehicle...", logType.TRACE);
            if (gV.getVerification() == null) gV.setVerification("pending");
            gV.insert();
            logger.log("Vehicle created", logType.TRACE);
        }
        else {
            //Set gateway ImageId to null so json method of both gateways should return the same String
            //This change won't affect this Vehicle imageId in database
            gVComp.setImageId(null);
            if (!gV.json().equals(gVComp.json())) throw new WrongVehicleInfo(gV.getNumberPlate());
        }
        logger.log("Creating new relation with the vehicle identified by numberPlate = "+ gV.getNumberPlate() +
                " and driver identified by username = " + username, logType.TRACE);
        GatewayDriverVehicle gDV = new GatewayDriverVehicle(gV.getNumberPlate(), username);
        gDV.insert();
        logger.log("Relation inserted\nEnd creation vehicle method", logType.TRACE);
    }

    /**
     * @brief Metode per llegir els Vehicle de l'usuari "userName"
     * @param userName userName del driver del que es volen saber els seus vehicles
     * @pre userName no es null
     * @return Llistat de vehicles de l'usuari "userName"
     */
    //@GetMapping("/readVehicles")
    public List<GatewayVehicle> readVehiclesUser(String userName) throws SQLException {
        logger.log("\nStarting readVehicles method with userName '" + userName + "' ...", logType.TRACE);
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        final List <GatewayVehicle> l = fDV.findVehiclesByUser(userName);
        String log = "Returning the vehicles... (End of method)\n";
        for (GatewayVehicle gV: l ) log += gV.json() + "\n";
        logger.log(log + "End of method", logType.TRACE);
        return l;
    }

    public List<GatewayVehicle> readAllVehicles() throws SQLException {
        logger.log("\nStarting readAllVehicles method...", logType.TRACE);
        FinderVehicle fV = FinderVehicle.getInstance();
        ArrayList<GatewayVehicle> aL = fV.findAll();
        String log = "Returning this vehicles:\n";
        for (GatewayVehicle gV : aL) log += gV.json() + "\n";
        logger.log(log + "End of method", logType.TRACE);
        return aL;
    }

    /**
     * @brief Metode per eliminar un driver a un vehicle.
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @param username Username del driver
     * @pre numberPlate i userNDriver no son null
     * @post Elimina com a driver el driver identificat per userNDriver al vehicle identificat per numberPlate
     */
    @DeleteMapping("/{numberPlate}/from/{username}")
    public void removeDriverVehicle(@PathVariable String username, @PathVariable String numberPlate) {
        logger.log("\nInicianting the delete of the relation between vehicle with numberPlate '" + numberPlate + "' and" +
                "driver '" + username + "' ...", logType.TRACE);
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        FinderVehicle fV = FinderVehicle.getInstance();
        FinderDriver fD = FinderDriver.getInstance();
        try {
            if (fV.findByNumberPlate(numberPlate) == null) throw new VehicleNotFound(numberPlate);
            if (fD.findByUserName(username) == null) throw new DriverNotFound(username);
            GatewayDriverVehicle gDV = fDV.findByNumberPlateDriver(username, numberPlate);
            if ( gDV != null) {
                logger.log("Removing the relation mentioned before...", logType.TRACE);
                gDV.remove();
                logger.log("Relation removed", logType.TRACE);
                if (fDV.findByNumberPlateV(numberPlate).isEmpty() ) {
                    logger.log("Found that that relation was the last with the vehicle with number plate '" + numberPlate, logType.TRACE);
                    logger.log("Deleting that vehicle...", logType.TRACE);
                    GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
                    File fileToDelete = new File("../Images/vehicle-images/" + gV.getImageId());
                    boolean success = fileToDelete.delete();
                    if (success) logger.log("File was removed succesfully", logType.TRACE);
                    else logger.log("WARNING: File couldn't be removed", logType.WARN);
                    gV.remove();
                    logger.log("Vehicle removed.", logType.TRACE);
                }
            }
            else throw new DriverVehicleNotFound(username,numberPlate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.log("removeDriverVehicle method ended", logType.TRACE);

    }

    @PutMapping("/{numberPlate}/verify")
    public void verifyVehicle(@PathVariable String numberPlate) throws SQLException {
        logger.log("\nStarting verifyVehicle method with vehicle = '" + numberPlate + "' ...", logType.TRACE);
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        logger.log("Actual state of verification attribute is " + gV.getVerification(), logType.TRACE);
        gV.verify();
        gV.update();
        gV = fV.findByNumberPlate(numberPlate);
        logger.log("Now, the vehicle has this attributes (end of method)", logType.TRACE);
        logger.log(gV.json(), logType.TRACE);
    }
}
