import { reactive, readonly } from "vue";
import type { IAngebotListeItem } from "@/services/IAngebotListeItem";
import { Client } from "@stomp/stompjs";

import { useLogin } from '@/services/useLogin'
const { logindata } = useLogin()

interface IAngebotState {
    angebotliste: IAngebotListeItem[],
    errormessage: string
}

const angebotState: IAngebotState = reactive({
    angebotliste: [],
    errormessage: ""
});

async function updateAngebote() {
    //bei allen fetch()- Backend-AnfragendasHeaderfeld“Authorization: Bearer jwttoken”mitgeschickt wird.
    fetch("api/angebot", {headers: {'Authorization': `Bearer ${logindata.jwtToken}`}})
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
            JSON.parse(message.body)
            updateAngebote()
        });
    }

    stompclient.onDisconnect = () => {
        console.log("Disconnected");
    }

    stompclient.activate();
}

export function useAngebot() {
    return {
        angebote: readonly(angebotState),
        updateAngebote,
        receiveAngebotMessages
    }
}
