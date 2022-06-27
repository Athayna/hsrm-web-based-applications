<template>
  <div v-if="angebote.errormessage!=''">
    <b-alert show variant="danger">{{angebote.errormessage}}</b-alert>
  </div>
  <div>
    <h1>Wir haben aktuell {{angebote.angebotliste.length}} Angebote für Sie.</h1>
    <AngebotListe />
    <b-button variant="outline-primary" @click="update">Reload</b-button>
  </div>
</template>

<script setup lang="ts">
  import AngebotListe from '@/components/AngebotListe.vue';
  import { useAngebot } from '@/services/useAngebot';
  import { onMounted } from 'vue';

  const angebote = useAngebot().angebote;
  const update = useAngebot().updateAngebote;
  const messages = useAngebot().receiveAngebotMessages;

  onMounted( async () => {
    await update();
    messages();
  });
</script>
