import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._


object StreamingKafka extends App{

  //sparkConf
  val sparkConf = new SparkConf().setAppName("kafka").setMaster("local[*]")

  //创建StreamingContext
  val ssc = new StreamingContext(sparkConf, Seconds(2))

  // 连接Kafka
  val topic = "streaming_topic_2"
  val kafkaParams = Map(
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "hadoop001:9092",
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
    ConsumerConfig.GROUP_ID_CONFIG -> "kafka",
    //smallest、latest, 比如已经有CG在消费TOPIC，如果有新的CG加入，那么 smallest表示从最小的offset开始消费，latest是从当前TOPIC最大消费的Offset来消费
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "latest",
    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> (true: java.lang.Boolean)
  )

  // ConsumerStrategies.Subscribe有一个重载方法，能够让你传入offsets: collection.Map[TopicPartition, Long] 来设置偏移量，该偏移量应该是从ZK中获取
  //1.这个是streaming高级api处理kafka数据，调用的是kafka低级api（机制：这种方式是spark处理完数据后自己提交偏移量到kafka(kafkaoffset也是维护在zookeeper里面)）这种方式也是保证原子操作的，公司里面用的多(这个生成rdd的分区和kafka里面topic分区数一致)
  //2.还有一种方式是用低级api ，调用的是 kafka高级api（机制：spark收到数据后，kafka高级api就更新了offset,但是这个时候数据在spark里面还没有处理，如果有WAL,我是在写完日志后，才让kafka高级api更改offset,所以如果没有WAL,当spark数据还没处理完发生宕机就会丢失数据）
  // 框架自己管理offset,这里reciver就是一个消费者组，里面线程池里面的线程数和topic分区数一样多，但是生成rdd分区数跟topic分区数没有关系），如下是代码示例
  // val Array(zk,topic,groupId,threadNum) =args
  //    val sc = new SparkConf()//.setAppName("KafkaReceiverWordCount").setMaster("local[2]")
  //    val ssc = new StreamingContext(sc,Seconds(5))
  //
  //    val topicMap = topic.split(",").map((_,threadNum.toInt)).toMap
  //
  //    val kafkaSteaming =KafkaUtils.createStream(ssc,zk,groupId,topicMap)
  val lines = KafkaUtils.createDirectStream[String, String](ssc,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, String](Iterable(topic), kafkaParams))


  lines.map(line => "******:" + line.value()).foreachRDD(rdd => {

    // spark core操作
    rdd.foreachPartition(items => {

      //获取kafka连接池
      val pool = KafkaProxyPool("hadoop001:9092","log3")
      //获取了kafkaProxy对象
      val kafkaProxy = pool.borrowObject()

      //写入消息
      for (item <- items){
        println(item)
        kafkaProxy.send("kafka",item)
      }

      //归还对象
      pool.returnObject(kafkaProxy)
    })

    // 拿到Offset 这里面每个rdd的partition和kafka里面partition是一一对应的
    val offsetRanges =rdd.asInstanceOf[HasOffsetRanges].offsetRanges
    //同步offset到Kafka
    rdd.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
  })

  // 启动程序
  ssc.start()
  ssc.awaitTermination()


}
