package com.transsnet.report


import java.sql.DriverManager

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}

//cd /data/deploy/aws_analysis_membersouce nohup spark-submit \
//--conf spark.storage.memoryFraction=0.5 \
//--conf spark.shuffle.memoryFraction=0.3 \
//--conf spark.akka.frameSize=1024 \
//--name SparkMemberSource \
//--master local[*] \
//--driver-cores 1 \
//--driver-memory 512m \
//--num-executors 1 \
//--executor-cores 1 \
//--executor-memory 1g \
//--class com.transsnet.palmpay.scala.spark.AppFlyerInstall \
//--jars $(echo /usr/lib/spark/jars/*.jar |sed 's/ /,/g') \
///data/deploy/aws_analysis_membersouce/aws_analysis-1.0.jar \
//"hdfs://172.30.18.191:8020/appflyer_logs/logs/*" 1>> /data/deploy/aws_analysis_membersouce/logs/normal.log 2>> /data/deploy/aws_analysis_membersouce/logs/error.log &
/**
  *把hdfs上的gz格式文件（里面是json格式）转化为表数据储存
  */
object Appflyer_report {
  def getLogRDD(ss: SparkSession, path: String) = {

    //读取日志文件
    val ds: Dataset[String] = ss.read.textFile(path)

    import ss.implicits._
    val cleanDS = ds.mapPartitions(
      str =>
        //过滤掉脏数据
        str.filter(
          tempStr =>
            tempStr.startsWith("{") && tempStr.endsWith("}") && tempStr.indexOf("customer_user_id") != -1 && tempStr.indexOf("event_time") != -1)
    )
    //这里运用了sparksession里面的一个高级api(专门用来解析json格式)生成dataframe（schema来自json里面的key）
    ss.read.json(cleanDS)
  }
  def main(args: Array[String]): Unit = {
    if (args.length == 0) {
      println("please input hdfs path! eg : hdfs://10.200.110.53:8020/appflyer/unzip/*")
      System.exit(-1);
    }
    val hdfsPath = args(0)


    val conf = new SparkConf().setAppName(getClass.getName)
      .setMaster("local[*]")
      .set("spark.io.compression.codec", "snappy") //规避lz4找不到的问题
      .set("spark.debug.maxToStringFields","255")
    val sc = new SparkContext(conf)
    val sparkSession = SparkSession.builder().config(sc.getConf).getOrCreate()
    //这里生成的dataframe 生成的schema就是来自ss.read.json里面json的key
    val logRDD = getLogRDD(sparkSession, hdfsPath)

    println(logRDD.show())

    logRDD.createOrReplaceTempView("logRDD")

    //这里定义的字段（或者别名）跟mysql里面schema需要一一对应，如果mysql里面没有表，那自动生成的表的
    //shema为mac,af_sub1,customer_user_id,bundle_id。。。，但是类型大部分是text类型,所以建议最好自己定义schema
    val sql0 = "select mac,af_sub1,customer_user_id,bundle_id,af_cost_value,app_version,city,fb_campaign_id,device_model,af_cost_model,af_c_id,selected_currency,app_name,install_time_selected_timezone,wifi,install_time,operator,fb_adgroup_id,currency,attributed_touch_type,af_adset_id,re_targeting_conversion_type,attributed_touch_time,click_time_selected_timezone,revenue_in_selected_currency,is_retargeting,country_code,event_type,appsflyer_device_id,http_referrer,af_sub5,fb_campaign_name,click_url,media_source,campaign,af_keywords,event_value,ip,event_time,click_time,af_sub4,imei,fb_adgroup_name,af_sub2,attribution_type,android_id,af_adset,fb_adset_id,af_ad,agency,fb_adset_name,cost_per_install,af_channel,af_cost_currency,device_brand,download_time,af_siteid,language,app_id,af_ad_type,carrier,event_name,advertising_id,os_version,platform,af_sub3,download_time_selected_timezone,af_ad_id,sdk_version,event_time_selected_timezone" +
      " from logRDD where event_type = 'install'"
    //所有时间类型是install的日志记录
    val installRDD = sparkSession.sql(sql0)

    //先清空表  再使用appending模式插入  保证表的schema不变
    Class.forName("com.mysql.jdbc.Driver")

    val url ="jdbc:mysql://hadoop001:3306/rep?characterEncoding=utf-8"
    val userName ="root"
    val password="123456"
    val con = DriverManager.getConnection(url,
      userName,password)
    val sm = con.prepareCall("truncate table "+ "t_app_log_install")
    sm.execute()
    sm.close()

    installRDD.write.format("jdbc")
      .option("url", url)
      .option("user", userName)
      .option("password", password)
      .option("dbtable", "t_app_log_install")
      .mode(SaveMode.Append)
      .save()

  }
}
