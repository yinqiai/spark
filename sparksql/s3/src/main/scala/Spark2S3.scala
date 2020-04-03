import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author yinqi
  * @date 2020/4/2
  */
object Spark2S3 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("Spark2S3")
      .setMaster("local[*]")
    /*      .set("fs.s3a.access.key", "AKIAXJZQUR4EH3EHCTMT")
          .set("fs.s3a.secret.key", "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")
          //.set("fs.s3a.endpoint", "aws-logs-502076313352-ap-east-1")
          .set("fs.s3a.endpoint", "aws-logs-502076313352-ap-east-1")*/


    // .set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")

    val sc = new SparkContext(conf)
    sc.hadoopConfiguration.set("fs.s3a.access.key","AKIAXJZQUR4EH3EHCTMT")
    sc.hadoopConfiguration.set("fs.s3a.secret.key","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")
    sc.hadoopConfiguration.set("fs.s3a.endpoint","s3.ap-east-1.amazonaws.com")



    val rdd: RDD[String] = sc.textFile("s3a://aws-logs-502076313352-ap-east-1/appflyer/data.txt")
    rdd.foreach(println)

  }

}
