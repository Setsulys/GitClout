<template>
  <div class ="chartMenu">
    <div class="title">
      <h2 class="ui header">
        <i class="chart bar icon white"></i>
        <div class="content white">
          <div class="white">
            Statistiques du git blamé
          </div>
          <div class="sub header white">Valeurs en dur</div>
        </div>
      </h2>
    </div>
    <div class="chartCard">
      <div class ="chartBox">
        <div class="wrapper">
          <div class ="colLarge">
            <div class="barBox">
              <canvas id="barChart"></canvas>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Chart from 'chart.js/auto';

export default {
  data(){
    return {
      chartData: {
        labels: [], // Populate this dynamically
        datasets: [
          {
            label: 'javascript',
            backgroundColor: 'Orange',
            data: [], // Populate this dynamically
            //barThickness: 20,
          },
          {
            label: 'java',
            backgroundColor: 'Yellow',
            data: [], // Populate this dynamically
            //barThickness: 20,
          },
          {
            label: 'markdown',
            backgroundColor: 'lightblue',
            data: [], // Populate this dynamically
            //barThickness: 20,
          },
          {
            label: 'makefile',
            backgroundColor: 'gray',
            data: [], // Populate this dynamically
            //barThickness: 20,
          },
          {
            label: 'configuration',
            backgroundColor: 'purple',
            data: [], // Populate this dynamically
            //barThickness: 20,
          },
        ],
      },
    };
  },
  methods:{
    initializeChart() {
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
          responsive: true,
          maintainAspectRatio: this.chartData.labels.length < 10,
          legend: { position: 'left' },
          scales : {
            x:{
              barPercentage :0.9,
            }
          },
        },
      };
      const ctx = document.getElementById('barChart').getContext('2d');
      new Chart(ctx, config);
    },
  },
  mounted() {
    this.chartData.labels = ["Steven","julien","kingue","yassine","christophe","stephane","kevin","ben","alexandre","catherine","aurélie","hugo","pierre","sylvain","lucas","jean","michael","max","lewis"]; // Populate labels array
    this.chartData.datasets[0].data = [20, 559, 5, 56, 58,68, 59, 2, 45,18]; // Populate javascript data array
    this.chartData.datasets[1].data = [1200, 59, 5, 56, 58,12, 59, 85, 23]; // Populate java data array
    this.chartData.datasets[2].data = [12, 59, 5, 56, 58, 12, 59, 12, 74]; // Populate markdown data array
    this.chartData.datasets[3].data = [20, 0, 0, 0, 0, 0, 0, 0, 0]; // Populate makefile data array
    this.chartData.datasets[4].data = [150, 59, 0, 0, 0, 0, 0, 0, 0]; // Populate configuration data array

    this.initializeChart();
  }
}


</script>
<style>
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
  margin-top: 1vh;
}

.chartBox{
  width: 80%;
  padding: 20px;
  border-radius: 20px;
  border: solid 3px rgba(0, 0, 0, 1);
  background: rgba(243,249,245,0.7);
  display: gl;
  align-items: center;
  justify-content: center;
  margin-top: 1vh;
}

.colLarge{
  max-width: 100%;
  overflow-x: auto;
}
.barBox{
  height: 500px;
  width: calc(4000px - 35px);
}

.title{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 5vh;
  margin-bottom: 1vh;
  color:white;
}
</style>