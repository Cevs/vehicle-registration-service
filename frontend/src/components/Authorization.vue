<!-- src/components/Authorization.vue -->
<template>
  <div class="login-container">
    <div class="input-wrapper">
      <input
          id="username"
          type="text"
          :value="username"
          @input="$emit('update:username', $event.target.value)"
          placeholder="Username"
          required
      />
    </div>

    <div class="input-wrapper">
      <input
          id="password"
          type="password"
          :value="password"
          @input="$emit('update:password', $event.target.value)"
          placeholder="Password"
          required
      />
    </div>

    <button
        class="login-button"
        :class="loggedIn ? 'logout' : 'login'"
        @click="handleAuth"
        :disabled="!canToggle"
    >
      {{ loggedIn ? 'Logout' : 'Login' }}
    </button>
  </div>
</template>

<script>
export default {
  name: 'AuthFields',
  props: {
    username: {
      type: String,
      default: ''
    },
    password: {
      type: String,
      default: ''
    }
  },
  computed: {
    canToggle() {
      if (this.loggedIn) return true;
      return this.username.trim() !== '' && this.password.trim() !== '';
    }
  },
  data() {
    return {
      loggedIn: false
    }
  },
  mounted() {
    // Check localStorage to see if credentials are already stored
    const storedUsername = localStorage.getItem('username')
    const storedPassword = localStorage.getItem('password')
    if (storedUsername && storedPassword) {
      this.loggedIn = true
      // Optionally, emit these values upward so parent can stay in sync:
      this.$emit('update:username', storedUsername)
      this.$emit('update:password', storedPassword)
    }
  },
  methods: {
    handleAuth() {
      if (!this.loggedIn && !this.canToggle) {
        return
      }

      if (!this.loggedIn) {
        // Logging in: store username/password in localStorage
        localStorage.setItem('username', this.username)
        localStorage.setItem('password', this.password)
        this.loggedIn = true
        this.$emit('login', { username: this.username, password: this.password })
      } else {
        // Logging out: clear stored credentials
        localStorage.removeItem('username')
        localStorage.removeItem('password')
        this.loggedIn = false

        this.$emit('update:username', '')
        this.$emit('update:password', '')
        this.$emit('logout')
        this.$router.push({ name: 'CreateAccount' })
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  gap: 8px; /* spacing between input fields and button */
  align-items: center;
}

/* Each input-wrapper takes equal available space */
.login-container .input-wrapper {
  flex: 1;
}

/* Inputs fill their wrapper */
.login-container input[type="text"],
.login-container input[type="password"] {
  width: 100%;
  padding: 0.5rem;
  font-size: 0.8rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

/* Button styling */
.login-button {
  flex: 0 0 auto;
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
  font-weight: 500;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  color: #fff;
}

/* Green background when not yet logged in ("Login" state) */
.login-button.login {
  background-color: #28a745;
}

/* Red background when already logged in ("Logout" state) */
.login-button.logout {
  background-color: #dc3545;
}

/* Adjust on hover */
.login-button.login:hover {
  background-color: #218838;
}

.login-button.logout:hover {
  background-color: #c82333;
}

/* Disabled state */
.login-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Only apply hover style if not disabled */
.login-button.login:hover:not(:disabled) {
  background-color: #218838;
}
</style>
