package all.BotLanguage;

import java.util.Map;

/**
 * BotLanguage的实体类
 */
public class BQL {
    private String type;
    private Map<String, String> keywords;

    @Override
    public String toString() {
        return "BQL{" +
                "type='" + type + '\'' +
                ", keywords='" + keywords +
                "}'";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Map<String, String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Map<String, String> keywords) {
        this.keywords = keywords;
    }
}
