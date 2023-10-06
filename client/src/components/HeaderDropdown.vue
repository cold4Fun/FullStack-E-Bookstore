<script setup lang="ts">
import { useCategoryStore } from "@/stores/CategoryStore";

const categoryStore = useCategoryStore();
</script>

<style scoped>
.header-dropdown {
  position: relative;
}

.categories-button {
  position: relative;
  z-index: 2;
}

.categories-button::after {
  font-family: FontAwesome;
  font-size: 14px;
  content: "\f0d7";
  margin-left: 5px;
}

.categories-button {
  color: #ffffff;
  font: inherit;
  margin: 0;
}

.header-dropdown ul {
  background-color: #f1f6f6;
  color: black;
  display: none;
  border-radius: 0 0 10px 10px;
}

.header-dropdown li {
  color: black;
  padding: 0.25em 0.5em;
}

.header-dropdown a {
  color: black;
  text-decoration: none;
  text-shadow: none;
}

.header-dropdown li:hover {
  background: #d4e2e2;
  font-weight: bold;
}

.header-dropdown li:last-child:hover {
  border-radius: 0 0 10px 10px;
}

.header-dropdown:hover ul {
  display: block;
  position: absolute;
  min-width: 8.15em;
  z-index: 3;
  cursor: pointer;
  margin-top: -12%;
  padding-top: 16%;
  z-index: 1;
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
}

.header-dropdown.unselected-link {
  display: block;
  height: 25px;
  border-radius: 8px;
  color: var(--neutral-color);
  background-color: #ffffff;
  text-decoration: none;
  font-size: 16px;
}

.header-dropdown.unselected-link:hover,
.header-dropdown.unselected-link:active {
  display: block;
  height: 30px;
  font-size: large;
  text-align: center;
  transition: all 200ms;
  color: whitesmoke;
  background-color: var(--primary-color-dark);
  text-decoration: none;
  font-size: 18px;
}
</style>

<template>
  <div class="header-dropdown">
    <button class="button categories-button">Categories</button>
    <ul>
      <li
        v-for="category in categoryStore.categoryList"
        :key="category.categoryId"
      >
        <router-link
          :to="'../category/' + category.name"
          class="header-dropdown unselected-link"
        >
          {{ category.name }}
        </router-link>
      </li>
    </ul>
  </div>
</template>
