<template>
    <h1>{{msg}}</h1>
    <h3> revenez plus tard</h3>
    <h3>Inserez votre lien GIT</h3>

    <div class="ui large action input">
          <input class="wide-input" v-model="text" placeholder="https://gitlab.com/nom/projet">
          <div class="ui button" @click="submit">Gitclouting</div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'HomePage',
  data(){
    return {
        msg:'',
        text:'',
    }
  },

  methods:{
    submit(){
        const data ={
            gitLink : this.text,
        };
        axios.post("/app/rest/toTheBack",data)
        .then((response) => {
            console.log(response.data);
        })
        .catch((error) =>{
        console.error(error);
        });
     },
  },
  mounted(){
    fetch("/app/rest/hello")
    .then((response) => response.text())
    .then((data) =>{
        this.msg = data;
  })
  }
}
</script>
<style>
.wide-input{
min-width: 600px;
width: 50%;
}
</style>
