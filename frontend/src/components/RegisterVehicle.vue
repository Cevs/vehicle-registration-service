<!-- src/components/RegisterVehicle.vue -->
<template>

  <AuthFields
      v-model:username="username"
      v-model:password="password"
  />

  <div class="form-container" style="margin-top: 80px;">
    <h2>Register Vehicle</h2>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="registrationCode">Registration Code</label>
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
        <label for="validUntil">Valid Until</label>
      </div>

      <button type="submit">Submit</button>
      <p v-if="message" :class="messageType">{{ message }}</p>
    </form>
  </div>
</template>

<script>
import AuthFields from './AuthFields.vue'
import axios from 'axios'

export default {
  name: 'RegisterVehicle',
  components: {
    AuthFields
  },
  data() {
    return {
      // Bound via AuthFields.vue
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
      const apiHost = import.meta.env.VITE_API_BASE_URL
      const credentials = btoa(`${this.username}:${this.password}`)
      console.log("Username: " + this.username)
      console.log("password: " + this.password)

      const payload = {
        registrationCode: this.registrationCode,
        validUntil: this.validUntil
      }

      try {
        const response = await axios.post(`${apiHost}/register`, payload, {
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
          this.message = err.response.data.description;
        } else {
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
