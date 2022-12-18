package all.control.framework;

import all.BotLanguage.BQL;
import all.BotLanguage.Configure;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class Msg {
    private String msg;
    private String replyMsg;

    public boolean isReply() {
        return true;
    }

    //处理需要回复的消息
    public String getReply() {
        return replyMsg;
    }
}
