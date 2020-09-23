<template>
  <div style="text-align: center">
    <div class="body-top">
      <div class="img-wrap">
        <img src="../assets/background-new.jpg" alt="This is a background picture." />
      </div>
      <div class="system-name-wrap">
        <span>Online grAph System for academIcS</span>
      </div>
      <Search
          v-if="showSearch"
          :search-mode="mode"
          :search-content="searchContent"></Search>
    </div>
    <div class="body-bottom" v-if="displayBottom">
      <el-row>
        <el-col :span="23">
          <div class="breadcrumb">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/home' }">Home</el-breadcrumb-item>
              <el-breadcrumb-item>Search</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
        </el-col>
        <el-col :span="17" :offset="1">
          <div class="result-title-wrap">
            <div class="result-title">
              Results for <span class="emphasize content" :title="resultTitleContent">{{resultTitleContent}}</span> in <span class="emphasize">{{resultTitleMode}}</span>:
            </div>
            <div v-if="simplePaperVO[0]" class="sort-mode-wrap">
              <span class="sort-mode-before">sort by:</span>
              <el-dropdown class="sort-mode" trigger="click" @command="handleSortMode">
                <el-button class="sort-mode-button" type="primary">
                  {{sortMode}}<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="Relevance">Relevance</el-dropdown-item>
                  <el-dropdown-item command="Newest">Newest</el-dropdown-item>
                  <el-dropdown-item command="Oldest">Oldest</el-dropdown-item>
                  <el-dropdown-item command="Title A-Z">Title A-Z</el-dropdown-item>
                  <el-dropdown-item command="Title Z-A">Title Z-A</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>

          <div class="result-content">
            <Card
              v-for="(item, index) in simplePaperVO"
              :key="index"
              :simple-paper-v-o="item"
            >
            </Card>
          </div>
          <div v-if="!simplePaperVO[0]" class="message">No Result...</div>
          <el-button v-if="simplePaperVO[0]" class="result-more" @click="loadMore">load more...</el-button>
        </el-col>
        <el-col :span="6" class="body-bottom-right">
          <RankList
            :size="tableSize"
            :mode="tableMode"
            :data="tableData"
            :page="1">
          </RankList>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  import Card from "../components/Card";
  import {search, getRank, searchable} from "../api/api"
  import RankList from "../components/RankList";
  import {Loading} from "element-ui";
  import Search from "../components/Search";
  export default {
      name: 'SearchPage',
      components: {Search, RankList, Card},
      data(){
          return{
              displayBottom: false,
              mode: 'All',
              simplePaperVO: [],
              searchContent: '',
              showSearch: false,
              currentPage: 1,
              sortMode: 'Relevance',
              resultTitleMode: '',
              resultTitleContent: '',
              tableSize: 'small',
              tableMode: '0',
              tableData: [],
              tableModeDic: {
                  'All': '1',
                  'Title': '1',
                  'Author': '2',
                  'Affiliation': '4',
                  'Publication': '5',
                  'Keyword': '6',
                  'Advanced': '1',
              },
              tableModeDic2: {
                  'All': 'Paper-Cited',
                  'Title': 'Paper-Cited',
                  'Author': 'Author-Paper',
                  'Affiliation': 'Affiliation-Paper',
                  'Publication': 'Publication-Paper',
                  'Keyword': 'Keyword-Paper',
                  'Advanced': 'Paper-Cited'
              }
          }
      },
      mounted() {
          let loadingInstance = Loading.service({ fullscreen: true, text:'Server not available, please wait for a moment...'});
          searchable().then(res=>{
              if(res.success){
                  loadingInstance.close();
                  let content = this.$route.params.content;
                  if(content !== null && content !== ''){
                      this.searchBegin();
                  }else{
                      window.location.href = '/home';
                  }
              }
          });
      },
      watch:{
          sortMode: function(){
              this.searchBegin();
          }
      },
      methods: {
          handleSortMode(command) {
              this.sortMode = command;
          },
          searchBegin: function () {
              let content = this.$route.params.content;
              if (content !== null) {
                  this.searchContent = content;
                  this.mode = this.$route.params.mode;
              }

              this.currentPage = 1;

              this.simplePaperVO.length = 0;
              search(this.searchContent, this.mode, this.currentPage, this.sortMode, 10).then(res => {
                  this.simplePaperVO = res;
                  this.resultTitleMode = this.mode;
                  this.resultTitleContent = this.searchContent;
                  this.displayBottom = true;

                  this.tableMode = this.tableModeDic[this.mode];
                  let tableMode = this.tableModeDic2[this.mode];
                  getRank(tableMode, 1, true, 2013, 2019).then(res => {
                      this.tableData = res.content.rankList;
                  });

              });
              this.showSearch = true;
          },
          loadMore: function () {
              this.currentPage += 1;
              search(this.searchContent, this.mode, this.currentPage, this.sortMode, 10).then(res => {
                  for (let result in res){
                      this.simplePaperVO.push(res[result]);
                  }
              });
          }
      }
  }
</script>

<style scoped>
  .body-top{
    position: relative;
  }
  .img-wrap:after{
    position: absolute;
    content: '';
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    /*box-shadow:0 0 50px 20px #ffffff inset;*/
  }
  img{
    width: 100%;
  }
  .system-name-wrap{
    position: absolute;
    top: 36px;
    left: 50px;
    font-family: 'Arial Rounded MT Bold', serif;
    font-size: 40px;
    color: #ffffff;
  }
  .body-bottom{
    margin: 10px 0 100px;
  }
  .breadcrumb{
    margin-left: 50px;
  }
  .result-title-wrap{
    font-size: 24px;
    text-align: left;
    margin: 5px 0 0 20px;
    display: flex;
    justify-content: space-between;
    line-height: 48px;
  }
  .result-title{
    display: flex;
  }
  .sort-mode-before{
    font-size: 15px;
    font-weight: bold;
  }
  .sort-mode{
    margin-right: 50px;
  }
  .sort-mode-button{
    color: #000000;
    background-color: #ffffff;
    border: 0;
  }
  .sort-mode-button:hover{
    color: #000000;
    background-color: #ffffff;
    border: 0;
  }
  .sort-mode-button:focus{
    color: #000000;
    background-color: #ffffff;
    border: 0;
  }
  .result-content{
    margin-bottom: 30px;
  }
  .result-more{
    padding-left: 15px;
    padding-right: 15px;
  }
  .emphasize{
    color: #b04c50;
    margin: 0 8px;
  }
  .content{
    max-width: 400px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .body-bottom-right{
    border-left: 1px solid #ebeef5;
    padding: 0 30px 50px 8px;
  }
  .message{
    margin: 20px 0 10px;
    font-size: 32px;
  }
</style>
