

<template>
  <div class ="gitMenu">
    <div class="title">
      <h2 class="ui header">
        <i class="github icon"></i>
        <div class="content">
          Liens des projets deja Blamés
          <div class="sub header">Cliquez dessus</div>
        </div>
      </h2>
    </div>
    <div class="gitCard">
      <div class ="gitBox">
        <div class="wrapper">
          <div class ="gitcolLarge">
            <div class="gitboxbox">
              <div class="ui relaxed divided list">
                <div v-for="(item, index) in links" :key="index" class="item">
                  <i class="large github middle aligned icon"   @click="openInNewTab('https://www.youtube.com/watch?v=GtL1huin9EE')"></i>
                  <div class="content" @click="openInNewTab('https://www.youtube.com/watch?v=GtL1huin9EE')">
                    <a class="header">{{ item }}</a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name:'GitPage',
  data(){
  return{
    links:[],
  };
  },
  methods:{
    openInNewTab(url) {
      window.open(url, '_blank', 'noreferrer');
    },
  },
  mounted(){
    fetch('/app/rest/getlink')
        .then(response => response.json())
        .then(data =>{
          this.links= data;
          if(this.links.length!==0){
            this.$emit("haveData");
          }
        })
        .catch(error=>{
          console.error('Erreur lors de la récupération des liens', error);
        });

  }
}
</script>
<style>
.gitMenu p{
  font-size: 30px;
  margin-top: 50vh;
  display: flex;
  justify-content: center;
}

.gitCard{
  width: 100vw;
  height: calc(20vh - 40px);
  display: flex;
  align-items: center;
  justify-content: center;
  //overflow-y: auto;
}

.gitBox{
  max-height: calc(20vh - 40px);

  width: auto;
  min-width: 36%;
  max-width: 80%;
  padding: 20px;
  border-radius: 20px;
  border: solid 3px rgba(0, 0, 0, 1);
  background: white;
  overflow-y:auto;
}

.gitcolLarge{
  display: flex;
  justify-content: center;
  max-width: 100%;
  overflow-x: auto;
//overflow-y: auto;
}
.gitboxbox{
  width: auto;
  flex-grow: 1;
  display:flex;
}
.title{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 5vh;
  margin-bottom: 1vh;
}
</style>