<template>
  <div class="form-container validate-registration">
    <h2>Validate Vehicle Registration</h2>
    <form @submit.prevent="validateRegistration">
      <input
          id="registrationCode"
          v-model="registrationCode"
          type="text"
          placeholder="AB-123-CD"
          required
      >
      <button type="submit">Validate</button>
    </form>

    <div
        v-if="messageType === 'success'"
        class="success-message vehicle-registration-validation">
      <p class="message">{{ message }}</p>
      <p class="label">Valid Until:</p>
      <p class="value">{{ validUntil }}</p>
    </div>

    <div
        v-if="messageType === 'error'"
        class="error-message error-display">
      <p class="message">{{ message }}</p>
    </div>

    <div
        v-if="messageType === 'warning'"
        class="warning-message vehicle-registration-validation">
      <p class="message">{{ message }}</p>
      <p class="label">Valid Until:</p>
      <p class="value">{{ validUntil }}</p>
    </div>

  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "VehicleRegistration",
  data() {
    return {
      username: "",
      password: "",
      registrationCode: "",
      validUntil: "",
      message: "",
      messageType: "",
    };
  },
  methods: {
    async validateRegistration() {
      const storedUsername = localStorage.getItem("username");
      const storedPassword = localStorage.getItem("password");
      const apiHost = import.meta.env.VITE_API_BASE_URL;
      const credentials = btoa(`${storedUsername}:${storedPassword}`);

      try {
        const response = await axios.get(
            // Adjust path if needed, e.g. `${apiHost}/registration/${this.registrationCode}`
            `${apiHost}/registration/registrationCode`,
            {
              headers: {
                "Content-Type": "application/json",
                "Registration-Code": this.registrationCode,
                Authorization: `Basic ${credentials}`,
              },
            }
        );
        this.message = response.data.message;
        if(this.message.includes("expired")) {
          this.messageType = "warning";
        } else {
          this.messageType = "success";
        }

        this.validUntil = response.data.validUntil;

      } catch (err) {
        if (
            err.response &&
            err.response.status === 404 &&
            err.response.data &&
            err.response.data.message
        ) {
          this.message = err.response.data.message;
        } else {
          this.message = err.message;
        }
        this.messageType = "error";
      }
    },
  },
};
</script>

<style scoped>
.validate-registration {
  width: 700px !important;
}

.vehicle-registration-validation {
  padding: 10px;
  margin-top: 20px;
}

</style>
