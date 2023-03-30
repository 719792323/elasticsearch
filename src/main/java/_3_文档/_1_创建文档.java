package _3_文档;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class _1_创建文档 {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = null;
        try {
            //创建ES客户端
            client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost("localhost", 9200, "http")));

            IndexRequest request = new IndexRequest();
            request.index("school").id("1001");//指定索引和数据id
            Student student = new Student("zhangsan", 10, "female");
            //用jackson将对象转换成json
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(student);
            //放入json数据
            request.source(json, XContentType.JSON);
            //也可以
            //new IndexRequest().index("school").id("1001").source(XContentType.JSON, "name", "zhangsan", "age", "10", "sex","女")
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            System.out.println(response.getResult());
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
