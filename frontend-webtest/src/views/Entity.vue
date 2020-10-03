<template>
<div>
  <div class = "body_top">
    <div class="top-img">
      <img src="../assets/background-new2.jpg" alt="This is a background pic."/>
    </div>
    <div class="system-name-wrap">
      <span>Online grAph System for academIcS</span>
    </div>
  </div>
  <div class="body_bottom">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">Home</el-breadcrumb-item>
      <el-breadcrumb-item><span>{{academicEntityVO.name}}</span></el-breadcrumb-item>
    </el-breadcrumb>
    <div class="body_bottom_body">
      <el-row>
        <el-col :span="academicEntityVO.refSum>-1?19:22" :offset="1">
          <div class="entity-title">
            <span class="name">{{academicEntityVO.name}}</span>
            <hot-graph v-if="academicEntityVO.popTrend" :data="academicEntityVO.popTrend" :id="type + '' + id"></hot-graph>
          </div>
          <hr v-if="academicEntityVO.refSum>-1"/>
        </el-col>
        <el-col :span="3" :offset="1" v-if="academicEntityVO.refSum>-1">
          <div class="reference citation_box">
            <div class="citation_title">Reference</div>
            <div class="citation_count">{{academicEntityVO.refSum}}</div>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col class="body_bottom_top" :span="22" :offset="1">
          <el-row>
            <el-col>
            <el-tabs v-model="activeName" type="border-card">
              <el-tab-pane v-if="academicEntityVO.authors&&academicEntityVO.authors.length>0" name="author">
                <span slot="label"><i class="el-icon-s-custom"></i> Authors</span>
                <el-row>
                  <el-col class="column" :span="12" v-for="(author, index) in academicEntityVO.authors" :key="index">
                    <el-row class="entity-wrap">
                      <el-col :span="9">
                        <a class="entity" :title="author.name" :href="'/entity/author/' + author.id">
                          {{(index + 1) + '. ' +author.name}}
                        </a>
                      </el-col>
                      <el-col :span="15">
                        <hot-graph :data="author.popTrend" :id="'1' + author.id"></hot-graph>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </el-tab-pane>
              <el-tab-pane v-if="academicEntityVO.affiliations&&academicEntityVO.affiliations.length>0" name="affiliation">
                <span slot="label"><i class="el-icon-s-home"></i> Affiliations</span>
                <el-row>
                  <el-col class="column" :span="12" v-for="(affiliation,index) in academicEntityVO.affiliations" :key="index">
                    <el-row  class="entity-wrap">
                      <el-col :span="14">
                        <a class="entity" :title="affiliation.name" :href="'/entity/affiliation/' + affiliation.id">
