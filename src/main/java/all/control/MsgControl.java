package all.control;

import all.botLanguage.BQL;
import all.botLanguage.Configure;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 控制Bot判断消息的回复和进出处理
 */
public class MsgControl {
    private final String msg;
    private String replyMsg;

    private final long SenderID;


    public MsgControl(String msg, long SenderID) {
        this.msg = msg;
        this.SenderID = SenderID;
    }

    //判断是否需要回复,并且设置回复内容
    public boolean isReply() {
        //@情况下禁用
        if (msg.contains("@"))
            return false;
        //判断Msg是否触发了BQL中的关键词
        List<BQL> bqlList = Configure.getBqls();
        //遍历输出bqlList
        for (BQL bql : bqlList) {
            Map<String, String> map = bql.getKeywords();
            //判断Msg是否触发关键词
            for (String key : map.keySet())
                if (msg.contains(key)) {
                    if (bql.getType().equals("API")) {
                        //获取values中储存的信息：（类:方法地址）
                        String[] values = map.get(key).split(":");
                        try {
                            //通过反射注入字段，调用方法
                            Class<?> clazz = Class.forName(values[0]);
                            Object obj = clazz.newInstance();
                            Method method = clazz.getMethod(values[1], String.class);
                            replyMsg = (String) method.invoke(obj, msg);
                        } catch (Exception e) {
                            replyMsg = "有相关API，但是在控制层调用失败，异常如下：\r\n" + e;
                            return true;
                        }
                        return true;
                    }
                }
        }
        return false;
    }

    //处理需要回复的消息
    public String getReply() {
        return replyMsg;
    }

}
