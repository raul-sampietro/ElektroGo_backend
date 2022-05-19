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
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    /**
     * @brief Metode per crear un Vehicle donat un json amb la informacio del vehicle excepte la imatge.
     * @param gV Objecte amb la informacio del vehicle.
     * @param userNDriver Username del driver del vehicle.
     * @pre gV i userNDriver no son null.
     * @post Es crea un nou Vehicle amb la informacio de gV en cas que no existeixi el vehicle i es relaciona amb el driver identificat amb userNDriver.
     */
    @PostMapping("/create")
    public void createVehicle(@RequestBody GatewayVehicle gV, @RequestParam String userNDriver) throws SQLException {
        logger.log("\nCreating vehicle, vehicle arrived with this information:" + gV.json() + "\nAnd with username of driver " + "'" +userNDriver+"'");
        FinderDriver fD = FinderDriver.getInstance();
        FinderVehicle fV = FinderVehicle.getInstance();
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        if (fDV.findByNumberPlateDriver(userNDriver, gV.getNumberPlate()) != null) throw new DriverVehicleAlreadyExists(userNDriver, gV.getNumberPlate());
        if (fD.findByUserName(userNDriver) == null) throw new DriverNotFound(userNDriver);
        GatewayVehicle gVComp = fV.findByNumberPlate(gV.getNumberPlate());
        if (gVComp == null) {
            logger.log("Vehicle didn't exists ,creating new vehicle...");
            gV.insert();
            logger.log("Vehicle created");
        }
        else {
            //Set gateway ImageId to null so json method of both gateways should return the same String
            //This change won't affect this Vehicle imageId in database
            gVComp.setImageId(null);
            if (!gV.json().equals(gVComp.json())) throw new WrongVehicleInfo(gV.getNumberPlate());
        }
        logger.log("Creating new relation with the vehicle identified by numberPlate = "+ gV.getNumberPlate() +
                " and driver identified by username = " + userNDriver);
        GatewayDriverVehicle gDV = new GatewayDriverVehicle(gV.getNumberPlate(), userNDriver);
        gDV.insert();
        logger.log("Relation inserted");
        logger.log("End creation vehicle method");
    }

    /**
     * @brief Metode per posar imatge al Vehicle identificat per numberPlate.
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @param file Arxiu que conte la imatge.
     * @pre numberPlate i file no son null.
     * @post El Vehicle identificat per numberPlate te la imatge continguda a file.
     */
    @PostMapping("/setImage")
    public void setImage(@RequestParam String numberPlate  ,@RequestParam("image") MultipartFile file) throws IOException, SQLException {
        logger.log("\nSetting image with original filename '" + file.getOriginalFilename() + "' with size "+ file.getSize() + " bytes");
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = numberPlate+"."+extension;
        logger.log("New filename = " + fileName);
        gV.setImageId(fileName);
        gV.update();
        logger.log("Filename settled in Database");
        Path uploadPath = Paths.get("../Images/vehicle-images/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        else logger.log("WARNING: file with same filename already exists");
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.log("File saved correctly");
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
        logger.log("End setting image");

    }

    /**
     * @brief Metode per llegir els Vehicle de l'usuari "userName"
     * @param userName userName del driver del que es volen saber els seus vehicles
     * @pre userName no es null
     * @return Llistat de vehicles de l'usuari "userName"
     */
    @GetMapping("/readVehicles")
    public List<GatewayVehicle> readVehicles(@RequestParam String userName) throws SQLException {
        logger.log("\nStarting readVehicles method with userName '" + userName + "' ...");
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        logger.log("Returning the vehicles... (End of method)");
        return fDV.findVehiclesByUser(userName);
    }

    /**
     * @brief Metode per rebre la imatge del Vehicle identificat per numberPlate.
     * @param response resposta http on es posa la imatge
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @pre numberPlate es son null.
     * @post La imatge demanada esta al response, per tant el client la rep
     */
    @GetMapping("/getImage")
    public void getImage(HttpServletResponse response, @RequestParam String numberPlate) throws IOException, SQLException {
        logger.log("\nStarting getImage method");
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        InputStream in = new BufferedInputStream(new FileInputStream("../Images/vehicle-images/" + gV.getImageId()));
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
        logger.log("getImage method ended.");
    }

    /**
     * @brief Metode per afegir un driver a un vehicle.
     * @param nPVehicle Matricula del Vehicle que l'identifica.
     * @param userDriver Username del driver
     * @pre nPVehicle i userNDriver no son null
     * @post Afegeix com a driver el driver identificat per userNDriver al vehicle identificat per nPVehicle
     */
    @PostMapping("/addDriverVehicle")
    public void addDriverVehicle(@RequestParam String nPVehicle, @RequestParam String userDriver) throws SQLException {
        logger.log("\nStarting addDriverVehicle method...");
        GatewayDriverVehicle gDV = new GatewayDriverVehicle(nPVehicle,userDriver);
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        FinderVehicle fV = FinderVehicle.getInstance();
        FinderDriver fD = FinderDriver.getInstance();
        if (fV.findByNumberPlate(nPVehicle) == null) throw new VehicleNotFound(nPVehicle);
        if (fD.findByUserName(userDriver) == null) throw new DriverNotFound(userDriver);
        if (fDV.findByNumberPlateDriver(userDriver, nPVehicle) != null) throw new DriverVehicleAlreadyExists(userDriver, nPVehicle);
        gDV.insert();
        logger.log("addDriverVehicle method ended");
    }

    /**
     * @brief Metode per eliminar un driver a un vehicle.
     * @param nPVehicle Matricula del Vehicle que l'identifica.
     * @param userDriver Username del driver
     * @pre nPVehicle i userNDriver no son null
     * @post Elimina com a driver el driver identificat per userNDriver al vehicle identificat per nPVehicle
     */
    @PostMapping("/deleteDriverVehicle")
    public void removeDriverVehicle(@RequestParam String nPVehicle, @RequestParam String userDriver) {
        logger.log("\nInicianting the delete of the relation between vehicle with numberPlate '" + nPVehicle + "' and" +
                "driver '" + userDriver + "' ...");
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        FinderVehicle fV = FinderVehicle.getInstance();
        FinderDriver fD = FinderDriver.getInstance();
        try {
            if (fV.findByNumberPlate(nPVehicle) == null) throw new VehicleNotFound(nPVehicle);
            if (fD.findByUserName(userDriver) == null) throw new DriverNotFound(userDriver);
            GatewayDriverVehicle gDV = fDV.findByNumberPlateDriver(userDriver, nPVehicle);
            if ( gDV != null) {
                logger.log("Removing the relation mentioned before...");
                gDV.remove();
                logger.log("Relation removed");
                if (fDV.findByNumberPlateV(nPVehicle).isEmpty() ) {
                    logger.log("Found that that relation was the last with the vehicle with number plate '" + nPVehicle);
                    logger.log("Deleting that vehicle...");
                    GatewayVehicle gV = fV.findByNumberPlate(nPVehicle);
                    File fileToDelete = new File("../Images/vehicle-images/" + gV.getImageId());
                    boolean success = fileToDelete.delete();
                    if (success) logger.log("File was removed succesfully");
                    else logger.log("WARNING: File couldn't be removed");
                    gV.remove();
                    logger.log("Vehicle removed.");
                }
            }
            else throw new DriverVehicleNotFound(userDriver,nPVehicle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.log("removeDriverVehicle method ended");

    }

    /**
     * @brief Metode per llegir un vehicle.
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @pre numberPlate no es null
     * @return Retorna l'objecte GatewayVehicle identificat per numberPlate
     */
    @GetMapping("/read")
    public GatewayVehicle readVehicle(@RequestParam String numberPlate) throws SQLException {
        logger.log("\nStarting readVehicle method with numberPlate '" + numberPlate + "' ...");
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        logger.log("Returning the vehicle (end of method)");
        return gV;
    }

    /**
     * @brief Metode per eliminar un vehicle.
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @pre numberPlate no es null
     * @post Elimina el Vehicle identificat per numberPlate i les associacions que tenia amb drivers
     */
    @PostMapping("/delete")
    public void deleteVehicle(@RequestParam String numberPlate) {
        logger.log("\nIntiating deleteVehicle method...");
        logger.log("Vehicle that will be deleted is identified by " + numberPlate);
        FinderVehicle fV = FinderVehicle.getInstance();
        try {
            GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
            if (gV != null) {
                FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
                ArrayList<GatewayDriverVehicle> aL = fDV.findByNumberPlateV(numberPlate);
                for (GatewayDriverVehicle gDV : aL) gDV.remove();
                logger.log("Deleting image of the vehicle...");
                File fileToDelete = new File("../Images/vehicle-images/" + gV.getImageId());
                boolean success = fileToDelete.delete();
                if (success) logger.log("File was removed succesfully");
                else logger.log("WARNING: File couldn't be removed");
                gV.remove();
            }
            else throw new VehicleNotFound(numberPlate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.log("End of method deleteVehicle.");
    }


}
