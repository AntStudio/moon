package org.antstudio.pw.action;

import java.util.Map;

import javax.annotation.Resource;

import org.antstudio.pw.domain.TimeStatistics;
import org.antstudio.rbac.domain.User;
import org.antstudio.rbac.domain.annotation.LoginRequired;
import org.antstudio.support.session.SessionContext;
import org.antstudio.support.spring.annotation.FormParam;
import org.antstudio.utils.MessageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reeham.component.ddd.model.ModelContainer;

/**
 * 时间统计
 * @author Gavin
 * @date 2013-9-2 下午11:56:28
 */
@RequestMapping("/time")
@Controller
@LoginRequired
public class TimeStatisticsAction {

	
	@Resource
	private ModelContainer modelContainer;
	
	@RequestMapping("")
	public String timeStatisticsPage(){
		return "pages/pw/timeStatistics";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public Map<String,Object> timeStatisticsSave(@FormParam("timeStatistics") TimeStatistics timeStatistics){
		timeStatistics.setUser_id(Long.parseLong(SessionContext.getRequest().getSession().getAttribute(User.CURRENT_USER_ID).toString()));
		timeStatistics = modelContainer.enhanceModel(timeStatistics);
		timeStatistics.saveOrUpdate();
		return MessageUtils.getMapMessage(true,"timeStatistics",timeStatistics);
	}
}
