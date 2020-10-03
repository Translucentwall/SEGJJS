<template>
  <div>
    <div class="logout"  @click="logout">
      Logout
      <i class="el-icon-delete"></i>
    </div>
    <div>
      <el-row>
        <el-col :span="5">
          <el-menu
            :default-active="mode"
            class="manage-menu"
            active-text-color="#000000"
            @select="select"
          >
            <el-submenu index="1">
              <template slot="title"><i class="el-icon-upload"></i>Add Alias</template>
              <el-menu-item index="1-1"><div class="rank-select-item"><i class="el-icon-location"></i>Author</div><div><i class="el-icon-arrow-right"></i></div></el-menu-item>
              <el-menu-item index="1-2"><div class="rank-select-item"><i class="el-icon-document"></i>Affiliation</div><div><i class="el-icon-arrow-right"></i></div></el-menu-item>
            </el-submenu>
            <el-submenu index="2">
              <template slot="title"><i class="el-icon-setting"></i>Cancel Alias</template>
              <el-menu-item index="2-1"><div class="rank-select-item"><i class="el-icon-location"></i>Author</div><div><i class="el-icon-arrow-right"></i></div></el-menu-item>
              <el-menu-item index="2-2"><div class="rank-select-item"><i class="el-icon-document"></i>Affiliation</div><div><i class="el-icon-arrow-right"></i></div></el-menu-item>
            </el-submenu>
          </el-menu>
        </el-col>
        <el-col :span="19" class="body-right">
          <el-row>
            <el-col :span="23" class="table" v-if="mode==='1-1'||mode==='1-2'">
              <el-row>
                <el-col :span="5" class="head source"><strong>Source</strong></el-col>
                <el-col :span="16" class="head targets"><strong>Target(s)</strong></el-col>
                <el-col :span="3" class="head"><strong>Operation</strong></el-col>
              </el-row>
              <el-row v-for="(item,index) in aliasList" :key="index" class="line">
                <el-col :span="5" class="column source">
                  <el-tag
                    :title="item.name"
                    @click="toOtherEntity(item.type, item.sonId)"
                  >{{item.name}}</el-tag>
                </el-col>
                <el-col :span="16" class="column targets">
                  <el-tag
                    v-for="father in item.fathers"
                    :key="father.fatherId"
                    :title="father.aliasName"
                    @click="toOtherEntity(item.type, father.fatherId)"
                  >{{father.aliasName}}</el-tag>
                </el-col>
                <el-col :span="3" class="column operation">
                  <el-popover
                    placement="right"
                    width="400"
                    trigger="click">
                    <a slot="reference">operate</a>
                    <el-row>
                      <el-col :span="24">Choose one target to add</el-col>
                    </el-row>
                    <el-row class="operate-source">
                      <el-col :span="4">Source:</el-col>
                      <el-col :span="20">
                        <strong :title="item.name">{{item.name}}</strong>
                      </el-col>
                    </el-row>
                    <el-row class="operate-target">
                      <el-col :span="4">Targets:</el-col>
                      <el-col :span="20">
                        <el-tag :title="item.name" @click="confirmAlias(item.sonId, item.sonId, item.type)">{{item.name}}</el-tag>
                        <el-tag
                          v-for="father in item.fathers"
                          :key="father.fatherId"
                          :title="father.aliasName"
                          @click="confirmAlias(item.sonId, father.fatherId, item.type)"
                        >{{father.aliasName}}</el-tag>
                      </el-col>
                    </el-row>
                  </el-popover>
                </el-col>
              </el-row>
              <el-row>
                <div class="message" v-if="aliasList.length===0">no data...</div>
              </el-row>
            </el-col>
            <el-col :span="23" class="table" v-if="mode==='2-1'||mode==='2-2'">
              <el-row>
                <el-col :span="5" class="head source"><strong>Source</strong></el-col>
                <el-col :span="16" class="head targets"><strong>Target(s)</strong></el-col>
                <el-col :span="3" class="head"><strong>Operation</strong></el-col>
              </el-row>
              <el-row v-for="(item,index) in aliasList" :key="index" class="line">
                <el-col :span="5" class="column source">
                  <el-tag
                    :title="item.name"
                    @click="toOtherEntity(item.type, item.sonId)"
                  >{{item.name}}</el-tag>
                </el-col>
                <el-col :span="16" class="column targets">
                  <el-tag
                    v-for="father in item.fathers"
                    :key="father.fatherId"
                    :title="father.aliasName"
                    @click="toOtherEntity(item.type, father.fatherId)"
                  >{{father.aliasName}}</el-tag>
                </el-col>
                <el-col :span="3" class="column operation">
                  <a @click="confirmCancelAlias(item.sonId, item.type)">cancel</a>
                </el-col>
              </el-row>
              <el-row>
                <div class="message" v-if="aliasList.length===0">no data...</div>
              </el-row>
            </el-col>
            <el-col :span="24" v-if="aliasList.length>0">
              <el-pagination
                @current-change="showAlias"
                :current-page.sync="page"
                :page-size="1"
                layout="prev, pager, next, jumper"
                :total="total">
              </el-pagination>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
    import {cancelAlias, getConfusedAlias, getEffectiveAlias, logout, modifyAlias} from "../api/api";
    import cookie from 'js-cookie';

    export default {
        name: "Manage",
        data(){
            return{
                mode: '1-1',
                modeDic: {'1-1':1,'1-2':2, '2-1':1, '2-2':2},
                page: 1,
                total: 50,
                aliasList: [],
            }
        },
        mounted(){
            this.showAlias();
        },
        methods:{
            logout: function () {
                this.$confirm('Are you sure you want to log out', 'tip', {
                    confirmButtonText: 'sure',
                    cancelButtonText: 'cancel',
                    type: 'info'
                }).then(() => {
                    logout().then(res=>{
                        if(res.status === '100'){
                            cookie.remove('Authorization');
                            window.location.href = '/login';
                        }
                    })
                }).catch(() => {
                    console.log('取消登出');
                });
            },
            select: function (key) {
                this.mode = key;
                this.page=1;
                this.total=50;
                this.showAlias();
            },
            toOtherEntity: function (type, id) {
                let typeDic2 = {1:"author", 2:'affiliation', 3:'issue', 4:'term', 5:'paper'};
                window.location.href='/entity/' + typeDic2[type] + '/' + id;
            },
            showAlias: function () {
                if(this.mode==='1-1'||this.mode==='1-2'){
                    getConfusedAlias(this.page-1, this.modeDic[this.mode])
                        .then(res=>{
                            if(res.status === '000'){
                                this.$alert('Identity expired, please login again','Tips', {
                                    type: 'error',
                                    confirmButtonText: 'confirm',
                                    showClose: false
                                }).then(()=>{
                                    cookie.remove('Authorization');
                                    window.location.href = '/login';
                                })
                            }else{
                                if(res.length===0&&this.page!==1){
                                    this.$alert('Page error','Tips', {
                                        type: 'error',
                                        confirmButtonText: 'confirm',
                                        showClose: false
                                    }).then(()=> {
                                        this.total = this.page - 1;
                                        this.page = 1;
                                        this.showAlias();
                                    })
                                }else{
                                    this.aliasList = res;
                                }
                            }
                        })
                        .catch(()=>{
                            this.$alert('Identity verification failed, please login again','Tips', {
                                type: 'error',
                                confirmButtonText: 'confirm',
                                showClose: false
                            }).then(()=>{
                                cookie.remove('Authorization');
                                window.location.href = '/login';
                            });
                        });
                }else{
                    getEffectiveAlias(this.page-1, this.modeDic[this.mode])
                        .then(res=>{
                            if(res.status === '000'){
                                this.$alert('Identity expired, please login again','Tips', {
                                    type: 'error',
                                    confirmButtonText: 'confirm',
                                    showClose: false
                                }).then(()=>{
                                    cookie.remove('Authorization');
                                    window.location.href = '/login';
                                })
                            }else{
                                if(res.length===0&&this.page!==1){
                                    this.$alert('Page error','Tips', {
                                        type: 'error',
                                        confirmButtonText: 'confirm',
                                        showClose: false
                                    }).then(()=> {
                                        this.total = this.page - 1;
                                        this.page = 1;
                                        this.showAlias();
                                    })
                                }else{
                                    this.aliasList = res;
                                }
                            }
                        })
                        .catch(()=>{
                            this.$alert('Identity verification failed, please login again','Tips', {
                                type: 'error',
                                confirmButtonText: 'confirm',
                                showClose: false
                            }).then(()=>{
                                cookie.remove('Authorization');
                                window.location.href = '/login';
                            })
                        });
                }
            },
            confirmAlias: function (sonId, fatherId, type) {
                modifyAlias(sonId, fatherId, type).then(res=>{
                    if(res.success){
                        this.$alert('Add alias map succeed!','Tips', {
                            type: 'success',
                            confirmButtonText: 'confirm',
                            showClose: false
                        }).then(()=>{
                            this.showAlias();
                        });
                    }else if(res.status==='000') {
                        this.$alert('Identity expired, please login again','Tips', {
                            type: 'error',
                            confirmButtonText: 'confirm',
                            showClose: false
                        }).then(()=>{
                            cookie.remove('Authorization');
                            window.location.href = '/login';
                        })
                    }else {
                        this.$alert('Add alias map failed, please try again!','Tips', {
                            type: 'error',
                            confirmButtonText: 'confirm',
                            showClose: false
                        })
                    }
                })
            },
            confirmCancelAlias: function (sonId, type) {
                this.$confirm('Are you sure you want to cancel ', 'tip', {
                    confirmButtonText: 'yes',
                    cancelButtonText: 'no',
                    type: 'info'
                }).then(() => {
                    cancelAlias(sonId,type).then(res=>{
                        if(res.success){
                            this.$alert('Cancel alias map succeed!','Tips', {
                                type: 'success',
                                confirmButtonText: 'confirm',
                                showClose: false
                            }).then(()=>{
                                this.showAlias();
                            });
                        }else if(res.status==='000') {
                            this.$alert('Identity expired, please login again','Tips', {
                                type: 'error',
                                confirmButtonText: 'confirm',
                                showClose: false
                            }).then(()=>{
                                cookie.remove('Authorization');
                                window.location.href = '/login';
                            })
                        }else {
                            this.$alert('Cancel alias map failed, please try again!','Tips', {
                                type: 'error',
                                confirmButtonText: 'confirm',
                                showClose: false
                            })
                        }
                    })
                }).catch(() => {
                    console.log('取消');
                });

            }
        }
    }
