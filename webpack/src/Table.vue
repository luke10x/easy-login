<template>
  <div>
    <h2>Table</h2>
    {{ error }}
    <div v-if="error">
      Error: {{ error }}
    </div>

    <ul v-if="names.length">
      <li v-for="name in names" :key="name.id">{{ name.name }} {{ name.desc }}</li>
    </ul>
    <p v-else>No names found.</p>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';

export default {
  name: 'NamesList',
  setup() {
    const names = ref([]);
    const error = ref(undefined);

    // Fetch names data from the API
    const fetchNames = async () => {

      try {
        const response = await fetch('data.json');
        if (response.ok) {
          const data = await response.json();
          names.value = data;
        } else {
          throw new Error('Failed to fetch names');
        }
      } catch (e) {
        error.value = "problem with fetching: " + e
        console.log("bibgg gd")
      }
    };

    // Fetch names on component mount
    onMounted(fetchNames);

    return {
      names, error
    };
  }
};
</script>

<style scoped>
h2 {
  font-size: 24px;
  margin-bottom: 16px;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 8px;
}
</style>
