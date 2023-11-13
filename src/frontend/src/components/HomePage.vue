<template>
    <h1>{{msg}}</h1>
    <h3> Site en production</h3>
    <h3> revenez plus tard</h3>
    <h3>Inserez votre lien GIT</h3>




    <div class="ui large action input">
          <input class="wide-input" v-model="text" placeholder="https://gitlab.com/nom/projet">
          <div class="ui button" @click="submit">Gitclouting</div>
    </div>

    <!--<input v-model="text" placeholder="https://gitlab.com/nom/projet">-->
    <!--<button class="ui primary button" @click="submit">Gitclouting</button>-->
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
        axios.post("/app/messages/toTheBack",data)
        .then((response) => {
            console.log(response.data);
        })
        .catch((error) =>{
        console.error(error);
        });
     },
  },
  mounted(){
    fetch("/app/messages/hello")
    .then((response) => response.text())
    .then((data) =>{
        this.msg = data;
  })
  }
}
</script>

.wide-input{
min-width: 600px;
width: 50%;
}

