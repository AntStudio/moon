package org.moon.support.theme;

import java.util.ArrayList;
import java.util.Collection;

/**
 * website theme
 * @author GavinCook
 * @since 1.0.0
 **/
public class Theme {

    private String name;

    private String description;

    private String indexPage;

    private Collection<ThemeResource> themeResources;

    private boolean isDefault = false;

    /**
     * the handle page for theme, if present, all the pages except index page,would redirect to this handle page
     */
    private String themeHandlePage;

    public Theme(String name, String description, String indexPage, boolean isDefault) {
        this.name = name;
        this.description = description;
        this.indexPage = indexPage;
        this.isDefault = isDefault;
    }

    public Theme(String name, String description, String indexPage, Collection<ThemeResource> themeResources, boolean isDefault) {
        this.name = name;
        this.description = description;
        this.indexPage = indexPage;
        this.themeResources = themeResources;
        this.isDefault = isDefault;
    }

    /**
     * add a resource into theme
     * @param themeResource the resource which need be added
     */
    public void addResource(ThemeResource themeResource){
        if(this.themeResources == null){
            this.themeResources = new ArrayList<>();
        }
        this.themeResources.add(themeResource);
    }

    /**
     * generate the theme resources import string for the special <code>type</code> resources,
     * if the <code>type</code> is <code>null</code>, would generate all resources import string
     * @param type the theme resource type which need generate
     * @return resouce import string
     */
    public String generateResourceImportString(ThemeResourceType type){
        if(themeResources == null){
            return "";
        }
        StringBuilder importStr = new StringBuilder();
        themeResources.stream().filter(themeResource -> type == null || type == themeResource.getType())
                .forEach(themeResource ->importStr.append(themeResource.generateResourceImportString()) );
        return importStr.toString();
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIndexPage() {
        return indexPage;
    }

    public void setIndexPage(String indexPage) {
        this.indexPage = indexPage;
    }

    public Collection<ThemeResource> getThemeResources() {
        return themeResources;
    }

    public void setThemeResources(Collection<ThemeResource> themeResources) {
        this.themeResources = themeResources;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getThemeHandlePage() {
        return themeHandlePage;
    }

    public void setThemeHandlePage(String themeHandlePage) {
        this.themeHandlePage = themeHandlePage;
    }
}
