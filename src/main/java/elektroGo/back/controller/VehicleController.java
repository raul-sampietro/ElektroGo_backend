package elektroGo.back.controller;

import elektroGo.back.data.Finders.FinderDriver;
import elektroGo.back.data.Finders.FinderDriverVehicle;
import elektroGo.back.data.Finders.FinderVehicle;
import elektroGo.back.data.Gateways.GatewayDriverVehicle;
import elektroGo.back.data.Gateways.GatewayVehicle;
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

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @PostMapping("/create")
    public void createVehicle(@RequestBody GatewayVehicle gV, @RequestParam String userNDriver, @RequestParam("imageFile") MultipartFile file ) throws SQLException, IOException {
        FinderDriver fD = FinderDriver.getInstance();
        FinderVehicle fV = FinderVehicle.getInstance();
        if (fV.findByNumberPlate(gV.getNumberPlate()) != null) throw new VehicleAlreadyExists(gV.getNumberPlate());
        if (fD.findByUserName(userNDriver) != null) {
            System.out.println("Original Image Byte Size - " + file.getBytes().length);
            gV.insert();
            GatewayDriverVehicle gDV = new GatewayDriverVehicle(gV.getNumberPlate(), userNDriver);
            gDV.insert();
        }
        else throw new DriverNotFound(userNDriver);

    }

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


        @GetMapping("/read")
    public GatewayVehicle readVehicle(@RequestParam String numberPlate) throws SQLException {
        FinderVehicle fV = FinderVehicle.getInstance();
        GatewayVehicle gV = fV.findByNumberPlate(numberPlate);
        if (gV == null) throw new VehicleNotFound(numberPlate);
        else return gV;

    }

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
