package all.BotLanguage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对注解了BotLanguage的类与内容进行装配
 */
public class Configure {

    private static List<BQL> getBqls = AnnotateAssembly();

    public static List<BQL> getGetBqls() {
        return getBqls;
    }

    //对注解的类进行装配
    private static List<BQL> AnnotateAssembly() {
        List<String> lines = Basis.getSrcClass();
        List<BQL> bqls = new ArrayList<>();
        for (String line : lines) {
            try {
                //遍历所有类，获取配置了Type的类
                Class<?> clazz = Class.forName(line);
                BQL bql = new BQL();
                //获取类中配置了Keyword的方法,并且将方法名装配
                if (clazz.isAnnotationPresent(Type.class)) {
                    //设置type为注释关键字
                    bql.setType(clazz.getAnnotation(Type.class).value());
                    for (Method method : clazz.getDeclaredMethods())
                        //添加keyword，格式为key:关键之 value:类名:方法名
                        if (method.isAnnotationPresent(Keyword.class)) {
                            Map<String, String> map = new HashMap<>();
                            map.put(method.getAnnotation(Keyword.class).value(), line + ":" + method.getName());
                            bql.setKeywords(map);
                        }
                    if (bql.getKeywords() != null)
                        bqls.add(bql);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //重复压缩
        bqls = getMerge(bqls);
        return bqls;
    }

    //将bqls同类型type的map合并
    private static List<BQL> getMerge(List<BQL> bqls) {
        List<BQL> newBqls = new ArrayList<>();
        for (BQL bql : bqls) {
            if (newBqls.size() == 0) {
                newBqls.add(bql);
            } else {
                boolean flag = false;
                for (BQL bql1 : newBqls) {
                    if (bql1.getType().equals(bql.getType())) {
                        bql1.getKeywords().putAll(bql.getKeywords());
                        flag = true;
                    }
                }
                if (!flag)
                    newBqls.add(bql);
            }
        }
        return newBqls;
    }

}