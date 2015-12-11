package org.moon.tag;

import org.moon.tag.util.Constants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.io.Reader;

/**
 * used to move the scripts and css links into to head tag, it's useful when use jsp:include to contain sub page.
 * @author GavinCook
 * @since 1.0.0
 **/
public class OptimizeStructure extends BodyTagSupport {

    @Override
    public void doInitBody() throws JspException {
        super.doInitBody();
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            Reader reader = bodyContent.getReader();
            char[] data = new char[1024];
            int length;
            StringBuilder sb = new StringBuilder();
            while((length = reader.read(data)) != -1){
                sb.append(new String(data,0,length));
            }
            int start = sb.indexOf(Constants.REQUIRE_START);
            int end = sb.indexOf(Constants.REQUIRE_END);
            int startTagLength = Constants.REQUIRE_START.length();
            int endTagLength = Constants.REQUIRE_END.length();

            StringBuilder resources = new StringBuilder();
            while(start != -1 && end != -1){
                resources.append(sb.substring(start + startTagLength,end)).append("\n");
                sb.delete(start,end + endTagLength);
                start = sb.indexOf(Constants.REQUIRE_START);
                end = sb.indexOf(Constants.REQUIRE_END);
            }

            sb.insert(sb.indexOf("</head>"),resources);
            bodyContent.getEnclosingWriter().write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }
}
