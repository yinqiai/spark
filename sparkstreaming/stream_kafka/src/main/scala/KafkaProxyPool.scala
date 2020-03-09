import java.util.Properties

import org.apache.commons.pool2.impl.{DefaultPooledObject, GenericObjectPool}
import org.apache.commons.pool2.{BasePooledObjectFactory, PooledObject}
import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

class KafkaProxy(brokerList:String,topic:String){
         val kafkaProducer ={
           val properties = new Properties()
           //要连接上的broker
           properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,brokerList)
           //这里面注意在java里面是StringSerializer.class.getName()
           properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
           properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
           //这个属性可要可不要
           properties.put(ProducerConfig.CLIENT_ID_CONFIG, "producer.client.id.demo")
           //创建生产者对象
           // 这里一定要加上 key value的泛型的类型[String,String] scala这点上不知道为啥推断不出来
           new KafkaProducer[String,String](properties)
         }

        //封装发送消息的方法通过生产者对象发送消息
        def send(key:String,value:String): Unit ={
          //创建kProducerRecord 消息的载体(包括具体消息(key value)，消息属于哪个topic)
          val kafkarecord= new ProducerRecord[String,String](topic,key,value)
          kafkaProducer.send(kafkarecord)

        }

}

/**
  * 定义一个工厂类来生产kafkaProducer
  */
//class KafkaProxyFactory extends Base
// 这个类是创建池化对象的工厂类
class KafkaProxyFactory(brokenList:String,topic:String) extends BasePooledObjectFactory[KafkaProxy]{

  // 创建实体
  override def create(): KafkaProxy = new KafkaProxy(brokenList,topic)

  // 包装实体
  override def wrap(t: KafkaProxy): PooledObject[KafkaProxy] = new DefaultPooledObject[KafkaProxy](t)

}

// 执行具体的池创建
/**
  * 如果需要获取KafkaProxy，通过pool.borrowObject()来获取
  *   // 中间这一块写你使用kafkaProxy对象的业务逻辑。
  * 如果使用完毕，那么需要将kafkaProxy对象归还，通过pool.returnObject(kafkaProxy) 来实现
  */
object KafkaProxyPool {

  // 具体的池对象
  private var pool:GenericObjectPool[KafkaProxy] = null

  // 获取池对象
  def apply(brokenList:String,topic:String): GenericObjectPool[KafkaProxy]={
    KafkaProxyPool.synchronized{
      if(null == pool){
        val poolFactory = new KafkaProxyFactory(brokenList,topic)
        pool = new GenericObjectPool[KafkaProxy](poolFactory)
      }
    }
    pool
  }
}

