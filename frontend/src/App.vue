<!-- src/App.vue -->
<template>
  <div id="app">
    <Navigation />

    <!-- Main content area: router-view will render either AccountForm or RegisterVehicle -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script>
import Navigation from './components/Navigation.vue'

export default {
  name: 'App',
  components: {
    Navigation
  }
}
</script>

<style>
body {
  font-family: sans-serif;
}

/* Make sure content is pushed down below the fixed nav (adjust height to match your nav) */
.main-content {
  margin-top: 64px;
}
</style>
