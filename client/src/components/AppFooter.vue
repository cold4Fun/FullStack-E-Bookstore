<script setup lang="ts"></script>

<style scoped>
footer {
  display: flex;
  width: 100%;
  background: rgba(197, 145, 0, 0.88);
}

.container {
  float: left;
  align-items: center;
}
footer .links a:not(:first-child) {
  padding-left: 0.5em;
  border-left: 1px solid white;
}

footer a:hover {
  color: #f1d3a4 !important;
}

.copyright,
.links,
.social-media-icons,
footer a {
  display: flex;
  flex-direction: row;
  align-items: center;
  color: white !important;
  gap: 0.5em;
  text-decoration: none;
}

.links a:hover {
  text-decoration: underline;
}

.social-media-icons {
  text-align: center;
  cursor: pointer;
  font-size: 0.4em;
  gap: 1.5em;
}
</style>

<template>
  <footer class="container">
    <section class="social-media-icons">
      <a class="icon-only-button-icon"><i class="fab fa-facebook"></i></a>
      <a class="icon-only-button-icon"><i class="fab fa-twitter"></i></a>
      <a class="icon-only-button-icon"><i class="fab fa-instagram"></i></a>
    </section>
    <section class="copyright">
      <p>All rights reserved</p>
      <i class="far fa-copyright"></i>
      <span>2022</span>
    </section>
    <section class="links">
      <router-link to="/">About</router-link>
      <router-link to="/">Directions</router-link>
      <router-link to="/">Contact</router-link>
      <router-link to="/">Sign up to get newsletter</router-link>
    </section>
  </footer>
</template>
