package com.common.common;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common
 * @ClassName: TestAnalyzer
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-10-08  11:21
 * @UpdateDate: 2022-10-08  11:21
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class TestAnalyzer {
}
    /*Analyzer 简介
    我们知道 Elasticsearch 可以实现秒级的搜索速度，其中很重要的一个原因就当一个文档被存储的时候，同时它也对文档的数据进行了索引（indexing）。这样在以后的搜索中，就可以变得很快。简单地说，当一个文档进入到 Elasticsearch 时，它会经历如下的步骤：



        中间的那部分就叫做 Analyzer。我们可以看出来，它分为三个部分：Char Filters, Tokenizer 及 Token Filters。它们的作用分别如下：

        Char Filters: 字符过滤器的工作是执行清除任务，例如剥离 HTML 标记。
        Tokenizer: 下一步是将文本拆分为称为标记的术语。 这是由 tokenizer 完成的。 可以基于任何规则（例如空格）来完成拆分。 有关 tokennizer 的更多详细信息，请访问以下 URL：Tokenizer reference | Elasticsearch Guide [7.15] | Elastic。
        Token filters: 一旦创建了 token，它们就会被传递给 token filters，这些过滤器会对 token 进行规范化。 Token filters 可以更改 token，删除术语或向 token 添加术语。


        Elasticsearch 已经提供了比较丰富的 analyzer。我们可以自己创建自己的 token analyzer，甚至可以利用已经有的 char filters，tokenizer 及 token filters 来重新组合成一个新的 analyzer，并可以对文档中的每一个字段分别定义自己的 analyzer。如果大家对 analyzer 比较感兴趣的话，请参阅我们的网址 Built-in analyzer reference | Elasticsearch Guide [7.15] | Elastic。

        你也可以阅读我的另外一篇关于 analyzer 的文章 “Elasticsearch: analyzer”。

        在默认的情况下，standard analyzer 是 Elasticsearch 的默认分析器：

        没有 Char Filter
        使用 standard tokonizer
        把字符串变为小写，同时有选择地删除一些 stop words 等。默认的情况下 stop words 为 _none_，也即不过滤任何 stop words。


        下面我们简单地展示一下我们的 analyzer 是如何实现的。



        GET twitter/_analyze
        {
        "text": [
        "Happy Birthday"
        ],
        "analyzer": "standard"
        }
        在上面的接口中，我们使用标准的 analyzer 来对字符串 "Happy birthday" 来分析，那么如下就是我我们看到的结果。



        我们可以看到有两个 token:  happy 和 birthday。两个 token 都变成小写的了。同时我们也可以看到它们在文档中的位置信息。

        如果感兴趣的同学，我也可以尝试一下 english analyzer：

        GET twitter/_analyze
        {
        "text": [
        "Happy Birthday"
        ],
        "analyzer": "english"
        }
        显示的结果是：

        {
        "tokens" : [
        {
        "token" : "happi",
        "start_offset" : 0,
        "end_offset" : 5,
        "type" : "<ALPHANUM>",
        "position" : 0
        },
        {
        "token" : "birthdai",
        "start_offset" : 6,
        "end_offset" : 14,
        "type" : "<ALPHANUM>",
        "position" : 1
        }
        ]
        }
        我们可以看出来有完全不一样的结果。这里显示的词根。同样地，我们可以使用 whitespace analyzer：

        GET twitter/_analyze
        {
        "text": [
        "Happy Birthday"
        ],
        "analyzer": "whitespace"
        }
        显示的结果为：

        {
        "tokens" : [
        {
        "token" : "Happy",
        "start_offset" : 0,
        "end_offset" : 5,
        "type" : "word",
        "position" : 0
        },
        {
        "token" : "Birthday",
        "start_offset" : 6,
        "end_offset" : 14,
        "type" : "word",
        "position" : 1
        }
        ]
        }
        在这里，我们可以看出来没有经过大小写的处理。这对于精确的搜索是有用的，比如我们想区分字母的大小写。

        很多人很好奇，想知道中文字的切割时怎么样的。我们下面来做一个简单的实验。

        GET twitter/_analyze
        {
        "text": [
        "生日快乐"
        ],
        "analyzer": "standard"
        }
        那么下面就是我们想看到的结果：

        {
        "tokens" : [
        {
        "token" : "生",
        "start_offset" : 0,
        "end_offset" : 1,
        "type" : "<IDEOGRAPHIC>",
        "position" : 0
        },
        {
        "token" : "日",
        "start_offset" : 1,
        "end_offset" : 2,
        "type" : "<IDEOGRAPHIC>",
        "position" : 1
        },
        {
        "token" : "快",
        "start_offset" : 2,
        "end_offset" : 3,
        "type" : "<IDEOGRAPHIC>",
        "position" : 2
        },
        {
        "token" : "乐",
        "start_offset" : 3,
        "end_offset" : 4,
        "type" : "<IDEOGRAPHIC>",
        "position" : 3
        }
        ]
        }
        我们可以看到有四个 token，并且它们的 type 也有所变化。

        GET twitter/_analyze
        {
        "text": [
        "Happy.Birthday"
        ],
        "analyzer": "simple"
        }
        显示的结果是：

        {
        "tokens" : [
        {
        "token" : "happy",
        "start_offset" : 0,
        "end_offset" : 5,
        "type" : "word",
        "position" : 0
        },
        {
        "token" : "birthday",
        "start_offset" : 6,
        "end_offset" : 14,
        "type" : "word",
        "position" : 1
        }
        ]
        }
        我们可以看到在我们的字符串中的 "." 也被正确认识，并作为分隔符把 Happy.Birthday 切割为两个 token。

        GET twitter/_analyze
        {
        "text": ["Happy Birthday"],
        "tokenizer": "keyword"
        }
        当我们使用 keyword 分析器时，我们可以看到上面的整个字符串无论有多长，都被当做是一个 token。这个对我们的 term 相关的搜索及聚合是有很大的用途的。上面的分析结果显示：

        {
        "tokens" : [
        {
        "token" : "Happy Birthday",
        "start_offset" : 0,
        "end_offset" : 14,
        "type" : "word",
        "position" : 0
        }
        ]
        }
        我么也可以使用 filter 处理我们的 token，比如：

        GET twitter/_analyze
        {
        "text": ["Happy Birthday"],
        "tokenizer": "keyword",
        "filter": ["lowercase"]
        }
        经过上面的处理，我们的 token 变成为：

        {
        "tokens" : [
        {
        "token" : "happy birthday",
        "start_offset" : 0,
        "end_offset" : 14,
        "type" : "word",
        "position" : 0
        }
        ]
        }
        我们也可以使用单独使用 tokenizer 来分析我们的文字：

        standard tokenizer

        POST _analyze
        {
        "tokenizer": "standard",
        "text": "Those who dare to fail miserably can achieve greatly."
        }
        它将生成如下的 token:

        [Those, who, dare, to, fail, miserably, can, achieve, greatly]
        keyword tokenizer

        POST _analyze
        {
        "tokenizer": "keyword",
        "text": "Los Angeles"
        }
        上面返回的结果是：

        {
        "tokens" : [
        {
        "token" : "Los Angeles",
        "start_offset" : 0,
        "end_offset" : 11,
        "type" : "word",
        "position" : 0
        }
        ]
        }
        在这里值得注意的是当我们在写入文档和进行搜索时，我们都需要用到 analyzer。搜索的过程其实就是把经过分析的文字变成相应的 tokens 并进行匹配而得到的结果：



        为了方便大家对比，我也创建了一个很小的 Python 应用。它是针对 Elastic Stack 8.x 而写的：

        analyzers.py

        from elasticsearch import Elasticsearch

        # Connect to the elastic cluster
        # Password for the 'elastic' user generated by Elasticsearch
        es = Elasticsearch("https://elastic:ba1u**GF*n4_wcgy0ETO@localhost:9200",
        ca_certs="/Users/liuxg/elastic/elasticsearch-8.2.0/config/certs/http_ca.crt",
        verify_certs=True)

        print(es)
        es.info()

        analyzers = ["english", "standard", "simple", "whitespace", "stop", "keyword", "pattern", "fingerprint"]

        for analyzer in analyzers:
        res = es.indices.analyze( analyzer = analyzer, text = ["This is Xiaoguo Liu from Beijing China, and I am developer.100"])
        tokens = [ sample['token'] for c, sample in enumerate(res['tokens']) ]
        print("\n")
        print("Analyzer : {}".format(analyzer))
        print('tokens: {}'.format([sample['token'] for c, sample in enumerate(res['tokens'])]))
        print(len([sample['token'] for c, sample in enumerate(res['tokens'])]))
        你需要根据自己的 Elasticsearch 安装的路径对上面的连接部分进行修改。上面我们使用了 analyzers 列表来罗列了常见的一些 analyzers。运行上面的应用，我们可以看到：

        Analyzer : english
        tokens: ['xiaoguo', 'liu', 'from', 'beij', 'china', 'i', 'am', 'develop', '100']
        9


        Analyzer : standard
        tokens: ['this', 'is', 'xiaoguo', 'liu', 'from', 'beijing', 'china', 'and', 'i', 'am', 'developer', '100']
        12


        Analyzer : simple
        tokens: ['this', 'is', 'xiaoguo', 'liu', 'from', 'beijing', 'china', 'and', 'i', 'am', 'developer']
        11


        Analyzer : whitespace
        tokens: ['This', 'is', 'Xiaoguo', 'Liu', 'from', 'Beijing', 'China,', 'and', 'I', 'am', 'developer.100']
        11


        Analyzer : stop
        tokens: ['xiaoguo', 'liu', 'from', 'beijing', 'china', 'i', 'am', 'developer']
        8


        Analyzer : keyword
        tokens: ['This is Xiaoguo Liu from Beijing China, and I am developer.100']
        1


        Analyzer : pattern
        tokens: ['this', 'is', 'xiaoguo', 'liu', 'from', 'beijing', 'china', 'and', 'i', 'am', 'developer', '100']
        12
        如何使用 Analyzer
        我们可以在一个索引的 mapping 中来定义字段的 analyzer，比如：

        PUT my-index-000001
        {
        "mappings": {
        "properties": {
        "text": {
        "type": "text",
        "fields": {
        "english": {
        "type":     "text",
        "analyzer": "english"
        }
        }
        }
        }
        }
        }
        在上面，我们的索引的字段 text 在默认的情况下使用的是 standard analyzer，也就是说，上面的命令等同于：

        PUT my-index-000001
        {
        "mappings": {
        "properties": {
        "text": {
        "type": "text",
        "analyzer": "standard",
        "fields": {
        "english": {
        "type":     "text",
        "analyzer": "english"
        }
        }
        }
        }
        }
        }
        上面的 text 字段使用的是默认的 standard 分词器，而 text.english 使用的是 english 分词器。我们尝试写入如下的文档：

        PUT my-index-000001/_doc/1
        {
        "text": "quick brown fox"
        }

        PUT my-index-000001/_doc/2
        {
        "text": "quick brown foxes"
        }
        我们使用如下的命令来进行搜索：

        GET my-index-000001/_search
        {
        "query": {
        "multi_match": {
        "query": "quick brown foxes",
        "fields": [
        "text",
        "text.english"
        ],
        "type": "most_fields"
        }
        }
        }
        上面搜索的结果为：

        {
        "took": 650,
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
        "max_score": 1.6047549,
        "hits": [
        {
        "_index": "my-index-000001",
        "_id": "2",
        "_score": 1.6047549,
        "_source": {
        "text": "quick brown foxes"
        }
        },
        {
        "_index": "my-index-000001",
        "_id": "1",
        "_score": 0.9116078,
        "_source": {
        "text": "quick brown fox"
        }
        }
        ]
        }
        }
        在上面：

        text 字段使用的是 standard 分词器
        text.english 字段使用的是 english 分词器
        我们分别写入两个文档，一个含有 fox，而另外一个含有 foexe
        我们针对两个字段 text 及 text.english 进行查询并结合分数。它的使用的是 multi-match 的 most_fields 计分方法。
        大家如果对 anaylyzer 感兴趣的话，可以读更多的资料：Analyzers | Elasticsearch Guide [7.3] | Elastic。

        大家可以参阅我更及进一步的学习文档：Elasticsearch: analyzer。

        至此，我们基本上已经完成了对 Elasticsearch 最基本的了解。上面所有的 script 可以在如下的地址下载：

        https://github.com/liu-xiao-guo/es-scripts-7.3

        如果你想了解更多关于Elastic Stack相关的知识，请参阅我们的官方网站：Elastic Stack and Product Documentation | Elastic
        ————————————————
        版权声明：本文为CSDN博主「Elastic 中国社区官方博客」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
        原文链接：https://blog.csdn.net/UbuntuTouch/article/details/99621105*/