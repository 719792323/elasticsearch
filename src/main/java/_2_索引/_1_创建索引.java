package _2_索引;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
//注意导入如下路径包，而不是admin路径下的包
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;

import java.io.IOException;

public class _1_创建索引 {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = null;
        try {
            // 创建客户端对象
            client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost("localhost", 9200, "http")));
            // 创建索引请求对象，如创建一个school的索引
            CreateIndexRequest request = new CreateIndexRequest("school");
            // 发送创建索引请求
            CreateIndexResponse response = client.indices().create(request,
                    RequestOptions.DEFAULT);
            boolean acknowledged = response.isAcknowledged();
            // 获取响应状态
            System.out.println(acknowledged);

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
