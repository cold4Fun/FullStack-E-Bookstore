<script setup lang="ts">
import CategoryNav from "@/components/CategoryNav.vue";
import CategoryBookList from "@/components/CategoryBookList.vue";
import { useRoute } from "vue-router";
import { inject, ref, watch } from "vue";
import { apiUrl } from "@/services/ApiService";

import { BookItem, CategoryItem } from "@/types";

const route = useRoute();
let bookList = ref([] as BookItem[]);
const categoryList: CategoryItem[] = inject("categoryList", []);

async function fetchBooks(categoryName: string) {
  const selectedCategory =
    categoryList?.find((category) => category.name === categoryName) ||
    categoryList[0];
  //const url = `http://cs5244.cs.vt.edu:8080/WeitingBookstoreFetch/api/categories/${selectedCategory.categoryId}/books`;
  //const url = `http://localhost:8080/WeitingBookstoreFetch/api/categories/${selectedCategory.categoryId}/books`;
  const url = apiUrl + "categories/${selectedCategory.categoryId}/books";
  bookList.value = await fetch(url).then((response) => response.json());
}
watch(
  () => route.params.name,
  (newName) => {
    fetchBooks(newName as string);
  },
  { immediate: true }
);
</script>

<style scoped></style>

<template>
  <div>
    <category-nav></category-nav>
    <category-book-list :bookList="bookList"> </category-book-list>
  </div>
</template>
