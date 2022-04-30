package elektroGo.back.model;

import java.io.Serializable;

public class Post implements Serializable {

    private String promotorGestor;
    private String acces;
    private String tipusVelocitat;
    private String tipusConnexi;
    private double latitud;
    private double longitud;
    private String designaciDescriptiva;
    private double kw;
    private String acdc;
    private String idePdr;
    private String nplacesestaci;
    private String tipusVehicle;

    public String getPromotorGestor() {
        return promotorGestor;
    }

    public void setPromotorGestor(String promotorGestor) {
        this.promotorGestor = promotorGestor;
    }

    public String getAcces() {
        return acces;
    }

    public void setAcces(String acces) {
        this.acces = acces;
    }

    public String getTipusVelocitat() {
        return tipusVelocitat;
    }

    public void setTipusVelocitat(String tipusVelocitat) {
        this.tipusVelocitat = tipusVelocitat;
    }

    public String getTipusConnexi() {
        return tipusConnexi;
    }

    public void setTipusConnexi(String tipusConnexi) {
        this.tipusConnexi = tipusConnexi;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDesignaciDescriptiva() {
        return designaciDescriptiva;
    }

    public void setDesignaciDescriptiva(String designaciDescriptiva) {
        this.designaciDescriptiva = designaciDescriptiva;
    }

    public double getKw() {
        return kw;
    }

    public void setKw(double kw) {
        this.kw = kw;
    }

    public String getAcdc() {
        return acdc;
    }

    public void setAcdc(String acdc) {
        this.acdc = acdc;
    }

    public String getIdePdr() {
        return idePdr;
    }

    public void setIdePdr(String idePdr) {
        this.idePdr = idePdr;
    }

    public String getNplacesestaci() {
        return nplacesestaci;
    }

    public void setNplacesestaci(String nplacesestaci) {
        this.nplacesestaci = nplacesestaci;
    }

    public String getTipusVehicle() {
        return tipusVehicle;
    }

    public void setTipusVehicle(String tipusVehicle) {
        this.tipusVehicle = tipusVehicle;
    }

    @Override
    public String toString() {
        return "Post{" +
                "promotorGestor='" + promotorGestor + '\'' +
                ", acces='" + acces + '\'' +
                ", tipusVelocitat='" + tipusVelocitat + '\'' +
                ", tipusConnexi='" + tipusConnexi + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", designaciDescriptiva='" + designaciDescriptiva + '\'' +
                ", kw=" + kw +
                ", acdc='" + acdc + '\'' +
                ", idePdr='" + idePdr + '\'' +
                ", nplacesestaci='" + nplacesestaci + '\'' +
                ", tipusVehicle='" + tipusVehicle + '\'' +
                '}';
    }
}
