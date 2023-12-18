<template>
  <img src="./assets/gclout.png">
  <HomePage @openNewFile ="openNewFile"/>
    <NewFile v-if="showNewFile  " @closeNewFile = "closeNewFile"/>
  <div id="app">

  </div>
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
  data(){
    return{
      showNewFile : false,
    };
  },

  methods:{
    openNewFile(){
      this.showNewFile = true;
    },
    closeNewFile(){
      this.showNewFile = false;
    }

  }
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
</style>
