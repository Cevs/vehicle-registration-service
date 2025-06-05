<template>
  <div class="form-container">
    <h2>Create Account</h2>
    <form @submit.prevent="createAccount">
      <input
          id="accountId"
          v-model="accountId"
          type="email"
          placeholder="you@example.com"
          required
      >
      <button type="submit">Create</button>
    </form>

    <!-- Success message: use v-html so only the password part is bold -->
    <div v-if="messageType === 'success'" class="success-message">
      <p class="message" v-html="message"></p>
    </div>

    <!-- Error message (unchanged) -->
    <div v-if="messageType === 'error'" class="error-message">
      <p class="message">{{ message }}</p>
    </div>
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
      messageType: ''
    };
  },
  methods: {
    async createAccount() {
      // Reset previous messages
      this.message = '';
      this.messageType = '';

      const id = this.accountId.trim();
      if (!id) {
        this.messageType = 'error';
        this.message = 'Please enter a valid email address.';
        return;
      }

      try {
        const response = await axios.post(
            "/account",
            { accountId: id },
            {
              headers: { 'Content-Type': 'application/json' }
            }
        );

        if (response.data && response.data.password) {
          this.messageType = 'success';
          // Wrap only the password in <strong>…</strong>
          this.message = `${response.data.message} Your password is: <strong>${response.data.password}</strong>`;
        } else {
          this.messageType = 'error';
          this.message = response.data.message || 'Unexpected response from the server.';
        }
      } catch (err) {
        this.messageType = 'error';

        if (err.response && err.response.status === 409) {
          this.message = err.response.data && err.response.data.message
              ? err.response.data.message
              : 'An account with that email already exists.';
        }
        else if (err.response && err.response.data && err.response.data.message) {
          this.message = err.response.data.message;
        }
        else {
          this.message = err.message;
        }
      }
    }
  }
};
</script>

<style scoped>

.success-message {
  background-color: #e6f9e6;
  color: #256d25;
  padding: 10px;
  margin-top: 15px;
  border-radius: 4px;
}

.error-message {
  background-color: #f8d7da;
  color: #842029;
  padding: 10px;
  margin-top: 15px;
  border-radius: 4px;
}

.success-message .message,
.error-message .message {
  margin: 0;
  font-weight: normal !important;
}
</style>
