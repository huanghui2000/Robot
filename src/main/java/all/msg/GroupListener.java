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

    @OnGroup
    @Filters({@Filter(value = "丢.*个骰子", matchType = MatchType.REGEX_MATCHES), @Filter(value = "丢骰子", matchType = MatchType.REGEX_MATCHES), @Filter(value = "丢个骰子", matchType = MatchType.REGEX_MATCHES)})
    public void onDice(GroupMsg groupMsg, MsgSender sender) {
        int number = Basics.getNumber(groupMsg.getMsgContent().getMsg(), "来", "个");
        if (number > 0 && number < 10)
            for (int i = 0; i < number; i++)
                sender.SENDER.sendGroupMsg(groupMsg, "[CAT:dice,random=true]");
        else sender.SENDER.sendGroupMsg(groupMsg, "骰子数量错误");

    }

    //对诗功能
    @OnGroup
    @Filter(atBot = true, matchType = MatchType.CONTAINS)
    public void getExtracted(GroupMsg groupMsg, MsgSender sender) throws Exception {
        if (Open && !String.valueOf(groupMsg.getMsgContent().getCats()).contains("骂")) {
            //空格，at
            String said = groupMsg.getMsgContent().getMsg();
            said = said.replace(" ", "");
            said = said.replaceAll("\\[CAT:at,code=.*?]", "");
            if (PoetryAPI.getPoetry(said) != null)
                sender.SENDER.sendGroupMsg(groupMsg, PoetryAPI.extracted(said));
            else
                sender.SENDER.sendGroupMsg(groupMsg, "没有找到相关的诗句");
        }
    }

    //查询诗句
    @OnGroup
    @Filter(value = "查诗", matchType = MatchType.CONTAINS)
    public void getPoetry(GroupMsg groupMsg, MsgSender sender) throws Exception {
        if (Open) {
            //去掉”查诗“和空格，at
            String said = groupMsg.getMsgContent().getMsg().replace("查诗", "");
            said = said.replace(" ", "");
            said = said.replaceAll("\\[CAT:at,code=.*?]", "");
            if (PoetryAPI.getPoetry(said) != null)
                sender.SENDER.sendGroupMsg(groupMsg, PoetryAPI.getPoetry(said));
            else
                sender.SENDER.sendGroupMsg(groupMsg, "没有找到相关的诗句");
        }

    }

    //at指定骂人
    @OnGroup
    @Filter(anyAt = true, value = "骂", matchType = MatchType.CONTAINS)
    public void atSaid(GroupMsg groupMsg, MsgSender sender) {
        if (Open && !String.valueOf(groupMsg.getMsgContent().getCats()).contains("2089745104"))
            BotRepackaging.sendGroupMsg(groupMsg, sender, groupMsg.getMsgContent().getCats(), "傻逼能不能死一死的", "脑瘫", "你是什么贵物", "郭楠");
    }

    //翻译功能
    @OnGroup
    @Filter(value = "翻译", matchType = MatchType.CONTAINS)
    public void at(GroupMsg groupMsg, MsgSender sender) throws Exception {
        if (Open) {
            //判断最开头两个字是不是”语音“
            if (groupMsg.getMsg().startsWith("语音翻译"))
                sender.SENDER.sendGroupMsg(groupMsg, "[CAT:voice,file=/www/wwwroot/QQRot/image/" + TranslateAPI.getLanguage(groupMsg.getMsgContent().getMsg()) + ".wav]");
            else
                sender.SENDER.sendGroupMsg(groupMsg, TranslateAPI.getLanguage(groupMsg.getMsgContent().getMsg()));
        }
    }

    @OnGroup
    @Filters({@Filter(value = "来.*张图", matchType = MatchType.REGEX_MATCHES), @Filter(value = "来张图", matchType = MatchType.REGEX_MATCHES)})
    public void getChart(GroupMsg groupMsg, MsgSender sender) {
        if (Open) {
            int number = Basics.getNumber(groupMsg.getMsgContent().getMsg(), "来", "张");
            //倘若数字小于1或者大于10，则返回图片数量信息错误
            if (number <= 0 || number > 10)
                sender.SENDER.sendGroupMsg(groupMsg, "图片数量错误");
            else {
                //新建一个线程，每0.5秒发送一张图片,结束销毁线程
                Thread thread = new Thread(() -> {
                    for (int i = 0; i < number; i++) {
                        long time = ChartAPI.getImage();
                        sender.SENDER.sendGroupMsg(groupMsg, BotRepackaging.sendImage(time));
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        }
    }

    //启动&关闭控制
    @OnGroup
    @Filters({@Filter(value = "robot", matchType = MatchType.CONTAINS), @Filter(value = "bot", matchType = MatchType.CONTAINS)})
    public void upAndDown(GroupMsg groupMsg, MsgSender sender) {
        String control = String.valueOf(groupMsg.getMsgContent());
        if (String.valueOf(control).contains("重启")) {
            sender.SENDER.sendGroupMsg(groupMsg, "服务器已重启");
            sender.SENDER.sendGroupMsg(groupMsg, "服务器时间" + Basics.getTime(DatePattern.NORM_TIME_PATTERN));
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