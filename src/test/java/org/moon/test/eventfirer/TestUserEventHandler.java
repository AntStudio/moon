package org.moon.test.eventfirer;

import com.reeham.component.ddd.annotation.OnEvent;
import org.springframework.stereotype.Component;
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
