package Serialization

import java.io._

/**
  * @author yinqi
  * @date 2021/4/29
  */
object FileSerializer {
  def main(args: Array[String]): Unit = {
   /* val task = new SimpleTask()
    FileSerializer.writeObjectToFile(task, "task.ser")

    */

    //ClassManipulator.saveClassFile(task)
   /* val task = new SimpleTask()
    ClassManipulator.saveClassFile(task)*/

    /*特别重要，方式1和方式二的实验结果可以验证 ：
    通过ObjectOutputStream序列化对象，仅包含类的描述（而非定义），对象的状态数据，由于缺少类的定义，
    也就是缺少SimpleTask的字节码，反序列化过程中就会出现ClassNotFound的异常。*/
    /*方式1（从task.ser）反序列化 ，
    反序列化方法底层 会调用ObjectInputStream.resolveClass默认回去target/classes目录下取得

    */
    val task1 = FileSerializer.readObjectFromFile("task.ser").asInstanceOf[Task]
    task1.run()

    /*方式2（从task.ser）反序列化 ，这里面没有重新classLoad
    *
    *
     */
   /* val task = new SimpleTask()
     ClassManipulator.saveClassFile(task)*/
    val classsLoad = new FileClassLoader()
    val task2 = FileSerializer.readObjectFromFile("task.ser",classsLoad).asInstanceOf[Task]
    task2.run()
  }

  def writeObjectToFile(obj: Object, file: String) = {
    val fileStream = new FileOutputStream(file)
    val oos = new ObjectOutputStream(fileStream)
    oos.writeObject(obj)
    oos.close()
  }

  def readObjectFromFile(file: String): Object = {
    val fileStream = new FileInputStream(file)
    val ois = new ObjectInputStream(fileStream)
    val obj = ois.readObject()
    ois.close()
    obj
  }

  def readObjectFromFile(file: String,classLoader: ClassLoader): Object = {
    val fileStream = new FileInputStream(file)
    val ois = new ObjectInputStream(fileStream){
      override def resolveClass(desc:ObjectStreamClass):Class[_]=Class.forName(desc.getName,false,classLoader)
    }
    val obj = ois.readObject()
    ois.close()
    obj
  }
}