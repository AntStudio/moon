package org.moon.dictionary.action;

import javax.annotation.Resource;

import org.moon.base.action.BaseAction;
import org.moon.dictionary.domain.Dictionary;
import org.moon.dictionary.repository.DictionaryRepository;
import org.moon.dictionary.service.DictionaryService;
import org.moon.message.WebResponse;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.support.spring.annotation.FormParam;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Gavin
 * @date 2014-05-08
 */
@Controller
@RequestMapping("/dictionary")
public class DictionaryAction extends BaseAction{
@Resource
	SqlSessionFactoryBean sessionFactoryBean;
	@Resource
	private DictionaryRepository dictionaryRepository;
	@Resource
	private DictionaryService dictionaryService;
	@Get("")
	@MenuMapping(code="platform_8",name="数据字典",parentCode="platform",url="/dictionary")
	public ModelAndView showDictionaryPage(){
		return new ModelAndView("pages/dictionary/dictionary");
	}
	
	@Post("/add")
	public @ResponseBody WebResponse addDictionary(@FormParam("dictionary") Dictionary dictionary){
		 dictionaryRepository.save(dictionary);
		 return WebResponse.build();
	}
	
	@Post("/update")
	public @ResponseBody WebResponse updateDictionary(@FormParam("dictionary") Dictionary dictionary){
		 dictionaryRepository.update(dictionary);
		 return WebResponse.build();
	}
	
	@Post("/delete")
	public @ResponseBody WebResponse deleteDictionary(@RequestParam("ids")Long[] ids){
		 dictionaryRepository.delete(Dictionary.class, ids);
		 return WebResponse.build();
	}
	
	@Get("/list")
	public @ResponseBody WebResponse listDictionary() throws Exception{
		 return WebResponse.build().setResult(dictionaryService.list());
	}
	
}
