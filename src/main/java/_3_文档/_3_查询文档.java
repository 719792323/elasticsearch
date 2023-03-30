package _3_文档;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class _3_查询文档 {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = null;
        try {
            client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost("localhost", 9200, "http")));
            GetRequest request = new GetRequest();
            request.index("school").id("1001");
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            //获取结果json
            System.out.println(response.getSourceAsString());
            ObjectMapper objectMapper = new ObjectMapper();
            Student student = objectMapper.readValue(response.getSourceAsString(), Student.class);
            System.out.println(student);
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
