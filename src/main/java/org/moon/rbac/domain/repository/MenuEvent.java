package org.moon.rbac.domain.repository;

import java.io.Serializable;

import org.moon.rbac.domain.Menu;
import org.moon.rbac.domain.Role;

import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.Send;
import com.reeham.component.ddd.message.DomainMessage;

/**
 * load the menu properties or some operations of menu self
 * MenuDomain link to database across the loader ,the process is:
 * <p>the MenuLoader fire a message to disruptor or future by @send(value)
 * -->then the corresponding handler(@OnEvent(value)) which value equals
 * -->handler call the repository component to handle -->return result if need</p>
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27
 */
@Introduce("message")
public class MenuEvent implements Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7539619209514636258L;

	/**
	 * get the sub menus by parent menu id,if parentId is <code>null</code>
	 * return the top menus
	 * @param userDomain
	 * @return
	 */
	@Send(value="getTopMenusByRole")
	public DomainMessage getTopMenusByRole(Role role){
		return new DomainMessage(role.getId());
	}
	
	@Send(value="getParentMenu")
	public DomainMessage getParent(Menu menu){
		return new DomainMessage(menu);		
	}
	
	@Send(value="saveOrUpdateMenu")
	public DomainMessage saveOrUpdate(Menu menu){
		return new DomainMessage(menu);
	}
	
}
