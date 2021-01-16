## 小组成员


## 流水线部署


## 项目部署地址

http://47.103.192.117/home   

## 接口说明

请求方法：get
请求路径：/admin/refresh
返回结果：
success


请求方法：get
请求路径：/admin/getConfusedAlias?page=xx&type=xx
返回结果：
{
    List<AliasVO>    
}


请求方法：post
请求路径：/admin/modifyAlias
提交数据：
{
    sonId,
    fatherId,
    type,
}
返回结果：
{
    success,
}


请求方法：post
请求路径：/admin/getEffectiveAlias
提交数据：
{
    page,
    type,
}
返回结果：
{
    List<AliasVO>,
}


请求方法：post
请求路径：/admin/cancelAlias
提交数据：
{
    sonId,
    type,
}
返回结果：
{
    success,
}


请求方法：get
请求路径：/search/{text}/{mode}?pageNumber=xx&sortMode=xx&perPage=xx
返回结果：
{
    List<SimplePaperVO>
}


请求方法：get
请求路径：/paperDetail/{id}
返回结果：
{
    PaperVO
}


请求方法：get
请求路径：/searchable
返回结果：
{
    success
}


请求方法：get
请求路径：/peerReview/complete/{prefix}
返回结果：
{
    List<String>
}


请求方法：post
请求路径：/peerReview/recommend
提交数据：
{
    InformationReviewed
}
返回结果：
{
    List<Author>
}


请求方法：get
请求路径：/rank/{mode}?pageNumber=xx&descend=xx&startYear=xx&endYear=xx
返回结果：
{
    RankVO
}


请求方法：get
请求路径：/hot?type=xx
返回结果：
{
    List<PopRankItem>
}


