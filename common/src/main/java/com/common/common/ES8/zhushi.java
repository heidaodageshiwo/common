package com.common.common.ES8;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.ES8
 * @ClassName: zhushi
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-09-26  09:28
 * @UpdateDate: 2022-09-26  09:28
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class zhushi {
    /**
     * 文档检索
     *
     * @param param
     * @return
     * @throws IOException
     */
  /*  @Override
    public PageInfo<Doc> docSearch(DocSearchParam param) throws IOException {
        // 分页查询
        SearchResponse<Doc> response = elasticsearchClient.search(s -> searchConditionBuilder(param, s)
                , Doc.class);
        log.info("检索完成,耗时={}ms,hit总数={}", response.took(), response.hits().total().value());
        List<Doc> docList = response.hits().hits().stream().map(x -> {
            Doc doc = x.source();
            if (doc == null) {
                return null;
            }
            Map<String, List<String>> highlight = x.highlight();
            List<String> titleList = highlight.get(SystemConstant.ElasticConstants.FIELD_TITLE);
            List<String> contentList = highlight.get(SystemConstant.ElasticConstants.FIELD_CONTENT);

            if (!CollectionUtils.isEmpty(titleList)) {
                doc.setTitle(titleList.get(0));
            }
            if (CollectionUtils.isEmpty(contentList)) {
                int length = doc.getContent().length();
                doc.setContent(doc.getContent().substring(0, Math.min(length, 300)).replaceAll("\n", ""));
            } else {
                doc.setContent(contentList.stream().limit(5).collect(Collectors.joining()).replaceAll("\n", ""));
            }
            return doc;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return PageInfo.pageOf(docList, Long.valueOf(response.hits().total().value()).intValue(), param.getPageSize(), param.getPageNum());
    }


    *//**
     * doc检索表达式
     *
     * @param param
     * @param s
     * @return
     *//*
    private SearchRequest.Builder searchConditionBuilder(DocSearchParam param, SearchRequest.Builder s) {
        SearchRequest.Builder builder = s
                .index(SystemConstant.ElasticConstants.INDEX_DOC_ALL)
                .query(q -> q
                        //.term(t -> t
                        //        .field("content")
                        //        .value(param.getKw()).
                        //)
                        .bool(b -> b.should(should -> should
                                                //.wildcard(m -> m
                                                //        .field(SystemConstant.ElasticConstants.FIELD_TITLE)
                                                //        .value("*" + param.getKw() + "*")
                                                //)
                                                //字段映射中必须使用分词器，否则查不出来
                                                //.fuzzy(f -> f
                                                //        .field(SystemConstant.ElasticConstants.FIELD_TITLE)
                                                //        .value(param.getKw())
                                                //)
                                                .match(m -> m
                                                        .field(SystemConstant.ElasticConstants.FIELD_TITLE)
                                                        .query(param.getKw())
                                                )
                                        )
                                        .should(should -> should
                                                .match(m -> m
                                                        .field(SystemConstant.ElasticConstants.FIELD_CONTENT)
                                                        .query(param.getKw())
                                                )
                                        )
                        )
                )
                //高亮
                .highlight(h -> h
                        .fields(SystemConstant.ElasticConstants.FIELD_CONTENT, f -> f
                                .preTags("<font color='red'>")
                                .postTags("</font>")
                        )
                        .fields(SystemConstant.ElasticConstants.FIELD_TITLE, f -> f
                                .preTags("<font color='red'>")
                                .postTags("</font>")
                        )
                )
                .from(param.getPageNum() * param.getPageSize() - param.getPageSize())
                .size(param.getPageSize());
        //排序字段为空，则使用默认排序
        if (StringUtils.isNotBlank(param.getOrderField())) {
            builder.sort(sort -> sort.field(f -> f
                            .field(Optional.ofNullable(param.getOrderField()).orElse(SystemConstant.ElasticConstants.FIELD_UPDATE_TIME))
                            .order(SortOrder.Desc.jsonValue().equals(param.getOrderType()) ? SortOrder.Desc : SortOrder.Asc)
                    )
            );
        }
        return builder;
    }
*/
}
