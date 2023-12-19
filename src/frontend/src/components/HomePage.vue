
<template>
    <h1>{{msg}}</h1>
    <h3> revenez plus tard</h3>
    <h3>Inserez votre lien GIT</h3>
    <div class="ui large action input">
      <input class="wide-input" v-model="text" @keyup.enter="onclick" placeholder="https://gitlab.com/nom/projet">
        <div class="ui button" @click="onclick" >Gitclouting</div>
    </div>
  <div class="title">
    <div class v-if="!isFirstTime">
      <div class v-if="!isRunnable">
        <div class="title">
          <h2 class="ui header">
            <i class="exclamation circle icon error"></i>
            <div class="content">
              <small class="error">Ce n'est pas un repo git</small>
            </div>
          </h2>
        </div>
      </div>
    </div>
  </div>
  <GitPage/>
  <chart-page/>
</template>

<script>
import axios from 'axios';
import ChartPage from "@/components/ChartPage.vue";
import GitPage from "@/components/GitPage.vue";

export default {
  name: 'HomePage',
  components:{
    ChartPage,
    GitPage,
  },
  data() {
    return {
      msg: '',
      text: '',
      isRunnable:false,
      isFirstTime:true,
    };
  },

  methods: {

    reloadCurrentPage(){
      window.location.reload();
    },
    onclick(){
      this.submit();
      this.openNewTab();
    },
    openNewTab(){
      /*      const newTab = './HelloWorld.vue';
            window.open(newTab,'_blank');*/
      //this.$router.push({name:'HelloWorld'})
      const newTab = window.open('', '_blank');
      newTab.location.href = this.$router.resolve({ name: 'NewFile' }).href;
    },
    submit() {
      const data = {
        gitLink: this.text,
      };
      axios
          .post('/app/rest/toTheBack', data)
          .then((response) => {
            console.log(response.data);
            this.isRunnable=response.data;
            if(this.isRunnable){
              this.reloadCurrentPage();
            }
            else{
              this.isRunnable=false;
            }
            this.isFirstTime=false;
          })
          .catch((error) => {
            console.error(error);
          });
    },
  },

  mounted() {
    fetch('/app/rest/hello')
        .then((response) => response.text())
        .then((data) => {
          this.msg = data;
        })
        .catch((error) => {
          console.error('Error fetching data:', error);
        });
  },

};
</script>
<style>
.wide-input{
min-width: 600px;
width: 50%;
}

.error{
  color: darkred;
}
</style>
