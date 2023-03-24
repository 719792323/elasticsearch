package _2_索引;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class _1_创建索引 {
    public static void main(String[] args) throws IOException {
        // 创建客户端对象
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        // 创建索引请求对象，如创建一个school的索引
        CreateIndexRequest request = new CreateIndexRequest("school");
        // 发送请求
        CreateIndexResponse response = client.indices().create(request,
                RequestOptions.DEFAULT);
        boolean acknowledged = response.isAcknowledged();
        // 获取响应状态
        System.out.println(acknowledged);
        // 关闭客户端连接
        client.close();
    }
}
