package com.transsnet.scala_utils

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}


/**
 * @Author: JK
 * @Date: 2019/10/8 15:44
 * @Description:
 */
object MongoCurdUtilsTest {

  def main(args: Array[String]): Unit = {

 /*   val collection =JdbcUtils.getConnection("mycol1")
   // println(collection.find().count())
  //  println(DateUtils.strToDate(Utils.formateDateToDate(new Date(), "yyyy-MM")))


    val member = palmforce.MemberDetail("7777",4,1,1,1,1,1,1,new Date(),new Date(),Utils.formateDateToDate(new Date(), "yyyy-MM"))
    var query = MongoDBObject("member_id"->member.member_id,"month"->"Oct 1, 2019 12:00:00 AM")
    var value = new BasicDBObject("$set",MongoUtils.beanToDBObject(member))

    collection.update(query,value,true,true)*/

    val conf = new SparkConf()
    conf.setMaster("local[*]")
      .setAppName("MakeTag2PalmforceRole")
      .set("spark.io.compression.codec", "snappy")
    val defaultURI = ""
    val sc = new SparkContext(conf)
    val ss = SparkSession.builder().config(sc.getConf).getOrCreate()
    MongoCurdUtils.getMongoData(sc, defaultURI, "trade_data", "ng_t_pay_flow").createOrReplaceTempView("ng_t_pay_flow")
    //ss.sql("select create_time from ng_t_pay_flow  where payer_member_id = '4E74F73D2CEA4E36964EF91C97717406' and trans_type in ('04','15','16','17','29','34') and pay_status = 1 order by create_time ").show()

    ss.sql("select * from ng_t_pay_flow where payer_member_id = 'CFD30E6AF3B147E5B27C9F2B1007D139'").show()




  }
}
