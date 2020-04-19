package com.robot_brain.nlu.communal.myInterface;

import java.util.Map;

public interface StandardModule {
    String _main_(Map<String,String> outsideApiName);//模块的主入口
    void _init_();//模块的初始化方法，用于主要用于加载数据到redis
}
