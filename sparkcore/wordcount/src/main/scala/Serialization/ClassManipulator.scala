package Serialization

import java.io.{ByteArrayOutputStream, FileInputStream, FileOutputStream}

/**
  * @author yinqi
  * @date 2021/4/29
  */
object ClassManipulator {
  def saveClassFile(obj: AnyRef): Unit = {
    val classLoader = obj.getClass.getClassLoader
    val className = obj.getClass.getName
    val classFile = className.replace('.', '/') + ".class"

    println(classFile)

    val stream = classLoader.getResourceAsStream(classFile)

    // just use the class simple name as the file name
    val outputFile = className.split('.').last + ".class"

    println(outputFile)
    //生成文件的目录和路径
    val fileStream = new FileOutputStream("testyinqi/SimpleTask.class")
    var data = stream.read()
    while (data != -1) {
      println(data)
      fileStream.write(data)
      data = stream.read()
    }
    fileStream.flush()
    fileStream.close()
  }

  def main(args: Array[String]): Unit = {

    //saveClassFile(new SimpleTask())
  }
}


class FileClassLoader() extends ClassLoader {
  override def findClass(fullClassName: String): Class[_] = {

    //val file = fullClassName.split('.').last + ".class"
    val filePath ="C:\\Users\\chenlimin\\Desktop\\big-data\\spark\\testyinqi\\SimpleTask.class"
    println(filePath)
    val in = new FileInputStream(filePath)
    val bos = new ByteArrayOutputStream
    val bytes = new Array[Byte](4096)
    var done = false
    while (!done) {
      val num = in.read(bytes)
      if (num >= 0) {
        bos.write(bytes, 0, num)
      } else {
        done = true
      }
    }
    val data = bos.toByteArray
    defineClass(fullClassName, data, 0, data.length)
  }
}