</script>

<style scoped>
  .logout{
    position: absolute;
    top: 8px;
    right: 8px;
    font-size: 20px;
    cursor: pointer;
  }
  .el-menu{
    padding: 20vh 0 50vh;
    text-align: left;
  }
  .el-submenu{
    margin: 0 0 0 60px;
  }
  .el-menu-item{
    overflow: hidden;
    border-right: 2px solid #ffffff;
    color: #909399;
    display: flex;
    padding-right: 20px;
    justify-content: space-between;
  }
  .el-menu-item:hover{
    background-color: #ffffff;
    color: #000000;
  }
  .el-menu-item:focus{
    background-color: #ffffff;
  }
  .el-menu-item.is-active{
    border-right: 2px solid #000000;
  }
  .rank-select-item{
    font-size: 16px;
  }
  i{
    margin-bottom: 4px;
  }
  .body-right{
    margin: 0 0 40px;
  }
  .table{
    margin: 40px 2vw 10px;
  }
  .head{
    border-bottom: 1px solid #cccccc;
  }
  .line{
    border-bottom: 1px solid #cccccc;
  }
  .column{
    min-height: 52px;
    padding: 10px 4px 0;
  }
  .source{
    overflow: hidden;
    white-space:nowrap;
    text-overflow: ellipsis;
    text-align: left;
  }
  .targets{
    text-align: left;
  }
  .operation{
    color: #409eff;
    text-decoration: underline;
    cursor: pointer;
  }
  .el-tag{
    max-width: 200px;
    overflow: hidden;
    white-space:nowrap;
    text-overflow: ellipsis;
    cursor: pointer;
  }
  .el-tag:hover{
    color: black;
    text-decoration: underline;
  }
  .message{
    margin: 20px 0 10px;
    font-size: 32px;
  }
  .operate-source{
    margin: 10px 0;
  }
</style>
