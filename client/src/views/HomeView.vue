<script setup lang="ts"></script>

<style scoped>
.home-page {
  height: 695px;
  width: 2200px;
  background: no-repeat url("@/assets/images/site/922661.jpg");
  justify-content: right;
  background-size: 50% 100%;
  display: flex;
}

.welcome-text {
  width: 30em;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 1rem 2rem;
}

.welcome-text h2 {
  color: var(--neutral-color-dark);
  font-size: 3.5em;
  font-family: "Nunito", sans-serif;
  font-weight: 100;
}

.welcome-text p {
  color: var(--neutral-color);
  font-size: 1.5em;
  font-weight: 100;
}

.shop-button {
  width: 15em;
  margin-top: 2rem;
}
</style>
<template>
  <div class="home-page">
    <section class="welcome-text flow-content container">
      <h2>Welcome to Well Red Bookstore</h2>
      <p>Go where the stories take you</p>
      <router-link to="category/Classics">
        <button class="button shop-button">Start Shopping!</button>
      </router-link>
    </section>
  </div>
</template>
