# 1.索引
## 1.1 创建索引(POST)
向es服务器发起一个put请求来创建索引，如果请求成果会返回一个json格式消息。
shards_acknowledged的值表示是否响应成功，index表示创建的索引名称
```json
{
    "acknowledged": true,
    "shards_acknowledged": true,
    "index": "shopping"
}
```
* 指令url:es服务器地址/索引名称
## 1.2 设置索引结构(POST)
* url:es服务/索引名/_mapping
> 假设创建好了一个名为user的索引，添加了name，sex，tel三个属性
* type:表示这个属性数据类型，text属性支持分词查询，keyword属性不支持分词查询，而要完全匹配查询
* index表示是否支持索引，如果为true的话，表示该字段可以用来查询，如果为false，则不能用该字段来查询，如果用了会报错
```text
{
    "properties": {
        "name":{
        	"type": "text",
        	"index": true
        },
        "sex":{
        	"type": "keyword",
        	"index": true
        },
        "tel":{
        	"type": "keyword",
        	"index": false
        }
    }
}

```
## 1.3 查看索引(GET)
* 查看具体的索引:es服务器地址/索引名称
```json
{
    "shopping": {
        "aliases": {},
        "mappings": {},
        "settings": {
            "index": {
                "creation_date": "1679206997448",
                "number_of_shards": "1",
                "number_of_replicas": "1",
                "uuid": "3PBN3zyWRcqLhWjl5VaQ0A",
                "version": {
                    "created": "7080099"
                },
                "provided_name": "shopping"
            }
        }
    }
}
```
* 查看全部索引:http://127.0.0.1:9200/_cat/indices?v
>如下返回的并不是json字符串，第一行是列名称。
```text
health status index    uuid                   pri rep docs.count docs.deleted store.size pri.store.size
yellow open   shopping 3PBN3zyWRcqLhWjl5VaQ0A   1   1          0            0       208b           208b
```
## 1.4 删除索引(DELETE)
* 删除具体的索引:es服务器地址/索引名称
```json
{
    "acknowledged": true
}
```

