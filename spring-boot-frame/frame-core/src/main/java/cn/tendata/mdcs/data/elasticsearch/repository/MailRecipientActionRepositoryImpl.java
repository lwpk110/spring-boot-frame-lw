package cn.tendata.mdcs.data.elasticsearch.repository;

import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.data.elasticsearch.domain.MailDeliveryTaskReportDocument;
import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import cn.tendata.mdcs.mail.MailDeliveryTaskReport;
import cn.tendata.mdcs.mail.MailRecipientAction;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

public class MailRecipientActionRepositoryImpl implements MailRecipientActionRepositoryCustom {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public Page<MailRecipientActionDocument> search(User user, String email, Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withFilter(boolFilter()
                        .must(hasParentFilter(MailDeliveryTaskReportDocument.TYPE, termFilter("user.userId", user.getId())))
                        .must(termFilter("email", email)))
                .withPageable(pageable)
                .build();
        return elasticsearchTemplate.queryForPage(searchQuery, MailRecipientActionDocument.class);
    }

    @Override
    public List<MailRecipientActionDocument> search(String taskId, String email) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withFilter(boolFilter()
                        .must(termFilter("taskId", taskId))
                        .must(termFilter("email", email)))
                .build();

        return elasticsearchTemplate.queryForList(searchQuery, MailRecipientActionDocument.class);
    }

    @Override
    public List countReport(String filed, String taskId) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                //.withQuery(matchQuery("taskId",taskId))  //非过滤方式
                .withQuery(filteredQuery(null, termFilter("taskId", taskId)))
                .withSearchType(SearchType.COUNT)
                .withIndices(MailDeliveryTaskReportDocument.INDEX).withTypes(MailRecipientActionDocument.TYPE)
                .addAggregation(
                        AggregationBuilders
                                .terms("counts")
                                .field(filed)).build();
        Aggregations aggregations = this.elasticsearchTemplate
                .query(searchQuery, (searchResponse) -> {
                    return searchResponse.getAggregations();
                });

        List<Terms.Bucket> list = (((StringTerms) aggregations.asMap().get("counts"))).getBuckets();

        return list;

    }


}
