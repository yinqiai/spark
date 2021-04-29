package Serialization

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

/**
  * @author yinqi
  * @date 2021/4/29
  */
object FileSerializer {
  def main(args: Array[String]): Unit = {
    val task = new SimpleTask()
    FileSerializer.writeObjectToFile(task, "task.ser")

    ClassManipulator.saveClassFile(task)

/*    val task1 = FileSerializer.readObjectFromFile("task.ser").asInstanceOf[Task]
    task1.run()*/
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
}