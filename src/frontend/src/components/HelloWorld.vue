<template>
    <h1>{{msg}}</h1>
    <h2>steven julien</h2>
    <h3> Site en production</h3>
    <h3> revenez plus tard</h3>
</template>

<script>
export default {
  name: 'HelloWorld',
  data(){
    return {
        msg:''
    }
  },
  mounted(){
    fetch("/app/messages/hello")
    .then((response) => response.text())
    .then((data) =>{
        this.msg = data;
  })
  }

}
</script>
