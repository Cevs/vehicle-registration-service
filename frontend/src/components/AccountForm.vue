<template>
  <div class="form-container">
    <h2>Create Account</h2>
    <form @submit.prevent="createAccount">
      <input id="accountId" v-model="accountId" type="email" placeholder="you@example.com" required>
      <button type="submit">Create</button>
    </form>
    <p v-if="message" :class="messageType">{{ message }}</p>
  </div>

</template>

<script>
import axios from 'axios';

export default {
  name: 'AccountForm',
  data() {
    return {
      accountId: '',
      message: '',
      messageType: '' // "success" or "error"
    }
  },
  methods: {
    async createAccount() {
      // Clear previous messages
      this.message = '';
      this.messageType = '';

      // Trim and validate
      const id = this.accountId.trim();
      if (!id) {
        this.messageType = 'error';
        this.message = 'Please enter a valid email address.';
        return;
      }

      try {
        // Send JSON: { accountId: "user@example.com" }
        console.log("id:" + id)
        const response = await axios.post(
            `${import.meta.env.VITE_API_BASE_URL || ''}/account`,
            { accountId: id },
            {
              headers: { 'Content-Type': 'application/json' }
            }
        );

        // If your backend returns something like { success: true, password: "..." }
        if (response.data && response.data.password) {
          this.messageType = 'success';
          this.message = `Account created! Your password is: ${response.data.password}`;
        } else {
          // If your backend returns { success: false, message: "..." }
          this.messageType = 'error';
          this.message = response.data.message || 'Unexpected response';
        }
      } catch (err) {
        // Handle errors (e.g. 400/409 with JSON { error: "..." })
        this.messageType = 'error';
        this.message = err.response.data.message;
      }
    }
  }
}
</script>

<style>
/* Center the body content */
body {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  margin: 0;
  background-color: #f5f5f5;
  font-family: Arial, sans-serif;
}

/* Larger form container */
.form-container {
  background-color: #ffffff;
  padding: 3rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 500px;          /* Increased width */
  box-sizing: border-box;
  text-align: center;
}

/* Bigger title */
.form-container h2 {
  margin-top: 0;
  margin-bottom: 2rem;
  font-size: 2rem;       /* Larger font */
  color: #333333;
}

/* Larger input field */
.form-container input[type="text"],
.form-container input[type="email"],
.form-container input[type="password"] {
  width: 100%;
  padding: 1rem 0.75rem; /* Increased vertical padding */
  margin-bottom: 1.5rem; /* More space below */
  border: 1px solid #cccccc;
  border-radius: 4px;
  font-size: 1.1rem;     /* Slightly bigger text */
  box-sizing: border-box;
}

/* Larger create button */
.form-container button {
  width: 100%;
  padding: 1rem;         /* Increased padding */
  background-color: #4CAF50;
  color: #ffffff;
  border: none;
  border-radius: 4px;
  font-size: 1.1rem;     /* Slightly bigger text */
  cursor: pointer;
}

.form-container button:hover {
  background-color: #45a049;
}

.success {
  color: green;
  margin-top: 1rem;
}

.error {
  color: red;
  margin-top: 1rem;
}

</style>