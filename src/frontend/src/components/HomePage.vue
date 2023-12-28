
<template>
  <h1>{{msg}}</h1>
  <h3> revenez plus tard</h3>
  <h3>Inserez votre lien GIT</h3>
  <div class="ui large action input">
    <input class="wide-input grayBackground" v-model="text" @keyup.enter="onclick" placeholder="https://gitlab.com/nom/projet">
    <div class="ui button" @click="onclick" >Gitclouting</div>
  </div>
  <div class="title">
    <div class v-show="loading">
      <div class="ui active centered inline loader"></div>
      <p>{{percent}}%</p>
      <p>Traitement du git</p>
    </div>
  </div>

  <div class="title">
      <div class v-show="!isRunnable">
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
  <div v-show="showElement">
    <div v-show="isRunnable">
      <div class="ui compact menu grayBackground">
        <div class="ui simple dropdown item grayBackground">
          <i class="tag icon red"></i>
          {{ selectedTag==='undo'?'Project Tag':selectedTag }}
          <i class="dropdown icon"></i>
          <div class="menu">
            <div class="choice">
              <div class="item" v-for="choice in choices" :key="choice" @click="selectTag(choice)">
                <i class="tags icon red"></i>
                {{ choice }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div v-show="selectedTag !== 'undo'">
    <div v-if="isRunnable">
      <ChartPage :selected-tag-name="selectedTag"/>
      <RadarChartPage :project-name="text"/>
    </div>
  </div>
  <GitPage @haveData="handleGitEvent"/>
</template>

<script>
import axios from 'axios';
import ChartPage from "@/components/ChartPage.vue";
import GitPage from "@/components/GitPage.vue";
import RadarChartPage from "@/components/RadarChartPage.vue";

export default {
  name: 'HomePage',
  components:{
    ChartPage,
    GitPage,
    RadarChartPage,
  },
  data() {
    return {
      msg: '',
      text: localStorage.getItem('projectText') || '',
      isRunnable:true,
      loading:false,
      percent:0,
      choices:[
          'undo',
        'Tag 1', 'Tag 2', 'Tag 3', 'Tag 4', 'Tag 5',
        'Tag 6', 'Tag 7', 'Tag 8', 'Tag 9', 'Tag 10',
        'Tag 11', 'Tag 12', 'Tag 13', 'Tag 14', 'Tag 15'
      ],
      selectedTag: 'undo',
      showElement:false,
    };
  },

  methods: {
    handleGitEvent(){
      this.showElement=true;
    },
    selectTag(choice) {
      if(choice==='undo'){
        this.selectedTag='';
      }
      this.selectedTag = choice;
    },
    reloadCurrentPage(){
      localStorage.setItem('projectText', this.text);
      window.location.reload();

    },
    onclick(){
      localStorage.setItem('isNotFirstTime', 'true');
      this.submit();
      this.loading=true;

    },
    submit() {
      const data = {
        gitLink: this.text,
      };
      axios.post('/app/rest/toTheBack', data)
          .then((response) => {
            console.log(response.data);
            this.isRunnable=response.data;
/*            this.getTags();*/
            if(this.isRunnable){
              this.reloadCurrentPage();
            }

            this.loading=false;
            return response.data;
          })
          .catch((error) => {
            console.error(error);
          });

    },
    checkPercent(){
      const data = {
        gitLink: this.text,
      };
      axios.post('app/rest/percentFinished',data)
          .then((response)=>{
/*            console.log(response.data);*/
            if(response.data!=null){
              this.percent=Number(response.data).toFixed(2);
            }
          }).catch(error=>{
        console.error('Error fetching percent',error);
      });
    },
    /*getTags(){
      const data = {
        gitLink: this.text,
      };
      axios.post('app/rest/getTags',data)
          .then((response)=>{
            const r = response.data;
            this.choices.push(r.tag);
          }).catch(error=>{
            console.error('Error fetching tags',error);
      });
    },*/
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
    this.checkPercent();
    setInterval(() =>{
      this.checkPercent();
    },2000);
  },

};
</script>
<style>
.wide-input{
min-width: 600px;
width: 50%;
}

.error{
  color: darkgoldenrod;
}
.title{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 5vh;
  margin-bottom: 1vh;
}
.choice{
  overflow-y:auto;
  max-height: 30vh;
}

.grayBackground{
  background-color: rgb(23,29,25,0.3);
}
</style>