# 2. 文档
> 前提需要创建好索引
## 2.1 创建文档(POST)
* 指令url:es服务器地址/索引名称/_doc
> 要存的数据以json的格式放在POST的Body里，注意返回的数据_id的只每次都会变化，表示这个操作不是幂等性的
```json
{
    "_index": "shopping",
    "_type": "_doc",
    "_id": "Sha9-IYBMiJEJo2xKGvb",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 0,
    "_primary_term": 1
}
```
* 指令url:es服务器地址/索引名称/_doc/自己指定的id
> 因为_id如果不指定系统会自动生成，所以可以自己指定id，如下指定id为1001
```json
{
    "_index": "shopping",
    "_type": "_doc",
    "_id": "1001",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 1,
    "_primary_term": 1
}
```
## 2.2 查询文档(GET)
### 2.2.1 查询某个索引下具体文档
* 指令url:es服务器地址/索引名称/_doc/文档id
> found表示是否找到，_source是数据
```json
{
    "_index": "shopping",
    "_type": "_doc",
    "_id": "1001",
    "_version": 1,
    "_seq_no": 1,
    "_primary_term": 1,
    "found": true,
    "_source": {
        "title": "华为手机",
        "price": 3999
    }
}
```
### 2.2.2 查询某个索引下全部文档
* 指令url:es服务器地址/索引名称/_search
> 对应body里可以把match换成match_all:{}，也表示全部查询，took表示该索引下数据条数，hints里是具体数据
```json
{
    "took": 2,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 2,
            "relation": "eq"
        },
        "max_score": 1.0,
        "hits": [
            {
                "_index": "shopping",
                "_type": "_doc",
                "_id": "Sha9-IYBMiJEJo2xKGvb",
                "_score": 1.0,
                "_source": {
                    "title": "小米手机",
                    "price": 3999
                }
            },
            {
                "_index": "shopping",
                "_type": "_doc",
                "_id": "1001",
                "_score": 1.0,
                "_source": {
                    "title": "华为手机",
                    "price": 3999
                }
            }
        ]
    }
}
```
## 2.3 条件查询(GET)
> 假设shopping索引下有如下数据。
>> 注：es的查询是分词拆解查询，es会将数据库中的文本分词后存储（具体是这些分词进行倒排索引），查询内容也会进行分词。实际查询是查询的内容的分词和数据库的分词匹配
>> 所以查询会有一个score字段，score分数越高，表示分词匹配度越高
>> 下面的例子中使用match都表示这种分词匹配，如果想完全匹配（不启用分词）把match改成match_phrase
```json
[{
 "title": "小米手机",
 "price": 3999
},
{
  "title": "荣耀手机",
  "price": 4999
}]
```
### 2.3.1 url参数方式
* url:es服务/索引/_search?q=查询条件
> 例：查询shopping下的title含有手机的文档
>> http://127.0.0.1:9200/shopping/_search?q=title:手机
```json
{
    "took": 29,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 2,
            "relation": "eq"
        },
        "max_score": 0.36464313,
        "hits": [
            {
                "_index": "shopping",
                "_type": "_doc",
                "_id": "Sha9-IYBMiJEJo2xKGvb",
                "_score": 0.36464313,
                "_source": {
                    "title": "小米手机",
                    "price": 3999
                }
            },
            {
                "_index": "shopping",
                "_type": "_doc",
                "_id": "1001",
                "_score": 0.36464313,
                "_source": {
                    "title": "荣耀手机",
                    "price": 4999
                }
            }
        ]
    }
}
```
### 2.3.2 body参数方式
* url:es服务/索引/_search
> 请求还是GET请求，但是把参数放在body里，如下请求参数查询shopping下的title含有手机的文档
```json
{
    "query":{
        "match":{
            "title":"手机"
        }
    }
}
```
### 2.3.3 分页查询
> 参考2.3.2写法，加上from与size参数，from表示返回数据的第几页，size表示每页返回几个数据
```json
{
    "query":{
        "match":{
            "title":"手机"
        }
    },
    "from":0,
    "size":2
}
```
### 2.3.4 指定返回字段
> 参考2.3.2写法，加上_source参数，指定查询返回的字段，如下只返回title字段
```json
{
    "query":{
        "match":{
            "title":"手机"
        }
    },
    "from":0,
    "size":1,
    "_source":["title"]
}
```
### 2.3.5 按字段排序返回
> 参考2.3.2写法，加上sort参数，并指定按哪个字段排序，order指定升序(asc)还是降序(desc)。如下指定按price，升序返回
```json
{
    "query":{
        "match":{
            "title":"手机"
        }
    },
    "from":0,
    "size":2,
    "_source":["title"],
    "sort":{
        "price":{
            "order":"asc"
        }
    }
}
```
### 2.3.6 多条件查询 must/should
> 参考2.3.2写法，match里写条件，如果用must表示这些条件必须同时满足，用should表示只需要满足其一即可
```json
{
    "query":{
        "bool":{
            "must":[
                {
                    "match":{
                        "title":"手机"
                    }
                },
                {
                    "match":{
                        "price":"4999"
                    }
                }
            ]
        }
    }
}
```
### 2.3.7 范围查询
> 参考2.3.2写法，加上filter，如下表示查询price字段大于(gt)4000的手机
```json
{
    "query": {
        "bool": {
            "must": [
                {
                    "match": {
                        "title": "手机"
                    }
                }
            ],
            "filter": {
                "range": {
                    "price": {
                        "gt": 4000
                    }
                }
            }
        }
    }
}
```
### 2.3.8 聚合查询
> 分组名可以自定义，在分组名下选择聚合函数，terms表示求分组个数，avg求平均值...，如下求以price字段分组，各组文档个数
>> 注意size可加可不加
```text
{
    "aggs": {
        //自定义分组名
        "price_group": {
            //terms求分组个数，换成avg求分组平均值
            "terms": {
                "field": "price"
            }
        }
    }
    //  ,
    //如果只要聚合结果不需要原始数据，添加size=0这个hints不会返回数据，也就是变成"hints":[]
    //  "size":0
}
```
## 2.4 修改数据
### 2.4.1 覆盖式更新(PUT)
* 指令url:es服务器地址/索引名称/_doc/文档id
> 数据以json的格式放在body里，覆盖式更新相当于直接用新数据覆盖掉原来存储的内容
```json
{
    "_index": "shopping",
    "_type": "_doc",
    "_id": "1001",
    "_version": 2,
    "result": "updated",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 2,
    "_primary_term": 2
}
```
### 2.4.2 局部更新(POST)
* 指令url:es服务器地址/索引名称/_update/文档id
> 相较于覆盖式更新，局部更新是更新原数据中的某个内容，修改的内容需要用"doc":{}，包裹起来，然后发送给es，如下修改字段a的内容至xx
```json
{
    "doc" :{
        "a":"xx"
    }
}
```
```json
{
    "_index": "shopping",
    "_type": "_doc",
    "_id": "1001",
    "_version": 5,
    "result": "updated",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 5,
    "_primary_term": 2
}
```

