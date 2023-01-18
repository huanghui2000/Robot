package all.api;

import all.botLanguage.Keyword;
import all.botLanguage.Type;
import all.plug.Basics;

import java.net.URL;

/**
 * 查询诗歌API
 */
@SuppressWarnings("unused")
@Type("API")
public class PoetryAPI {

    //get诗歌
    @Keyword("查诗")
    public static String getPoetry(String Msg) {
        String data = Msg.replace("查诗", "");
        data = data.replace(" ", "");
        String text;
        try {
            text = getText(data);
        } catch (Exception e) {
            return "没有找到相关的诗句";
        }
        //判断是否存在该诗
        if (getExist(text, data))
            return getString(text);
        else
            return null;
    }

    //查询是否是存在古诗词
    private static boolean getExist(String text, String input) {
        //如果有"阅读全文"则返回null
        if (text.contains("阅读全文"))
            return false;
        //如果不存在”textarea“则返回null
        if (!text.contains("<textarea"))
            return false;
        return text.contains(input);
    }

    //获取textarea中的文本
    private static String getString(String text) {
        //获取第一个textarea标签的内容，去掉结尾的https:链接
        text = text.substring(text.indexOf("<textarea") + "<textarea".length(), text.indexOf("</textarea>"));
        text = text.substring(text.indexOf(">") + 1);
        text = text.substring(0, text.indexOf("h"));
        return text;
    }

    //获取古诗查询完返回结果
    private static String getText(String text) throws Exception {
        return String.valueOf(Basics.getStringBuilder(new URL("https://so.gushiwen.cn/search.aspx?value=" + text + "&valuej=" + text.charAt(0)), 0));
    }
}
