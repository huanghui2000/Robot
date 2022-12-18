package all.api;

import all.BotLanguage.Keyword;
import all.BotLanguage.Type;
import all.plug.Basics;

import java.net.URL;

@SuppressWarnings("unused")
@Type("API")
public class MusicAPI {
    //获取歌词
    @Keyword("查歌词")
    public static String getLyricUrl(String Msg) {
        String data = Msg.replace("查歌词", "");
        String music;
        try {
            music = String.valueOf(Basics.getStringBuilder(new URL(getSearch(data)), 0));
        } catch (Exception e) {
            return "没有找到相关的歌曲或者查询出错";
        }
        music = music.substring(music.indexOf("</h5>") + 5);
        music = music.substring(0, music.indexOf("<h5>"));
        //将所有<p>换为空,</p>换为换行
        music = music.replaceAll("<p>", "");
        music = music.replaceAll("</p>", "\n");
        //去除所有空格
        music = music.replaceAll(" ", "");
        return music;
    }

    //拼接搜索内容
    private static String getSearch(String data) throws Exception {
        //https://www.hifini.com/search-.*.htm
        URL search = new URL("https://www.hifini.com/search-" + data + ".htm");
        String text = String.valueOf(Basics.getStringBuilder(search, 0));
        //切割第一个 thread-XXX.htm
        text = text.substring(text.indexOf("thread-"));
        text = text.substring(0, text.indexOf(".htm"));
        return "https://www.hifini.com/" + text + ".htm";
    }

}
