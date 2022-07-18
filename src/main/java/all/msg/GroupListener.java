package all.msg;

import all.api.*;
import all.plug.*;
import cn.hutool.core.date.DatePattern;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;


@Beans
public class GroupListener {
    //启动&关闭控制
    static boolean Open = true;

    //at指定骂人
    @OnGroup
    @Filter(anyAt = true, value = "骂", matchType = MatchType.CONTAINS)
    public void atSaid(GroupMsg groupMsg, MsgSender sender) {
        if (Open && !String.valueOf(groupMsg.getMsgContent().getCats()).contains("2089745104"))
            BotRepackaging.sendGroupMsg(groupMsg, sender, groupMsg.getMsgContent().getCats(), "傻逼能不能死一死的", "脑瘫", "你是什么贵物", "郭楠");
    }

    //翻译功能
    @OnGroup
    @Filter(atBot = true)
    public void at(GroupMsg groupMsg, MsgSender sender) throws Exception {
        if (Open)
            sender.SENDER.sendGroupMsg(groupMsg, translateAPI.getLanguage(groupMsg.getMsgContent().getMsg()));
    }

    //启动&关闭控制
    @OnGroup
    @Filters({@Filter(value = "robot", matchType = MatchType.CONTAINS), @Filter(value = "bot", matchType = MatchType.CONTAINS)})
    public void upAndDown(GroupMsg groupMsg, MsgSender sender) {
        String control = String.valueOf(groupMsg.getMsgContent());
        if (String.valueOf(control).contains("重启")) {
            sender.SENDER.sendGroupMsg(groupMsg, "服务器已重启");
            Open = true;
        } else if (String.valueOf(control).contains("启动")) {
            if (Open)
                sender.SENDER.sendGroupMsg(groupMsg, "服务器已经启动");
            else
                sender.SENDER.sendGroupMsg(groupMsg, "服务器时间" + Basics.getTime(DatePattern.NORM_TIME_PATTERN) + ",正常运行");
            Open = true;
        } else if (String.valueOf(control).contains("关闭")) {
            if (Open)
                sender.SENDER.sendGroupMsg(groupMsg, "服务器时间" + Basics.getTime(DatePattern.NORM_TIME_PATTERN) + ",关闭成功");
            else
                sender.SENDER.sendGroupMsg(groupMsg, "服务器已经关闭");
            Open = false;
        }

    }

    //天气回复
    @OnGroup
    @Filters({@Filter(value = "天气", matchType = MatchType.CONTAINS), @Filter(value = "气温", matchType = MatchType.CONTAINS), @Filter(value = "温度", matchType = MatchType.CONTAINS)})
    public void Weather(GroupMsg groupMsg, MsgSender sender) throws Exception {
        if (Open && !String.valueOf(groupMsg.getMsgContent().getCats()).contains("at")) {
            //获取文本中的城市
            String city = Basics.getCity(String.valueOf(groupMsg.getMsgContent()));
            //不存在城市回馈
            if (city == null)
                sender.SENDER.sendGroupMsg(groupMsg, "无法查询");
            else if (String.valueOf(groupMsg.getMsgContent()).contains("当前"))
                sender.SENDER.sendGroupMsg(groupMsg, WeatherAPI.getCurrentWeather(city));
            else if (String.valueOf(groupMsg.getMsgContent()).contains("明天"))
                sender.SENDER.sendGroupMsg(groupMsg, WeatherAPI.getTodayWeather(city, 2));
            else
                sender.SENDER.sendGroupMsg(groupMsg, WeatherAPI.getTodayWeather(city, 1));
        }
    }

}