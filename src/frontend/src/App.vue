<template>
  <body>
  <img src="./assets/gclout.png">
  <HomePage/>
  <div id="app">

  </div>
  </body>
</template>

<script>
import HomePage from './components/HomePage.vue'

export default {
  name: 'App',
  components: {
    HomePage,
  },
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  background-color: rgba(43,49,55,0.7);
  margin-top: 60px;
}
img{
  width: 18%;
}
body{
  background-color: rgba(43,49,55,0.7);
  color:white;
}
</style>

