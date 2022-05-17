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
public class VehicleController {

    /**
     * @brief Metode per crear un Vehicle donat un json amb la informacio del vehicle excepte la imatge.
     * @param gV Objecte amb la informacio del vehicle.
     * @param userNDriver Username del driver del vehicle.
     * @pre gV i userNDriver no son null.
     * @post Es crea un nou Vehicle amb la informacio de gV en cas que no existeixi el vehicle i es relaciona amb el driver identificat amb userNDriver.
     */
    @PostMapping("/drivers/{username}/vehicles")
    public void createVehicle(@RequestBody GatewayVehicle gV, @PathVariable String userNDriver) throws SQLException {
        System.out.println("\nCreating vehicle, vehicle arrived with this information:" + gV.json() + "\nAnd with username of driver " + "'" +userNDriver+"'");
        FinderDriver fD = FinderDriver.getInstance();
        FinderVehicle fV = FinderVehicle.getInstance();
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        if (fDV.findByNumberPlateDriver(userNDriver, gV.getNumberPlate()) != null) throw new DriverVehicleAlreadyExists(userNDriver, gV.getNumberPlate());
        if (fD.findByUserName(userNDriver) == null) throw new DriverNotFound(userNDriver);
        GatewayVehicle gVComp = fV.findByNumberPlate(gV.getNumberPlate());
        if (gVComp == null) {
            System.out.println("Vehicle didn't exists ,creating new vehicle...");
            if (gV.getVerification() == null) gV.setVerification("pending");
            gV.insert();
            System.out.println("Vehicle created");
        }
        else {
            //Set gateway ImageId to null so json method of both gateways should return the same String
            //This change won't affect this Vehicle imageId in database
            gVComp.setImageId(null);
            if (!gV.json().equals(gVComp.json())) throw new WrongVehicleInfo(gV.getNumberPlate());
        }
        System.out.println("Creating new relation with the vehicle identified by numberPlate = "+ gV.getNumberPlate() +
                " and driver identified by username = " + userNDriver);
        GatewayDriverVehicle gDV = new GatewayDriverVehicle(gV.getNumberPlate(), userNDriver);
        gDV.insert();
        System.out.println("Relation inserted");
        System.out.println("End creation vehicle method");
    }

