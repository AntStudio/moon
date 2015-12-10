package org.moon.support.theme;

import org.moon.core.session.SessionContext;

/**
 * @author GavinCook
 * @since 1.0.0
 **/
public class ThemeResource {

    private ThemeResourceType type;

    private String path;

    public ThemeResource(ThemeResourceType type, String path) {
        this.type = type;
        this.path = path;
    }

    public String generateResourceImportString(){
        if(!path.startsWith("http://") && !path.startsWith("https://")){
            path = SessionContext.getContextPath()+ (path.startsWith("/")?path:("/"+path));
        }
        return type.generateResourceImportString(path);
    }

    public ThemeResourceType getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public static void main(String[] args) {
        String a = true ? "s":"2"+3;
        System.out.println(a);
    }
}
