<script setup lang="ts">
import { useRoute } from "vue-router";
import { useCategoryStore } from "@/stores/CategoryStore";

const route = useRoute();
const categoryStore = useCategoryStore();
//const categoryList = inject("categoryList");
</script>

<style scoped>
.category-buttons {
  display: flex;
  flex-direction: row;
  text-align: center;
  background-color: white;
  border-top: 2px solid var(--primary-background-color);
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
}

.button.selected-category-button {
  background-color: rgb(114, 104, 84);
  color: var(--neutral-color-dark);
  font-weight: bold;
}

.category-buttons .button {
  margin: 5px;
  box-shadow: none;
}

.button.unselected-category-button,
.button.unselected-category-button:visited {
  background-color: white;
  color: var(--neutral-color);
}

.button.unselected-category-button:hover,
.button.unselected-category-button:active {
  background-color: rgb(60 121 123 / 7%);
}
</style>

<template>
  <nav class="category-nav">
    <ul class="category-buttons">
      <li
        v-for="category in categoryStore.categoryList"
        :key="category.categoryId"
      >
        <router-link
          :to="{ name: 'category-view', params: { name: category.name } }"
          class="button"
          :class="
            category.name === route.params.name
              ? 'selected-category-button'
              : 'unselected-category-button'
          "
        >
          {{ category.name }}
        </router-link>
      </li>
    </ul>
  </nav>
</template>
