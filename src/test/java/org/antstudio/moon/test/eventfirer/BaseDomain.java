package org.antstudio.moon.test.eventfirer;

import com.reeham.component.ddd.annotation.Model;

/**
 * @author Gavin
 * @Date Dec 30, 2013 10:48:39 AM
 */
@Model
public class BaseDomain{

    public void save(){
       System.out.println(this.getClass().getSimpleName().toLowerCase());
    }
    
}
