package _1_客户端;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class 客户端创建与关闭 {
    public static void main(String[] args) throws IOException {
        //创建ES客户端
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost",9200,"http")));
        //关闭ES客户端
        client.close();
    }
}
