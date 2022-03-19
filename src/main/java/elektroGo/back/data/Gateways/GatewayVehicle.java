package elektroGo.back.data.Gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import elektroGo.back.data.Database;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class GatewayVehicle implements Gateway{
    private String brand;
    private String model;
    private String numberPlate;
    private Integer drivingRange;
    private LocalDate fabricationYear;
    private Integer seats;
    private String imageId;

    public GatewayVehicle(){}

    public GatewayVehicle(String brand, String model, String numberPlate, Integer drivingRange, LocalDate fabricationYear, Integer seats, String imageId) {
        setUp(brand, model, numberPlate, drivingRange, fabricationYear, seats);
        this.imageId = imageId;
    }

    public GatewayVehicle(String brand, String model, String numberPlate, Integer drivingRange, LocalDate fabricationYear, Integer seats) {
        setUp(brand, model, numberPlate, drivingRange, fabricationYear, seats);
    }

    private void setUp(String brand, String model, String numberPlate, Integer drivingRange, LocalDate fabricationYear, Integer seats) {
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

    public LocalDate getFabricationYear() {
        return fabricationYear;
    }

    public void setFabricationYear(LocalDate fabricationYear) {
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
        if (fabricationYear != null) pS.setDate(5,Date.valueOf(fabricationYear)); else pS.setString(5, null);
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
        if (fabricationYear != null) pS.setDate(4,Date.valueOf(fabricationYear)); else pS.setString(4, null);
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