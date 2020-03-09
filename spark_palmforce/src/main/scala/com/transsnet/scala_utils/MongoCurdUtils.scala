package com.transsnet.scala_utils

import com.mongodb.{BasicDBObject, DBCollection}
import com.mongodb.casbah.Imports
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.{ReadConfig, WriteConfig}
import com.transsnet.bean.MemberDetail
import com.transsnet.utils.MongoUtils
import org.apache.spark.SparkContext
import org.apache.spark.sql.DataFrame

/**
  * @author yinqi
  *
  */
object MongoCurdUtils {
   /*
   *
   * 查询
   * */
   def find(query:Imports.DBObject,coll:DBCollection) ={
    coll.find(query)
    }

  /*
  *
  * 插入
  * */
    def insert[T](bean:T,coll:DBCollection) ={
      coll.insert(MongoUtils.bean2DBObject(bean))
    }


  /**
    * 更改数据MemberDetail
    * @param member
    * @param coll
    * @return
    */
  def update(member:MemberDetail,coll:DBCollection) ={

    val query = MongoDBObject("member_id"->member.member_id,"month"->member.month)
    val value = new BasicDBObject("$set",MongoUtils.beanToDBObject(member))
    coll.update(query,value,true, true)
  }

  /**
   *
   * @param bean
   * @param collection
   * @param query
   * @param allowInsert  不存在是否允许插入
   * @tparam T
   * @return
   */
  def update[T](bean:T,collection: DBCollection,query:Imports.DBObject,allowInsert:Boolean) ={
    val value = new BasicDBObject("$set",  MongoUtils.bean2DBObject(bean))
    collection.update(query,value,allowInsert,true)
  }

  /**
   *
   * @param bean
   * @param collection
   * @param query
   * @tparam T
   * @return
   */
  def update[T](bean:T,collection: DBCollection,query:Imports.DBObject) ={
    val value = new BasicDBObject("$set",  MongoUtils.bean2DBObject(bean))
    collection.update(query,value,true,true)
  }

  /**
   * 更新查询到的所有记录
   * @param collection
   * @param query

   * @return
   */
  def update(collection: DBCollection,query:Imports.DBObject,value:BasicDBObject) ={
    collection.update(query,value,false,true)
  }

  /**
   * 获取mongo中的表数据
   *
   * @param sc
   * @param uri
   * @param database
   * @param collection
   * @return
   */
  def getMongoData(sc: SparkContext, uri: String, database: String, collection: String) = {
    MongoSpark.load(sc, ReadConfig(Map("uri" -> uri, "database" -> database, "collection" -> collection))).toDF()
  }

  /**
   * 实现覆盖写入mongo数据 即先删除collection 再写入
   * @param dateFrame
   * @param writeConfig
   */
  def dropCollectionAndInsert(dateFrame:DataFrame, writeConfig:WriteConfig) ={

    JdbcUtils.getConnection(writeConfig.databaseName,writeConfig.collectionName).drop()
    MongoSpark.save(dateFrame,writeConfig)

  }
}
