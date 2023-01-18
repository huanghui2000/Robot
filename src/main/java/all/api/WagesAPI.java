package all.api;

import all.api.beans.Wages;
import all.api.beans.WorkingHours;
import all.botLanguage.Keyword;
import all.botLanguage.Type;
import all.plug.Basics;

@SuppressWarnings("unused")
@Type("API")
public class WagesAPI {
    @Keyword("工资统计")
    public static String getWages(String data) {
        String result = "总工时为：" + (int) Wages.getTotalHours() / 60 + "小时" + (int) Wages.getTotalHours() % 60 + "分钟\n";
        result += "工资为：" + (int) Wages.getTotalHours() * 16 / 60 + "元";
        return result;
    }

    @Keyword("工时写入")
    public static String addHours(String data) {
        //切割data，其中第二行为日期:XX 第三行之后为上班时间 餐前时间 餐后时间 下班时间
        String[] split = data.split("\n");
        String date = split[1].split(":")[1];
        String goWork = split[2];
        String beforeFood = split[3];
        String afterFood = split[4];
        String offWork = split[5];
        WorkingHours workingHours = new WorkingHours();
        workingHours.setGoWorkTime(goWork);
        workingHours.setBeforeFoodTime(beforeFood);
        workingHours.setAfterFoodTime(afterFood);
        workingHours.setOffWorkTime(offWork);
        Wages.addHours(Integer.parseInt(date), workingHours);
        return "写入成功";
    }

    @Keyword("下班计算器")
    public static String getOffWorkTime(String data) {
        //切割data，其中第二行为上班时间 餐前时间 餐后时间 ,就算出做满六小时的下班时间
        String[] split = data.split("\n");
        String goWork = split[1];
        String beforeFood = split[2];
        String afterFood = split[3];
        int offWorkTime = 360 - Basics.timeDifference(goWork, beforeFood);
        //转换为HH:mm格式
        //切割afterFood，得到上班时间的小时和分钟，加上offWorkTime的小时和分钟，得到下班时间
        String[] afterFoodTime = afterFood.split(":");
        int offWorkHour = Integer.parseInt(afterFoodTime[0]) + offWorkTime / 60;
        int offWorkMinute = Integer.parseInt(afterFoodTime[1]) + offWorkTime % 60;
        //分钟进位处理
        if (offWorkMinute >= 60) {
            offWorkHour++;
            offWorkMinute -= 60;
        }
        //对HH:mm格式的时间进行补零处理
        String offWorkHourString = offWorkHour < 10 ? "0" + offWorkHour : "" + offWorkHour;
        String offWorkMinuteString = offWorkMinute < 10 ? "0" + offWorkMinute : "" + offWorkMinute;
        return "下班时间为：" + offWorkHourString + ":" + offWorkMinuteString;
    }
}