# HTTP各种请求写法参考
## POST请求
如下创建一个创建shopping的索引
### curl
```shell
curl --location --request PUT 'http://127.0.0.1:9200/shopping'
```
> 附带数据
```shell
curl --location --request PUT 'http://127.0.0.1:9200/shopping' \
--header 'Content-Type: application/json' \
--data-raw '{"key":"value"}'
```
### wget
```shell
wget --no-check-certificate --quiet \
  --method PUT \
  --timeout=0 \
  --header '' \
   'http://127.0.0.1:9200/shopping'
```
> 附带数据
```shell
wget --no-check-certificate --quiet \
  --method PUT \
  --timeout=0 \
  --header 'Content-Type: application/json' \
  --body-data '{"key":"value"}' \
   'http://127.0.0.1:9200/shopping'
```
### Java okhttp
```text
OkHttpClient client = new OkHttpClient().newBuilder()
  .build();
MediaType mediaType = MediaType.parse("text/plain");
RequestBody body = RequestBody.create(mediaType, "");
Request request = new Request.Builder()
  .url("http://127.0.0.1:9200/shopping")
  .method("PUT", body)
  .build();
Response response = client.newCall(request).execute();
```
> 附带数据(json)
```text
OkHttpClient client = new OkHttpClient().newBuilder()
  .build();
MediaType mediaType = MediaType.parse("application/json");
RequestBody body = RequestBody.create(mediaType, "{\"key\":\"value\"}");
Request request = new Request.Builder()
  .url("http://127.0.0.1:9200/shopping")
  .method("PUT", body)
  .addHeader("Content-Type", "application/json")
  .build();
Response response = client.newCall(request).execute();
```
> 附带数据(text)
```text
OkHttpClient client = new OkHttpClient().newBuilder()
  .build();
MediaType mediaType = MediaType.parse("text/plain");
RequestBody body = RequestBody.create(mediaType, "{\"key\":\"value\"}");
Request request = new Request.Builder()
  .url("http://127.0.0.1:9200/shopping")
  .method("PUT", body)
  .addHeader("Content-Type", "text/plain")
  .build();
Response response = client.newCall(request).execute();
```
### python requests
```text
import requests

url = "http://127.0.0.1:9200/shopping"
payload={}
headers = {}
response = requests.request("PUT", url, headers=headers, data=payload)
print(response.text)
```
> 附带数据(json)
```text
import requests
import json

url = "http://127.0.0.1:9200/shopping"
payload = json.dumps({
  "key": "value"
})
headers = {
  'Content-Type': 'application/json'
}
response = requests.request("PUT", url, headers=headers, data=payload)
print(response.text)
```
> 附带数据(text)
```text
import requests

url = "http://127.0.0.1:9200/shopping"
payload = "{\"key\":\"value\"}"
headers = {
  'Content-Type': 'text/plain'
}
response = requests.request("PUT", url, headers=headers, data=payload)
print(response.text)

```
## GET请求
如下查看shopping索引信息
### curl
```shell
curl --location --request GET 'http://127.0.0.1:9200/shopping'
```
### wget
```shell
wget --no-check-certificate --quiet \
  --method GET \
  --timeout=0 \
  --header '' \
   'http://127.0.0.1:9200/shopping'
```
### Java okhttp
```text
OkHttpClient client = new OkHttpClient().newBuilder()
  .build();
Request request = new Request.Builder()
  .url("http://127.0.0.1:9200/shopping")
  .method("GET", null)
  .build();
Response response = client.newCall(request).execute();
```
### python requests
```text
import requests
url = "http://127.0.0.1:9200/shopping"
payload={}
headers = {}
response = requests.request("GET", url, headers=headers, data=payload)
print(response.text)
```
## DELETE请求
如下删除shopping索引
### curl
```shell
curl --location --request DELETE 'http://127.0.0.1:9200/shopping'
```
### wget
```shell
wget --no-check-certificate --quiet \
  --method DELETE \
  --timeout=0 \
  --header '' \
   'http://127.0.0.1:9200/shopping'
```
### Java okhttp
```text
OkHttpClient client = new OkHttpClient().newBuilder()
  .build();
MediaType mediaType = MediaType.parse("text/plain");
RequestBody body = RequestBody.create(mediaType, "");
Request request = new Request.Builder()
  .url("http://127.0.0.1:9200/shopping")
  .method("DELETE", body)
  .build();
Response response = client.newCall(request).execute();
```
### python requests
```text
import requests

url = "http://127.0.0.1:9200/shopping"
payload={}
headers = {}
response = requests.request("DELETE", url, headers=headers, data=payload)
print(response.text)
```
## PUT请求
> 和POST指令基本一样，只需要把POST的地方换成PUT即可