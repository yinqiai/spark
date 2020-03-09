package com.transsnet.palmforce

import java.util.Date

import com.mongodb.casbah.commons.MongoDBObject
import com.transsnet.bean.PalmforceIncentiveHistoryBean
import com.transsnet.scala_utils.{JdbcUtils, MongoCurdUtils}
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author yinqi
  * @date 2019/10/9
  */
object PalmforceIncentiveHistory {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local[*]")
        .set("spark.io.compression.codec", "snappy")
    conf.setAppName("PalmforceIncentiveHistory")
    val defaultURI = "mongodb://root:123456@hadoop001:27017/?authSource=admin"

    val sc = new SparkContext(conf)
    val ss = SparkSession.builder().config(sc.getConf).getOrCreate()

    MongoCurdUtils.getMongoData(sc, defaultURI, "divide_profit_data", "ng_palmforce_incentive_order_detail").createOrReplaceTempView("ng_palmforce_incentive_order_detail")

    val sql = " select member_id," +
      " contributor," +
      " collect_set(contributor_relative_level)[0] contributor_relative_level," +
      " sum(case when rewards_type =2 then business_amount else 0 end) trading_volume," +
      " sum(case when rewards_type =1 then incentive_amount else 0 end) referral_bonus," +
      " sum(case when rewards_type =2 then incentive_amount else 0 end) commission," +
      " sum(incentive_amount) rewards," +
      " sum(case when settle_status =0 then incentive_amount else 0 end) unsettled_rewards," +
      " sum(case when settle_status =1 then incentive_amount else 0 end) settled_rewards," +
      " collect_set(currency)[0] currency," +
      " collect_set(app_source)[0] app_source," +
      " '' remark" +
      " from ng_palmforce_incentive_order_detail" +
      " where pay_status not in (0,1) " +
      " group by member_id,contributor"

    //方式1：先删除再插入，效率更好，但是有某段时间数据为空，页面上是实时查
    /* import ss.implicits._
    ss.sql(sql).printSchema
    ss.sql(sql).show()
    val df=ss.sql(sql).as[PalmforceIncentiveHistoryBean].toDF()
    val resDFIterator = ss.sql(sql).collectAsList().iterator()

    MongoCurdUtils.dropCollectionAndInsert(ds,WriteConfig(Map("uri" -> "mongodb://ReadUsr:ReadUsr1232@10.200.110.15:27017/?authSource=admin", "database" -> "divide_profit_data", "collection" -> "ng_palmforce_incentive_history_report")))*/

    val resDFIterator = ss.sql(sql).collectAsList().iterator()

    val collection = JdbcUtils.getConnection("divide_profit_data", "ng_palmforce_incentive_history_report")
    //方式2：update 效率更差
    while (resDFIterator.hasNext) {
      val nextRow = resDFIterator.next()


      val memberId = nextRow.getAs[String]("member_id")
      val contributor = nextRow.getAs[String]("contributor")
      val contributorRelativeLevel = nextRow.getAs[Int]("contributor_relative_level")
      val trading_volume = nextRow.getAs[Long]("trading_volume")
      val referralBonus = nextRow.getAs[Long]("referral_bonus")
      val commission = nextRow.getAs[Long]("commission")
      val rewards = nextRow.getAs[Long]("rewards")
      val currency = nextRow.getAs[String]("currency")
      val appSource = nextRow.getAs[Int]("app_source")
      val remark = nextRow.getAs[String]("remark")
      val settledRewards = nextRow.getAs[Long]("settled_rewards")
      val unsettledRewards = nextRow.getAs[Long]("unsettled_rewards")
      val query = MongoDBObject("member_id" -> memberId,
        "contributor" -> contributor)
      val bean = new PalmforceIncentiveHistoryBean(memberId,
        contributor,
        contributorRelativeLevel,
        trading_volume,
        referralBonus,
        commission ,
        rewards ,
        settledRewards,
        unsettledRewards,
        currency,
        appSource,
        remark,
        new Date(),
        new Date()
      )


      //不存在就插入
      if(!MongoCurdUtils.find(query,collection).hasNext){

        MongoCurdUtils.insert(bean, collection)
        //存在就更新
      }else{
        bean.create_time=null
        MongoCurdUtils.update(bean, collection, query,true)
      }
    }
    println("############################palmforce_history_profit_process-successful############################")
  }
}
