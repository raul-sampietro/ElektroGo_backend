package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.finders.FinderChargingStations;
import elektroGo.back.data.gateways.GatewayChargingStations;
import elektroGo.back.model.Post;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.SQLException;


@Service
public class RestService {

    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) throws SQLException {

        this.restTemplate = restTemplateBuilder.build();
        UpdateBD();
    }

    private Post[] getPostsPlainJSON() {
        String url = "https://analisi.transparenciacatalunya.cat/resource/tb2m-m33b.json?$$app_token=ACtIou1t9Z9Gqv6Io2ouZA3Cj";
        return this.restTemplate.getForObject(url, Post[].class);
    }


    public void UpdateBD() throws SQLException {
        System.out.println("----------------------------------------");
        System.out.println("-----Obtenint dades punts de càrrega----");
        System.out.println("----------------------------------------");
        Post[] dades = getPostsPlainJSON();
        FinderChargingStations fc = FinderChargingStations.getInstance();
        Database d = Database.getInstance();
        Integer id = 1;
        for (Post a: dades) {
            boolean valid = true;
            BigDecimal lat = BigDecimal.valueOf(a.getLatitud());
            if (lat.compareTo(BigDecimal.valueOf(90)) == 1) valid = false;
            else if (lat.compareTo(BigDecimal.valueOf(-90)) == -1) valid = false;
            BigDecimal log = BigDecimal.valueOf(a.getLongitud());
            if (log.compareTo(BigDecimal.valueOf(180)) == 1) valid = false;
            else if (log.compareTo(BigDecimal.valueOf(-180)) == -1) valid = false;
            Double kw = a.getKw();
            if (valid){
                GatewayChargingStations gc = fc.findByID(id);
                GatewayChargingStations gCS = new GatewayChargingStations(id, a.getPromotor_gestor(), a.getAcces(), a.getTipus_velocitat(),
                        a.getTipus_connexi(), lat, log, a.getDesignaci_descriptiva(), kw, a.getAc_dc(), a.getIde_pdr(), a.getNplaces_estaci(), a.getTipus_vehicle());
                if (gc == null) {
                    System.out.println("M'he inserit: " + id + " " + a.getPromotor_gestor());
                    gCS.insert();
                }
                else {
                    System.out.println("M'he actualitzat: " + id + " " + a.getPromotor_gestor());
                    gCS.update();
                }
                id++;
            }
        }
        System.out.println("----------------------------------------");
        System.out.println("-----Dades obtingudes correcatement-----");
        System.out.println("----------------------------------------");
    }
}

