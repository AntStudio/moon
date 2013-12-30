package org.antstudio.moon.test.eventfirer;

import javax.annotation.Resource;

import com.reeham.component.ddd.annotation.Model;
import com.reeham.component.ddd.message.DomainMessage;
import com.reeham.component.ddd.message.EventMessageFirer;

/**
 * @author Gavin
 * @Date Dec 30, 2013 10:50:13 AM
 */
@Model
public class User extends BaseDomain{

    @Resource
    public EventMessageFirer eventMessageFirer;
    
    public void test(){
        eventMessageFirer.fireDisruptorEvent("usersave", new DomainMessage("测试而已"));
    }
    
}
