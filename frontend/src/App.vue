<template>
  <div id="app">
    <main>
      <AccountForm />
    </main>
  </div>
</template>

<script>
import AccountForm from './components/AccountForm.vue';

export default {
  name: 'App',
  components: { AccountForm },
};
</script>

<style>
body {
  font-family: sans-serif;
}
header {
  text-align: center;
  padding: 1rem;
  background-color: #f4f4f4;
  margin-bottom: 2rem;
}
</style>
