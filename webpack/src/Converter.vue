
<template>
  <div>
    <h2>Kilograms to Pounds Converter</h2>
    <div>
      <label for="kilograms">Kilograms:</label>
      <input type="number" id="kilograms" v-model="kilograms" />
    </div>
    <div>
      <button @click="convert">Convert</button>
    </div>
    <div v-if="result">
      <p>Result: {{ kilograms }} kilograms = {{ result }} pounds</p>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  name: 'KilogramsToPoundsConverter',
  setup() {
    const kilograms = ref(0);
    const result = ref(null);

    const convert = () => {
      const pounds = kilograms.value * 2.20462;
      result.value = pounds.toFixed(2);
    };

    return {
      kilograms,
      result,
      convert
    };
  }
};
</script>

<style scoped>
h2 {
  font-size: 24px;
  margin-bottom: 16px;
}

label {
  margin-right: 8px;
}

button {
  padding: 8px 16px;
}
</style>