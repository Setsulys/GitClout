<template>
  <div class="chartMenu">
    <div class="title">
      <h2 class="ui header">
        <i class="chart bar icon white"></i>
        <div class="content white">
          <div class="white">
            Moyenne de ligne écrite par personne par langage
          </div>
          <div class="sub header white">Valeurs en dur</div>
        </div>
      </h2>
    </div>
    <div class="chartCard">
      <div class="chartBox">
        <div class="wrapper">
          <div class="colLarge">
            <div class="radarbox">
              <div v-for="(data, index) in chartDataList" :key="index">
                <canvas :id="'radarChartpage' + index" width="400" height="400"></canvas>
              </div>
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
  data() {
    return {
      // Create an array of radar chart data
      chartDataList: [
        {
          labels: ['java', 'markdown', 'makefile', 'javascript', 'git'],
          datasets: [{
            label: 'Moyenne de ligne pour Steven',
            data: [800, 500, 20, 500, 100],
            backgroundColor: 'rgba(64, 120, 192, 0.7)',
          }]
        },
        {
          labels: ['java', 'markdown', 'makefile', 'javascript', 'git'],
          datasets: [{
            label: 'Moyenne de ligne pour Julien',
            data: [300, 200, 50, 200, 30],
            backgroundColor: 'rgba(64, 120, 192, 0.7)',
          }]
        },
        {
          labels: ['java', 'markdown', 'makefile', 'javascript', 'git'],
          datasets: [{
            label: 'Moyenne de ligne pour Yassine',
            data: [100, 28, 500, 15, 300],
            backgroundColor: 'rgba(64, 120, 192, 0.7)',
          }]
        },
        {
          labels: ['java', 'markdown', 'makefile', 'javascript', 'git'],
          datasets: [{
            label: 'Moyenne de ligne pour Christophe',
            data: [200, 600, 30, 100, 70],
            backgroundColor: 'rgba(64, 120, 192, 0.7)',
          }]
        },
      ],
    };
  },
  methods: {
    initializeRadarChart(index) {
      const config = {
        type: 'radar',
        data: this.chartDataList[index],
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

      const ctx = document.getElementById(`radarChartpage${index}`).getContext('2d');
      new Chart(ctx, config);
    }
  },
  mounted() {
    // Initialize radar charts for each set of data
    this.chartDataList.forEach((data, index) => {
      this.initializeRadarChart(index);
    });
  }
}
</script>

<style>
.radarbox{
  max-width: 100%;
  //width: calc(4000px - 35px);
  height: 500px;
  display:flex;
  overflow-x: auto;
}

</style>