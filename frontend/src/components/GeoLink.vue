<template>
    <a target="_blank" :href="link">
        <slot>Link-Text</slot>
    </a>
</template>

<script setup lang="ts">
    import {computed, defineProps, withDefaults} from "vue";

    const props = withDefaults(
        defineProps<{
            lat: number,
            lon: number,
            zoom?: number
        }>(),
        {zoom: 17}
    )

    const link = computed(() => `https://www.openstreetmap.org/#map=${props.zoom}/${props.lat}/${props.lon}`)
</script>