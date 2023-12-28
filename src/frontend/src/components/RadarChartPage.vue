<template>
  <div class="chartMenu">
    <div class="title">
      <h2 class="ui header">
        <i class="chart bar icon white"></i>
        <div class="content white">
          <div class="white">
            Moyenne de ligne écrite par personne par langage
          </div>
          <div class="sub header white">Valeurs en dur : {{projectName}}</div>
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
import axios from 'axios';

export default {
  props:{
    projectName:String,
  },
  data() {
    return {
      chartDataList: [
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
    },
    gatherData(){
      const data ={
        gitLink:this.projectName,
      };
      axios.post('app/rest/RadarData',data)
          .then((response)=>{
            const upperMap = response.data;
            console.log(upperMap);
            Object.entries(upperMap).forEach(([key,value])=>{
              console.log(`Key: ${key}`);
              console.log(`value : ${value}`)
              const newDataElement = {
                labels:['java','python','C'],
                datasets:[{
                  label:`Moyenne pour ${key}`,
                  data:[510,123,52],
                  backgroundColor: 'rgba(64, 120, 192, 0.7)',
                }]
              };
              this.chartDataList.push(newDataElement);
            });
            this.$nextTick(() => {
              console.log(this.chartDataList.length);
              this.chartDataList.forEach((chartData, index) => {
                console.log('data :' + chartData.labels);
                this.initializeRadarChart(index);
              });
            });
          })
          .catch((error)=>{
            console.error('Error fetching data:',error);
          });
    },
  },
  mounted() {
    this.chartDataList=[];
    this.gatherData();

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