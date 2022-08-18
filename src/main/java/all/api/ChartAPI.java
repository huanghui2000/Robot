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

}
