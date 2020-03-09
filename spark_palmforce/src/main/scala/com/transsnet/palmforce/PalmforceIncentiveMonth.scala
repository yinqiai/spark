package com.transsnet.palmforce

import java.util.Date

import com.mongodb.casbah.commons.MongoDBObject
import com.transsnet.bean.PalmforceIncentiveMonthBean
import com.transsnet.scala_utils.{JdbcUtils, MongoCurdUtils, Utils}
import com.transsnet.utils.DateUtils
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}


/**
 * @Author: yinqi
 * @Date: 2019/10/9 14:51
 * @Description:
 */
object PalmforceIncentiveMonth {

  def main(args: Array[String]): Unit = {

    var month = Utils.formateDateToDate(new Date(), "yyyy-MM")
    var firstday=DateUtils.getFirstDayOfMonth(new Date())
    val numargs = args.length
    if (numargs >= 1) {

      for (i <- 0 to args.length - 1) {
        println("Parameter " + i + "==" + args(i) + "\n")
      }
      month = args(0)
      firstday=month +"-01"
    }

    val conf = new SparkConf()
    conf.setMaster("local[*]")
    .set("spark.io.compression.codec", "snappy")
    conf.setAppName("ForceTotal")
    val defaultURI = "mongodb://ReadUsr:ReadUsr82992sk@172.29.164.77:27017/?authSource=admin"
    val sc = new SparkContext(conf)
    val ss = SparkSession.builder().config(sc.getConf).getOrCreate()

    //获取本月时间
    val dateStr = "'" + month + "'"
    val dataFrame= MongoCurdUtils.getMongoData(sc, defaultURI, "divide_profit_data", "ng_palmforce_incentive_order_detail")
    dataFrame.filter("").show()
    dataFrame.createOrReplaceTempView("ng_palmforce_incentive_order_detail")

    val sql = " select member_id member_id," +
      " contributor contributor," +
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
      " where date_format(create_time,'yyyy-MM')= " + dateStr +
      " and pay_status not in (0,1) " +
      " group by member_id,contributor"

    val resDFIterator = ss.sql(sql).collectAsList().iterator()
    val collection = JdbcUtils.getConnection("divide_profit_data","ng_palmforce_incentive_monthly_report")

    while (resDFIterator.hasNext) {
      val nextRow = resDFIterator.next()


      val memberId=nextRow.getAs[String]("member_id")
      val contributor = nextRow.getAs[String]("contributor")
      val contributorRelativeLevel = nextRow.getAs[Int]("contributor_relative_level")
      val trading_volume = nextRow.getAs[Long]("trading_volume")
      val referralBonus = nextRow.getAs[Long]("referral_bonus")
      val commission = nextRow.getAs[Long]("commission")
      val rewards =nextRow.getAs[Long]("rewards")
      val currency = nextRow.getAs[String]("currency")
      val appSource = nextRow.getAs[Int]("app_source")
      val remark= nextRow.getAs[String]("remark")
      //月数据也算结算跟未结算
      val settledRewards = nextRow.getAs[Long]("settled_rewards")
      val unsettledRewards = nextRow.getAs[Long]("unsettled_rewards")

      val query = MongoDBObject("member_id" -> memberId,
          "contributor" -> contributor,
          "report_month" -> firstday)

      val bean = new PalmforceIncentiveMonthBean(
        firstday,
        memberId,
        contributor,
        contributorRelativeLevel,
        trading_volume,
        referralBonus,
        commission,
        rewards,
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
      println("############################palmforce_month_profit_process-successful############################")
  }


}
