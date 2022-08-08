package all.api;

import all.plug.*;

import java.net.URL;

/*
 * 查询诗歌API
 * */

public class PoetryAPI {

    //AI可能不太理解何为断句，所有自己手写一个切割方法
    public static String extracted(String query) throws Exception {
        String poetry = getPoetry(query);
        //从query在poetry中位置开始往前找。？！
        int index = poetry.indexOf(query);
        //如果是开头则不进行前切割
        if (index != 0)
            for (int i = index + 1; i >= 0; i--)
                if (poetry.charAt(i) == '。' || poetry.charAt(i) == '？' || poetry.charAt(i) == '！') {
                    poetry = poetry.substring(i + 1);
                    break;
                }
        //再往后切割，找到。？！
        for (int i = 0; i < poetry.length(); i++)
            if (poetry.charAt(i) == '。' || poetry.charAt(i) == '？' || poetry.charAt(i) == '！') {
                poetry = poetry.substring(0, i + 1);
                break;
            }
        //对query在poetry中的位置进行判断，如果在逗号前，则返回逗号后的句子
        if (poetry.indexOf(query) < poetry.indexOf('，'))
            poetry = poetry.substring(poetry.indexOf('，') + 1, poetry.length() - 1);
        return poetry;
    }

    //get诗歌
    public static String getPoetry(String data) throws Exception {
        if (data == null) return null;
        data = getChinaText(data);
        String text = getText(data);
        //判断是否存在该诗
        if (getExist(text, data)) {
            return getString(text);
        } else {
            return null;
        }
    }

    //将语句中英文符号替换成中文符号
    public static String getChinaText(String text) {
        //去掉末尾的特殊符号
        if (text.charAt(text.length() - 1) == '，' || text.charAt(text.length() - 1) == '？' || text.charAt(text.length() - 1) == '！' || text.charAt(text.length() - 1) == '。')
            text = text.substring(0, text.length() - 1);

        //将英文符号替换成中文符号
        text = text.replace(",", "，");
        text = text.replace(".", "。");
        text = text.replace("?", "？");
        text = text.replace("!", "！");
        text = text.replace(":", "：");
        return text;
    }

    //查询是否是存在古诗词
    public static boolean getExist(String text, String input) {
        //如果有"阅读全文"则返回null
        if (text.contains("阅读全文"))
            return false;
        //如果不存在”textarea“则返回null
        if (!text.contains("<textarea"))
            return false;
        text = getString(text);
        //在text中input下一位是否为标点符号
        char c = text.charAt(text.indexOf(input) + input.length());
        if (!(c == '，') && !(c == '。') && !(c == '？') && !(c == '！'))
            return false;
        //判断text是否存在input
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
    public static String getText(String text) throws Exception {
        return String.valueOf(Basics.getStringBuilder(new URL("https://so.gushiwen.cn/search.aspx?value=" + text + "&valuej=" + text.charAt(0)), 0));
    }
}
