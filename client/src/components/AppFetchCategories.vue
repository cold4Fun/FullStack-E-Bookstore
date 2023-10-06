<script setup lang="ts">
import AppHeader from "@/components/AppHeader.vue";
import AppFooter from "@/components/AppFooter.vue";
import { useCategoryStore } from "@/stores/CategoryStore";

const categoryStore = useCategoryStore();
categoryStore.fetchCategories();

//console.log(categoryStore.categoryList[0]);
//provide("categoryList", categoryList);
</script>

<style></style>

<template>
  <div id="app">
    <app-header></app-header>
    <div class="content-wrap">
      <router-view></router-view>
    </div>
    <app-footer></app-footer>
  </div>
</template>
