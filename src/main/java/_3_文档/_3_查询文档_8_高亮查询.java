package _3_文档;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.io.IOException;

public class _3_查询文档_8_高亮查询 {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = null;
        try {
            client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
            // 创建搜索请求对象
            SearchRequest request = new SearchRequest();
            request.indices("school");
            // 构建查询的请求体
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            //查询条件
            TermQueryBuilder termQuery = QueryBuilders.termQuery("name", "zhangsan");
            //高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            //自定义设置将高亮部分的包裹标签
            highlightBuilder.preTags("<font color='read'>");
            highlightBuilder.postTags("</font>");
            //设置高亮部分
            highlightBuilder.field("name");
            sourceBuilder.highlighter(highlightBuilder);
            sourceBuilder.query(termQuery);
            request.source(sourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            // 查询匹配
            SearchHits hits = response.getHits();
            System.out.println("took:" + response.getTook());
            System.out.println("timeout:" + response.isTimedOut());
            System.out.println("total:" + hits.getTotalHits());
            System.out.println("MaxScore:" + hits.getMaxScore());
            for (SearchHit hit : hits) {
                //输出每条查询的结果信息
                System.out.println(hit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                //关闭ES客户端
                client.close();
            }
        }
    }
}
