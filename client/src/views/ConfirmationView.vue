<script setup lang="ts">
import { useOrderDetailsStore } from "@/stores/OrderDetailsStore";
import Confirmation from "@/components/ConfirmationPage.vue";
import ConfirmationPage from "@/components/ConfirmationPage.vue";
</script>

<style scoped>
h1 {
  margin: 1em auto;
  text-align: center;
}
</style>

<template>
  <div id="confirmation">
    <h1>Comfirmation Summary</h1>
    <confirmation-page></confirmation-page>
  </div>
</template>
