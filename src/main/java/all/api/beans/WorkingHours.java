package all.api.beans;

/**
 * 计算每日工时的实体类
 */
public class WorkingHours {

    //每日时长分为上班时间 餐前时间 餐后时间 下班时间
    private String goWorkTime;

    private String beforeFoodTime;

    private String afterFoodTime;

    private String offWorkTime;

    private String dayTime;

    @Override
    public String toString() {
        return "Wages{" +
                "goWorkTime='" + goWorkTime + '\'' +
                ", beforeFoodTime='" + beforeFoodTime + '\'' +
                ", afterFoodTime='" + afterFoodTime + '\'' +
                ", offWorkTime='" + offWorkTime + '\'' +
                '}';
    }

    public String getGoWorkTime() {
        return goWorkTime;
    }

    public void setGoWorkTime(String goWorkTime) {
        this.goWorkTime = goWorkTime;
    }

    public String getBeforeFoodTime() {
        return beforeFoodTime;
    }

    public void setBeforeFoodTime(String beforeFoodTime) {
        this.beforeFoodTime = beforeFoodTime;
    }

    public String getAfterFoodTime() {
        return afterFoodTime;
    }

    public void setAfterFoodTime(String afterFoodTime) {
        this.afterFoodTime = afterFoodTime;
    }

    public String getOffWorkTime() {
        return offWorkTime;
    }

    public void setOffWorkTime(String offWorkTime) {
        this.offWorkTime = offWorkTime;
    }
}
