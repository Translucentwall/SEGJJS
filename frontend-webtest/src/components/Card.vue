<template>
  <div class="card">
    <div class="title-wrap">
      <a class="title" :href="'/paper/'+simplePaperVO.id" v-html="simplePaperVO.title"></a>
    </div>
    <div class="author-affiliation-wrap" v-if="simplePaperVO.author_simpleAffiliationVOS&&simplePaperVO.author_simpleAffiliationVOS[0]&&simplePaperVO.author_simpleAffiliationVOS[0].author!==''">
      <div v-for="(item,index) in simplePaperVO.author_simpleAffiliationVOS" class="author-affiliation" v-if="index < 3">
        <a class="author" :href="'/entity/author/'+item.authorId" v-html="item.author"></a>
        <span v-if="item.affiliation&&item.affiliation!=='NA'">,</span>
        <a class="affiliation" :href="'/entity/affiliation/'+item.affiliationId" v-if="item.affiliation&&item.affiliation!=='NA'" v-html="item.affiliation"></a>
        <span>;</span>
      </div>
      <div v-if="simplePaperVO.author_simpleAffiliationVOS.length > 3">...</div>
    </div>
    <div class="publication-wrap" v-if="simplePaperVO.publicationTitle">
      <span>publication: </span>
      <a class="publication" :href="'/entity/issue/'+simplePaperVO.conferenceId">
        <span v-html="simplePaperVO.publicationTitle"></span>
      </a>
    </div>
    <div class="keyword-wrap" v-if="simplePaperVO.keywords&&simplePaperVO.keywords[0]">
      <span v-for="(word,index) in simplePaperVO.keywords" v-if="index < 5">
        <span class="keyword" @click="searchItem(word)" v-html="word"></span>
      </span>
    </div>
  </div>
</template>

<script>
export default {
    name: 'Card',
    data(){
        return{
        }
    },
    props: {
        simplePaperVO: {
            type: Object,
            default: {}
        }
    },
    methods: {
        searchItem: function (item) {
            let pattern = /<b><span style="color: #b04c50; ">/;
            if(pattern.test(item)){
                let contentTmp = item.replace(/<b><span style="color: #b04c50; ">/g, '');
                item = contentTmp.replace(/<\/span><\/b>/g, '');
            }
            window.location.href = '/search/Keyword/'+item;
        }
    }
}
</script>

<style scoped>
  .card{
    background-color: white;
    margin: 0 50px 20px;
    box-shadow: 0 1px 0 #aaaaaa;
    text-align: left;
    padding: 0 20px 5px 2px;
  }
  .title{
    color: #069;
    font-size: 24px;
    text-decoration: none;
  }
  .title:hover{
    color: #409eff;
    text-decoration: underline;
  }
  .author-affiliation-wrap{
    /*display: flex;*/
    padding: 0 12px;
  }
  .author-affiliation{
    margin-right: 10px;
  }
  .author{
    color: #000000;
    text-decoration: none;
  }
  .author:hover{
    color: #409eff;
    text-decoration: underline;
  }
  .affiliation{
    color: #000000;
    text-decoration: none;
  }
  .affiliation:hover{
    color: #409eff;
    text-decoration: underline;
  }
  .publication-wrap{
    padding: 0 12px;
  }
  .publication{
    color: #000000;
    text-decoration: none;
  }
  .publication:hover{
    color: #409eff;
    text-decoration: underline;
  }
  .keyword-wrap{
    padding: 0 12px;
  }
  .keyword{
    margin-right: 20px;
    color: #069;
  }
  .keyword:hover{
    color: #409eff;
    text-decoration: underline;
  }
</style>
