
// Verwendung: 
// import type { IAngebotListeItem } from '@/services/IAngebotListeItem';
export interface IAngebotListeItem {
    angebotid: number,
    beschreibung: string,
    anbieterid: number,
    anbietername: string,
    mindestpreis: number,
    ablaufzeitpunkt: Date,
    abholort: string,
    lat: number,
    lon: number,
    topgebot: number,
    gebote: number
}

// 
export class AngebotListeDing implements IAngebotListeItem {
    angebotid = 0;
    beschreibung = "";
    anbieterid = 0;
    anbietername = "";
    mindestpreis = 0;
    ablaufzeitpunkt: Date;
    abholort = "";
    lat = 0;
    lon = 0;
    topgebot = 0;
    gebote = 0;

    constructor(beschr: string, aname: string, aort:string, minpreis: number, 
                ablaufstr: string, ngeb = 0, top = 0,
                lat=0, lon=0) {
        this.beschreibung = beschr
        this.ablaufzeitpunkt = new Date(ablaufstr)
        this.anbietername = aname;
        this.mindestpreis = minpreis;
        this.abholort = aort;
        this.lat = lat
        this.lon = lon
        this.topgebot = top;
        this.gebote = ngeb;
    }

}

/* Zur Erinnerung: Java-Serverseite sieht so aus

public record GetAngebotResponseDTO(
    long angebotid,             // ID des Angebots
    String beschreibung,        // Beschreibung
    long anbieterid,            // ID des Anbieters
    String anbietername,        // Name des Anbieters
    long mindestpreis,          // geforderter Mindestpreis
    LocalDateTime ablaufzeitpunkt, // Ende der Versteigerung
    String abholort,            // Abholort der Sache
    double lat,                 // mit Breite
    double lon,                 // und Länge
    long topgebot,              // Höhe des bisher maximalen Gebots (0, falls keines vorliegt)
    long gebote                 // Anzahl bisheriger Gebote
}
*/