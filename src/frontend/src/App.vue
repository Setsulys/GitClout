<template>
  <body>
  <img src="./assets/gclout.png">
  <HomePage/>
  <NewFile/>
  <div id="app">

  </div>
  </body>

</template>

<script>
import HomePage from './components/HomePage.vue'
import NewFile from "@/components/NewFile.vue";

export default {
  name: 'App',
  components: {
    HomePage,
    NewFile,
  },
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
img{
  width: 18%;
}
body{
  background-color: rgb(150, 153, 201,0.7);
}
</style>

