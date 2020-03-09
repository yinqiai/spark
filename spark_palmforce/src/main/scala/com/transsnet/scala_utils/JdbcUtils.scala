package com.transsnet.scala_utils

import com.mongodb.ServerAddress
import com.mongodb.casbah.{MongoClient, MongoCredential, MongoDB}

/**
  * @author yinqi
  *
  */
object JdbcUtils {
  val jdbcMongoHost = "hadoop001"
  val jdbcMongoPort = 27017
  val dbAuthName = "admin"
  val userName = "root"
  val password = "123456"

  def getConnection(dbName:String,collectionName:String) ={
    createDatabase(jdbcMongoHost,jdbcMongoPort,dbAuthName,dbName,userName,password).getCollection(collectionName)
  }
  //验证连接权限
  def createDatabase(url: String, port: Int, dbAuthName: String,dbName: String, loginName: String, password: String): MongoDB = {
    var server = new ServerAddress(url, port)
    //注意：MongoCredential中有6种创建连接方式，这里使用MONGODB_CR机制进行连接。如果选择错误则会发生权限验证失败
    var credentials = MongoCredential.createCredential(loginName, dbAuthName, password.toCharArray)
    var mongoClient = MongoClient(server, List(credentials))
    mongoClient.getDB(dbName)
  }

  //  无权限验证连接
  def createDatabaseNoAuth(url: String, port: Int, dbName: String): MongoDB = {
    MongoClient(url, port).getDB(dbName)
  }
}
