package com.transsnet.chapter01

import scala.collection.mutable.ArrayBuffer

/**
  * 稀疏数组
  */
object SparseArr {
  def main(args: Array[String]): Unit = {
    val rowSize=11
    val colSize=11
    //定义一个11*11数组
    val chessMap = Array.ofDim[Int](rowSize, colSize)
    chessMap(1)(2) = 1 //1表示黑子
    chessMap(2)(3) = 2 //2表示白子

    //输出原始地图
    for (arr <- chessMap) {
      for (arr1 <- arr) {
        printf("%d\t", arr1)
      }
      println
    }

    //将chessmap转成稀疏数组
    //思路=》达到对数组的压缩
    //class Node(row,col,value) 行列值
    //ArrayBuffer保存稀疏数组  因为可以追加
    val sparseArr = ArrayBuffer[Node]()
    //存稀疏数组的行列，value暂时为0 作为后面恢复用
    sparseArr.append(new Node(rowSize,colSize,0))
    //把chessMap转化为稀疏数组 i j 表示数组下标
    for (i <- 0 until chessMap.length) {
      for (j <- i until chessMap(i).length) {
        //把不为0的数字 下标和对应的值存入node
        if (chessMap(i)(j) != 0) {
          val node = new Node(i, j, chessMap(i)(j))
          //把node 放入稀疏数组
          sparseArr.append(node)
        }
      }
    }
    println("==================打印稀疏数组====================")
    for (item <- sparseArr) {
      printf("%d\t%d\t%d\n", item.row, item.col, item.value)
    }
    //存盘
    //读盘
    //恢复稀疏数组->原始数组
    //拿到稀疏数组的第一行中的node,里面存了原始数组的行列信息
    val node  = sparseArr(0)
    val newRow =node.row
    val newCol = node.col

    val repairMapChess = Array.ofDim[Int](newRow,newCol)
    //从第二行开始遍历稀疏数组拿到需要恢复的数据
    for(item <- 1 until sparseArr.length){
         //把得到的数据恢复回数组
         repairMapChess(sparseArr(item).row)(sparseArr(item).col) =sparseArr(item).value
    }

    println("==================打印恢复后的Map====================")
    for (arr <- repairMapChess) {
      for (arr1 <- arr) {
        printf("%d\t", arr1)
      }
      println
    }

  }

}

class Node(val row: Int, val col: Int, val value: Int)