    /**
     * @brief Metode per posar imatge al Vehicle identificat per numberPlate.
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @param file Arxiu que conte la imatge.
     * @pre numberPlate i file no son null.
     * @post El Vehicle identificat per numberPlate te la imatge continguda a file.
     */
    @PutMapping("/vehicles/{numberPlate}/image")
    public void setImage(@PathVariable String numberPlate  ,@RequestParam("image") MultipartFile file) throws IOException, SQLException {
        System.out.println("\nSetting image with original filename '" + file.getOriginalFilename() + "' with size "+ file.getSize() + " bytes");
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = numberPlate+"."+extension;
        System.out.println("New filename = " + fileName);
        gV.setImageId(fileName);
        gV.update();
        System.out.println("Filename settled in Database");
        Path uploadPath = Paths.get("../Images/vehicle-images/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        else System.out.println("WARNING: file with same filename already exists");
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File saved correctly");
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
        System.out.println("End setting image");

    }

    /**
     * @brief Metode per llegir els Vehicle de l'usuari "userName"
     * @param userName userName del driver del que es volen saber els seus vehicles
     * @pre userName no es null
     * @return Llistat de vehicles de l'usuari "userName"
     */
    //@GetMapping("/readVehicles")
    public List<GatewayVehicle> readVehiclesUser(String userName) throws SQLException {
        System.out.println("\nStarting readVehicles method with userName '" + userName + "' ...");
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        System.out.println("Returning the vehicles... (End of method)");
        return fDV.findVehiclesByUser(userName);
    }

    public List<GatewayVehicle> readAllVehicles() throws SQLException {
        System.out.println("\nStartin readAllVehicles method...");
        FinderVehicle fV = FinderVehicle.getInstance();
        ArrayList<GatewayVehicle> aL = fV.findAll();
        System.out.println("Returning this vehicles...");
        for (GatewayVehicle gV : aL) System.out.println(gV.json());
        System.out.println("End of method");
        return aL;
    }

    @GetMapping("/drivers/{username}/vehicles")
    public List<GatewayVehicle> readVehicles(@PathVariable String username) throws SQLException {
        return readVehiclesUser(username);
    }

    /**
     * @brief Metode per rebre la imatge del Vehicle identificat per numberPlate.
     * @param response resposta http on es posa la imatge
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @pre numberPlate es son null.
     * @post La imatge demanada esta al response, per tant el client la rep
     */
    @GetMapping("/vehicles/{numberPlate}/image")
    public void getImage(HttpServletResponse response, @PathVariable String numberPlate) throws IOException, SQLException {
        System.out.println("\nStarting getImage method");
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        InputStream in = new BufferedInputStream(new FileInputStream("../Images/vehicle-images/" + gV.getImageId()));
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
        System.out.println("getImage method ended.");
    }

    /**
     * @brief Metode per eliminar un driver a un vehicle.
     * @param nPVehicle Matricula del Vehicle que l'identifica.
     * @param userDriver Username del driver
     * @pre nPVehicle i userNDriver no son null
     * @post Elimina com a driver el driver identificat per userNDriver al vehicle identificat per nPVehicle
     */
    @DeleteMapping("/drivers/{userDriver}/vehicles/{nPVehicle}")
    public void removeDriverVehicle(@PathVariable String nPVehicle, @PathVariable String userDriver) {
        System.out.println("\nInicianting the delete of the relation between vehicle with numberPlate '" + nPVehicle + "' and" +
                "driver '" + userDriver + "' ...");
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        FinderVehicle fV = FinderVehicle.getInstance();
        FinderDriver fD = FinderDriver.getInstance();
        try {
            if (fV.findByNumberPlate(nPVehicle) == null) throw new VehicleNotFound(nPVehicle);
            if (fD.findByUserName(userDriver) == null) throw new DriverNotFound(userDriver);
            GatewayDriverVehicle gDV = fDV.findByNumberPlateDriver(userDriver, nPVehicle);
            if ( gDV != null) {
                System.out.println("Removing the relation mentioned before...");
                gDV.remove();
                System.out.println("Relation removed");
                if (fDV.findByNumberPlateV(nPVehicle).isEmpty() ) {
                    System.out.println("Found that that relation was the last with the vehicle with number plate '" + nPVehicle);
                    System.out.println("Deleting that vehicle...");
                    GatewayVehicle gV = fV.findByNumberPlate(nPVehicle);
                    File fileToDelete = new File("../Images/vehicle-images/" + gV.getImageId());
                    boolean success = fileToDelete.delete();
                    if (success) System.out.println("File was removed succesfully");
                    else System.out.println("WARNING: File couldn't be removed");
                    gV.remove();
                    System.out.println("Vehicle removed.");
                }
            }
            else throw new DriverVehicleNotFound(userDriver,nPVehicle);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("removeDriverVehicle method ended");

    }

    /**
     * @brief Metode per llegir un vehicle.
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @pre numberPlate no es null
     * @return Retorna l'objecte GatewayVehicle identificat per numberPlate
     */
    @GetMapping("/vehicles/{numberPlate}")
    public GatewayVehicle readVehicle(@PathVariable String numberPlate) throws SQLException {
        System.out.println("\nStarting readVehicle method with numberPlate '" + numberPlate + "' ...");
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        System.out.println("Returning the vehicle (end of method)");
        return gV;
    }

    /**
     * @brief Metode per eliminar un vehicle.
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @pre numberPlate no es null
     * @post Elimina el Vehicle identificat per numberPlate i les associacions que tenia amb drivers
     */
    @DeleteMapping("/vehicles/{numberPlate}")
    public void deleteVehicle(@PathVariable String numberPlate) {
        System.out.println("\nStarting deleteVehicle method...");
        System.out.println("Vehicle that will be deleted is identified by " + numberPlate);
        FinderVehicle fV = FinderVehicle.getInstance();
        try {
            GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
            if (gV != null) {
                FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
                ArrayList<GatewayDriverVehicle> aL = fDV.findByNumberPlateV(numberPlate);
                for (GatewayDriverVehicle gDV : aL) gDV.remove();
                System.out.println("Deleting image of the vehicle...");
                File fileToDelete = new File("../Images/vehicle-images/" + gV.getImageId());
                boolean success = fileToDelete.delete();
                if (success) System.out.println("File was removed successfully");
                else System.out.println("WARNING: File couldn't be removed");
                gV.remove();
            }
            else throw new VehicleNotFound(numberPlate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("End of method deleteVehicle.");
    }

    @PutMapping("/vehicles/verify/{numberPlate}")
    public void verifyVehicle(@PathVariable String numberPlate) throws SQLException {
        System.out.println("\nStarting verifyVehicle method with vehicle = '" + numberPlate + "' ...");
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        System.out.println("Actual state of verification attribute is " + gV.getVerification());
        gV.verify();
        gV.update();
        gV = fV.findByNumberPlate(numberPlate);
        System.out.println("Now, the vehicle has this attributes (end of method)");
        System.out.println(gV.json());
    }

    @GetMapping("/vehicles/notVerified")
    public ArrayList<GatewayVehicle> getNotVerified() throws SQLException {
        System.out.println("\nStarting getNotVerified method...");
        FinderVehicle fV = FinderVehicle.getInstance();
        ArrayList<GatewayVehicle> aL = fV.findNotVerified();
        System.out.println("Returning this vehicles... (end of method)");
        for (GatewayVehicle gV : aL) System.out.println(gV.json());
        return aL;
    }


}