<!--                          {{affiliation.name.length>25?affiliation.name.substr(0,25)+'...':affiliation.name}}-->
                          {{(index + 1) + '. ' +affiliation.name}}
                        </a>
                      </el-col>
                      <el-col :span="10">
                        <hot-graph :data="affiliation.popTrend" :id="'2' + affiliation.id"></hot-graph>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </el-tab-pane>
              <el-tab-pane v-if="academicEntityVO.conferences&&academicEntityVO.conferences.length>0" name="conference">
                <span slot="label"><i class="el-icon-time"></i> Conferences</span>
                <el-row>
                  <el-col class="column" :span="12" v-for="(conference,index) in academicEntityVO.conferences" :key="index">
                    <el-row class="entity-wrap">
                      <el-col :span="conference.popTrend?12:24">
                        <a class="entity" :title="conference.name" :href="'/entity/issue/' + conference.id">
                          {{(index + 1) + '. ' +conference.name}}
                        </a>
                      </el-col>
                      <el-col :span="12" v-if="conference.popTrend">
                        <hot-graph :data="conference.popTrend" :id="'3' + conference.id"></hot-graph>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </el-tab-pane>
              <el-tab-pane name="cloud" v-if="academicEntityVO.terms&&academicEntityVO.terms.length>0">
                <span slot="label"><i class="el-icon-cloudy"></i> Cloud</span>
                <div id="cloud-wrap">
                  <div class="svg" id="cloud"></div>
                </div>
                <div class="cloud_tip" v-if="hasCloud">
                  <em>hover or click to know more ...</em>
                </div>
              </el-tab-pane>
              <el-tab-pane name="graph">
                <span slot="label"><i class="el-icon-connection"></i> Relation Graph</span>
                <relation-graph v-if="id!==0&&activeName==='graph'" :eid="parseInt(id)" :etype="type" :size="size"></relation-graph>
                <div class="graph_entry">
                  <router-link :to="'/graph/' + this.$route.params.type + '/' + this.$route.params.id">
                    <el-tooltip :content="'More Relation Graph'" placement="bottom-start" effect="dark" :open-delay="400">
                      <img src="../assets/graph-entry.png" class="graph_entry">
                    </el-tooltip>
                  </router-link>
                </div>
              </el-tab-pane>
            </el-tabs>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="22" :offset="1" class="significantPaper_wrap">
          <strong class="significantPaper_title">Significant Papers</strong>
          <hr/>
          <div class="radio" v-if="this.academicEntityVO.yearlyTerms && this.academicEntityVO.yearlyTerms.length!==0">
            <el-row>
              <el-col :span="2">
                <strong>Year: </strong>
              </el-col>
              <el-col :span="22">
                <el-radio-group v-model="yearSelect">
                  <el-radio :label="-1">All</el-radio>
                  <el-radio v-for="(yearlyTerm, index) in academicEntityVO.yearlyTerms" :key="index" :label="yearlyTerm.year">
                    {{yearlyTerm.year}}
                  </el-radio>
                </el-radio-group>
              </el-col>
            </el-row>
          </div>
          <div class="radio" v-if="this.academicEntityVO.yearlyTerms && this.academicEntityVO.yearlyTerms.length!==0">
            <el-row>
              <el-col :span="2">
                <strong>Term: </strong>
              </el-col>
              <el-col :span="22">
                <el-radio-group v-model="termSelect">
                  <el-radio :label="-1">All</el-radio>
                  <el-radio v-for="(termItem, index) in showTermItems" :key="index" :label="termItem.id">
                    {{termItem.name}}
                  </el-radio>
                </el-radio-group>
              </el-col>
            </el-row>
            <hr/>
          </div>
          <Card
          v-for="(significantPaper,index) in academicEntityVO.significantPapers"
          :key="index"
          :simple-paper-v-o="significantPaper"
          ></Card>
        </el-col>
        <el-col :span="22" :offset="1" class="more"><a class="more-text" @click="search">More information about <strong>{{academicEntityVO.name}} ...</strong></a></el-col>
      </el-row>
    </div>
  </div>
</div>
</template>

