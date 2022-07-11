<template>
    <b-table-simple hover small responsive>
        <b-tbody>
            <b-tr>
                <b-td><RouterLink :to="'/gebot/' + angebot.angebotid">{{angebot.beschreibung}}</RouterLink></b-td>
                <b-td>{{angebot.gebote}} Gebote</b-td>
                <b-td>{{angebot.topgebot}} EUR</b-td>
                <b-td><b-button v-b-toggle.collapse-1 variant="primary">more</b-button></b-td>
            </b-tr>
        </b-tbody>
    </b-table-simple>
    
    <b-collapse id="collapse-1" class="mt-2">
        <b-card>
            <b-table-simple hover small responsive>
                <b-tbody>
                    <b-tr>
                        <b-td>Letztes Gebot</b-td>
                        <b-td>{{angebot.topgebot}} EUR (Mindestpreis war {{angebot.mindestpreis}} EUR)</b-td>
                    </b-tr>
                    <b-tr>
                        <b-td>Abholort</b-td>
                        <b-td><GeoLink :lat="lat" :lon="lon">{{angebot.abholort}}</GeoLink></b-td>
                    </b-tr>
                    <b-tr>
                        <b-td>bei</b-td>
                        <b-td>{{angebot.anbietername}}</b-td>
                    </b-tr>
                    <b-tr>
                        <b-td>bis</b-td>
                        <b-td>{{angebot.ablaufzeitpunkt}}</b-td>
                    </b-tr>
                </b-tbody>
            </b-table-simple>
        </b-card>
    </b-collapse>
</template>

<script setup lang="ts">
    import {defineProps} from 'vue';
    import type {IAngebotListeItem} from '@/services/IAngebotListeItem';
    import GeoLink from '@/components/GeoLink.vue';
    import { RouterLink } from 'vue-router'

    const props = defineProps<{
        angebot: IAngebotListeItem
    }>()

    const lat = props.angebot.lat as number;
    const lon = props.angebot.lon as number;
</script>