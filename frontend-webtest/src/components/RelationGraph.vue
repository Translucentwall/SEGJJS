<template>
  <div id='svgContainer'>
    <div class="every">
      <div class="svg" id="forceDirected"></div>
      <div class="option" v-if="size==='large'">
        <div class="option_name">{{changeType}}: <a class="center_name" :href="type<4?'/entity/'+this.$route.params.type+'/'+id: null">{{graphVO.centerName}}</a></div>
        <div v-if="type<4">
          <input type="checkbox" v-model="showTotal">Show Total Graph
        </div>
        <el-input
          class="search-input"
          v-model="searchText"
          placeholder="filter"
          @input="searchNodes">
        </el-input>
      </div>
    </div>
  </div>
</template>
<script>
    import * as d3 from 'd3'
    import {getGraph, getMoreGraph} from "../api/api";
    import {Loading} from "element-ui";

    export default {
        name: 'RelationGraph',
        data () {
            return {
                id: 0,
                type: 1,
                graphVO: {},
                moreGraphVO: {},
                moreGraphReady: false,
                showTotal: false,
                searchText: '',
                isSearching: false,
                nodeNum: 0,
                maxHot: 0
            }
        },
        props: {
          eid: {
            type: Number,
            default: 13689
          },
          etype: {
            type: Number,
            default: 1
          },
          size: {
            type: String,
            default: 'large'
          }
        },
        mounted () {
            this.id = this.eid;
            this.type = this.etype;
            let loadingInstance = Loading.service({ fullscreen: true, text:'loading...'});
            getGraph(this.id,this.type)
                .then(res=>{
                    this.graphVO = res;
                    loadingInstance.close();
                    if(this.showTotal&&this.type<4&&this.size==='large'){
                        getMoreGraph(this.id,this.type)
                            .then(res=>{
                                this.moreGraphVO = res;
                                this.moreGraphReady = true;
                                let nodes = this.graphVO.nodes.concat(this.moreGraphVO.nodes);
                                let links = this.graphVO.links.concat(this.moreGraphVO.links);
                                this.forceDirected (nodes, links);
                            });
                    }else{
                        let nodes = this.graphVO.nodes;
                        let links = this.graphVO.links;
                        this.forceDirected (nodes, links);
                        if(this.type<4&&this.size==='large'){
                            getMoreGraph(this.id, this.type)
                                .then(res=>{
                                    this.moreGraphVO = res;
                                    this.moreGraphReady = true;
                                });
                        }
                    }
                });
        },
        watch: {
            showTotal: function () {
                if(this.showTotal&&!this.moreGraphReady&&this.type<4){
                    this.$notify.info({
                        title: 'info',
                        message: 'Total graph is preparing!',
                        duration: 0,
                        showClose: false
                    });
                }else{
                    if(this.showTotal&&this.type<4){
                        this.$notify.closeAll();
                        let svg = document.getElementById('forceDirected');
                        let children = svg.childNodes;
                        children.forEach(function (child) {
                            svg.removeChild(child);
                        });
                        let nodes = this.graphVO.nodes.concat(this.moreGraphVO.nodes);
                        let links = this.graphVO.links.concat(this.moreGraphVO.links);
                        this.forceDirected (nodes, links);
                    }else{
                        this.$notify.closeAll();
                        let svg = document.getElementById('forceDirected');
                        let children = svg.childNodes;
                        children.forEach(function (child) {
                            svg.removeChild(child);
                        });
                        let nodes = this.graphVO.nodes;
                        let links = this.graphVO.links;
                        this.forceDirected (nodes, links);
                    }
                }
            },
            moreGraphReady: function () {
                if(this.size === 'large'){
                    this.$notify.closeAll();
                    let that = this;
                    this.$notify.success({
                        title: 'success',
                        message: 'Total graph is ready!',
                        duration:1500,
                        onClose: function () {
                            if(that.showTotal){
                                let svg = document.getElementById('forceDirected');
                                let children = svg.childNodes;
                                children.forEach(function (child) {
                                    svg.removeChild(child);
                                });
                                let nodes = that.graphVO.nodes.concat(that.moreGraphVO.nodes);
                                let links = that.graphVO.links.concat(that.moreGraphVO.links);
                                that.forceDirected (nodes, links);
                            }
                        }
                    });
                }
            }
        },
        computed: {
            changeType: function () {
                let type = this.$route.params.type;
                return type.substring(0,1).toUpperCase() + type.substring(1);
            }
        },
        methods: {
            forceDirected (nodes, links) {
                console.log(nodes.length);
                this.nodeNum= nodes.length;
                let maxHot = Math.max.apply(Math,links.map(item => { return item.value }));
                this.maxHot=maxHot;
                let width = document.getElementById('svgContainer').offsetWidth;
                let height = 400;
                if(this.size==='large'){
                    height = nodes.length<150?800:nodes.length<400?1200:1600;
                }else{
                    height = nodes.length<50?600:800;
                }
                // let padding = {
                //     left: 30,
                //     right: 30,
                //     top: 20,
                //     bottom: 20
                // };
                let centerId = Number(this.id);
                let centerType = Number(this.type);
                let that = this;
                let svg = d3.select('#forceDirected')
                    .append('svg')
                    .attr('id', 'graph')
                    .attr('width', width)
                    .attr('height', height);
                // 通过布局来转换数据，然后进行绘制
                let simulation = d3.forceSimulation()
                    .nodes(nodes)
                    .force('link', d3.forceLink(links).distance(nodes.length<200?200:100).id(d=>d.id))
                    .force('charge', d3.forceManyBody())
                    .force('center', d3.forceCenter(width / 2, height / 2));

                // 添加连线
                svg.selectAll('line')
                    .data(links)
                    .enter()
                    .append('line')
                    .attr("stroke", "#cccccc")
                    .attr('stroke-width', function (d) {
                        if((d.value+1)/(maxHot+1)>0.8){
                            return d.value>=maxHot?10:(d.value+1)/(maxHot+1)*8+2;
                        }else if(d.value>=0){
                            return (d.value+1)/(maxHot+1)*4+2;
                        }else{
                            return 1;
                        }
                    })
                    .attr('stroke-dasharray', function (d) {
                        if(d.source.entityType===4 || d.target.entityType===4){
                            return '';
                        }else{
                            return '5,5'
                        }
                    })
                    .attr('background', 'linear-gradient(to right, red , yellow)');
                // 添加描述
                svg.selectAll('text')
                    .data(nodes)
                    .enter()
                    .append('text')
                    .style('font-size', '10px')
                    .style('fill', '#000000')
                    .style('left', '10px')
                    .attr('dx', 10)
                    .attr('dy', 10)
                    .text(function (d) {
                        if(nodes.length<200){
                            if(d.entityName.length<=20){
                                return d.entityName;
                            }else{
                                return d.entityName.substr(0,20)+'...';
                            }
                        }else{
                            if(d.entityName.length<=10){
                                return d.entityName;
                            }else{
                                return d.entityName.substr(0,10)+'...';
                            }
                        }

                    });
                // 添加节点
                svg.selectAll('circle')
                    .data(nodes)
                    .enter()
                    .append('circle')
                    .attr('r', function (d) {
                        if(nodes.length<200){
                            if(d.entityId===centerId&&d.entityType===centerType){
                                return 10
                            }else if(d.popularity>=0){
                                return d.popularity>=that.graphVO.popularity?10:(d.popularity/that.graphVO.popularity)*6+4;
                            }else{
                                return 6
                            }
                        }else{
                            if(d.entityId===centerId&&d.entityType===centerType){
                                return 6
                            }else if(d.popularity>=0){
                                return d.popularity>=that.graphVO.popularity?6:(d.popularity/that.graphVO.popularity)*2+4;
                            }else{
                                return 5
                            }
                        }
                    })
                    .attr('stroke', '#000000')
                    .attr('stroke-width', 2)
                    .style('cursor', 'pointer')
                    .style('fill', function (d) {
                        if(d.entityId===centerId&&d.entityType===centerType){
                            return 'rgb(106,0,95)';
                        } else if(d.entityType===1 ){
                            return 'rgb(214, 39, 40)';
                        } else if(d.entityType===2 ){
                            return 'rgb(255, 127, 14)';
                        } else if(d.entityType===3 ){
                            return 'rgb(214,214,8)';
                        } else if(d.entityType===4 ){
                            return 'rgb(44, 160, 44)';
                        } else if(d.entityType===5 ){
                            return 'rgb(31, 119, 180)';
                        } else{
                            return 'rgb(0,0,0)';
                        }
                    })
                    .attr('class', function (d) {
                        if(d.entityType===1){
                            return 'author';
                        }else if(d.entityType===2){
                            return 'affiliation';
                        }else if(d.entityType===3){
                            return 'issue';
                        }else if(d.entityType===4){
                            return 'term';
                        }else if(d.entityType===5){
                            return 'paper';
                        }else{
                            return ''
                        }
                    })
                    .on('click',function (d) {
                        let typeDic2 = {1:"author", 2:'affiliation', 3:'issue', 4:'term', 5:'paper'};
                        if(d.entityType<5){
                            window.location.href='/graph/' + typeDic2[d.entityType] + '/'+ d.entityId;
                        }else{
                            window.location.href='/paper/' + d.entityId;
                        }
                    })
                    // 添加圆圈的拖拽事件，同时他会触发simulation的tick事件，重新布局该区域
                    .call(d3.drag()
                        .on('start', function (d) {
                            if (!d3.event.active) {
                                simulation.alphaTarget(0.8).restart() // 设置衰减系数，对节点位置移动过程的模拟，数值越高移动越快，数值范围[0，1]
                            }
                            d.fx = d.x;
                            d.fy = d.y;
                        })
                        .on('drag', function (d) {
                            d.fx = d3.event.x;
                            d.fy = d3.event.y;
                        })
                        .on('end', function (d) {
                            if (!d3.event.active) {
                                simulation.alphaTarget(0);
                            }
                            d.fx = null;
                            d.fy = null;
                        })
                    )
                    .on('mouseover', function (d) {
                        if(!that.isSearching){
                            svg.selectAll('line')
                                .style('stroke-width', function(link) {
                                    if (link.source === d || link.target === d) {
                                        return 2;
                                    }
                                })
                                .style('stroke', function(link) {
                                    if (link.source === d || link.target === d) {
                                        return '#000000';
                                    }
                                });
                        }
                    })
                    .on('mouseout', function (d) {
                        if(!that.isSearching){
                            svg.selectAll('line')
                                .style('stroke-width', function(link) {
                                    if (link.source === d || link.target === d) {
                                        if((link.value+1)/(maxHot+1)>0.8){
                                            return link.value>=maxHot?10:(link.value+1)/(maxHot+1)*8+2;
                                        }else if(link.value>=0){
                                            return (link.value+1)/(maxHot+1)*4+2;
                                        }else{
                                            return 1;
                                        }
                                    }
                                })
                                .style('stroke', function(link) {
                                    if (link.source === d || link.target === d) {
                                        return '#cccccc'
                                    }
                                })
                        }
                    })
                    .append('title')
                    .text(d=>d.entityName);
                //数据重绘
                simulation.on('tick', function () {
                    svg.selectAll('circle')
                        .attr('cx', function (d) { return d.x })
                        .attr('cy', function (d) { return d.y });
                    svg.selectAll('text')
                        .attr('x', function (d) { return d.x })
                        .attr('y', function (d) { return d.y });
                    svg.selectAll('line')
                        .attr('x1', function (d) { return d.source.x })
                        .attr('y1', function (d) { return d.source.y })
                        .attr('x2', function (d) { return d.target.x })
                        .attr('y2', function (d) { return d.target.y });
                });
                this.searchNodes();
            },
            searchNodes: function () {
                let svg = d3.select('#forceDirected');
                let centerId = Number(this.id);
                let centerType = Number(this.type);
                let that = this;
                if(this.searchText===''){
                    this.isSearching = false;
                    svg.selectAll('circle')
                        .style('fill', function (d) {
                            if(d.entityId===centerId&&d.entityType===centerType){
                                return 'rgb(106,0,95)';
                            }
                            else if(d.entityType===1 ){
                                return 'rgb(214, 39, 40)';
                            }else if(d.entityType===2 ){
                                return 'rgb(255, 127, 14)';
                            }else if(d.entityType===3 ){
                                return 'rgb(214,214,8)';
                            }else if(d.entityType===4 ){
                                return 'rgb(44, 160, 44)';
                            }else if(d.entityType===5 ){
                                return 'rgb(31, 119, 180)';
                            }else{
                                return 'rgb(0,0,0)';
                            }
                        })
                        .attr('stroke', '#000000');
                    svg.selectAll('line')
                        .style('stroke-width', function(link) {
                            if((link.value+1)/(that.maxHot+1)>0.8){
                                return link.value>=that.maxHot?10:(link.value+1)/(that.maxHot+1)*8+2;
                            }else if(link.value>=0){
                                return (link.value+1)/(that.maxHot+1)*4+2;
                            }else{
                                return 1;
                            }
                        })
                        .style('stroke', '#cccccc');
                    svg.selectAll('text')
                        .text(function (d) {
                            if(that.nodeNum.length<200){
                                if(d.entityName.length<=20){
                                    return d.entityName;
                                }else{
                                    return d.entityName.substr(0,20)+'...';
                                }
                            }else{
                                if(d.entityName.length<=10){
                                    return d.entityName;
                                }else{
                                    return d.entityName.substr(0,10)+'...';
                                }
                            }

                        });
                }else{
                    this.isSearching = true;
                    svg.selectAll('circle')
                        .style('fill', function (d) {
                            if(d.entityName.indexOf(that.searchText)!==-1){
                                if(d.entityId===centerId&&d.entityType===centerType){
                                    return 'rgb(106,0,95)';
                                }
                                else if(d.entityType===1 ){
                                    return 'rgb(214, 39, 40)';
                                }else if(d.entityType===2 ){
                                    return 'rgb(255, 127, 14)';
                                }else if(d.entityType===3 ){
                                    return 'rgb(214,214,8)';
                                }else if(d.entityType===4 ){
                                    return 'rgb(44, 160, 44)';
                                }else if(d.entityType===5 ){
                                    return 'rgb(31, 119, 180)';
                                }
                            }
                            else{
                                return '#cccccc'
                            }
                        })
                        .attr('stroke', function (d) {
                            if(d.entityName.indexOf(that.searchText)!==-1){
                                return '#000000'
                            }else {
                                return '#cccccc'
                            }
                        });
                    svg.selectAll('line')
                        .style('stroke-width', function(link) {
                            if (link.source.entityName.indexOf(that.searchText)!==-1 || link.target.entityName.indexOf(that.searchText)!==-1) {
                                return 2;
                            }
                        })
                        .style('stroke', function(link) {
                            if (link.source.entityName.indexOf(that.searchText)!==-1 || link.target.entityName.indexOf(that.searchText)!==-1) {
                                return '#000000';
                            }
                        });
                    svg.selectAll('text')
                        .text(function (d) {
                            if(d.entityName.indexOf(that.searchText)!==-1){
                                return d.entityName
                            }else{
                                return ''
                            }
                        })
                }
            }
        }
    }
</script>
<style>
  #svgContainer{
    width: 100%;
    height: 400px;
  }
  #forceDirected{
    width: 100%;
  }
  .every{
    /*margin:15px;*/
    width: 100%;
    float: left;
    position: relative;
  }
  .svg{
    /*border: 1px solid #cccccc;*/
  }
  .option{
    position: fixed;
    top: 40px;
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
