package all.botLanguage;

import all.plug.Basics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 支持注解的配置基础类，提供了获取类路径的方法
 */
public class Basis {
    //获取反射格式的类地址
    public static List<String> getSrcClass() {
        String jarPath;
        if (Basics.isJar())
            jarPath = "./Robot.jar";
        else
            jarPath = "target/miraiBot-1.0-SNAPSHOT.jar";
        List<String> myClassName = new ArrayList<>();
        try {
            //利用JarFile读取jar包
            JarFile jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> entry = jarFile.entries();
            //对jar包中的每个文件进行遍历
            while (entry.hasMoreElements()) {
                JarEntry jarEntry = entry.nextElement();
                String entryName = jarEntry.getName();
                //判断条件写入
                if (entryName.endsWith(".class") && !entryName.contains("springframework")) {
                    String className = entryName.substring(0, entryName.lastIndexOf(".")).replaceAll("/", ".");
                    className = className.substring(className.indexOf("BOOT-INF.classes.") + 17);
                    myClassName.add(className);
                }
            }
            return myClassName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myClassName;
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
