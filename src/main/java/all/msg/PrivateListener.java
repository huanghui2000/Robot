package all.msg;

import all.plug.BotRepackaging;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;


@Beans
public class PrivateListener {
    //私聊敷衍
    @OnPrivate
    public void privateMsg(PrivateMsg privateMsg, MsgSender sender) {
        sender.SENDER.sendPrivateMsg(privateMsg, BotRepackaging.sendImage(1));
    }

}