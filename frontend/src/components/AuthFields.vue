<!-- src/components/AuthFields.vue -->
<template>
  <div class="auth-fields form-container">
    <h2>Authorization</h2>
    <div class="field">
      <input
          id="username"
          type="text"
          :value="username"
          @input="$emit('update:username', $event.target.value)"
          placeholder="Username"
          required
      />
    </div>

    <div class="field">
      <input
          id="password"
          type="password"
          :value="password"
          @input="$emit('update:password', $event.target.value)"
          placeholder="Password"
          required
      />
    </div>
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
  }
}
</script>

<style scoped>
.auth-fields {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-template-rows: auto auto;
  gap: 1rem;
  margin-bottom: 2rem;

  /* Border around the entire container */
  border: 1px solid #ccc;
  padding: 1rem;
  border-radius: 4px;
}

/* Make the <h2> span both columns */
.auth-fields h2 {
  grid-column: 1 / -1;
  margin: 0 0 0.5rem;
  font-size: 1.25rem;
  text-align: center;
}

/* Each input field sits in its own grid cell */
.field {
  display: flex;
  flex-direction: column;
}

input[type="text"],
input[type="password"] {
  width: 100%;
  padding: 0.5rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
</style>
