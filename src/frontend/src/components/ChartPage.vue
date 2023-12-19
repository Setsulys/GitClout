<template>
  <div class ="chartMenu">
    <div class="title">
      <h2 class="ui header">
        <i class="chart bar icon"></i>
        <div class="content">
          Statistiques du git blamé
          <div class="sub header">Valeurs en dur</div>
        </div>
      </h2>
    </div>
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
  methods:{
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
  background: white;
  align-items: center;
  justify-content: center;
  margin-top: 1vh;
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

.title{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 5vh;
  margin-bottom: 1vh;
}
</style>