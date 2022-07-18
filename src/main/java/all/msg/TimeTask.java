package all.msg;

import all.plug.Basics;
import all.plug.TextParsing;

import java.util.List;
import java.util.concurrent.TimeUnit;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.timer.EnableTimeTask;
import love.forte.simbot.timer.Fixed;

@Beans
@EnableTimeTask
public class TimeTask {
    @Depend
    private BotManager botManager;

    //定时任务再包装一层，防止每次都要获取botManager
    @Fixed(value = 1, timeUnit = TimeUnit.MINUTES)
    public void task() throws Exception {
        //获取所有任务
        List<TextParsing.Task> list = TextParsing.getTask();
        //遍历所有任务
        for (TextParsing.Task task : list)
            //判断时间是否到达
            if (Basics.getTime("HH:mm").matches(task.getTime())) {
                //获取任务内容
                List<String> things = task.getThings();
                //判断个人任务还是群任务
                if (task.getObject().equals("个人"))
                    for (Object thing : things)
                        this.botManager.getDefaultBot().getSender().SENDER.sendPrivateMsg(task.getID(), String.valueOf(thing));
                else
                    for (Object thing2 : things)
                        this.botManager.getDefaultBot().getSender().SENDER.sendGroupMsg(task.getID(), String.valueOf(thing2));
            }
    }
}