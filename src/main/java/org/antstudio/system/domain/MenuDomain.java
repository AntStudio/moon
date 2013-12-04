package org.antstudio.system.domain;

import org.springframework.stereotype.Component;

import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.Send;
import com.reeham.component.ddd.message.DomainMessage;

/**
 * 
 * @author gavin
 * @date 2012-11-25 下午3:00:11
 */
@Component
@Introduce("message")
public class MenuDomain {
	
	 @Send(value="loadMenu")
	public DomainMessage getMenu(int id){
		return new DomainMessage(11);
	}
	
}
