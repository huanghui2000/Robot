package all.plug;

import all.api.NewAPI;
import all.api.WeatherAPI;
import cn.hutool.core.date.DatePattern;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Task类以及Task的解析
 */
public class TextParsing {
    //获取定时list
    public static List<Task> getTask() throws Exception {
        //IO获取定时文本内容
        DataInputStream in = new DataInputStream(new FileInputStream("Task.txt"));
        BufferedReader d = new BufferedReader(new InputStreamReader(in));
        d.readLine();
        List<Task> list = new LinkedList<>();
        //遍历每行文本
        for (String said = d.readLine(); said != null; said = d.readLine()) {
            Task task = new Task();
            //切割每行文本
            String[] line = said.split("\\s+");
            //获取Time
            task.setTime(line[0]);
            //获取Object
            task.setObject(line[1]);
            //获取ID
            task.setID(Long.valueOf(line[2]));
            //获取文本事件，包装为List
            List<String> txtList = new ArrayList<>(Arrays.asList(line).subList(3, line.length));
            task.setThings(txtList);
            list.add(task);
        }
        in.close();
        d.close();
        return list;
    }

    //对定时任务中的文本进行处理
    public static String keyWords(String txt) throws Exception {
        if (txt.contains("IMA"))
            txt = BotRepackaging.sendImage(1);
        else if (txt.contains("WEA"))
            txt = WeatherAPI.getTodayWeather(Basics.getCity(txt), 1);
        else if (txt.contains("TIME"))
            txt = Basics.getTime(DatePattern.NORM_TIME_PATTERN);
        else if (txt.contains("NEW")) {
            txt = NewAPI.getNew();
        }
        return txt;
    }

    //Task类
    public static class Task {
        private String time;
        private String object;
        private Long ID;
        private List<String> things;

        public String getTime() {
            return this.time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getObject() {
            return this.object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public Long getID() {
            return this.ID;
        }

        public void setID(Long ID) {
            this.ID = ID;
        }

        public List<String> getThings() {
            return this.things;
        }

        public void setThings(List<String> things) {
            this.things = things;
        }
    }
}