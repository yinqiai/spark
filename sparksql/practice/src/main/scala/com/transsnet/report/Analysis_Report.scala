package com.transsnet.report

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

case class tbStock(ordernumber:String,locationid:String,dateid:String) extends Serializable

case class tbStockDetail(ordernumber:String, rownum:Int, itemid:String, number:Int, price:Double, amount:Double) extends Serializable
case class tbDate(dateid:String, years:Int, theyear:Int, month:Int, day:Int, weekday:Int, week:Int, quarter:Int, period:Int, halfmonth:Int) extends Serializable


object Analysis_Report {

  private def insertHive(ss:SparkSession,tableName:String,df:DataFrame)={
       ss.sql("drop table if exists "+tableName)
       df.write.saveAsTable(tableName)
  }

  private def insertMySQL(tableName:String,df:DataFrame)={

    df.write
      .format("jdbc")
      .option("url", "jdbc:mysql://hadoop001:3306/rep")
      .option("dbtable", tableName)
      .option("user", "root")
      .option("password", "123456")
      .save()
  }

  def main(args: Array[String]): Unit = {
      val sparkConf = new SparkConf().setMaster("local[*]")
        .setAppName("Analysis_Report")
      val sparkSession = SparkSession.builder().
        enableHiveSupport() //操作hive
    .config(sparkConf).getOrCreate()
      //1.将数据加载到hive
    import sparkSession.implicits._
    val tbStockRDD =sparkSession.sparkContext.textFile("C:\\Users\\chenlimin\\Desktop\\big-data\\spark\\sparksql\\practice\\src\\main\\resources\\tbStock.txt")
    val tbStockDs = tbStockRDD.map(_.split(",")).map(arr=>tbStock(arr(0),arr(1),arr(2))).toDS()
    //insertHive(sparkSession,"tbStock",tbStockDs.toDF())
    println(tbStockDs.toDF().show())

    val tbStockDetailRDD = sparkSession.sparkContext.textFile("C:\\Users\\chenlimin\\Desktop\\big-data\\spark\\sparksql\\practice\\src\\main\\resources\\tbStockDetail.txt")
    val tbStockDetailDS = tbStockDetailRDD.map(_.split(",")).map(attr=> tbStockDetail(attr(0),attr(1).trim().toInt,attr(2),attr(3).trim().toInt,attr(4).trim().toDouble, attr(5).trim().toDouble)).toDS
    //insertHive(sparkSession,"tbStockDetail",tbStockDetailDS.toDF)

    val tbDateRDD = sparkSession.sparkContext.textFile("C:\\Users\\chenlimin\\Desktop\\big-data\\spark\\sparksql\\practice\\src\\main\\resources\\tbDate.txt")
    val tbDateDS = tbDateRDD.map(_.split(",")).map(attr=> tbDate(attr(0),attr(1).trim().toInt, attr(2).trim().toInt,attr(3).trim().toInt, attr(4).trim().toInt, attr(5).trim().toInt, attr(6).trim().toInt, attr(7).trim().toInt, attr(8).trim().toInt, attr(9).trim().toInt)).toDS
    //insertHive(sparkSession,"tbDate",tbDateDS.toDF)

    //需求一：注意当我们没有取别名的时候 对应自动生成表里面的schma就是c.theyear COUNT(DISTINCT a.ordernumber)  SUM(b.amount)
    val result1 = sparkSession.sql("SELECT c.theyear, COUNT(DISTINCT a.ordernumber), SUM(b.amount) FROM tbStock a JOIN tbStockDetail b ON a.ordernumber = b.ordernumber JOIN tbDate c ON a.dateid = c.dateid GROUP BY c.theyear ORDER BY c.theyear")
    //insertMySQL("xq1",result1)

    //需求二：
    val result2 = sparkSession.sql("SELECT theyear, MAX(c.SumOfAmount) AS SumOfAmount FROM (SELECT a.dateid, a.ordernumber, SUM(b.amount) AS SumOfAmount FROM tbStock a JOIN tbStockDetail b ON a.ordernumber = b.ordernumber GROUP BY a.dateid, a.ordernumber ) c JOIN tbDate d ON c.dateid = d.dateid GROUP BY theyear ORDER BY theyear DESC")
    //insertMySQL("xq2",result2)

    //需求三：
    val result3 = sparkSession.sql("SELECT DISTINCT e.theyear, e.itemid, f.maxofamount FROM (SELECT c.theyear, b.itemid, SUM(b.amount) AS sumofamount FROM tbStock a JOIN tbStockDetail b ON a.ordernumber = b.ordernumber JOIN tbDate c ON a.dateid = c.dateid GROUP BY c.theyear, b.itemid ) e JOIN (SELECT d.theyear, MAX(d.sumofamount) AS maxofamount FROM (SELECT c.theyear, b.itemid, SUM(b.amount) AS sumofamount FROM tbStock a JOIN tbStockDetail b ON a.ordernumber = b.ordernumber JOIN tbDate c ON a.dateid = c.dateid GROUP BY c.theyear, b.itemid ) d GROUP BY d.theyear ) f ON e.theyear = f.theyear AND e.sumofamount = f.maxofamount ORDER BY e.theyear")
    //insertMySQL("xq3",result3)

    sparkSession.close();
  }
}
