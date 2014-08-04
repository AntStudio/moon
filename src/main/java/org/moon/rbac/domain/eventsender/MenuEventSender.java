package org.moon.rbac.domain.eventsender;

import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.Send;
import com.reeham.component.ddd.message.DomainMessage;
import org.moon.base.domain.eventsender.EventSender;
import org.moon.rbac.domain.Menu;

@Introduce("message")
public class MenuEventSender implements EventSender{

	@Send("menu/getByCode")
	public DomainMessage getParentMenu(Menu menu){
		return new DomainMessage(menu.getParentCode());
	}
	
	@Send("menu/getSubMenus")
	public DomainMessage getSubMenus(Menu menu){
		return new DomainMessage(menu.getId());
	}
	
}
