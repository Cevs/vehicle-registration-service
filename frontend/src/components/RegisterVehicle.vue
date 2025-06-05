<template>

  <div class="form-container" style="margin-top: 80px;">
    <h2>Register Vehicle</h2>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <input
            id="registrationCode"
            type="text"
            v-model="registrationCode"
            required
            placeholder="Enter registration code"
        />
      </div>

      <div class="form-group">
        <input
            id="validUntil"
            type="date"
            v-model="validUntil"
            required
        />
      </div>

      <button type="submit">Submit</button>
      <div
          v-if="messageType === 'success'"
          class="success-message">
        <p class="message">{{ message }}</p>
      </div>

      <div
          v-if="messageType === 'error'"
          class="error-message error-display">
        <p class="message">{{ message }}</p>
      </div>

    </form>
  </div>
</template>

<script>
import Authorization from './Authorization.vue'
import axios from 'axios'

export default {
  name: 'RegisterVehicle',
  components: {
    AuthFields: Authorization
  },
  data() {
    return {
      // Bound via Authorization.vue
      username: '',
      password: '',
      // Vehicle fields
      registrationCode: '',
      validUntil: '',
      message:'',
      messageType:''
    }
  },
  methods: {
    async submitForm() {
      const storedUsername = localStorage.getItem("username");
      const storedPassword = localStorage.getItem("password");
      const apiHost = import.meta.env.VITE_API_BASE_URL
      const credentials = btoa(`${storedUsername}:${storedPassword}`)

      const payload = {
        registrationCode: this.registrationCode,
        validUntil: this.validUntil
      }

      try {
        const response = await axios.post("/register", payload, {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Basic ${credentials}`,
          },
        });

        this.messageType = "success"
        this.message = response.data.description

        //Reset form
        this.registrationCode = ''
        this.validUntil = ''
      } catch (err) {

        if (err.response && err.response.data && err.response.data.description) {
          this.message = err.response.data.description
        } else if (err.response.data.error) {
          this.message = err.response.data.error
        } else{
          this.message = err.message;
        }
        this.messageType = "error";

      }
    }
  }
}
</script>

<style scoped>

</style>
