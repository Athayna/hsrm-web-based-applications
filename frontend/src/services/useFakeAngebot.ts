import { AngebotListeDing, type IAngebotListeItem } from "@/services/IAngebotListeItem";
import { readonly, ref } from "vue";

// (sehr bescheidener) innerer "State": Liste von Angeboten als ref
// da die Liste leer initialisiert werden soll, geben wir der ref()-Funktion
// einen passenden Typparameter mit, da der Array-Typ aus [] allein nicht hervorgeht
const angebote = ref<IAngebotListeItem[]>([])


// Liste mit hardcodierten Angeboten (aber echten Geo-Koordinaten)
function makeFakeAngebote(): IAngebotListeItem[] {
    const angebote:IAngebotListeItem[] = [
        // https://www.openstreetmap.org/#map=18/50.09782/8.21676
        new AngebotListeDing("Rotes Sofa", "Meister Eder", 
            "Unter den Eichen 5, 65195 Wiesbaden",
            2000, "2022-12-20T10:30:00", 2, 100, 
            50.09782, 8.21676),
        // https://www.openstreetmap.org/#map=17/50.08030/8.21650
        new AngebotListeDing("Wackeldackel", "Jockele", 
            "Kurt-Schumacher-Ring 18, 65197 Wiesbaden", 
            42, "2022-08-20T12:00:00", 1, 123, 
            50.08030, 8.21650),
        // https://www.openstreetmap.org/#map=19/51.37827/12.48971
        new AngebotListeDing("Tupel-Poster", "Joghurta",
            "Poststraße 1, Taucha", 
            5, "2023-01-10T20:00:00", 2, 12,
            51.37827, 12.48971),
        // https://www.openstreetmap.org/#map=17/48.66646/8.46847
        new AngebotListeDing("Doidolon (neuwertig)", "Joghurta",
            "Bergweg 1, Enzklösterle",
            100, "2024-07-23T09:00:00", 0, 0,
            48.66646, 8.46847)
    ]
    return angebote
}

// Für alle Angebote werden die Top-Gebote zufällig erhöht
function bietenSimulieren(): void {
    console.log(`bietenSimulieren... es sind ${angebote.value.length}`)
    for (const i in angebote.value) {
        const alt = angebote.value[i].topgebot
        const neu = alt + Math.floor(Math.random()*50)
        angebote.value[i].gebote++
        angebote.value[i].topgebot = neu
        console.log(`bietenSimulieren[${i}]: ${alt} -> ${neu}`)
    }
}

// einfache State-Initialisierung, wird nur bei erstem Import ausgeführt (wie in Python)
// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Modules
angebote.value = makeFakeAngebote()


// Composition Function - gibt nur die nach außen freigegebenen Features des Moduls raus
export function useFakeAngebot() {
    return {
        angebote: readonly(angebote), // state vor unkontrollierten Änderungen von außen schützen
        bietenSimulieren
    }
}