import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Dataset, SparkSession}

/**
  * @author yinqi
  * @date 2020/4/2
  */
object S3Repair {
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

    ss.read.json(cleanDS)
  }
  def main(args: Array[String]): Unit = {
    //if (args.length == 0) {
    //println("please input hdfs path! eg : hdfs://10.200.110.53:8020/appflyer/unzip/*")
    //System.exit(-1);
    //}
    // val hdfsPath = args(0)
    val hdfsPath = "s3a://aws-logs-502076313352-ap-east-1/appflyer_in/*"

    val conf = new SparkConf().setAppName(getClass.getName)
      .setMaster("local[*]")
     // .set("spark.io.compression.codec", "snappy") //规避lz4找不到的问题
      //.set("spark.debug.maxToStringFields","255")

    val sc = new SparkContext(conf)
    sc.hadoopConfiguration.set("fs.s3a.access.key","AKIAXJZQUR4EH3EHCTMT")
    sc.hadoopConfiguration.set("fs.s3a.secret.key","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")
    sc.hadoopConfiguration.set("fs.s3a.endpoint","s3.ap-east-1.amazonaws.com")

    val sparkSession = SparkSession.builder().config(sc.getConf).getOrCreate()

    val logRDD = getLogRDD(sparkSession, hdfsPath)

    logRDD.createOrReplaceTempView("logRDD")

    val sql0 = "select mac,af_sub1,customer_user_id,bundle_id,af_cost_value,app_version,city,fb_campaign_id,device_model,af_cost_model,af_c_id,selected_currency,app_name,install_time_selected_timezone,wifi,install_time,operator,fb_adgroup_id,currency,attributed_touch_type,af_adset_id,re_targeting_conversion_type,attributed_touch_time,click_time_selected_timezone,revenue_in_selected_currency,is_retargeting,country_code,event_type,appsflyer_device_id,http_referrer,af_sub5,fb_campaign_name,click_url,media_source,campaign,af_keywords,event_value,ip,event_time,click_time,af_sub4,imei,fb_adgroup_name,af_sub2,attribution_type,android_id,af_adset,fb_adset_id,af_ad,agency,fb_adset_name,cost_per_install,af_channel,af_cost_currency,device_brand,download_time,af_siteid,language,app_id,af_ad_type,carrier,event_name,advertising_id,os_version,platform,af_sub3,download_time_selected_timezone,af_ad_id,sdk_version,event_time_selected_timezone" +
      " from logRDD where event_type = 'install'"
    //所有时间类型是install的日志记录
    val installRDD = sparkSession.sql(sql0)


    installRDD.write.save("s3a://aws-logs-502076313352-ap-east-1/appflyer_out/")

  }
}
