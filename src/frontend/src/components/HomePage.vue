<template>
    <h1>{{msg}}</h1>
    <h3> revenez plus tard</h3>
    <h3>Inserez votre lien GIT</h3>

    <div class="ui large action input">
          <input class="wide-input" v-model="text" placeholder="https://gitlab.com/nom/projet">
          <div class="ui button" @click="submit">Gitclouting</div>
    </div>

  <div class ="chartMenu">
    <div class="chartCard">
      <div class ="chartBox">
        <div class="wrapper">
          <div class ="colLarge">
            <div class="box">
              <canvas id="myChart"></canvas>
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
    };
  },

  methods: {
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

      const ctx = document.getElementById('myChart').getContext('2d');
      new Chart(ctx, {
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
      });
    },

    // Other methods can go here if needed
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


          // Call the method to initialize or update the chart
          this.initializeChart();
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

.chartMenu p{
  padding: 10px;
  font-size: 20px;
}

.chartCard{
  width: 100vw;
  height: calc(100vh - 40px);
  display: flex;
  align-items: center;
  justify-content: center;
}

.chartBox{
  width: 700px;
  padding: 20px;
  border-radius: 20px;
  border: solid 3px rgba(255, 26, 104, 1);
  background: white;
}

.colLarge{
  max-width: 700px;
  overflow-x: auto;
}
.box{
  width: calc(4000px - 35px);
  height: 500px;
}
</style>
