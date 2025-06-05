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
      <li class="nav-item">
        <router-link to="/statistics" class="nav-link">
          Statistics
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/registration" class="nav-link">
          Registration
        </router-link>
      </li>
    </ul>

    <div class="auth-container">
      <Authorization
          v-model:username="username"
          v-model:password="password"
          @login="handleLogin"
          @logout="handleLogout"
      />
    </div>
  </nav>
</template>

<script>
import Authorization from './Authorization.vue'

export default {
  name: 'Navigation',
  components: {
    Authorization
  },
  data() {
    return {
      username: '',
      password: ''
    }
  },
  methods: {
    handleLogin({ username, password }) {
      // Store credentials in localStorage (handled by AuthFields),
      // but here you can react to the login event if needed.
      // For example:
      console.log('User logged in:', username)
    },
    handleLogout() {
      // Handle any additional logout logic if necessary.
      console.log('User logged out')
    }
  }
}
</script>

<style scoped>
.navigation {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #2c3e50;
  padding: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 1000;
}

/* Spacer to push page content below the fixed navbar */
.navigation + * {
  margin-top: 100px; /* Adjust this if navbar height changes */
}

.nav-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  gap: 1.5rem;
}

.nav-link {
  color: #ecf0f1;
  text-decoration: none;
  font-weight: 500;
}

.nav-link.router-link-active {
  border-bottom: 2px solid #ecf0f1;
}


.auth-container .login-container {
  width: 100%;
}

.auth-container {
  padding-left: 20px;
  margin-right: 50px;
}
</style>
