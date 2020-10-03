<template>
  <div>
    <div class="body-top">
      <div class="top-img">
        <img src="../assets/background-new2.jpg" alt="This is a background pic."/>
      </div>
      <div class="system-name-wrap">
        <span>Online grAph System for academIcS</span>
      </div>
    </div>
    <div class="body-bottom">
      <el-row>
        <el-col :span="6" class="rank-select-wrap">
          <el-menu
            :default-active="tableMode"
            class="el-menu-demo"
            mode="vertical"
            @select="handleSelect"
            active-text-color="#000000"
          >
            <el-menu-item index="1"><div class="rank-select-item">Rank of Paper by Cited</div><div><i class="el-icon-arrow-right"></i></div></el-menu-item>
            <el-menu-item index="2"><div class="rank-select-item">Rank of Author by Paper</div><div><i class="el-icon-arrow-right"></i></div></el-menu-item>
            <el-menu-item index="3"><div class="rank-select-item">Rank of Author by Cited</div><div><i class="el-icon-arrow-right"></i></div></el-menu-item>
            <el-menu-item index="4"><div class="rank-select-item">Rank of Affiliation by Paper</div><div><i class="el-icon-arrow-right"></i></div></el-menu-item>
            <el-menu-item index="5"><div class="rank-select-item">Rank of Publication by Paper</div><div><i class="el-icon-arrow-right"></i></div></el-menu-item>
            <el-menu-item index="6"><div class="rank-select-item">Rank of Keyword by Paper</div><div><i class="el-icon-arrow-right"></i></div></el-menu-item>
          </el-menu>
        </el-col>
        <el-col :span="18">
          <div class="body-bottom-right">
            <span class="demonstration">
              <RankList
                :size="tableSize"
                :mode="tableMode"
                :data="tableData"
                :page="currentPage"
                @changeYear="changeYear"
              >
              </RankList>
            </span>
            <el-pagination
              @current-change="showRank"
              :current-page.sync="currentPage"
              :page-size="1"
              layout="total, prev, pager, next, jumper"
              :total="totalPage">
            </el-pagination>
          </div>
        </el-col>
      </el-row>

    </div>
  </div>
</template>

<script>
    import RankList from "../components/RankList";
    import {getRank} from "../api/api";
    export default {
        name: "Rank",
        components: {RankList},
        data(){
            return{
                href: window.location.href,
                tableSize: 'large',
                tableMode: '0',
                tableData: [],
                tableDescending: true,
                currentPage: 1,
                totalPage: 0,
                startYear: 2013,
                endYear: 2019
            }
        },
        mounted() {
            let mode = window.location.href.split('mode=');
            if (mode.length > 1 && mode[1] !== '') {
                this.tableMode = mode[1][0];
            }else{
                this.tableMode = '1';
            }
            this.showRank();
            let that = this;
            setTimeout(function () {
                let ascending = document.getElementsByClassName('ascending')[0];
                let descending = document.getElementsByClassName('descending')[0];
                ascending.classList.add('ascend');
                descending.classList.add('descend');
                ascending.classList.remove('sort-up-positive');
                ascending.classList.add('sort-up-negative');
                descending.classList.remove('sort-down-negative');
                descending.classList.add('sort-down-positive');
                ascending.addEventListener('click', ()=>{
                    if(that.tableDescending){
                        that.tableDescending = !that.tableDescending;
                        ascending.classList.remove('sort-up-negative');
                        ascending.classList.add('sort-up-positive');
                        descending.classList.remove('sort-down-positive');
                        descending.classList.add('sort-down-negative');
                        that.showRank();
                    }
                }, false);
                descending.addEventListener('click', ()=>{
                    if(!that.tableDescending){
                        that.tableDescending = !that.tableDescending;
                        ascending.classList.remove('sort-up-positive');
                        ascending.classList.add('sort-up-negative');
                        descending.classList.remove('sort-down-negative');
                        descending.classList.add('sort-down-positive');
                        that.showRank();
                    }
                }, false);
            }, 100);
        },
        watch:{
            $route: function () {
                this.tableMode = window.location.href.split('mode=')[1][0];
            },
            tableMode: function () {
                this.tableDescending = true;
                let ascending = document.getElementsByClassName('ascend')[0];
                let descending = document.getElementsByClassName('descend')[0];
                if(ascending && descending){
                    ascending.classList.remove('sort-up-positive');
                    ascending.classList.add('sort-up-negative');
                    descending.classList.remove('sort-down-negative');
                    descending.classList.add('sort-down-positive');
                }
                this.currentPage = 1;
                this.showRank();
            }
        },
        methods: {
            handleSelect: function(key, keyPath) {
                this.$router.push('/rank?mode=' + key).catch(err =>{
                    console.log(err);
                });
                this.tableMode = key;
            },
            showRank: function(){
                let mode;
                switch (this.tableMode) {
                    case "1": {
                        mode = 'Paper-Cited';
                        break;
                    }
                    case "2": {
                        mode = 'Author-Paper';
                        break;
                    }
                    case "3": {
                        mode = 'Author-Cited';
                        break;
                    }
                    case "4": {
                        mode = 'Affiliation-Paper';
                        break;
                    }
                    case "5": {
                        mode = 'Publication-Paper';
                        break;
                    }
                    case "6": {
                        mode = 'Keyword-Paper';
                        break;
                    }
                }
                getRank(mode, this.currentPage, this.tableDescending, this.startYear, this.endYear).then(res =>{
                    if(res.success){
                        this.tableData = res.content.rankList;
                        this.totalPage = res.content.totalPage;
                    }else{

                    }
                });
            },
            changeYear: function (year) {
                this.startYear = year[0];
                this.endYear = year[1];
                this.showRank();
            }
        }
      }
</script>

<style scoped>
  .body-top{
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
  .rank-select-wrap{
    /*margin-top: 100px;*/
  }
  .el-menu{
    padding: 50px 0;
  }
  .el-menu-item{
    text-align: left;
    overflow: hidden;
    margin: 0 0 0 90px;
    border-right: 2px solid #ffffff;
    color: #909399;
    display: flex;
    justify-content: space-between;
  }
  .el-menu-item:hover{
    background-color: #ffffff;
    color: #000000;
  }
  .el-menu-item:focus{
    background-color: #ffffff;
  }
  .is-active{
    border-right: 2px solid #000000;
  }
  .rank-select-item{
    font-size: 16px;
  }
  i{
    margin-bottom: 4px;
  }
  .body-bottom-right{
    margin: 0 5vw 50px 5vw;
  }
</style>
