package de.hsrm.mi.web.projekt.test.ueb06;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import de.hsrm.mi.web.projekt.geo.GeoServiceImpl;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class Web_Ueb06_A1_GeoService {
    @Autowired
    private GeoServiceImpl adressService;

    @Test
    @DisplayName("GeoService: findeAdress)Info() für UdE")
    public void finde_adresse_ude() throws IOException {
        // https://www.google.de/maps/@50.0970425,8.2162978,17z
        var infos = adressService.findeAdressInfo("Unter den Eichen 5, Wiesbaden");
        assertThat(infos).anyMatch(inf -> inf.display_name().contains("Hochschule RheinMain"));
        assertThat(infos).anyMatch(inf -> inf.display_name().contains("Camera"));
    }

    @Test
    @DisplayName("GeoService: findeAdressinfo() für Koninklijk Paleis, Amsterdam")
    public void finde_adresse_binnenhof17() throws IOException {
        var infos = adressService.findeAdressInfo("Koninklijk Paleis, Amsterdam");
        assertThat(infos).anyMatch(inf -> inf.display_name().contains("Nieuwezijds Voorburgwal"));
    }

    @Test
    @DisplayName("GeoService: findeAdressinfo() für The Orchid, Hawaii")
    public void finde_adresse_orchid() throws IOException {
        var infos = adressService.findeAdressInfo("The Orchid, Hawaii");
        assertThat(infos).anyMatch(inf -> inf.display_name().contains("North Kaniku Drive"));
    }

    @Test
    @DisplayName("GeoService: findeAdressinfo() für In den Blamüsen, Düsseldorf")
    public void finde_adresse_blamuesen() throws IOException {
        var infos = adressService.findeAdressInfo("In den Blamüsen, Düsseldorf");
        assertThat(infos).anyMatch(inf -> inf.display_name().contains("Angermund"));
    }

    @Test
    @DisplayName("GeoService: abstandInKm() für In den Blamüsen, Düsseldorf, und Am Löken, Ratingen")
    public void abstand_in_km_blamuesen_loeken() throws IOException {
        var a1 = adressService.findeAdressInfo("In den Blamüsen 17, Düsseldorf");
        var a2 = adressService.findeAdressInfo("Am Löken 17, Ratingen");
        var abstand = adressService.abstandInKm(a1.get(0), a2.get(0));
        assertThat(abstand).isBetween(3.0, 3.6);
    }

    @Test
    @DisplayName("GeoService: abstandInKm() Vollradisroda nach Den Haag")
    public void abstand_in_km_vollradisroda_denhaag() throws IOException {
        var a1 = adressService.findeAdressInfo("Vollradisroda, Germany");
        var a2 = adressService.findeAdressInfo("s'Gravenhage, Nederland");
        var abstand = adressService.abstandInKm(a1.get(0), a2.get(0));
        assertThat(abstand).isBetween(500.0, 520.0);
    }

    @Test
    @DisplayName("GeoService: abstandInKm() Berlin nach Tokio")
    public void abstand_in_km_berlin_tokio() throws IOException {
        var a1 = adressService.findeAdressInfo("Berlin, Germany");
        var a2 = adressService.findeAdressInfo("Tokio, Japan");
        var abstand = adressService.abstandInKm(a1.get(0), a2.get(0));
        assertThat(abstand).isBetween(8800.0, 9000.0);
    }

    @Test
    @DisplayName("GeoService: abstandInKm() The Orchid, North Kaniku Drive, nach Pu'uhonua o Honaunau")
    public void abstand_in_km_orchid_puuhonuaohonaunau() throws IOException {
        var a1 = adressService.findeAdressInfo("The Orchid, Hawaii, North Kaniku Drive");
        var a2 = adressService.findeAdressInfo("Pu'uhonua o Honaunau"); // https://www.nps.gov/puho/learn/historyculture/puuhonua-o-honaunau.htm
        var abstand = adressService.abstandInKm(a1.get(0), a2.get(0));
        assertThat(abstand).isBetween(55.0, 65.0);
    }

    @Test
    @DisplayName("GeoService: abstandNachGrad(1km)")
    public void abstand_5km_nach_grad() throws IOException {
        var abstand = adressService.abstandKmNachGrad(10);
        assertThat(abstand).isBetween(0.08,0.09);
    }

}
