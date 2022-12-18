package all.BotLanguage;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 对于BotLanguage的注解，标记类的触发类型
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {
    String value();
}
