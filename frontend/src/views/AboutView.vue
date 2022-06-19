<template>
  <div class="about">
    <h1>This is an about page</h1>
    <GeoLink :lat="52.9849" :lon="9.5794">Visselhövede</GeoLink><br>
    <GeoLink :lat="52.9849" :lon="9.5794" :zoom="15" >Visselhövede</GeoLink>
    <AngebotListeItem :angebot="fakeAngebote.angebote.value[0]" />
    <b-button variant="outline-primary" @click="fakeAngebote.bietenSimulieren">bieten simulieren</b-button>
    <AngebotListe />
  </div>
</template>

<script setup lang="ts">
  import GeoLink from '@/components/GeoLink.vue';
  import AngebotListeItem from '@/components/AngebotListeItem.vue';
  import AngebotListe from '@/components/AngebotListe.vue';
  import {useFakeAngebot} from '@/services/useFakeAngebot';

  const fakeAngebote = useFakeAngebot();
</script>

<style>
  @media (min-width: 1024px) {
    .about {
      min-height: 100vh;
      display: flex;
      align-items: center;
    }
  }
</style>
