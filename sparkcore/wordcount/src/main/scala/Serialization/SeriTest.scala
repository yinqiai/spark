package Serialization

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partition, SparkConf, SparkContext, TaskContext}

/**
  * @author yinqi
  * @date 2021/4/28
  */
object SeriTest {

  def main(args: Array[String]): Unit = {

    //1.初始化配置信息及SparkContext
    val sparkConf: SparkConf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    //2.创建一个RDD
    val rdd: RDD[String] = sc.parallelize(Array("hadoop", "spark", "hive", "atguigu"))

    //3.创建一个Search对象
    val search = new Search("yinqitest")

    //4.运用第一个过滤函数并打印结果
    /*val match1: RDD[String] = search.getMatch1(rdd)
    match1.collect().foreach(println)*/

    search.getMatche2(rdd)
    search.isMatch3()
  }
}
//解决系列化问题方式一extends Serializable
class Search(s:String) {
   val query="yinqi"
  //过滤出包含字符串的数据
  def isMatch(s: String): Boolean = {
    s.contains(query)
  }

  def isMatch3(): Unit = {

  }

  //过滤出包含字符串的RDD
  def getMatch1 (rdd: RDD[String]): RDD[String] = {

    rdd.filter(isMatch)
  }


  //
  def getMatch4 (rdd: RDD[String]): Unit = {

   // rdd.filter(a=>false)
    rdd.filter(isMatch)
  }
  //过滤出包含字符串的RDD
  def getMatche2(rdd: RDD[String]): RDD[String] = {
    //解决系列化问题方式二 将类变量赋值给局部变量
    val query_ : String = this.query

    //rdd.filter(x => x.contains(query))
    rdd.filter(x => x.contains(query_))
  }

}