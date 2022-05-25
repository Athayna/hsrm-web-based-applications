package de.hsrm.mi.web.projekt.geo;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GeoServiceImpl implements GeoService {
    @Override
    public List<AdressInfo> findeAdressInfo(String adresse) {
        if (adresse == null)
            return Collections.emptyList();

        adresse = adresse.replace("\s", "+");

        WebClient webClient = WebClient.create("https://nominatim.openstreetmap.org/");
        var result = webClient.get()
            .uri("search?q={adresse}&format=json", adresse)
            .retrieve()
            .bodyToFlux(GeoService.AdressInfo.class)
            .collectList()
            .block();
        
        return result;
    }

    @Override
    public double abstandInKm(AdressInfo adr1, AdressInfo adr2) {
        final double ERDRADIUS_KM = 6370;
        var grad2rad = Math.PI / 180.0;
        var phi_a = adr1.lat() * grad2rad;
        var lambda_a = adr1.lon() * grad2rad;
        var phi_b = adr2.lat() * grad2rad;
        var lambda_b = adr2.lon() * grad2rad;
        var zeta = Math.acos(Math.sin(phi_a) * Math.sin(phi_b)
        + Math.cos(phi_a)*Math.cos(phi_b)*Math.cos(lambda_b-lambda_a));
        return zeta * ERDRADIUS_KM;
    }

    @Override
    public double abstandKmNachGrad(double abstand) {
        return abstand / 111.139;
    }    
}
