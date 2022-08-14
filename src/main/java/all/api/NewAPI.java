package all.api;

import all.plug.Basics;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.net.URL;

public class NewAPI {

    //获取每日新闻
    public static String getNew() throws Exception {
        String url = "https://api.qqsuu.cn/api/60s?type=json";
        //获取新闻文本
        String text = String.valueOf(Basics.getStringBuilder(new URL(url), 0));
        JSONObject jb = JSONObject.parseObject(text);
        //分别获取name新闻标题，time时间信息，data新闻内容
        String nameStr = jb.getString("name");
        JSONArray time = jb.getJSONArray("time");
        //遍历time数组，拼接为一条字符串
        StringBuilder timeStr = new StringBuilder();
        for (int i = 0; i < time.size(); i++)
            timeStr.append(time.getString(i)).append("，");
        JSONArray data = jb.getJSONArray("data");
        //遍历data数组，拼接为一条字符串
        StringBuilder dataStr = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            if (i < data.size() - 1)
                dataStr.append(i + 1).append("、");
            //去除字符串中的'<'和'>'之间的内容，以及i、的内容
            dataStr.append(data.getString(i).replaceAll("<.*?>", "").replaceAll((i + 1) + "、", ""));
            if (i < data.size() - 1)
                dataStr.append("；\n\n");
        }
        return timeStr + nameStr + "\n\n" + dataStr;
    }
}
