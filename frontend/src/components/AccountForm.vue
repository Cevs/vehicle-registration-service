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

</style>