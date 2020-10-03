<template>
  <div>
    <el-menu
      :default-active="activeIndex"
      class="topBar"
      mode="horizontal"
      @select="handleSelect"
      active-text-color="#000000"
    >
      <el-menu-item class="home" index="/home" :style="{margin: this.$route.path==='/admin/manage'?'0 0 0 35%':'0 0 0 40%'}">Home</el-menu-item>
      <el-submenu index="2">
        <template  class="submenu-title" slot="title">Rank</template>
        <el-submenu index="/rank">
          <template slot="title">Paper</template>
          <el-menu-item index="/rank?mode=1">Cited Rank</el-menu-item>
        </el-submenu>
        <el-submenu index="2-2">
          <template slot="title">Author</template>
          <el-menu-item index="/rank?mode=2">Paper Rank</el-menu-item>
          <el-menu-item index="/rank?mode=3">Cited Rank</el-menu-item>
        </el-submenu>
        <el-submenu index="2-3">
          <template slot="title">Affiliation</template>
          <el-menu-item index="/rank?mode=4">Paper Rank</el-menu-item>
        </el-submenu>
        <el-submenu index="2-4">
          <template slot="title">Publication</template>
          <el-menu-item index="/rank?mode=5">Paper Rank</el-menu-item>
        </el-submenu>
        <el-submenu index="2-5">
          <template slot="title">Keyword</template>
          <el-menu-item index="/rank?mode=6">Paper Rank</el-menu-item>
        </el-submenu>
      </el-submenu>
      <el-menu-item class="admin" index="/admin/manage" v-if="this.$route.path==='/admin/manage'">Manage</el-menu-item>
    </el-menu>
    <div class="option">
      <a class="option-item" :href="'/home'">User</a>
      <span>|</span>
      <a class="option-item" :href="'/login'">Admin</a>
    </div>
  </div>
</template>

<script>
    export default {
        name: "TopBar",
        data() {
            return {
                activeIndex: '1'
            };
        },
        mounted(){
            let path = this.$route.path;
            let mode = window.location.href.split('mode=');
            if (mode.length > 1 && mode[1] !== ''){
                path = path + '?mode=' + mode[1][0];
            }
            this.activeIndex = path;
        },
        watch: {
            $route: function () {
                let path = this.$route.path;
                if (path!=='/home') {
                    let mode = window.location.href.split('mode=');
                    if (mode.length > 1 && mode[1] !== '') {
                        path = path + '?mode=' + mode[1][0];
                    }
                }
                this.activeIndex = path;
            }
        },
        methods: {
            handleSelect(key, keyPath) {
                window.location.href = keyPath[keyPath.length - 1];
                // this.$router.push(url).catch(err =>{
                //     console.log(err);
                // });
            }
        }

    }
</script>

<style scoped>
  .topBar{
    background-color: transparent;
  }
  .topBar .home{
    width: 10%;
  }
  .admin{
    width: 10%;
  }
  .option{
    position: absolute;
    top: 9px;
    left: 20px;
  }
  .option-item{
    color: #909399;
    text-decoration: none;
  }
  .option-item:hover{
    color: #000000;
    text-decoration: underline;
  }
</style>
