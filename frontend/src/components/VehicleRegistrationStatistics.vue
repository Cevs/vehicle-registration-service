<!-- src/components/VehicleStatistics.vue -->
<template>
  <div class="container">
    <h2>Account Vehicle Registrations</h2>
    <div class="table-container">
      <table>
        <thead>
        <tr>
          <th>Account ID</th>
          <th>Registration</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(count, accountID) in stats" :key="accountID">
          <td>{{ accountID }}</td>
          <td>{{ count }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Authorization from "./Authorization.vue";

export default {
  name: "VehicleStatistics",
  components: {
    AuthFields: Authorization
  },
  data() {
    return {
      stats: {},
      message: "",
      messageType: ""
    }
  },
  methods: {
    async fetchStatistics() {
      const storedUsername = localStorage.getItem("username");
      const storedPassword = localStorage.getItem("password");

      if (!storedUsername || !storedPassword) {
        this.messageType = "error";
        this.message = "Please log in first (username/password not found).";
        return;
      }

      const apiHost = import.meta.env.VITE_API_BASE_URL;
      const credentials = btoa(`${storedUsername}:${storedPassword}`);

      try {
        const response = await axios.get(
            `${apiHost}/statistics/accountID`,
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Basic ${credentials}`
              }
            }
        );

        this.stats = response.data;
        this.messageType = "";
        this.message = "";
      } catch (err) {
        const serverMsg =
            err.response?.data?.message || err.response?.data?.description;

        this.messageType = "error";
        this.message = serverMsg || err.message;
        console.error("Error fetching statistics:", err);
      }
    }
  },
  mounted() {
    // Automatically fetch as soon as this component mounts:
    this.fetchStatistics();
  }
}
</script>

<style>
.container {
  width: 1000px;
  margin: 40px auto;
  padding: 0 16px;
}

.table-container {
  width: 100%;
  overflow-x: auto;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 400px; /* Ensures table doesn’t collapse too much */
}

thead {
  background-color: #2c3e50;
  color: #fff;
}

th,
td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

tbody tr:nth-child(even) {
  background-color: #f9f9f9;
}

thead th {
  position: sticky;
  top: 0;
  z-index: 1;
}
</style>
