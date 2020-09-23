<template>
  <div class="search-wrap">
    <div class="search-top">
      <el-dropdown trigger="click" @command="handleMode">
        <el-button class="mode-button" type="primary">
          {{mode}}<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="All">All</el-dropdown-item>
          <el-dropdown-item command="Title">Title</el-dropdown-item>
          <el-dropdown-item command="Author">Author</el-dropdown-item>
          <el-dropdown-item command="Affiliation">Affiliation</el-dropdown-item>
          <el-dropdown-item command="Publication">Publication</el-dropdown-item>
          <el-dropdown-item command="Keyword">Keyword</el-dropdown-item>
          <el-dropdown-item command="Advanced">Advanced</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <el-input
        v-if="mode!=='Advanced'"
        class="search-input"
        v-model="content"
        placeholder="Please input the search content..."
        @keydown.13.native="search"
        @keydown.229="handleCN">
      </el-input>
      <div class="advanced" v-if="mode==='Advanced'">
        <el-dropdown trigger="click" @command="handleRelationMode">
          <el-button class="relation-mode" type="primary" size="small">
            {{relationMode}}<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="AND">AND</el-dropdown-item>
            <el-dropdown-item command="OR">OR</el-dropdown-item>
            <el-dropdown-item command="NOT">NOT</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-dropdown trigger="click" @command="handleAdvancedMode">
          <el-button class="advanced-mode" type="primary" size="small">
            {{advancedMode}}<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="Title">Title</el-dropdown-item>
            <el-dropdown-item command="Author">Author</el-dropdown-item>
            <el-dropdown-item command="Affiliation">Affiliation</el-dropdown-item>
            <el-dropdown-item command="Publication">Publication</el-dropdown-item>
            <el-dropdown-item command="Keyword">Keyword</el-dropdown-item>
            <el-dropdown-item command="Year">Year</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-input
          class="advanced-input"
          size="small"
          v-model="advancedContent"
          placeholder="Please input the advanced search..."
          @keydown.13.native="add"
          @keydown.229="handleCN">
        </el-input>
        <el-button class="advanced-add" @click="add" size="mini">
          <i class="el-icon-plus"></i>
        </el-button>
      </div>
      <el-button class="search-enter" @click="search">
        <i class="el-icon-search"></i>
      </el-button>
    </div>
    <div v-if="mode==='Advanced'" class="search-bottom">
      <div
        v-for="(advancedSearchItem, index) in advancedSearch"
        :class="'tag ' + advancedTypeDic[advancedSearchItem.mode]"
        :key="index"
        :title="advancedSearchItem.relation + ' ' + advancedSearchItem.mode + ': ' + advancedSearchItem.content">
        <div class="relation-text">{{advancedSearchItem.relation}}</div>
        <div class="tag-text">{{advancedSearchItem.content}}</div>
        <i class="el-icon-close tag-delete" @click="remove(index)"></i>
      </div>
    </div>
  </div>
</template>

