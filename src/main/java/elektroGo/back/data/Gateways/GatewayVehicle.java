package elektroGo.back.data.Gateways;

import elektroGo.back.data.Finders.FinderVehicle;
import elektroGo.back.data.Database;


import java.sql.SQLException;
import java.time.LocalDate;

public class GatewayVehicle {
    private long id;
    private String brand;
    private String model;
    private String numberPlate;
    private int drivingRange;
    private LocalDate fabricationYear;
    private int seats;
    private String imageId;
    //private Driver driver;

    private FinderVehicle fV;

    public GatewayVehicle(long id, String model, String numberPlate) {
        setUp(id, model, numberPlate);
    }

    public GatewayVehicle(long id, String brand, String model, String numberPlate, int drivingRange, LocalDate fabricationYear, int seats, String imageId) {
        setUp(id, model, numberPlate);
        this.brand = brand;
        this.drivingRange = drivingRange;
        this.fabricationYear = fabricationYear;
        this.seats = seats;
        this.imageId = imageId;
    }

    public GatewayVehicle(long id, String brand, String model, String numberPlate, int drivingRange, LocalDate fabricationYear, int seats) {
        setUp(id, model, numberPlate);
        this.brand = brand;
        this.drivingRange = drivingRange;
        this.fabricationYear = fabricationYear;
        this.seats = seats;
    }

    private void setUp(long id, String model, String numberPlate) {
        this.id = id;
        this.model = model;
        this.numberPlate = numberPlate;
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

    //Review if the next sql sentences are okay

    public void insert() throws SQLException {
        Database d = Database.getInstance();
        if (imageId == null) d.executeSQLUpdate("insert into vehicle values(" +
                id +","+brand+","+model+","+numberPlate+","+drivingRange+","+fabricationYear+","+seats+",null);");
        else  d.executeSQLUpdate("insert into vehicle values(" +
                id +","+brand+","+model+","+numberPlate+","+drivingRange+","+fabricationYear+","+seats+","+imageId+");");

    }

    public void update() throws SQLException {
        Database d = Database.getInstance();
        if (imageId != null) d.executeSQLUpdate("update vehicle set " +
                "id="+id +", brand="+ brand+ ", model="+model+ ", numberPlate="+ numberPlate + ", drivingRange= "+ drivingRange + ", fabricationYear="+ fabricationYear +
                ", seats="+ seats+ ", imageId="+imageId+ ";");
        else {
            d.executeSQLUpdate("update vehicle set " +
                    "id="+id +", brand="+ brand+ ", model="+model+ ", numberPlate="+ numberPlate + ", drivingRange= "+ drivingRange + ", fabricationYear="+ fabricationYear +
                    ", seats="+ seats+ ", imageId=null"+ ";");
        }
    }

    public void remove() throws SQLException {
        Database d = Database.getInstance();
        d.executeSQLUpdate("delete from vehicle where id=" + id + ";");
    }
}