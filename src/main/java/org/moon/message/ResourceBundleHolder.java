package org.moon.message;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * the resource bundle holder, easily be used in spring component.
 * default use <code>message</code> as baseName for <code>ResourceBundle</code>,
 * also <code>baseName</code> can be configurable by {@link #setBaseName(String)}.
 * the holder can be simply used:
 * <code>
 *     <bean class="org.moon.message.ResourceBundleHolder"></bean>
 * </code>
 * @author GavinCook
 * @since 1.0.0
 **/
public class ResourceBundleHolder implements ApplicationContextAware{

    private ResourceBundle resourceBundle;

    private String baseName = "messages";

    private Locale locale;

    public String getString(String key, Object ... o){
        if(Objects.isNull(o)){
            return resourceBundle.getString(key);
        }
        return MessageFormat.format(resourceBundle.getString(key),o);
    }

    public ResourceBundle getResourceBundle(){
        return resourceBundle;
    }

    public void setBaseName(String baseName) {
        Assert.notNull(baseName, "baseName for ResourceBundle must not null");
        this.baseName = baseName;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(Objects.isNull(locale)){
            this.locale = Locale.getDefault();
        }
        this.resourceBundle = ResourceBundle.getBundle(baseName, locale);
    }
}
