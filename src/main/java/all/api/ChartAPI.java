package all.api;

import all.plug.Basics;
import com.alibaba.fastjson.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChartAPI {
    //输入URL，获取图片文本在本地
    public static long getImage() {
        //设定当前时间为图片的关键字
        long time = System.currentTimeMillis();
        try {
            URL pic = getURL();
            String savePath = "image/" + time + ".jpg";
            //对图片的各种流操作
            HttpURLConnection connection = (HttpURLConnection) pic.openConnection();
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(new FileOutputStream(savePath));
            byte[] buffer = new byte[4096];
            int count;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            in.close();
            connection.disconnect();
        } catch (IOException e) {
            return 2;
        }
        return time;
    }

    //获取图片URL
    public static URL getURL() throws IOException {
        URL url = new URL("https://dev.iw233.cn/api.php?sort=top&type=json");
        String path = String.valueOf(Basics.getStringBuilder(url, 0));
        JSONObject jsonObject = JSONObject.parseObject(path);
        return new URL(jsonObject.getString("pic").substring(2, jsonObject.getString("pic").length() - 2));
    }

    //解析来X张图中的汉字数字
    public static int getNumber(String str) {
        //找到来和张图中的字符
        int start = str.indexOf("来");
        int end = str.indexOf("张");
        //判断来和张是否重合，若有返回1
        if (start == end)
            return 1;
        //获取汉字数字
        String number = str.substring(start + 1, end);
        //判断是否为数字，若是则返回数字
        if (number.matches("[0-9]+"))
            return Integer.parseInt(number);
        //将汉字一二三。。。十转换为数字
        switch (number) {
            case "一":
                return 1;
            case "二":
            case "两":
                return 2;
            case "三":
                return 3;
            case "四":
                return 4;
            case "五":
                return 5;
            case "六":
                return 6;
            case "七":
                return 7;
            case "八":
                return 8;
            case "九":
                return 9;
            case "十":
                return 10;
            default:
                return 0;
        }
    }
}
