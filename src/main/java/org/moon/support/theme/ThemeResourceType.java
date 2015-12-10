package org.moon.support.theme;

/**
 * @author GavinCook
 * @since 1.0.0
 **/
public enum ThemeResourceType {
    JS("<script src=\"%s\" type=\"text/javascript\"></script>"),
    CSS("<link href=\"%s\" rel=\"stylesheet\" type=\"text/css\"/>");

    private String resourceTemplate;

    ThemeResourceType(String resourceTemplate){
        this.resourceTemplate = resourceTemplate;
    }

    public String generateResourceImportString(String path){
        return String.format(this.resourceTemplate, path);
    }
}
