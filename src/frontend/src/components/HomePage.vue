<template>
  <div>
    <h1>{{ msg }}</h1>
    <h3>Revenez plus tard</h3>
    <h3>Insérez votre lien GIT</h3>

    <div class="ui large action input">
      <input class="wide-input" v-model="text" placeholder="https://gitlab.com/nom/projet">
      <div class="ui button" @click="submit">Gitclouting</div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'HomePage',
  data() {
    return {
      msg: '',
      text: '',
    };
  },

  methods: {
    submit() {
      const data = {
        gitLink: this.text,
      };
      axios.post("/app/rest/toTheBack", data)
          .then((response) => {
            console.log(response.data);
            // Open a new tab with the component when the button is pressed

            const newTab = window.open('zizi', '_blank');
            newTab.document.open();
            newTab.document.write('<html><head><title>New Tab Component</title></head><body><div id="app">ceci est un zizi</div></body></html>');
            newTab.document.close();

            // Dynamically load Vue and the NewTabComponent script
            const vueScript = newTab.document.createElement('script');
            vueScript.src = "https://cdn.jsdelivr.net/npm/vue@2";
            vueScript.onload = function () {
              const newTabScript = newTab.document.createElement('script');
              newTabScript.src = "/src/frontend/src/components/NewTabComponent.js"; // Update the path accordingly
              newTabScript.onload = function () {
                // NewTabComponent.js will mount the Vue app to #app in the new tab
              };
              document.getElementById("app").appendChild(newTabScript);
            };
            newTab.document.head.appendChild(vueScript);
          })
          .catch((error) => {
            console.error(error);
          });
    },
  },

  mounted() {
    fetch("/app/rest/hello")
        .then((response) => response.text())
        .then((data) => {
          this.msg = data;
        });
  },
};
</script>

<style>
.wide-input {
  min-width: 600px;
  width: 50%;
}
</style>
