package org.moon.dictionary.action;

import org.moon.base.action.BaseAction;
import org.moon.dictionary.domain.Dictionary;
import org.moon.dictionary.repository.DictionaryRepository;
import org.moon.dictionary.service.DictionaryService;
import org.moon.message.WebResponse;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.core.spring.annotation.FormParam;
import org.moon.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Gavin
 * @date 2014-05-08
 */
@Controller
@RequestMapping("/dictionary")
public class DictionaryAction extends BaseAction {
	@Resource
	private DictionaryService dictionaryService;

	@Get("")
	@MenuMapping(code = "platform_8", name = "数据字典", parentCode = "platform", url = "/dictionary")
	public ModelAndView showDictionaryPage() {
		return new ModelAndView("pages/dictionary/dictionary");
	}

	@Post("/add")
	public @ResponseBody WebResponse addDictionary(@FormParam("dictionary") Dictionary dictionary) {
		dictionary.sync(dictionary.save());
		return WebResponse.build();
	}

	@Post("/update")
	public @ResponseBody WebResponse updateDictionary(@FormParam("dictionary") Dictionary dictionary) {
		dictionary.sync(dictionary.update());
		return WebResponse.build();
	}

	@Post("/delete")
	public @ResponseBody WebResponse deleteDictionary(@RequestParam("ids") Long[] ids) {
		dictionaryService.delete(ids,false);
		return WebResponse.build();
	}

	@Get("/list")
	public @ResponseBody WebResponse listDictionary(HttpServletRequest request,
        @RequestParam(value = "parentId",required = false)Integer parentId) throws Exception {
        Map paramMap = ParamUtils.getParamsMapForPager(request);
        paramMap.put("parentId",parentId);
		return WebResponse.build().setResult(dictionaryService.listForPage(DictionaryRepository.class,"listWithChildrenStatus",paramMap));
    }

}
