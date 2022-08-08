package all.api;

import all.plug.Basics;

import java.net.URL;

public class NewAPI {

    //起始为1221，差值为今天到从2022/8/8天数的差距
    public static int getDay() {
        return 1221 + (Integer.parseInt(Basics.getTime("yyyy")) - 2022) * 365 + (Integer.parseInt(Basics.getTime("MM")) - 8) * 30 + (Integer.parseInt(Basics.getTime("dd")) - 8);
    }

    //获取每日新闻
    public static String getNew() throws Exception {
        //拼接URL
        String url = "https://www.jun.la/60snews/" + getDay() + ".html";
        //获取新闻文本
        String text = String.valueOf(Basics.getStringBuilder(new URL(url), 0));
        // 将文本按照<span style="font-size: 16px">切割，封装在String数组中，逐一换行,最后一条切割到</span></p>
        String[] Split = text.split("<span style=\"font-size: 16px\">");
        //遍历split合为一个字符串,开头为X月X日，星期X，每天60秒读懂世界！
        StringBuilder sb = new StringBuilder(Basics.getTime("MM月dd日") + "," + Basics.getWeek() + ",每天60秒读懂世界！\n");
        for (int i = 1; i < Split.length; i++) {
            String next = Split[i].split("</span>")[0];
            next = next.replaceAll("<.*?>", "");
            sb.append(next).append("\n");
        }
        return String.valueOf(sb);
    }

}
