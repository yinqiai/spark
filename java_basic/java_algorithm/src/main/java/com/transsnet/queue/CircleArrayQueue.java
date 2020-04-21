package com.transsnet.queue;

/**
 * 环形队列（数组实现）
 */
public class CircleArrayQueue {




}

class CircleArray{
    private int maxSize;//队列最大长度
    private int front;//队列的头 指向队列的第一个元素 初始值为0 队列空时候 rear=front
    //队列剩余元素个数 (rear+front -1)%maxSize 规则为进入一个元素 rear+1 取出一个元素front+1
    private int rear;//队列的尾 指向队列最后元素的下一个位置 初始值为0 队列满时候(预留一个空间) （rear+1）%maxSize=0
    private int[] arr;//用于存放队列元素（数组实现）

    //初始化队列大小
    public CircleArray(int maxSize){
        this.maxSize=maxSize;
        arr = new int[maxSize];
    }


}
