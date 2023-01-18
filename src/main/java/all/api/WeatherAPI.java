package all.api;

import all.botLanguage.Keyword;
import all.botLanguage.Type;
import all.plug.Basics;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * 天气API
 * "date":           日期
 * "text_day":       白天天气现
 * "text_night":     晚间天气
 * "text":           当前天气
 * "temperature":    当前温度
 * "high":           当天最高温度
 * "low":            当天最低温度
 * "wind_direction": 风向文字
 * "wind_speed":     风速，单位km/h
 * "wind_scale":     风力等级
 * "rainfall":       降水量，单位mm
 * "humidity":       相对湿度，0~100，单位为百分比
 */

@SuppressWarnings("unused")
@Type("API")
public class WeatherAPI {

    //get今日天气
    @Keyword("天气")
    public static String getTodayWeather(String Msg) {
        //对语句中城市的判断
        String city = Basics.getCity(Msg);
        if (city == null)
            return "无法查询";
        //对今日和明日的判断
        int choice;
        if (Msg.contains("明天"))
            choice = 2;
        else
            choice = 1;
        JSONObject secondDate;
        try {
            URL url = new URL("https://api.seniverse.com/v3/weather/daily.json?key=SHgUEuS8LSdvWLgs9&location=" + city + "&days=" + choice);
            secondDate = getJSON(url);
        } catch (Exception e) {
            return "出错了";
        }
        JSONArray daily = JSONArray.fromObject(secondDate.getString("daily"));
        JSONObject info = JSONObject.fromObject(daily.get(0));
        if (choice == 2)
            info = JSONObject.fromObject(daily.get(1));
        //MAP写入
        Map<String, Object> map = new HashMap<>();
        map.put("日期", info.getString("date"));
        map.put("白天天气", info.getString("text_day"));
        map.put("晚间天气", info.getString("text_night"));
        map.put("最高温度", info.getString("high"));
        map.put("最低温度", info.getString("low"));
        map.put("降水量", info.getString("rainfall"));
        map.put("降水概率", (Double.parseDouble(info.getString("precip")) * 1000) / 10 + "%");
        map.put("风向", info.getString("wind_direction"));
        map.put("风速", info.getString("wind_speed"));
        map.put("风力", info.getString("wind_scale"));
        map.put("相对湿度", info.getString("humidity"));
        String day = "今天";
        if (choice == 2)
            day = "明天";
        return day + "是" + map.get("日期") + "\n" + city
                + "的白天天气是" + map.get("白天天气") + "而晚间为" + map.get("晚间天气")
                + "\n温度为:" + map.get("最低温度") + "°C~" + map.get("最高温度") + "°C"
                + "\n降水概率:" + map.get("降水概率") + " 降水量:" + map.get("降水量") + " 相对湿度:" + map.get("相对湿度")
                + "\n" + map.get("风向") + "风 风力:" + map.get("风力") + "级 风速:" + map.get("风速");
    }

    //对URL处理
    private static JSONObject getJSON(URL url) throws Exception {
        StringBuilder sb = Basics.getStringBuilder(url, 0);
        JSONObject firstDate = JSONObject.fromObject(sb.toString());
        return (JSONObject) JSONArray.fromObject(firstDate.getJSONArray("results")).get(0);
    }

}