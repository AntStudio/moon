package org.moon.common.action;

import org.moon.rest.annotation.Get;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * common controller, now this one only contains the page redirect
 *
 * @author GavinCook
 * @since 1.0.0
 */
@Controller
public class CommonAction {

    @Get("/turn")
    public ModelAndView turn(@RequestParam("page") String page) {
        return new ModelAndView(page);
    }

}
