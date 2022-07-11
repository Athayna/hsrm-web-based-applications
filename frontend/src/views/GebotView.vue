<template>
    <div>
        <h1>Versteigerung {{angebot?.beschreibung}} ab {{angebot?.mindestpreis}} EUR</h1>
        <p>von {{angebot?.anbietername}}, abholbar in <GeoLink v-if="angebot!=null" :lat="angebot?.lat" :lon="angebot?.lon">{{angebot?.abholort}}</GeoLink> bis {{angebot?.ablaufzeitpunkt}}</p>
        <b-alert v-if="useAngebot().angebote.errormessage!=''" show variant="danger">{{useAngebot().angebote.errormessage}}</b-alert>
    </div>

    <div>
        <p>Bisheriges Topgebot von EUR {{gebote.topgebot}} ist von {{gebote.topbieter}}</p>

        <b-input-group>
            <b-form-input v-model="form" type="number" v-value="form + 1"></b-form-input>
            <b-input-group-append>
                <b-button type="submit" @click="sendeGebot(form)">bieten</b-button>
            </b-input-group-append>
        </b-input-group>
    
        <b-table-simple hover small responsive>
            <b-tbody>
                <b-tr v-for="(gebot, i) in gebotliste" v-bind:key="i">
                    <b-td>{{gebot.gebotzeitpunkt}}</b-td>
                    <b-td>{{gebot.gebietername}}</b-td>
                    <b-td>bietet {{gebot.betrag}} EUR</b-td>
                </b-tr>
            </b-tbody>
        </b-table-simple>
    </div>

</template>

<script setup lang="ts">
    import { useAngebot } from '@/services/useAngebot';
    import { useGebot } from '@/services/useGebot';
    import { computed, ref, onMounted } from 'vue';
    import GeoLink from '@/components/GeoLink.vue';

    const props = defineProps<{
        angebotidstr: string
    }>()

    const angebot = useAngebot().angebote.angebotliste.find(angebot => angebot.angebotid === Number(props.angebotidstr)) || null;
    const gebote = computed(() => useGebot(Number(props.angebotidstr)).gebote)
    const gebotliste = computed(() => gebote.value.gebotliste.slice().sort((a, b) => new Date(b.gebotzeitpunkt).getTime() - new Date(a.gebotzeitpunkt).getTime()))
    
    const form = ref(gebote.value.topgebot)
    const sendeGebot = useGebot(Number(props.angebotidstr)).sendeGebot;
    const updateGebote = useGebot(Number(props.angebotidstr)).updateGebote;

    onMounted(async () => {
        await updateGebote();
    })
</script>