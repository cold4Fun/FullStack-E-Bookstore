<script setup lang="ts">
import HeaderDropdown from "@/components/HeaderDropdown.vue";
import { useCartStore } from "@/stores/CartStore";

const cartStore = useCartStore();
</script>

<style scoped>
header {
  background: var(--secondary-background-color);
  display: flex;
  justify-content: space-between;
  padding: 1em;
  gap: 1em;
  z-index: 1;
}

.header-with-shadow {
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
}

header .button:not(.icon-only-button) {
  background-color: var(--neutral-color);
}

header .button:not(.icon-only-button):hover,
header .button:not(.icon-only-button):active,
.header-dropdown:hover .categories-button {
  background-color: var(--neutral-color-dark);
}

header .icon-only-button {
  color: var(--neutral-color);
}

header .icon-only-button:hover,
header .icon-only-button:active {
  color: var(--neutral-color-dark);
}

header a {
  text-decoration: none;
  text-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  color: rgb(255, 255, 255) !important;
}

.search-bar-text {
  padding: 0.5em 1.5em;
  border-radius: 20px;
  border: 0;
  background: var(--primary-background-color);
  width: 600px;
}

.search-bar-text:focus,
.search-bar-text:focus-visible {
  outline: none;
  box-shadow: 0 0 3pt 2pt var(--neutral-color);
}

.search-bar-button {
  margin-left: 0.4em;
  box-shadow: none;
  border-left: 1px solid;
  border-radius: 35%;
}

.header-dropdown-and-cart {
  gap: 0.5em;
}

@media (max-width: 600px) {
  header {
    flex-direction: column;
  }
}
</style>

<template>
  <header class="container header-with-shadow">
    <section class="bookstore-logo-title flex-centered-container">
      <router-link to="/">
        <img src="@/assets/images/site/Capture.png" alt="HomeLogo" />
      </router-link>
    </section>
    <section class="header-dropdown flex-centered-container">
      <header-dropdown></header-dropdown>
    </section>
    <section class="search-bar flex-centered-container">
      <form action="category.html" class="flex-centered-container">
        <input type="text" class="search-bar-text" placeholder="Search..." />
        <br />
        <input
          type="submit"
          class="button icon-inside-button search-bar-button"
          value="&#xf002;"
        />
      </form>
    </section>
    <section class="cart flex-centered-container">
      <router-link to="/cart">
        <button class="button icon-only-button">
          <i class="fas fa-shopping-cart icon-only-button-icon">
            <span class="icon-only-button-text">{{ cartStore.count }}</span>
          </i>
        </button>
      </router-link>
      <button class="button" style="padding: 7px">Hi! Weiting</button>
    </section>
  </header>
</template>
