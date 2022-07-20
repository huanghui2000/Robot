package all.plug;

import catcode.CatCodeUtil;
import catcode.CodeTemplate;
import catcode.Neko;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对于BOT的再封装
 */
public class BotRepackaging {

    //GroupMsg送包装
    public static void sendGroupMsg(GroupMsg groupMsg, MsgSender sender, List<Neko> list, String... Msg) {
        //遍历获取需要at的人
        StringBuilder at = new StringBuilder();
        for (Neko neko : list)
            if (String.valueOf(neko).contains("CAT:at"))
                at.append(neko);

        //发送at+消息
        for (String str : Msg)
            sender.SENDER.sendGroupMsg(groupMsg, at + str);
    }

    //image发送包装
    public static String sendImage(int Number) {
        //getImage序列消息
        Map<Integer, String> imageMap = new HashMap<>();
        imageMap.put(1, "Happy.jpg");
        imageMap.put(2, "temp.jpg");
        //转为可用的图片路径流
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        CodeTemplate<String> template = util.getStringTemplate();
        File file = new File("image/" + imageMap.get(Number));
        return template.image(file.getAbsolutePath());
    }

}
