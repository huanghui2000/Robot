package all.botLanguage;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 对于BotLanguage的注解，标记方法的触发关键字
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Keyword {
    String value();
}