<script>
    export default {
        name: "Search",
        data(){
            return{
                mode: 'All',
                content: '',
                relationMode: 'AND',
                advancedMode: 'Title',
                advancedContent: '',
                advancedSearch: [],
                advancedTypeDic: {
                  'Title': 'blue',
                  'Author': 'red',
                  'Affiliation': 'orange',
                  'Publication': 'yellow',
                  'Keyword': 'green',
                  'Year': 'light-blue'}
            }
        },
        props: {
            searchMode: {
                type: String,
                default: null
            },
            searchContent: {
                type: String,
                default: null
            }
        },
        mounted() {
            if(this.searchMode){
                this.mode = this.searchMode;
            }
            if(this.searchContent){
                this.content = this.searchContent;
            }
        },
        methods: {
            handleMode(command) {
                this.mode = command;
            },
            handleRelationMode(command){
                this.relationMode = command;
            },
            handleAdvancedMode(command){
              this.advancedMode = command;
            },
            handleCN: function () {
                console.log('我捕获了');
            },
            search: function () {
                if(this.mode!=='Advanced'){
                    let blankPattern = /^( )*$/;
                    if (blankPattern.test(this.content)){
                        this.$message({
                            message:'请输入有效内容',
                            type: 'error',
                            duration: 2000
                        });
                    }else {
                        let pattern = /[%\\/?+#&=]/;
                        if(pattern.test(this.content)){
                            this.$message({
                                message:'Search content can\'t include %,\\,/,?,+,#,&,=',
                                type: 'error',
                                duration: 2000
                            });
                        }else{
                            window.location.href = '/search/' + this.mode + '/' + this.content;
                        }
                    }
                }else{
                    if(!this.advancedSearch || this.advancedSearch.length === 0){
                        this.$message({
                            message:'请添加有效进阶查询内容',
                            type: 'error',
                            duration: 2000
                        });
                    }else{
                        let content = '';
                        this.advancedSearch.forEach(function (advancedSearchItem) {
                            content = content + advancedSearchItem.relation + ' ' + advancedSearchItem.mode + ':' + advancedSearchItem.content + ' ';
                        })
                        if(this.advancedSearch[0].relation !== 'NOT'){
                            content = content.replace(' ', '，').split('，')[1];
                        }
                        console.log(content);
                        sessionStorage.setItem('advancedContent', content);
                        window.location.href = '/search/' + this.mode + '/' + content;
                    }
                }
            },
            add: function () {
                let blankPattern = /^( )*$/;
                if (blankPattern.test(this.advancedContent)){
                    this.$message({
                        message:'请加入有效内容',
                        type: 'error',
                        duration: 2000
                    });
                }else {
                    let pattern = /[%\\/?+#&=]/;
                    if(pattern.test(this.content)){
                        this.$message({
                            message:'Search content can\'t include %,\\,/,?,+,#,&,=',
                            type: 'error',
                            duration: 2000
                        });
                    }else{
                        this.advancedSearch.push({relation: this.relationMode, mode: this.advancedMode, content: this.advancedContent});
                        this.advancedContent = '';
                    }
                }
            },
            remove: function (index) {
                this.advancedSearch.splice(index, 1);
            }
        }
    }
</script>

<style scoped>
  .search-wrap{
    position: absolute;
    top: 40%;
    left: 28vw;
    display: block;
  }
  .search-top{
    display: flex;
    border: 2px solid #ffffff;
    border-radius: 4px;
  }
  .mode-button{
    width: 9vw;
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
  }
  .search-input{
    width: 31vw;
  }
  .advanced{
    display: flex;
    border: 4px solid #ffffff;
    border-right-width: 2px;
    border-left-width: 2px;
  }
  .relation-mode{
    width: 6vw;
    border-radius: 0;
    border-right: 1px solid #ffffff;
  }
  .advanced-mode{
    width: 9vw;
    border-radius: 0;
  }
  .advanced-input{
    width: 12vw;
  }
  .advanced-add{
    width: 4vw;
  }
  .search-enter{
    width: 4vw;
    border-top-left-radius: 0;
    border-bottom-left-radius: 0;
  }
  .el-icon-search{
    color: #ffffff;
  }
  .search-bottom{
    width: 44vw;
    min-height: 100px;
    max-height: 160px;
    margin-top: 1px;
    border: #ffffff dashed 2px;
    border-radius: 4px;
    padding: 2px;
    overflow-y: scroll;
  }
  .tag{
    height: 28px;
    width: 10vw;
    margin: 2px 6px;
    font-size: 18px;
    display: flex;
    float: left;
    padding-left: 2px;
    color: #ffffff;
  }
  .relation-text{
    margin-right: 2px;
  }
  .tag-text{
    width: 80%;
    overflow: hidden;
  }
  .tag-delete{
    margin-top: 4px;
    cursor: pointer;
  }
  .red{
    background-color: rgb(214, 39, 40);
  }
  .orange{
    background-color: rgb(255, 127, 14);
  }
  .yellow{
    background-color: rgb(214, 214, 8);
  }
  .green{
    background-color: rgb(44, 160, 44);
  }
  .blue{
    background-color: rgb(31, 119, 180);
  }
  .light-blue{
    background-color: #409eff;
  }
</style>
