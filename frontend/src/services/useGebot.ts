import { reactive, readonly } from "vue";

import { Client } from '@stomp/stompjs';
const wsurl = `ws://${window.location.host}/stompbroker`

import { useLogin } from '@/services/useLogin'
const { logindata } = useLogin()

export function useGebot(angebotid: number) {
    /*
     * Mal ein Beispiel für CompositionFunction mit Closure/lokalem State,
     * um parallel mehrere *verschiedene* Versteigerungen managen zu können
     * (Gebot-State ist also *nicht* Frontend-Global wie Angebot(e)-State)
     */

    const DEST = `/topic/gebot/${angebotid}`

    interface IGetGebotResponseDTO {
        gebotid: number,
        gebieterid: number,
        gebietername: string,
        angebotid: number,
        angebotbeschreibung: string,
        betrag: number,
        gebotzeitpunkt: string
    }

    interface IGebotState {
        angebotid: number,
        topgebot: number,
        topbieter: string,
        gebotliste: IGetGebotResponseDTO[],
        receivingMessages: boolean,
        errormessage: string
    }

    const gebotState: IGebotState = reactive({angebotid: angebotid, topgebot: 0, topbieter: "", gebotliste: [], receivingMessages: false, errormessage: ""})

    function processGebotDTO(gebotDTO: IGetGebotResponseDTO) {
        const dtos = JSON.stringify(gebotDTO)
        console.log(`processGebot(${dtos})`)

        /*
         * suche Angebot für 'gebieter' des übergebenen Gebots aus der gebotliste (in gebotState)
         * falls vorhanden, hat der User hier schon geboten und das Gebot wird nur aktualisiert (Betrag/Gebot-Zeitpunkt)
         * falls nicht, ist es ein neuer Bieter für dieses Angebot und das DTO wird vorne in die gebotliste des State-Objekts aufgenommen
         */
        const angebot = gebotState.gebotliste.find(g => g.angebotid === gebotDTO.angebotid)
        if (angebot) {
            angebot.betrag = gebotDTO.betrag
            angebot.gebotzeitpunkt = gebotDTO.gebotzeitpunkt
        } else {
            gebotState.gebotliste.unshift(gebotDTO)
        }

        /*
         * Falls gebotener Betrag im DTO größer als bisheriges topgebot im State,
         * werden topgebot und topbieter (der Name, also 'gebietername' aus dem DTO)
         * aus dem DTO aktualisiert
         */
        if (gebotDTO.betrag > gebotState.topgebot) {
            gebotState.topgebot = gebotDTO.betrag
            gebotState.topbieter = gebotDTO.gebietername
        }

    }


    function receiveGebotMessages() {
        /*
         * analog zu Message-Empfang bei Angeboten
         * wir verbinden uns zur brokerURL (s.o.),
         * bestellen Nachrichten von Topic DEST (s.o.)
         * und rufen die Funktion processGebotDTO() von oben
         * für jede neu eingehende Nachricht auf diesem Topic auf.
         * Eingehende Nachrichten haben das Format IGetGebotResponseDTO (s.o.)
         * Die Funktion aktiviert den Messaging-Client nach fertiger Einrichtung.
         * 
         * Bei erfolgreichem Verbindungsaubau soll im State 'receivingMessages' auf true gesetzt werden,
         * bei einem Kommunikationsfehler auf false 
         * und die zugehörige Fehlermeldung wird in 'errormessage' des Stateobjekts geschrieben
         */
        const client = new Client({brokerURL: wsurl})
        client.onConnect = () => {
            client.subscribe(DEST, (message) => {
                const gebotDTO:IGetGebotResponseDTO = JSON.parse(message.body)
                processGebotDTO(gebotDTO)
            })
        }
        client.onConnect = () => {
            gebotState.receivingMessages = true
        }
        client.onWebSocketError = (error) => {
            gebotState.receivingMessages = false
            gebotState.errormessage = error.message
        }
        client.onDisconnect = () => {
            gebotState.receivingMessages = false
        } 
        client.activate()
    }


    async function updateGebote() {
        /*
         * holt per fetch() auf Endpunkt /api/gebot die Liste aller Gebote ab
         * (Array vom Interface-Typ IGetGebotResponseDTO, s.o.)
         * und filtert diejenigen für das Angebot mit Angebot-ID 'angebotid' 
         * (Parameter der useGebot()-Funktion, s.o.) heraus. 
         * Falls erfolgreich, wird 
         *   - das Messaging angestoßen (receiveGebotMessages(), s.o.), 
         *     sofern es noch nicht läuft
         *   - das bisherige maximale Gebot aus der empfangenen Liste gesucht, um
         *     die State-Properties 'topgebot' und 'topbieter' zu initialisieren
         *   - 'errormessage' auf den Leerstring gesetzt
         * Bei Fehler wird im State-Objekt die 'gebotliste' auf das leere Array 
         * und 'errormessage' auf die Fehlermeldung geschrieben.
         */
        
        try{
            const response = await fetch(`/api/gebot/`, {headers: {'Authorization': `Bearer ${logindata.jwtToken}`}})
            const gebotliste:IGetGebotResponseDTO[] = await response.json()
            gebotState.gebotliste = gebotliste.filter(g => g.angebotid === angebotid)
            if (!gebotState.receivingMessages) {
                receiveGebotMessages()
            }
            const topgebot = gebotState.gebotliste.find(g => g.betrag === gebotState.gebotliste.reduce((acc, g) => Math.max(acc, g.betrag), 0))
            gebotState.topbieter = topgebot?.gebietername ? topgebot.gebietername : ""
            gebotState.topgebot = topgebot?.betrag ? topgebot.betrag : 0
        } catch (error) {
            gebotState.gebotliste = []
            gebotState.errormessage = `${error}`
        }
    }


    // Analog Java-DTO AddGebotRequestDTO.java
    interface IAddGebotRequestDTO {
        benutzerprofilid: number,
        angebotid: number,
        betrag: number
    }

    async function sendeGebot(betrag: number) {
        /*
         * sendet per fetch() POST auf Endpunkt /api/gebot ein eigenes Gebot,
         * schickt Body-Struktur gemäß Interface IAddGebotRequestDTO als JSON,
         * erwartet ID-Wert zurück (response.text()) und loggt diesen auf die Console
         * Falls ok, wird 'errormessage' im State auf leer gesetzt,
         * bei Fehler auf die Fehlermeldung
         */
        const body:IAddGebotRequestDTO = {
            benutzerprofilid: logindata.benutzerprofilid,
            angebotid: angebotid,
            betrag: betrag
        }
        try {
            const response = await fetch('/api/gebot', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${logindata.jwtToken}`
                },
                body: JSON.stringify(body)
            })
            const id = await response.text()
            console.log(`Gebot mit ID ${id} wurde gesendet`)
            gebotState.errormessage = ""
        }
        catch (error) { 
            gebotState.errormessage = `${error}`
        }

    }

    // Composition Function -> gibt nur die nach außen freigegebenen Features des Moduls raus
    return {
        gebote: readonly(gebotState),
        updateGebote,
        sendeGebot
    }
}

