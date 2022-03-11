package elektroGo.back.data.Gateways;

import elektroGo.back.data.Finders.FinderVehicle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class GatewayVehicle {
    private long id;
    private String brand;
    private String model;
    private String numberPlate;
    private int range;
    private Date yearOfProduction;
    private int maxSeats;
    private String pathPhoto;

    private FinderVehicle fV;


    public GatewayVehicle(long id, String brand, String model, String numberPlate, int range, Date yearOfProduction, int maxSeats, String pathPhoto) {
        fV = FinderVehicle.getInstance();
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.numberPlate = numberPlate;
        this.range = range;
        this.yearOfProduction = yearOfProduction;
        this.maxSeats = maxSeats;
        this.pathPhoto = pathPhoto;
    }

    public GatewayVehicle(long id, String brand, String model, String numberPlate, int range, Date yearOfProduction, int maxSeats) {
        fV = FinderVehicle.getInstance();
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.numberPlate = numberPlate;
        this.range = range;
        this.yearOfProduction = yearOfProduction;
        this.maxSeats = maxSeats;
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

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public Date getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(Date yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public void setPathPhoto(String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }

    //SQL operations

    //Review if the next sql sentences are okay

    public void insert() throws SQLException {
        Connection c = fV.getConnection();
        Statement s = c.createStatement();
        if (pathPhoto == null) s.executeUpdate("insert into vehicle values(" +
                id +","+brand+","+model+","+numberPlate+","+range+","+yearOfProduction+","+maxSeats+",null);");
        else s.executeUpdate("insert into vehicle values(" +
                id +","+brand+","+model+","+numberPlate+","+range+","+yearOfProduction+","+maxSeats+","+pathPhoto+");");

    }

    public void update() throws SQLException {
        Connection c = fV.getConnection();
        Statement s = c.createStatement();
        if (pathPhoto != null) s.executeUpdate("update vehicle set " +
                "id="+id +", brand="+ brand+ ", model="+model+ ", numberPlate="+ numberPlate + ", range= "+ range + ", yearOfProduction="+ yearOfProduction +
                ", maxSeats="+ maxSeats+ ", pathPhoto="+pathPhoto+ ";");
        else {
            s.executeUpdate("update vehicle set " +
                    "id="+id +", brand="+ brand+ ", model="+model+ ", numberPlate="+ numberPlate + ", range= "+ range + ", yearOfProduction="+ yearOfProduction +
                    ", maxSeats="+ maxSeats+ ", pathPhoto=null"+ ";");
        }
    }

    public void remove() throws SQLException {
        Connection c = fV.getConnection();
        Statement s = c.createStatement();
        s.executeUpdate("delete from vehicle where id=" + id + ";");
    }
}