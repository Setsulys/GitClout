<template>
  <div>
    <h2>New Tab Component</h2>
    <!-- Use the data passed through query to display content -->
    <p>{{ $route.query.data }}</p>
  </div>
</template>

<script>
export default {
  name: 'NewTabComponent',
}
</script>
