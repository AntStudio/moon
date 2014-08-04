package org.moon.test.eventfirer;

import com.reeham.component.ddd.model.ModelContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * @author Gavin
 * @Date Dec 30, 2013 10:51:19 AM
 */


@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration  
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring-servlet.xml","file:src/main/webapp/WEB-INF/applicationContext.xml" })  
public class EventFirerTest {
    @Resource
    private ModelContainer modelContainer;
    
    @Test
    public void test() throws Exception{
        User user = new User();
        modelContainer.enhanceModel(user);
        user.test();
    }
    
}
