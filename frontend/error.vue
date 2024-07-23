<script setup>
const props = defineProps({
    error: {
        type: Object,
        required: true
    }
});
</script>

<template>
    <div class="callout callout-danger">
        <h4><i class="bi bi-cone-striped"></i>&nbsp;Error!!</h4>
        <h2>{{ error.statusCode }}</h2>
        <pre>{{ error.message }}</pre>
        <NuxtLink to="/">回首頁</NuxtLink>
    </div>
</template>