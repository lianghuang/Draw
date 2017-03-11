package com.sectong.service;

import java.util.Random;

/**
 * Created by huangliangliang on 3/10/17.
 */
public class NickNames {
    private final static String[] names=new String[]{
            "天行者",
            "snow",
            "winter",
            "is ",
            "瓦利",
            "小指头",
            "雪诺",
            "龙妈",
            "瑟曦",
            "小恶魔",
            "女神",
            "三傻",
            "逼哥",
            "蝙蝠侠",
            "超人",
            "猫女",
            "叮当",
            "黑寡妇",
            "钢铁侠",
            "队长",
            "镇长",
            "雷神",
            "洛基",
            "夏洛克",
            "东方不败",
            "小李子",
            "水果姐",
            "大表姐",
            "金刚狼",
            "瘦瘦的长颈鹿",
            "汽水泡泡",
            "小天真",
            "逗比",
            "黑珍珠",
            "杰克船长",
            "巴博萨",
            "精灵王子",
            "甘道夫",
            "斯莱克",
            "达芬奇",
            "宙斯",
            "拉斐尔",
            "罗密欧",
            "朱丽叶",
            "阿甘",
            "夜王",
            "小玫瑰",
            "魔山",
            "马王",
            "罗曼蒂克",
    };

    public static String getNickName(){
        Random random=new Random();
        return names[random.nextInt(names.length)];
    }
}
