
<template>
    <h1>{{msg}}</h1>
    <h3> revenez plus tard</h3>
    <h3>Inserez votre lien GIT</h3>
    <div class="ui large action input">
          <input class="wide-input" v-model="text" placeholder="https://gitlab.com/nom/projet">
          <div class="ui button" @click="onclick" >Gitclouting</div>
    </div>
    <div class ="gitMenu">
      <div class="gitCard">
        <div class ="gitBox">
          <div class="wrapper">
            <div class ="gitcolLarge">
              <div class="gitboxbox">
                <div class="ui relaxed divided list">
                  <div v-for="(item, index) in links" :key="index" class="item">
                    <i class="large github middle aligned icon"   @click="openInNewTab('https://www.youtube.com/watch?v=dQw4w9WgXcQ')"></i>
                    <div class="content" @click="openInNewTab('https://www.youtube.com/watch?v=dQw4w9WgXcQ')">
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
    <div class ="chartMenu">
      <div class="chartCard">
        <div class ="chartBox">
          <div class="wrapper">
            <div class ="colLarge">
              <div class="box">
                <canvas id="radarChart" width="400" height="400"></canvas>
                <canvas id="barChart"></canvas>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
import axios from 'axios';
import Chart from 'chart.js/auto';

export default {
  name: 'HomePage',
  data() {
    return {
      msg: '',
      text: '',
      links:[],
      chartData: {
        labels: [], // Populate this dynamically
        datasets: [
          {
            label: 'javascript',
            backgroundColor: 'Orange',
            data: [], // Populate this dynamically
            barThickness: 20,
          },
          {
            label: 'java',
            backgroundColor: 'Yellow',
            data: [], // Populate this dynamically
            barThickness: 20,
          },
          {
            label: 'markdown',
            backgroundColor: 'lightblue',
            data: [], // Populate this dynamically
            barThickness: 20,
          },
          {
            label: 'makefile',
            backgroundColor: 'gray',
            data: [], // Populate this dynamically
            barThickness: 20,
          },
          {
            label: 'configuration',
            backgroundColor: 'purple',
            data: [], // Populate this dynamically
            barThickness: 20,
          },
        ],
      },
      radarChartData:{
          labels: [],
          datasets: [{
            label: 'nb of lines for a Language',
            data: [],
            backgroundColor: 'rgba(64, 120, 192, 0.7)',
          }]
      },
    };
  },

  methods: {
    openInNewTab(url) {
      window.open(url, '_blank', 'noreferrer');
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
            // Update chart data based on the response if needed
          })
          .catch((error) => {
            console.error(error);
          });
    },

    initializeChart() {
      const box = document.querySelector('.box');
      const boxWidth = Math.max(this.chartData.labels.length*150 ,10)
      box.style.width = `calc(${boxWidth}px)`;
      const config ={
        type: 'bar',
        data: this.chartData,
        options: {
          tooltips: {
            displayColors: true,
            callbacks: {
              mode: 'x',
            },
          },
          scales: {
            xAxes: [
              {
                barPercentage: 1,
                categoryPercentage: 0.5,
                stacked: true,
                gridLines: {
                  display: false,
                },
              },
            ],
            yAxes: [
              {
                stacked: true,
                ticks: {
                  beginAtZero: true,
                },
                type: 'linear',
              },
            ],
          },
          responsive: true,
          maintainAspectRatio: this.chartData.labels.length < 10,
          legend: { position: 'left' },
        },
      };
      const ctx = document.getElementById('barChart').getContext('2d');
      new Chart(ctx, config);
    },
    initializeRadarChart(){
      const config = {
        type: 'radar',
        data: this.radarChartData,
        options: {
          responsive: false,
          maintainAspectRatio: false,
          width: 2000,
          height: 2000,
          elements: {
            line: {
              borderWidth: 3
            }
          }
        },
      };

      var ctx = document.getElementById("radarChart").getContext('2d');
      new Chart(ctx,config);
    }
  },

  mounted() {
    fetch('/app/rest/hello')
        .then((response) => response.text())
        .then((data) => {
          this.msg = data;
          // Update chart data based on the received data if needed
          this.chartData.labels = ["Steven","julien","kingue","yassine","christophe","stephane","kevin","ben","alexandre","carine","carine","carine","carine","carine","julien","kingue","yassine","christophe","stephane","kevin","ben","alexandre","carine","carine","carine","carine","carine","julien"]; // Populate labels array
          this.chartData.datasets[0].data = [20, 559, 5, 56, 58,68, 59, 2, 45,18]; // Populate javascript data array
          this.chartData.datasets[1].data = [1200, 59, 5, 56, 58,12, 59, 85, 23]; // Populate java data array
          this.chartData.datasets[2].data = [12, 59, 5, 56, 58, 12, 59, 12, 74]; // Populate markdown data array
          this.chartData.datasets[3].data = [20, 0, 0, 0, 0, 0, 0, 0, 0]; // Populate makefile data array
          this.chartData.datasets[4].data = [150, 59, 0, 0, 0, 0, 0, 0, 0]; // Populate configuration data array

          this.radarChartData.labels=['java', 'markdown', 'makefile', 'javascript', 'git'];
          this.radarChartData.datasets[0].data =[800, 500, 20, 500, 100];


          // Call the method to initialize or update the chart
          this.initializeChart();
          this.initializeRadarChart();
        })
        .catch((error) => {
          console.error('Error fetching data:', error);
        });
    fetch('/app/rest/getlink')
        .then(response => response.json())
        .then(data =>{
            this.links= data;
        })
        .catch(error=>{
          console.error('Erreur lors de la récupération des liens', error);
        });
  },

};
</script>
<style>
.wide-input{
min-width: 600px;
width: 50%;
}

.chartMenu p{
  padding: 10px;
  font-size: 20px;
}

.chartCard{
  width: 100vw;
  height: calc(80vh - 40px);
  display: flex;
  align-items: center;
  justify-content: center;
}

.chartBox{
  width: 80%;
  padding: 20px;
  border-radius: 20px;
  border: solid 3px rgba(0, 0, 0, 1);
  background: white;
}

.colLarge{
  display: flex;
  max-width: 100%;
  overflow-x: auto;
}
.box{
  width: calc(4000px - 35px);
  height: 500px;
  display:flex;
}


.gitMenu p{
  font-size: 30px;
  margin-top: 50vh;
}

.gitCard{
  width: 100vw;
  height: calc(30vh - 40px);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow-y: auto;
}

.gitBox{
  max-height: 20vh;

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
  overflow-y: auto;
}
.gitboxbox{
  width: auto;
  flex-grow: 1;
  display:flex;
}
</style>
