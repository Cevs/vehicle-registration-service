<template>
  <nav class="navigation">
    <ul class="nav-list">
      <li class="nav-item">
        <router-link to="/create-account" class="nav-link">
          Create Account
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/register-vehicle" class="nav-link">
          Register Vehicle
        </router-link>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  name: 'Navigation'
}
</script>

<style scoped>
.navigation {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #2c3e50;
  padding: 1rem 0;
  z-index: 1000;
}

.nav-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center; /* centers the menu horizontally */
  gap: 1.5rem;
}

.nav-item {
  /* no extra styles needed here */
}

.nav-link {
  color: #ecf0f1;
  text-decoration: none;
  font-weight: 500;
}

.nav-link.router-link-active {
  border-bottom: 2px solid #ecf0f1;
}
</style>
