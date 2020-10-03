<template>
  <div style="text-align: center">
    <div class="body-top">
      <div class="img-wrap">
        <img src="../assets/background-new.jpg" alt="This is a background picture." />
      </div>
      <div class="system-name-wrap">
        <span>Online grAph System for academIcS</span>
      </div>
      <Search></Search>
    </div>
    <div class="body-bottom">
      <el-row>
        <el-col :span="24">
          <strong class="rank-wrap-title">Hot Rank</strong>
        </el-col>
        <el-col :span="24">
          <el-row class="rank-wrap" type="flex" justify="space-around">
            <el-col :span="7" class="rank">
              <el-row>
                <el-col :span="24" class="rank-title">
                  <strong>Author Hot Rank</strong>
                </el-col>
                <el-row>
                  <el-col :span="3"><strong>Rank</strong></el-col>
                  <el-col :span="15" :offset="1"><strong>Author Name</strong></el-col>
                  <el-col :span="4" :offset="1"><strong>Hot</strong></el-col>
                </el-row>
                <el-row v-for="(data, index) in authorTableData" :key="index" class="rank-item">
                  <el-col :span="3">{{index+1}}</el-col>
                  <el-col :span="15" :offset="1" class="data-name-column"><el-tooltip :content="data.name" placement="bottom-start" effect="dark" :open-delay="400"><a class="data-name" :href="'/entity/author/'+data.id">{{data.name}}</a></el-tooltip></el-col>
                  <el-col :span="4" :offset="1">{{data.value.toFixed(2)}}</el-col>
                </el-row>
              </el-row>
            </el-col>
            <el-col :span="7" class="rank">
              <el-col :span="24" class="rank-title">
                <strong>Affiliation Hot Rank</strong>
              </el-col>
              <el-row>
                <el-col :span="3"><strong>Rank</strong></el-col>
                <el-col :span="15" :offset="1"><strong>Affiliation Name</strong></el-col>
                <el-col :span="4" :offset="1"><strong>Hot</strong></el-col>
              </el-row>
              <el-row v-for="(data, index) in affiliationTableData" :key="index" class="rank-item">
                <el-col :span="3">{{index+1}}</el-col>
                <el-col :span="15" :offset="1" class="data-name-column"><el-tooltip :content="data.name" placement="bottom-start" effect="dark" :open-delay="400"><a class="data-name" :href="'/entity/affiliation/'+data.id">{{data.name}}</a></el-tooltip></el-col>
                <el-col :span="4" :offset="1">{{data.value.toFixed(2)}}</el-col>
              </el-row>
            </el-col>
            <el-col :span="7" class="rank">
              <el-col :span="24" class="rank-title">
                <strong>Term Hot Rank</strong>
              </el-col>
              <el-row>
                <el-col :span="3"><strong>Rank</strong></el-col>
                <el-col :span="15" :offset="1"><strong>Term</strong></el-col>
                <el-col :span="4" :offset="1"><strong>Hot</strong></el-col>
              </el-row>
              <el-row v-for="(data, index) in termTableData" :key="index" class="rank-item">
                <el-col :span="3">{{index+1}}</el-col>
                <el-col :span="15" :offset="1" class="data-name-column"><el-tooltip :content="data.name" placement="bottom-start" effect="dark" :open-delay="400"><a class="data-name" :href="'/graph/term/'+data.id">{{data.name}}</a></el-tooltip></el-col>
                <el-col :span="4" :offset="1">{{data.value.toFixed(2)}}</el-col>
              </el-row>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
    import {getPopRank, searchable} from "../api/api";
    import {Loading} from "element-ui";
    import Search from "../components/Search";

    export default {
        name: "Home",
        components: {Search},
        data(){
            return{
                authorTableData: [],
                affiliationTableData: [],
                termTableData: []
            }
        },
        mounted(){
            let loadingInstance = Loading.service({ fullscreen: true, text:'Server not available, please wait for a moment...'});
            searchable().then(res=>{
                if(res.success){
                    getPopRank(1).then(res=>{
                        this.authorTableData = res;
                    });
                    getPopRank(2).then(res=>{
                        this.affiliationTableData = res;
                    });
                    getPopRank(4).then(res=>{
                        this.termTableData = res;
                    });
                    loadingInstance.close();
                }
            });
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
    margin: 30px 30px 60px;
  }
  .rank-wrap-title{
    text-decoration: underline #000000;
    font-size: 36px;
  }
  .rank{
    margin: 20px 0;
    padding: 10px 14px 30px 20px;
    box-shadow: 3px 3px 10px 3px #e4e8ef;
    text-align: left;
    color: #6d6d6d;
  }
  .rank-title{
    text-align: center;
    font-size: 24px;
    margin: 16px 0;
  }
  .rank-item{
    margin: 8px 0 0;
    color: #6d6d6d;
  }
  .data-name-column{
    overflow: hidden;
    white-space:nowrap;
    text-overflow: ellipsis;
  }
  .data-name{
    color: #6d6d6d;
    text-decoration: none;
  }
  .data-name:hover{
    color: #409eff;
    text-decoration: underline;
  }

</style>
