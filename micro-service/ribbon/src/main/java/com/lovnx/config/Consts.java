package com.lovnx.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务器集合
 * Created by Mr.zhang on 2019/8/2
 */
public class Consts {
        public static List<String> serviceList = new CopyOnWriteArrayList<>(); //配置服务List
        public static AtomicInteger ruleType = new AtomicInteger(1); //设置选择策略
        public static Map<String, CopyOnWriteArrayList<String>> tagList = new ConcurrentHashMap<>();
        public static volatile String tag = "prod";

}
