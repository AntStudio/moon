package org.antstudio.moon.test.eventfirer;

import org.springframework.stereotype.Component;

import com.reeham.component.ddd.annotation.OnEvent;
/**
 * @author Gavin
 * @Date Dec 30, 2013 10:55:01 AM
 */
@Component
public class TestUserEventHandler {

    @OnEvent("usersave")
    public void save(){
        System.out.println("执行了");
    }
    
}
