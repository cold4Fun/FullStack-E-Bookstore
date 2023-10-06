<script setup lang="ts">
import { defineProps } from "vue";
import { BookItem } from "@/types";
import { useCartStore } from "@/stores/CartStore";
import { asDollarsAndCents } from "@/main";

const cartStore = useCartStore();
const props = defineProps<{
  book: BookItem;
}>();
const bookImageFileName = function (book: BookItem): string {
  let name = book.title.toLowerCase();
  name = name.replace(/ /g, "-");
  name = name.replace(/'/g, "");
  return `${name}.jpg`;
};

function addToCart(book: BookItem) {
  cartStore.addToCart(book);
}
</script>
<style scoped>
.book-box {
  display: flex;
  flex-direction: column;
  background-color: var(--card-background-color);
  padding: 1em;
  gap: 0.25em;
}

.book-title {
  font-weight: bold;
}

.book-author {
  font-style: italic;
}
.is-featured {
  background-color: lightcyan;
  color: black;
}
</style>

<template>
  <li class="book-box">
    <div class="book-image">
      <img
        :src="require('@/assets/images/books/' + bookImageFileName(book))"
        :alt="book.title"
        height="auto"
      />
    </div>
    <div class="book-title">{{ book.title }}</div>
    <div class="book-author">{{ book.author }}</div>
    <div class="book-price">{{ asDollarsAndCents(book.price) }}</div>
    <button class="button" @click="addToCart(book)">Add to Cart</button>
    <button class="button" v-if="book.isPublic">Read Now</button>
  </li>
</template>
