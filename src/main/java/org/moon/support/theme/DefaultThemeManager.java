package org.moon.support.theme;

import org.moon.exception.ApplicationRunTimeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * the default theme manager
 * @author GavinCook
 * @since 1.0.0
 **/
public class DefaultThemeManager implements ThemeManager {

    private List<Theme> themes = new ArrayList<>();

    private Theme defaultTheme;

    @Override
    public void register(Theme theme) {
        if(theme == null){
            return;
        }
        if(theme.isDefault()){
            if(defaultTheme != null){
                throw new ApplicationRunTimeException("There already has a default theme, The default theme is : "
                        + defaultTheme.getName()+ ", current registering theme is : "+theme.getName());
            }
            defaultTheme = theme;
        }
        themes.add(theme);
    }

    @Override
    public Theme getTheme(String name) {
        if(name == null){
            return null;
        }

        Theme theme = null;
        for(Theme t : themes){
            if(name.equalsIgnoreCase(t.getName())){
                theme = t;
                break;
            }
        }

        return theme;
    }

    @Override
    public Theme getDefaultTheme() {
        if(defaultTheme == null){
            return themes.isEmpty() ? null : themes.get(0);
        }
        return defaultTheme;
    }

    @Override
    public Collection<Theme> getThemes() {
        return Collections.unmodifiableCollection(themes);
    }

    public void setThemes(List<Theme> themes){
        final Theme[] defaultThemes = new Theme[1];
        if(themes.stream().filter(theme->{
            if(theme.isDefault()){
                defaultThemes[0] = theme;
            }
            return theme.isDefault();
        }).count() < 2){
            this.themes = themes;
            this.defaultTheme = defaultThemes[0];
            return;
        }

        throw new ApplicationRunTimeException("Can only specify only one default theme");
    }
}
