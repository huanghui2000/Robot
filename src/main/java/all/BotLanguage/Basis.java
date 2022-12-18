package all.BotLanguage;

import all.Start;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 支持注解的配置基础类，提供了获取类路径的方法
 */
public class Basis {

    //获取反射格式的类地址
    public static List<String> getSrcClass() {
        //获取包all的路径
//        String path = "src/main/java/";
//        List<String> list = searchPath(path);
//        //对类路径修改为基本反射的可用地址
//        for (int i = 0; i < list.size(); i++) {
//            //将路径中的//\统一为\
//            path = path.replaceAll("/", "\\\\");
//            String pathTemp = list.get(i).substring(list.get(i).indexOf(path) + path.length())
//                    .replaceAll("\\\\", ".")
//                    .replaceAll(".java", "")
//                    .replaceAll(".class", "");
//            //如果pathTemp以.开头，则去掉.
//            if (pathTemp.startsWith("."))
//                pathTemp = pathTemp.substring(1);
//            list.set(i, pathTemp);
//        }
        List<String> list = new ArrayList<>();
        list.add("all.api.MusicAPI");
        list.add("all.api.PoetryAPI");
        list.add("all.api.TranslateAPI");
        list.add("all.api.WeatherAPI");
        return list;
    }

    //搜索项目中所有类路径
    public static List<String> searchPath(String path) {
        List<String> list = new ArrayList<>();
        //基于对java源地址的遍历收集
        File[] files = new File(path).listFiles();
        if (files != null)
            for (File f : files)
                if (f.isDirectory())
                    list.addAll(searchPath(path + "/" + f.getName()));
                else if (f.getName().endsWith(".java") || f.getName().endsWith(".class"))
                    list.add(f.getAbsolutePath());
        return list;
    }

}
