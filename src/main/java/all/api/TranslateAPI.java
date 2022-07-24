package all.api;

import all.plug.Basics;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/*
 *各种语种翻译为中文
 *最喜欢的例句:I've been breathing for a long time to cover my tears. I'm mourning how difficult the people's livelihood is
 */
public class TranslateAPI {
    //语句中的语种解析
    public static String getLanguage(String said) throws Exception {
        //把句子中的[CAT:at,code=.....]去掉
        said = said.replaceAll("\\[CAT:at,code=.*?]", "");
        //提取语种和对应符号集
        String k = "中文 繁体 英语 日语 韩语 法语 西班牙语 葡萄牙语 意大利语 俄语 越南语 德语 阿拉伯语 印尼语 南非荷兰语 波斯尼亚语 保加利亚语 粤语 加泰隆语 克罗地亚语 捷克语 丹麦语 荷兰语 爱沙尼亚语 斐济语 芬兰语 希腊语 海地克里奥尔语 希伯来语 印地语 白苗语 匈牙利语 斯瓦希里语 克林贡语 拉脱维亚语 立陶宛语 马来语 马耳他语 挪威语 波斯语 波兰语 克雷塔罗奥托米语 罗马尼亚语 斯洛伐克语 斯洛语尼亚语 瑞典语 塔希提语 泰语 汤加语 土耳其语 乌克兰语 乌尔都语 威尔士语 尤卡坦玛雅语 阿尔巴尼亚语 阿姆哈拉语 亚美尼亚语 阿塞拜疆语 孟加拉语 巴斯克语 白俄罗斯语 宿务语 科西嘉语 世界语 菲律宾语 弗里西语 加利西亚语 格鲁吉亚语 古吉拉特语 豪萨语 夏威夷语 冰岛语 伊博语 爱尔兰语 爪哇语 卡纳达语 哈萨克语 高棉语 库尔德语 柯尔克孜语 老挝语 拉丁语 卢森堡语 马其顿语 马尔加什语 马拉雅拉姆语 毛利语 马拉地语 蒙古语 缅甸语 尼泊尔语 齐切瓦语 普什图语 旁遮普语 萨摩亚语 苏格兰盖尔语 塞索托语 修纳语 信德语 僧伽罗语 索马里语 巽他语 塔吉克语 泰米尔语 泰卢固语 乌兹别克语 南非科萨语 意第绪语 约鲁巴语 南非祖鲁语";
        String v = "zh-CHS zh-CHT en ja ko fr es pt it ru vi de ar id af bs bg yue ca hr cs da nl et fj fi el ht he hi mww hu sw tlh lv lt ms mt no fa pl otq ro sk sl sv ty th to tr uk ur cy yua sq am hy az bn eu be ceb co eo tl fy gl ka gu ha haw is ig ga jw kn kk km ku ky lo la lb mk mg ml mi mr mn my ne ny ps pa sm gd st sn sd si so su tg ta te uz xh yi yo zu";
        //按照空格切割语种和对应符号集，得到语种和对应符号集的Map
        Map<String, String> map = new HashMap<>();
        String[] ks = k.split(" ");
        String[] vs = v.split(" ");
        for (int i = 0; i < ks.length; i++)
            map.put(ks[i], vs[i]);
        //查找有用部分是否在语种中，如果在，则返回对应符号集，否则返回auto，并且把开头的语种去掉
        String to = "auto";
        //切割句子到”翻译“，如果”译“字后面有冒号,则去掉冒号
        if (said.contains("翻译:"))
            said = said.replaceFirst("翻译:", "");
        else
            said = said.replaceFirst("翻译", "");
        for (String key : map.keySet())
            if (said.contains(key)) {
                if (map.get(key).equals("zh-CHS"))
                    said = said.substring(said.indexOf("文") + 1);
                else if (map.get(key).equals("zh-CHT"))
                    said = said.substring(said.indexOf("体") + 1);
                else
                    said = said.substring(said.indexOf("语") + 1);
                to = map.get(key);
                break;
            }
        //若字符串只有若干空格，设said为对不起没有检测到您的语句
        if (said.equals(" ") || said.equals(""))
            said = "对不起没有检测到您的语句";
        return TranslateAPI.getTranslate(said, to);
    }

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

    //对一词多义的处理
    public static String getWord(JSONObject firstDate) {
        //获取一词多义
        JSONArray fist = firstDate.getJSONObject("basic").getJSONArray("explains");
        //将json按照,切割，每个句子换行
        StringBuilder sb = new StringBuilder();
        for (Object o : fist) {
            sb.append(o);
            sb.append("\n");
        }
        return sb.toString();
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
        //对单词的纯处理
        String sb = String.valueOf(Basics.getStringBuilder(url));
        JSONObject firstDate = JSONObject.fromObject(sb);
        if (sb.contains("basic"))
            //获取一词多义json
            return getWord(firstDate);
        return String.valueOf(JSONArray.fromObject(firstDate.getJSONArray("translation")).get(0));
    }


}