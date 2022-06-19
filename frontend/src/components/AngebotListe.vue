<template>
    <b-input-group prepend="Search">
        <b-form-input v-model="form" type="text" placeholder="search an offer"></b-form-input>
        <b-input-group-append>
            <b-button variant="danger" type="reset" @click="form = ''">clear</b-button>
        </b-input-group-append>
    </b-input-group>
    <div v-for="(e, i) in angebote" v-bind:key="i">
        <AngebotListeItem :angebot="e" />
    </div>
</template>

<script setup lang="ts">
    import { ref, computed } from 'vue';
    import AngebotListeItem from '@/components/AngebotListeItem.vue';
    import {useFakeAngebot} from '@/services/useFakeAngebot';

    const form = ref('')
    const angebote = computed(() => useFakeAngebot().angebote.value.filter(e => e.beschreibung.toLowerCase().includes(form.value.toLowerCase()) || e.abholort.toLowerCase().includes(form.value.toLowerCase()) || e.anbietername.toLowerCase().includes(form.value.toLowerCase())))
</script>