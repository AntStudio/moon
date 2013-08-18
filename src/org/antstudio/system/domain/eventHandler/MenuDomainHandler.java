package org.antstudio.system.domain.eventHandler;

import org.springframework.stereotype.Component;

import com.reeham.component.ddd.annotation.OnEvent;

@Component
public class MenuDomainHandler {

	@OnEvent(value="loadMenu")
	public Object getMenu(Integer id){
		System.out.println("事件处理"+id+"呵呵");
		return "你妹";
	}
	
}
