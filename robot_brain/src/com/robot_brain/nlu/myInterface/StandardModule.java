package com.robot_brain.nlu.myInterface;

public interface StandardModule {
    String _main_(String conversationID);//模块的主入口
    void _init_();//模块的初始化方法，用于主要用于加载数据到redis
}
