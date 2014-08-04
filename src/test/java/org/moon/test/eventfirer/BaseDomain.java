package org.moon.test.eventfirer;

import com.reeham.component.ddd.message.EventMessageFirer;

import javax.annotation.Resource;

/**
 * @author Gavin
 * @Date Dec 30, 2013 10:48:39 AM
 */
public class BaseDomain{

    @Resource
    public EventMessageFirer eventMessageFirer;
    
    public void save(){
       System.out.println(this.getClass().getSimpleName().toLowerCase());
    }
    
}
