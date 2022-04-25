package elektroGo.back;

import elektroGo.back.data.Database;
import elektroGo.back.data.gateways.GatewayChargingStations;
import elektroGo.back.model.Post;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;


@Service
public class RestService {

    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) throws SQLException {
        this.restTemplate = restTemplateBuilder.build();
        Post a = getPostsPlainJSON()[0];
        System.out.println(a.toString());
        BigDecimal lat = BigDecimal.valueOf(a.getLatitud());
        BigDecimal log = BigDecimal.valueOf(a.getLongitud());
        Integer num = Integer.valueOf(a.getNplaces_estaci());
        //GatewayChargingStations gCS = new GatewayChargingStations(99, lat, log, num);
        //Database d = Database.getInstance();
        //gCS.insert();
    }

    public Post[] getPostsPlainJSON() {
        String url = "https://analisi.transparenciacatalunya.cat/resource/tb2m-m33b.json?$$app_token=ACtIou1t9Z9Gqv6Io2ouZA3Cj";
        return this.restTemplate.getForObject(url, Post[].class);
    }
}

