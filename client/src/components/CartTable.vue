<script setup lang="ts">
import { useCartStore } from "@/stores/CartStore";
import { useCategoryStore } from "@/stores/CategoryStore";
import { asDollarsAndCents } from "@/main";

const categoryStore = useCategoryStore();

const cartStore = useCartStore();
import { BookItem } from "@/types";
const bookImageFileName = function (book: BookItem): string {
  let name = book.title.toLowerCase();
  name = name.replace(/ /g, "-");
  name = name.replace(/'/g, "");
  return `${name}.jpg`;
};

const updateCart = function (book: BookItem, quantity: number) {
  cartStore.cart.update(book, quantity);
  cartStore.updateBookQuantity(book, quantity);
};
</script>

<style scoped>
.cart-table {
  display: grid;
  grid-template-columns: max-content minmax(20em, 70em) repeat(3, max-content);
  row-gap: 1em;
  width: fit-content;
  margin: 0 auto;
  background-color: aliceblue;
}
.cart-description {
  margin-top: 3.125em;
  display: flex;
  flex-direction: column;
  align-items: center;
}

ul {
  display: contents;
}

ul > li {
  display: contents;
}

.table-heading {
  background-color: #333;
  color: white;
}

.table-heading > * {
  background-color: #333;
  padding: 0.5em;
}

.heading-book {
  grid-column: 1 / 3;
}

.heading-price {
  grid-column: 3 / 5;
  text-align: right;
}

.heading-subtotal {
  text-align: right;
  grid-column: -2 / -1;
}

.cart-book-image {
  padding: 0 1em;
}

.cart-book-image > * {
  margin-left: auto;
  margin-right: 0;
}

img {
  display: block;
  width: 100px;
  height: auto;
}
/* TODO The width above is for book images that are normally 200px wide. Change this (if necessary) */
/* TODO to accommodate your own book images. the rule of thumb is that the image here should be about */
/* TODO half the size of the book images as they appear on your category page. */

.cart-book-price {
  margin: auto;
  padding-left: 1em;
  text-align: right;
}

.cart-book-quantity {
  margin: auto;
  padding-left: 1em;
  padding-right: 1em;
}

.cart-book-subtotal {
  margin: auto;
  text-align: right;
  padding-left: 1em;
  padding-right: 1em;
}

/* Row separators in the table */

.line-sep {
  display: block;
  height: 2px;
  background-color: gray;
  grid-column: 1 / -1;
}

/* Increment/decrement buttons */

/* TODO Consider using icon buttons for your increment and decrement buttons. */
/* TODO Modify the CSS here to suit your own design. */
/* TODO In particular, you may or may not have custom properties */
/* TODO primary-color and primary-color-dark defined in your global CSS file. */
.icon-button {
  border: none;
  cursor: pointer;
  margin: 0 auto;
}

.inc-button {
  font-family: FontAwesome;
  background: transparent;
  color: var(--primary-color);
  box-shadow: none;
}

.inc-button:hover {
  color: var(--primary-color-dark);
}

.dec-button {
  font-family: FontAwesome;
  background: transparent;
  color: var(--primary-color);
  box-shadow: none;
}

.dec-button:hover {
  color: #aaa;
}
.container {
  display: flex;
}

.container.space-around {
  justify-content: space-around;
}
.container.space-between {
  justify-content: space-between;
}

select {
  background-color: var(--primary-color);
  color: white;
  border: 2px solid var(--primary-color-dark);
  border-radius: 3px;
}

h3 {
  text-align: center;
}

.cart-actions {
  margin: 3.125em;
  display: flex;
  justify-content: space-around;
}
</style>

<template>
  <h3 class="" v-if="!cartStore.cart.empty">
    The subtotal is {{ asDollarsAndCents(cartStore.cart.total) }} + surcharge
    {{ asDollarsAndCents(500) }}
  </h3>
  <h3 class="" v-if="!cartStore.cart.empty" style="padding: 1em">
    Your total today is
    {{ asDollarsAndCents(cartStore.cart.total + 500) }}.
  </h3>
  <section class="cart-actions">
    <button
      class="button clear-cart-button"
      @click="cartStore.clearCart() && localStorage.clear()"
      v-if="!cartStore.cart.empty"
    >
      Clear cart
    </button>

    <router-link :to="'/category/' + categoryStore.selectedCategoryName">
      <button class="button icon-with-text-button" style="position: center">
        Continue Shopping
      </button>
    </router-link>

    <router-link to="/checkout" v-if="!cartStore.cart.empty">
      <button class="button icon-with-text-button-significant">Checkout</button>
    </router-link>
  </section>

  <div class="cart-table" v-if="!cartStore.cart.empty">
    <ul>
      <li class="table-heading">
        <div class="heading-book">Book</div>
        <div class="heading-price">Price / Quantity</div>
        <div class="heading-subtotal">Amount</div>
      </li>
      <template v-for="item in cartStore.cart.items" :key="item.book.bookId">
        <li>
          <div class="cart-book-image">
            <img
              :src="
                require('@/assets/images/books/' + bookImageFileName(item.book))
              "
              :alt="item.book.title"
              width="100px"
              height="auto"
            />
          </div>
          <div class="cart-book-title">{{ item.book.title }}</div>
          <div class="cart-book-price">
            {{ asDollarsAndCents(item.book.price) }}
          </div>
          <div class="cart-book-quantity">
            <button
              class="button dec-button"
              @click="updateCart(item.book, item.quantity - 1)"
            >
              <i class="fa-sharp fa-solid fa-minus"> </i>
            </button>
            <input
              type="number"
              min="0"
              max="9"
              step="1"
              placeholder="Quantity desired"
              v-model.trim.number.lazy="item.quantity"
              @change="cartStore.updateCart(item.product, item.quantity)"
            />
            <button
              class="icon-button inc-button"
              @click="updateCart(item.book, item.quantity + 1)"
            >
              <i class="fa-sharp fa-solid fa-plus"></i>
            </button>
          </div>
          <div class="cart-book-subtotal">
            {{ asDollarsAndCents(item.quantity * item.book.price) }}
          </div>
        </li>
        <li class="line-sep"></li>
      </template>
    </ul>
  </div>

  <h3 style="padding-top: 50px" v-if="cartStore.cart.empty">
    Your cart is empty.
  </h3>
  <h3 style="padding-top: 50px" v-if="cartStore.count == 1">
    You have 1 book in cart.
  </h3>
  <h3 style="padding-top: 50px" v-if="cartStore.count >= 2">
    You have {{ cartStore.count }} books in cart.
  </h3>
</template>
