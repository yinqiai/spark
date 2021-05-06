package partitions

import org.apache.spark.{SparkConf, SparkContext}

object CusPartitios {
  def main(args: Array[String]): Unit = {
    val sc =new SparkContext(new SparkConf().setAppName("CusPartitios").setMaster("local[*]"))
    //val blankLines = sc.accumulator(0)
    val data = sc.parallelize(Array((1,1),(2,2),(3,3),(4,4),(5,5),(6,6)))

    val par = data.partitionBy(new CustomerPartitioner(2))

    par.mapPartitionsWithIndex((index,items)=>items.map((index,_))).collect

    sc.stop()

  }

}

class CustomerPartitioner(numParts:Int) extends org.apache.spark.Partitioner{

  //覆盖分区数
  override def numPartitions: Int = numParts

  //覆盖分区号获取函数
  override def getPartition(key: Any): Int = {
    val ckey: String = key.toString
    println(ckey)
    ckey.substring(ckey.length-1).toInt%numParts
  }
}