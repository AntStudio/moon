package org.moon.test.eventfirer;

import com.reeham.component.ddd.annotation.Model;
import com.reeham.component.ddd.message.DomainMessage;

/**
 * @author Gavin
 * @Date Dec 30, 2013 10:50:13 AM
 */
@Model
public class User extends BaseDomain{
  
    public void test(){
        eventMessageFirer.fireDisruptorEvent("usersave", new DomainMessage("测试而已"));
    }

}
