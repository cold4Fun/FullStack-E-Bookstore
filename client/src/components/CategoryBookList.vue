<script setup lang="ts">
import { useBookStore } from "@/stores/BookStore";
import CategoryBookListItem from "@/components/CategoryBookListItem.vue";

const bookStore = useBookStore();
</script>

<style scoped>
ul {
  display: flex;
  flex-wrap: wrap;
  padding: 1em;
  gap: 1em;
}
</style>

<template>
  <ul>
    <template v-for="book in bookStore.bookList" :key="book.bookId">
      <category-book-list-item :book="book"></category-book-list-item>
    </template>
  </ul>
</template>
