package _2_索引;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;

import java.io.IOException;

public class _2_查询索引 {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = null;
        try {
            // 创建客户端对象
            client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost("localhost", 9200, "http")));
            // 查询索引 - 请求对象
            GetIndexRequest request = new GetIndexRequest("school");
            // 发送请求，获取响应
            GetIndexResponse response = client.indices().get(request,
                    RequestOptions.DEFAULT);
            System.out.println("aliases:" + response.getAliases());
            System.out.println("mappings:" + response.getMappings());
            System.out.println("settings:" + response.getSettings());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                // 关闭客户端连接
                client.close();
            }
        }

    }
}
