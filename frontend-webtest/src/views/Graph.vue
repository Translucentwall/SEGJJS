<template>
  <div>
    <relation-graph v-if="id!==0" :eid="id" :etype="type" :size="size"></relation-graph>
  </div>
</template>
<script>
    import * as d3 from 'd3'
    import {getGraph, getMoreGraph} from "../api/api";
    import {Loading} from "element-ui";
    import RelationGraph from "../components/RelationGraph";

    export default {
        name: 'Graph',
        data(){
            return {
                id: 0,
                type: 1,
                size: 'large',
                typeDic: {"author":1, 'affiliation':2, 'issue':3, 'term': 4, 'paper':5},
            }
        },
        components: {RelationGraph},
        mounted () {
            this.id = parseInt(this.$route.params.id);
            this.type = this.typeDic[this.$route.params.type];
            if (this.id === undefined || this.type === undefined) {
              window.location.href = '/home';
            }
        }
    }
</script>
<style scoped>
  #svgContainer{
    width: 100%;
    height: 100%;
  }
  #forceDirected{
    width: 100%;
  }
  .every{
    /*margin:15px;*/
    float: left;
    position: relative;
  }
  .svg{
    /*border: 1px solid #cccccc;*/
  }
  .option{
    position: absolute;
    top: 0;
    left: 20px;
    text-align: left;
    min-width: 160px;
  }
  .option_name{
    margin-left: 0.5ex;
  }
  .option .el-input__inner{
    border: 1px solid #cccccc;
    border-radius: 4px;
    font-size: 14px;
    height: 24px;
    padding: 0 5px;
  }
  .search-input{
    width: 16vw;
  }
  .center_name{
    color: #000000;
  }
  .center_name:hover{
    color: #409eff;
  }
  .author{
    color: rgb(214, 39, 40);
  }
  .affiliation{
    color: rgb(255, 127, 14);
  }
  .issue{
    color: rgb(214, 214, 8);
  }
  .term{
    color: rgb(44, 160, 44);
  }
  .paper{
    color: rgb(31, 119, 180);
  }
</style>
