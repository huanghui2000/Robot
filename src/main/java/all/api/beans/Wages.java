package all.api.beans;

import java.util.HashMap;
import java.util.Map;

import all.plug.Basics;

/**
 * 统计工资的实体类
 */
public class Wages {
    static Map<Integer, WorkingHours> HoursList = new HashMap<>();

    //写入工时
    public static void addHours(int day, WorkingHours hours) {
        //如果存在则覆盖
        if (HoursList.containsKey(day)) {
            HoursList.remove(day);
        }
        HoursList.put(day, hours);
    }

    //统计总工时
    public static double getTotalHours() {
        double total = 0;
        for (Map.Entry<Integer, WorkingHours> entry : HoursList.entrySet()) {
            total += Basics.timeDifference(entry.getValue().getGoWorkTime(), entry.getValue().getOffWorkTime()) - Basics.timeDifference(entry.getValue().getBeforeFoodTime(), entry.getValue().getAfterFoodTime());
        }
        return 4737+total;
    }

}
