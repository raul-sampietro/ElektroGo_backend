/**
 * @file VehicleController.java
 * @author Daniel Pulido
 * @date 15/03/2022
 * @brief Implementació del Rest Controller de Vehicle.
 */

package elektroGo.back.controller;

import elektroGo.back.data.Finders.FinderDriver;
import elektroGo.back.data.Finders.FinderDriverVehicle;
import elektroGo.back.data.Finders.FinderVehicle;
import elektroGo.back.data.Gateways.GatewayDriverVehicle;
import elektroGo.back.data.Gateways.GatewayVehicle;
import elektroGo.back.exceptions.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;

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
     * @post Es crea un nou Vehicle amb la informacio de gV i es relaciona amb el driver identificat amb userNDriver.
     */
    @PostMapping("/create")
    public void createVehicle(@RequestBody GatewayVehicle gV, @RequestParam String userNDriver) throws SQLException {
        FinderDriver fD = FinderDriver.getInstance();
        FinderVehicle fV = FinderVehicle.getInstance();
        if (fV.findByNumberPlate(gV.getNumberPlate()) != null) throw new VehicleAlreadyExists(gV.getNumberPlate());
        if (fD.findByUserName(userNDriver) != null) {
            gV.insert();
            GatewayDriverVehicle gDV = new GatewayDriverVehicle(gV.getNumberPlate(), userNDriver);
            gDV.insert();
        }
        else throw new DriverNotFound(userNDriver);

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
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = numberPlate+"."+extension;
        gV.setImageId(fileName);
        gV.update();
        Path uploadPath = Paths.get("src/main/resources/images/vehicle-images/");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }

    }

    /*
    @GetMapping("/getImage")
    public void getImage(HttpServletResponse response) throws IOException {
        FileInputStream fIS = new FileInputStream("src/main/resources/images/vehicle-images/TestV.png");
        InputStream in = new BufferedInputStream(new FileInputStream("user-photos/TestV.png"));
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }*/

    /**
     * @brief Metode per afegir un driver a un vehicle.
     * @param nPVehicle Matricula del Vehicle que l'identifica.
     * @param userDriver Username del driver
     * @pre nPVehicle i userNDriver no son null
     * @post Afegeix com a driver el driver identificat per userNDriver al vehicle identificat per nPVehicle
     */
    @PostMapping("/addDriverVehicle")
    public void addDriverVehicle(@RequestParam String nPVehicle, @RequestParam String userDriver) throws SQLException {
        GatewayDriverVehicle gDV = new GatewayDriverVehicle(nPVehicle,userDriver);
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        FinderVehicle fV = FinderVehicle.getInstance();
        FinderDriver fD = FinderDriver.getInstance();
        if (fV.findByNumberPlate(nPVehicle) == null) throw new VehicleNotFound(nPVehicle);
        if (fD.findByUserName(userDriver) == null) throw new DriverNotFound(userDriver);
        if (fDV.findByNumberPlateDriver(userDriver, nPVehicle) != null) throw new DriverVehicleAlreadyExists(userDriver, nPVehicle);
        gDV.insert();

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
        FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
        FinderVehicle fV = FinderVehicle.getInstance();
        FinderDriver fD = FinderDriver.getInstance();
        try {
            if (fV.findByNumberPlate(nPVehicle) == null) throw new VehicleNotFound(nPVehicle);
            if (fD.findByUserName(userDriver) == null) throw new DriverNotFound(userDriver);
            GatewayDriverVehicle gDV = fDV.findByNumberPlateDriver(userDriver, nPVehicle);
            if ( gDV != null)
                gDV.remove();
            else throw new DriverVehicleNotFound(userDriver,nPVehicle);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * @brief Metode per llegir un vehicle.
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @pre numberPlate no es null
     * @return Retorna l'objecte GatewayVehicle identificat per numberPlate
     */
    @GetMapping("/read")
    public GatewayVehicle readVehicle(@RequestParam String numberPlate) throws SQLException {
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        else return gV;

    }

    /**
     * @brief Metode per eliminar un vehicle.
     * @param numberPlate Matricula del Vehicle que l'identifica.
     * @pre numberPlate no es null
     * @post Elimina el Vehicle identificat per numberPlate
     */
    @PostMapping("/delete")
    public void deleteVehicle(@RequestParam String numberPlate) {
        FinderVehicle fV = FinderVehicle.getInstance();
        try {
            GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
            if (gV != null) {
                System.out.println(gV.json());
                FinderDriverVehicle fDV = FinderDriverVehicle.getInstance();
                ArrayList<GatewayDriverVehicle> aL = fDV.findByNumberPlateV(numberPlate);
                for (GatewayDriverVehicle gDV : aL) gDV.remove();
                gV.remove();
            }
            else throw new VehicleNotFound(numberPlate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
