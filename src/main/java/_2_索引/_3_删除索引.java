package _2_索引;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class _3_删除索引 {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = null;
        try {
            //创建ES客户端
            client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost("localhost", 9200, "http")));
            DeleteIndexRequest request = new DeleteIndexRequest("school");
            // 发送请求，获取响应
            AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
            // 操作结果
            System.out.println("操作结果：" + response.isAcknowledged());
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