<script>
  import {getAcademicEntity, getSignificantPaper} from "../api/api";
  import Card from "../components/Card";
  import {Loading} from "element-ui";
  import * as d3 from 'd3';
  import * as d3_cloud from 'd3-cloud';
  import RelationGraph from "../components/RelationGraph";
  import HotGraph from "../components/HotGraph";
  import echarts from 'echarts';
  import 'echarts-wordcloud'

      export default {
          name: "Entity",
          components: {HotGraph, RelationGraph, Card},
          data(){
              return{
                  id: 0,
                  type: 0,
                  size: 'small',
                  activeName: 'author',
                  typeDic: {"author":1, 'affiliation':2, 'issue':3, 'term': 4, 'paper':5},
                  academicEntityVO: {},
                  hasCloud: false,
                  yearSelect: -1,
                  termSelect: -1,
                  allTermItems: [],
                  showTermItems: [],
                  significantPapers: []
              }
          },
          watch: {
              activeName: function(){
                  if(this.activeName === 'cloud' && !this.hasCloud && this.academicEntityVO.terms && this.academicEntityVO.terms.length > 0){
                      let that = this;
                      setTimeout(function (){that.renderCloudNew();},100)
                  }
              },
              yearSelect: function () {
                  if(this.yearSelect === -1){
                      this.showTermItems = this.allTermItems;
                  }else{
                      let that = this;
                      this.academicEntityVO.yearlyTerms.forEach(function (yearlyTerm){
                          if(yearlyTerm.year===that.yearSelect){
                              that.showTermItems = yearlyTerm.termItemList;
                          }
                      })
                  }
                  if(this.termSelect === -1){
                      this.renderSignificantPaper();
                  }else{
                    this.termSelect = -1;
                  }
              },
              termSelect: function () {
                  this.renderSignificantPaper();
              }
          },
          mounted() {
              this.id=this.$route.params.id;
              this.type=this.typeDic[this.$route.params.type];
              if(this.id===undefined || this.type===undefined){
                  window.location.href = '/home';
              }
              let loadingInstance = Loading.service({ fullscreen: true, text:'loading...'});
              getAcademicEntity(this.id,this.type)
                  .then(res => {
                      res.yearlyTerms.sort(function (a, b) {
                          return b.year-a.year;
                      });
                      this.academicEntityVO = res;
                      if(res.authors&&res.authors[0]){
                          this.activeName = 'author';
                      }else if(res.affiliations&&res.affiliations[0]){
                          this.activeName = 'affiliation';
                      }else if(res.conferences&&res.conferences[0]){
                          this.activeName = 'conference';
                      }else if(res.terms&&res.terms[0]){
                          this.activeName = 'cloud';
                      }else{
                          this.activeName = 'graph';
                      }
                      let allTermItems = [];
                      this.academicEntityVO.yearlyTerms.forEach(function(yearlyTerm){
                          yearlyTerm.termItemList.forEach(function (termItem) {
                              let has = false;
                              allTermItems.forEach(function(term){
                                  if(term.id===termItem.id && term.name===termItem.name){
                                    has = true;
                                  }
                              });
                              if(!has){
                                  allTermItems.push(termItem);
                              }
                          })
                      });
                      this.allTermItems = allTermItems;
                      this.showTermItems = this.allTermItems;
                      loadingInstance.close();
                  })
                  .catch(()=>{
                      this.$alert('Fail to get entity，please search again', 'Tips',{
                          type: 'error',
                          confirmButtonText: 'confirm',
                          showClose: false
                      }).then(()=>{
                          window.location.href = '/home';
                      })
                  });
          },
          methods: {
              toOtherEntity: function (type, id) {
                  window.location.href='/entity/' + type + '/' + id;
              },
              renderCloud: function () {
                  let data = this.academicEntityVO.terms;
                  data.sort(function (a, b) {
                      return b.hot-a.hot;
                  });
                  let maxHot = Math.max.apply(Math,data.map(item => { return item.hot }));
                  let width = document.getElementById('cloud-wrap').offsetWidth;
                  // console.log(document.getElementById('cloud-wrap').offsetWidth);
                  // console.log(document.getElementById('cloud-wrap').innerWidth);
                  // console.log(document.getElementById('cloud-wrap').clientWidth);
                  let height = data.length>20?600:400;
                  let color = d3.scaleOrdinal(d3.schemeCategory10);
                  let svg = d3.select('#cloud')
                      .append('svg')
                      .attr('width', width)
                      .attr('height', height);

                  data=data.slice(0,50);

                  const layout = d3_cloud()
                      .size([width, height])
                      .words(data)
                      .padding(function (d) {
                          if((d.hot+1)/(maxHot+1)>0.8){
                              return 30;
                          }else{
                              return 30;
                          }
                      })
                      .rotate(function(d) {
                          // if(d.hot===maxHot){
                          //     return 30;
                          // }else{
                          //     return (d.hot+1)/(maxHot+1)>0.8?30:0;
                          // }
                          return Math.round(Math.random())*45;
                      })
                      .font('Impact')
                      .fontSize(function(d) {
                          if((d.hot+1)/(maxHot+1)>0.8){
                              return 20+(d.hot+1)/(maxHot+1)*20;
                          }else{
                              return 10+(d.hot+1)/(maxHot+1)*20;
                          }
                      })
                      .on('end', draw);

                  layout.start();
                  function draw(words) {
                      svg.append('g')
                          .attr('transform', 'translate('+ (layout.size()[0] / 2) + ',' + (layout.size()[1] / 2)+ ')')
                          .selectAll('text')
                          .data(words)
                          .enter()
                          .append('text')
                          .on('click', function(d) {
                              window.location.href = '/graph/term/' + d.id;
                          })
                          .style('cursor', 'pointer')
                          .attr('fill', (d, i) => color(i))
                          .style('font-size', function(d) {
                              if((d.hot+1)/(maxHot+1)>0.8){
                                  return 20+(d.hot+1)/(maxHot+1)*20;
                              }else{
                                  return 10+(d.hot+1)/(maxHot+1)*20;
                              }
                          })
                          .style('font-family', 'Impact')
                          .attr('text-anchor', 'middle')
                          .attr('transform', function(d) {
                              return 'translate('+[d.x*1.1, d.y*1.1]+')rotate('+d.rotate+')';
                          })
                          .text(function(d) {
                              return d.name;
                          })
                          .append('title')
                          .text(function(d) {
                              return d.name;
                          });

                      // svg.selectAll('text') // 创建动画
                      //     .style('fill-opacity', 0)
                      //     .transition()
                      //     .duration(100)
                      //     .delay(function(d, i) {
                      //         return i * 100
                      //     })
                      //     .style('fill-opacity', 1);
                  }
                  this.hasCloud = true;
              },
              renderCloudNew: function(){
                  let data = this.academicEntityVO.terms;
                  data.sort(function (a, b) {
                    return b.hot-a.hot;
                  });
                  let maxHot = Math.max.apply(Math,data.map(item => { return item.hot }));
                  let echartData = [];
                  data.forEach(function (d) {
                      echartData.push({
                        name: d.name,
                        id: d.id,
                        value: (d.hot+1)/(maxHot+1)>0.8? 20+(d.hot+1)/(maxHot+1)*20: 20+(d.hot+1)/(maxHot+1)*20
                      })
                  });
                  console.log(echartData);
                  let myChart = echarts.init(document.getElementById('cloud'));
                  let option = {
                    tooltip: {
                      trigger: 'item',
                      formatter: '{b}'
                    },
                    series: [
                      {
                        type: 'wordCloud',
                        gridSize: 2,
                        sizeRange: [20, 80],
                        rotationRange: [0, 0],
                        shape: 'circle',
                        textStyle: {
                          normal: {
                            color: function () {
                              return 'rgb(' + [
                                Math.round(Math.random() * 255),
                                Math.round(Math.random() * 255),
                                Math.round(Math.random() * 255)
                              ].join(',') + ')';
                            }
                          },
                          emphasis: {
                            shadowBlur: 5,
                            shadowColor: '#333'
                          }
                        },
                        data: echartData
                      }]
                  };
                  myChart.setOption(option);
                  myChart.on('click', function (word) {
                      window.location.href = '/graph/term/' + word.data.id;
                  });
              },
              search: function () {
                  let typeDic2= {1:"Author", 2:'Affiliation', 3:'Publication'}
                  window.location.href = '/search/'+typeDic2[this.type]+'/'+this.academicEntityVO.name;
              },
              renderSignificantPaper: function () {
                  let that = this;
                  let change = false;
                  this.significantPapers.forEach(function (significantPaper) {
                      if(that.yearSelect===significantPaper.year&&that.termSelect===significantPaper.term){
                          that.academicEntityVO.significantPapers = significantPaper.paper;
                          change = true;
                      }
                  })
                  if (!change){
                      getSignificantPaper(this.id, this.type, this.yearSelect, this.termSelect).then(res=>{
                          this.academicEntityVO.significantPapers = res;
                          this.significantPapers.push({year: this.yearSelect, term: this.termSelect, paper: res})
                      });
                  }
              }
          },
          computed: {
              changeName: function () {
                  if(this.type === 3 && this.academicEntityVO.name !== undefined){
                      let nameItem = this.academicEntityVO.name.split(' ');
                      if(nameItem[1].substr(-1,1)==='1'){
                          nameItem[1] += 'st';
                      }else if(nameItem[1].substr(-1,1)==='2'){
                          nameItem[1] += 'nd';
                      }else if(nameItem[1].substr(-1,1)==='3'){
                          nameItem[1] += 'rd';
                      }else {
                          nameItem[1] += 'th';
                      }
                      if(nameItem[2] === 'ASE'){
                          return nameItem[0] + ' ' + nameItem[1] + ' IEEE/ACM International Conference on Automated Software Engineering (ASE)';
                      }else{
                          return nameItem[0] + ' ' + nameItem[1] + ' IEEE/ACM International Conference on Software Engineering (ICSE)';
                      }
                  }else{
                      return this.academicEntityVO.name;
                  }
              }
          }
      }
