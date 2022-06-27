import { reactive, readonly } from "vue";
import type { IAngebotListeItem } from "@/services/IAngebotListeItem";
import { Client } from "@stomp/stompjs";

interface IAngebotState {
    angebotliste: IAngebotListeItem[],
    errormessage: string
}

const angebotState: IAngebotState = reactive({
    angebotliste: [],
    errormessage: ""
});

async function updateAngebote() {
    fetch("api/angebot")
        .then(response => {
            if (!response.ok) {
                throw new Error(response.statusText);
            }
            return response.json()
        })
        .then(angebotliste => {
            angebotState.angebotliste = angebotliste;
            angebotState.errormessage = "";
        })
        .catch(error => {
            angebotState.errormessage = error;
        })
}

function receiveAngebotMessages() {
    const url = `ws://${window.location.host}/stompbroker`;
    const destination = "/topic/angebot";
    const stompclient = new Client({brokerURL: url});
    stompclient.onWebSocketError = (event) => {
        console.log(event);
    }
    stompclient.onStompError = (frame) => {
        console.log(frame);
    }

    stompclient.onConnect = (frame) => {
        stompclient.subscribe(destination, (message) => {
            updateAngebote();
        });
    }

    stompclient.onDisconnect = () => {
        console.log("Disconnected");
    }

    stompclient.activate();

    try {
        stompclient.publish({destination: destination,
            body: "Hello World!"
        });
    } catch (error) {
        console.log(error);
    }
}

export function useAngebot() {
    return {
        angebote: readonly(angebotState),
        updateAngebote,
        receiveAngebotMessages
    }
}
