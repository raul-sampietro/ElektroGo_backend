/**
 * @file GatewayVehicle.java
 * @author Daniel Pulido
 * @date 11/03/2022
 * @brief Implementacio del Gateway de Vehicle.
 */

package elektroGo.back.data.Gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import elektroGo.back.data.Database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @brief La classe GatewayVehicle implementa el Gateway de Vehicle el qual te els atributs de Vehicle i fa insert/update/delete a la BD
 */
public class GatewayVehicle implements Gateway{
    /**
     * @brief Marca del Vehicle
     */
    private String brand;
    /**
     * @brief Model del vehicle
     */
    private String model;
    /**
     * @brief Matricula del vehicle
     */
    private String numberPlate;
    /**
     * @brief Autonomia del vehicle
     */
    private Integer drivingRange;
    /**
     * @brief Any de fabricacio del vehicle
     */
    private Integer fabricationYear;
    /**
     * @brief Seients del vehicle
     */
    private Integer seats;
    /**
     * @brief Identificador de la imatge del vehicle
     */
    private String imageId;

    /**
     * @brief Constructora buida
     * @post Crea un GatewayVehicle buit
     */
    public GatewayVehicle(){}

    /**
     * @brief Constructora de GatewayVehicle amb els parametres indicats a continuacio.
     * @param brand Marca del vehicle.
     * @param model Model del vehicle.
     * @param numberPlate Matricula del vehicle.
     * @param drivingRange Autonomia del vehicle.
     * @param fabricationYear Any de fabricacio del vehicle.
     * @param seats Seients del vehicle.
     * @param imageId Identificador de la imatge del vehicle.
     * @post Crea un vehicle amb els parametres indicats previament.
     */
    public GatewayVehicle(String brand, String model, String numberPlate, Integer drivingRange, Integer fabricationYear, Integer seats, String imageId) {
        setUp(brand, model, numberPlate, drivingRange, fabricationYear, seats);
        this.imageId = imageId;
    }

    /**
     * @brief Constructora de GatewayVehicle amb els parametres indicats a continuacio.
     * @param brand Marca del vehicle.
     * @param model Model del vehicle.
     * @param numberPlate Matricula del vehicle.
     * @param drivingRange Autonomia del vehicle.
     * @param fabricationYear Any de fabricacio del vehicle.
     * @param seats Seients del vehicle.
     * @post Crea un vehicle amb els parametres indicats previament.
     */
    public GatewayVehicle(String brand, String model, String numberPlate, Integer drivingRange, Integer fabricationYear, Integer seats) {
        setUp(brand, model, numberPlate, drivingRange, fabricationYear, seats);
    }

    /**
     * @brief Inicialitza els parametres indicats a continuacio.
     * @param brand Marca del vehicle.
     * @param model Model del vehicle.
     * @param numberPlate Matricula del vehicle.
     * @param drivingRange Autonomia del vehicle.
     * @param fabricationYear Any de fabricacio del vehicle.
     * @param seats Seients del vehicle.
     * @post Inicialitza els l'objecte amb els parametres indicats previament
     */
    private void setUp(String brand, String model, String numberPlate, Integer drivingRange, Integer fabricationYear, Integer seats) {
        this.brand = brand;
        this.model = model;
        this.numberPlate = numberPlate;
        this.drivingRange = drivingRange;
        this.fabricationYear = fabricationYear;
        this.seats = seats;
    }

    //Getters and setters

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public int getDrivingRange() {
        return drivingRange;
    }

    public void setDrivingRange(int drivingRange) {
        this.drivingRange = drivingRange;
    }

    public Integer getFabricationYear() {
        return fabricationYear;
    }

    public void setFabricationYear(Integer fabricationYear) {
        this.fabricationYear = fabricationYear;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    //SQL operations

    private void setPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setString(1, brand);
        pS.setString(2,model);
        pS.setString(3, numberPlate);
        if (drivingRange != null) pS.setInt(4,drivingRange); else pS.setString(4, null);
        if (fabricationYear != null) pS.setInt(5,fabricationYear); else pS.setString(5, null);
        if (seats != null) pS.setInt(6,seats); else pS.setString(6, null);
        pS.setString(7, imageId);
    }

    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO VEHICLE VALUES (?,?,?,?,?,?,?); ");
        setPreparedStatement(pS);
        pS.executeUpdate();
    }

    public void setPreparedStatementNoNP(PreparedStatement pS) throws SQLException {
        pS.setString(1, brand);
        pS.setString(2,model);
        if (drivingRange != null) pS.setInt(3,drivingRange); else pS.setString(3, null);
        if (fabricationYear != null) pS.setInt(4,fabricationYear); else pS.setString(4, null);
        if (seats != null) pS.setInt(5,seats); else pS.setString(5, null);
        pS.setString(6, imageId);
    }

    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE VEHICLE SET brand = ?, model = ?, " +
                "drivingRange = ?, fabricationYear = ?, seats = ?, imageId = ?;");
        setPreparedStatementNoNP(pS);
        pS.executeUpdate();
    }


    public void remove() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("DELETE FROM VEHICLE WHERE numberPlate = ?;");
        pS.setString(1, numberPlate);
        pS.executeUpdate();
    }

    public String json() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        mapper.registerModule(new JavaTimeModule());
        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}