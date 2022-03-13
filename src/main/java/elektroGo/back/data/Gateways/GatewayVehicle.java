package elektroGo.back.data.Gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elektroGo.back.data.Finders.FinderVehicle;
import elektroGo.back.data.Database;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class GatewayVehicle implements Gateway{
    private Long id;
    private String brand;
    private String model;
    private String numberPlate;
    private Integer drivingRange;
    private LocalDate fabricationYear;
    private Integer seats;
    private String imageId;
    private String userName; //CHANGE TYPE OF THIS ATTRIBUTE TO DRIVER WHEN IMPLEMENTED

    public String getuserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

    private FinderVehicle fV;

    public GatewayVehicle(long id, String model, String numberPlate, String userName) {
        setUp(id, model, numberPlate, userName);
    }

    public GatewayVehicle(long id, String brand, String model, String numberPlate, int drivingRange, LocalDate fabricationYear, int seats, String imageId, String userName) {
        setUp(id, model, numberPlate, userName);
        this.brand = brand;
        this.drivingRange = drivingRange;
        this.fabricationYear = fabricationYear;
        this.seats = seats;
        this.imageId = imageId;
    }

    public GatewayVehicle(long id, String brand, String model, String numberPlate, int drivingRange, LocalDate fabricationYear, int seats, String userName) {
        setUp(id, model, numberPlate, userName);
        this.brand = brand;
        this.drivingRange = drivingRange;
        this.fabricationYear = fabricationYear;
        this.seats = seats;
    }

    private void setUp(long id, String model, String numberPlate, String userName) {
        this.id = id;
        this.model = model;
        this.numberPlate = numberPlate;
        this.userName = userName; //WHEN userName CLASS IMPLEMENTED, CREATE A userName OBJECT HERE
    }

    //Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public void setFullPreparedStatement(PreparedStatement pS) throws SQLException {
        pS.setLong(1,id);
        pS.setString(2, brand);
        pS.setString(3,model);
        pS.setString(4, numberPlate);
        if (drivingRange != null) pS.setInt(5,drivingRange); else pS.setString(5, null);
        if (fabricationYear != null) pS.setDate(6,Date.valueOf(fabricationYear)); else pS.setString(6, null);
        if (seats != null) pS.setInt(7,seats); else pS.setString(7, null);
        pS.setString(8, imageId);
        pS.setString(9, userName);
    }

    private void setPreparedStatementNoID(PreparedStatement pS) throws SQLException {
        pS.setString(1, brand);
        pS.setString(2,model);
        pS.setString(3, numberPlate);
        if (drivingRange != null) pS.setInt(4,drivingRange); else pS.setString(4, null);
        if (fabricationYear != null) pS.setDate(5,Date.valueOf(fabricationYear)); else pS.setString(5, null);
        if (seats != null) pS.setInt(6,seats); else pS.setString(6, null);
        pS.setString(7, imageId);
        pS.setString(8, userName);
    }

    public void insert() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("INSERT INTO VEHICLE VALUES (?,?,?,?,?,?,?,?,?); ");
        setFullPreparedStatement(pS);
        pS.executeUpdate();
    }

    public void update() throws SQLException {
        Database d = Database.getInstance();
        Connection c = d.getConnection();
        PreparedStatement pS = c.prepareStatement("UPDATE VEHICLE SET brand = ?, model = ?, numberPlate = ?, " +
                "drivingRange = ?, fabricationYear = ?, seats = ?, imageId = ?, userName = ? ;");
        setPreparedStatementNoID(pS);
        pS.executeUpdate();
    }


    public void remove() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("DELETE FROM VEHICLE WHERE id=" + id + ";");
    }

    public String json() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}