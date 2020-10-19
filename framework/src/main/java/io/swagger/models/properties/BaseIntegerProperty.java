package io.swagger.models.properties;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName BaseIntegerProperty
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-19 12:34
 * @Version 1.0
 */
public class BaseIntegerProperty extends AbstractNumericProperty {
    public static final String TYPE = "integer";

    public BaseIntegerProperty() {
        this(null);
    }

    public BaseIntegerProperty(String format) {
        super.type = TYPE;
        super.format = format;
    }

    public static boolean isType(String type, String format) {
        return TYPE.equals(type);
    }

    @Override
    public void setExample(Object example) {
        if (example instanceof String && StringUtils.isNotBlank((String)example)) {
            try {
                this.example = Long.parseLong((String)example);
            } catch (NumberFormatException e) {
                this.example = example;
            }
        } else {
            this.example = example;
        }
    }
}
