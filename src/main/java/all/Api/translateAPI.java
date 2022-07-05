package all.Api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/*
 *各种语种翻译为中文
 *最喜欢的例句:I've been breathing for a long time to cover my tears. I'm mourning how difficult the people's livelihood is
 */
public class translateAPI {

    //多行语句再处理
    public static String getTranslate(String query, String to) throws Exception {
        StringBuilder reData = new StringBuilder();
        String[] data = query.split("\n");
        for (int i = 0; i < data.length; i++) {
            if (data[i].length() != 0)
                reData.append(getCH(data[i], to));
            if (i < data.length - 1)
                reData.append("\n");
        }
        return reData.toString();
    }

    //字段加密
    public static String getDigest(String string) throws Exception {
        if (string == null) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
        mdInst.update(btInput);
        byte[] md = mdInst.digest();
        int j = md.length;
        char[] str = new char[j * 2];
        int k = 0;
        for (byte byte0 : md) {
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    //对URL处理
    public static String getCH(String query, String to) throws Exception {
        String top = "https://openapi.youdao.com/api";
        String from = "auto";
        String appKey = "4bdf7e28714a791a";
        String salt = String.valueOf(System.currentTimeMillis());
        String signType = "v3";
        String input = query;
        String curTime = String.valueOf(System.currentTimeMillis() / 1000);
        if (query.length() > 20)
            input = input.substring(0, 10) + input.length() + input.substring(input.length() - 10);
        String sign = getDigest(appKey + input + salt + curTime + "I0DpQUYPJC592F0yr8kotoEyPSHSruJU");
        URL url = new URL(top + "?q=" + URLEncoder.encode(query, "utf-8") + "&from=" + from + "&to=" + to + "&salt=" + salt + "&appKey=" + appKey + "&signType=" + signType + "&curtime=" + curTime + "&sign=" + sign);
        URLConnection connectionData = url.openConnection();
        //JSON获取
        BufferedReader br = new BufferedReader(new InputStreamReader(
                connectionData.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line);
        JSONObject firstDate = JSONObject.fromObject(sb.toString());
        JSONArray results = JSONArray.fromObject(firstDate.getJSONArray("translation"));
        return String.valueOf(results.get(0));
    }
}