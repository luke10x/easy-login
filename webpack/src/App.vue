<template>
  <div>
    <h1>Vue application</h1>
    <Counter />
    <Converter />
    <Table />
  </div>
</template>

<script>
  import Counter from './Counter.vue';
  import Converter from './Converter.vue';
  import Table from './Table.vue';

  export default {
    components: {
      Counter,
      Converter,
      Table
    }
  };
</script>