</script>

<style scoped>
  .body_top{
    position: relative;
  }
  img{
    width:100%
  }
  .system-name-wrap{
    position: absolute;
    top: 36px;
    left: 50px;
    font-family: 'Arial Rounded MT Bold', serif;
    font-size: 40px;
    color: #ffffff;
  }
  .body_bottom{
    margin: 20px 50px;
  }
  .body_bottom_body{
    margin: 20px 2% 80px 2%;
    box-shadow: 6px 6px 20px 4px #e4e8ef;
    padding: 40px 0 40px;
  }
  .body_bottom_top{
    padding-top: 10px;
    padding-bottom: 10px;
    border-top: 1px solid #cccccc;
    border-bottom: 1px solid #cccccc;
  }
  .entity-title{
    display: flex;
  }
  .name{
    text-align: left;
    font-size: 30px;
    font-weight: bold;
    margin-right: 20px;
  }
  .citation_box{
    width: 80px;
    height: 50px;
    background-color: #000000;
    color: #ffffff;
    padding: 10px 0;
    border-radius: 3px;
    text-align: center;
  }
  .citation_title{
    font-size: 14px;
    font-weight: bold;
  }
  .citation_count{
    font-size: 28px;
  }
  .entity-wrap{
    margin: 10px 0;
  }
  .entity{
    color: #000000;
    text-decoration: underline;
    cursor: pointer;
    font-style: italic;
  }
  .entity:hover{
    color: #409eff;
    text-decoration: underline;
    cursor: pointer;
    font-style: italic;
  }
  #pane-graph{
    position: relative;
  }
  #pane-cloud{
    position: relative;
  }
  .cloud_tip{
    position: absolute;
    top: 0;
    left: 0;
  }
  .graph_entry{
    width: 3vw;
    position: absolute;
    top: 0;
    right: 0;
  }
  #cloud-wrap{
    width: 100%;
    text-align: left;
  }
  .significantPaper_wrap{
    margin-top: 20px;
    text-align: left;
  }
  .significantPaper_title{
    font-size: 24px;
  }
  .radio{
    margin: 10px 50px 0;
  }
  .column{
    text-align: left;
  }
  .el-tag{
    margin: 0 30px 10px 0;
    /*background-color: #d66881;*/
    /*border-color: #b04c50;*/
    /*color: #ffffff;*/
    cursor: pointer;
  }
  .el-tag:hover{
    color: #000000;
    text-decoration: underline;
  }
  .more{
    margin-top: 10px;
    text-align: left;
  }
  .more-text{
    text-decoration: underline;
  }
  .more-text:hover{
    color: #409eff;
  }
  .svg{
    width: 100%;
    height: 600px;
  }
</style>
