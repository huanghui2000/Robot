package all.msg;

import all.botLanguage.Configure;
import all.control.MsgControl;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.BotConfiguration;


/**
 * 对于框架启动的唯一入口
 */
public class GroupListener {

    //对消息进出的入口
    public static void afterLogin(Bot bot) {
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) -> {
            MsgControl msgControl = new MsgControl(event.getMessage().contentToString(), event.getSender().getId());
            if (msgControl.isReply())
                event.getSubject().sendMessage(new MessageChainBuilder().append(msgControl.getReply()).build());
        });
    }

    //QQ机器人配置和启动入口
    public static void main(String[] args) {
        //2831872948L, "QaZ14725839"     2089745104L, "QAQAQ0528"
        Bot bot = BotFactory.INSTANCE.newBot(2089745104L, "QAQAQ0528", new BotConfiguration() {{
            fileBasedDeviceInfo();
            setProtocol(MiraiProtocol.IPAD);
        }});
        bot.login();
        afterLogin(bot);
    }

}