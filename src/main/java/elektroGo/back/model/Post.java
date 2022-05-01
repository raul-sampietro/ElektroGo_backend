package elektroGo.back.model;

import java.io.Serializable;

public class Post implements Serializable {

    private String promotor_gestor;
    private String acces;
    private String tipus_velocitat;
    private String tipus_connexi;
    private double latitud;
    private double longitud;
    private String designaci_descriptiva;
    private double kw;
    private String ac_dc;
    private String ide_pdr;
    private String nplaces_estaci;
    private String tipus_vehicle;

    public String getPromotor_gestor() {
        return promotor_gestor;
    }

    public void setPromotor_gestor(String promotor_gestor) {
        this.promotor_gestor = promotor_gestor;
    }

    public String getAcces() {
        return acces;
    }

    public void setAcces(String acces) {
        this.acces = acces;
    }

    public String getTipus_velocitat() {
        return tipus_velocitat;
    }

    public void setTipus_velocitat(String tipus_velocitat) {
        this.tipus_velocitat = tipus_velocitat;
    }

    public String getTipus_connexi() {
        return tipus_connexi;
    }

    public void setTipus_connexi(String tipus_connexi) {
        this.tipus_connexi = tipus_connexi;
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

    public String getDesignaci_descriptiva() {
        return designaci_descriptiva;
    }

    public void setDesignaci_descriptiva(String designaci_descriptiva) {
        this.designaci_descriptiva = designaci_descriptiva;
    }

    public double getKw() {
        return kw;
    }

    public void setKw(double kw) {
        this.kw = kw;
    }

    public String getAc_dc() {
        return ac_dc;
    }

    public void setAc_dc(String ac_dc) {
        this.ac_dc = ac_dc;
    }

    public String getIde_pdr() {
        return ide_pdr;
    }

    public void setIde_pdr(String ide_pdr) {
        this.ide_pdr = ide_pdr;
    }

    public String getNplaces_estaci() {
        return nplaces_estaci;
    }

    public void setNplaces_estaci(String nplaces_estaci) {
        this.nplaces_estaci = nplaces_estaci;
    }

    public String getTipus_vehicle() {
        return tipus_vehicle;
    }

    public void setTipus_vehicle(String tipus_vehicle) {
        this.tipus_vehicle = tipus_vehicle;
    }

    @Override
    public String toString() {
        return "Post{" +
                "promotor_gestor='" + promotor_gestor + '\'' +
                ", acces='" + acces + '\'' +
                ", tipus_velocitat='" + tipus_velocitat + '\'' +
                ", tipus_connexi='" + tipus_connexi + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", designaci_descriptiva='" + designaci_descriptiva + '\'' +
                ", kw=" + kw +
                ", ac_dc='" + ac_dc + '\'' +
                ", ide_pdr='" + ide_pdr + '\'' +
                ", nplaces_estaci='" + nplaces_estaci + '\'' +
                ", tipus_vehicle='" + tipus_vehicle + '\'' +
                '}';
    }
}
