<template>
  <div id="app">
    <TopBar></TopBar>
    <router-view/>
  </div>
</template>

<script>
import TopBar from "./components/TopBar";
export default {
  name: 'App',
    components: {TopBar}
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #000000;
  /*background-color: #e8ecef;*/
  width: 100%;
  height: 100%;
  position: absolute;
  left: 0;
  top: 0;
}
body{
  margin: 0;
}
</style>
