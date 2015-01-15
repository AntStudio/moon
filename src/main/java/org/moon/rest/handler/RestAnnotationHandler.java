package org.moon.rest.handler;

import org.moon.rest.annotation.Delete;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.rest.annotation.Put;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class RestAnnotationHandler extends RequestMappingHandlerMapping{

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		RequestMappingInfo info = null;
		
		RequestMethod requestMethod =null;
		String[] value = null;
		if(method.isAnnotationPresent(Post.class)){
			Post postAnnotation = AnnotationUtils.findAnnotation(method, Post.class);
			requestMethod = RequestMethod.POST;
			value = postAnnotation.value();
		}else if(method.isAnnotationPresent(Get.class)){
			Get getAnnotation = AnnotationUtils.findAnnotation(method, Get.class);
			requestMethod = RequestMethod.GET;
			value = getAnnotation.value();
		}else if(method.isAnnotationPresent(Put.class)){
			Put putAnnotation = AnnotationUtils.findAnnotation(method, Put.class);
			requestMethod = RequestMethod.PUT;
			value = putAnnotation.value();
		}else if(method.isAnnotationPresent(Delete.class)){
			Delete deleteAnnotation = AnnotationUtils.findAnnotation(method, Delete.class);
			requestMethod = RequestMethod.DELETE;
			value = deleteAnnotation.value();
		}
		
		final String[] requestMappingValue = value;
		final RequestMethod requestMappingRequestMethod = requestMethod;
		if(requestMethod!=null){
			RequestMapping methodAnnotation  = new RequestMapping() {
				@Override
				public Class<? extends Annotation> annotationType() {
					return RequestMapping.class;
				}

                @Override
                public String name() {
                    return "";
                }

                @Override
				public String[] value() {
					return requestMappingValue;
				}
				
				@Override
				public String[] produces() {
					return new String[]{};
				}
				
				@Override
				public String[] params() {
					return new String[]{};
				}
				
				@Override
				public RequestMethod[] method() {
					return new RequestMethod[]{requestMappingRequestMethod};
				}
				
				@Override
				public String[] headers() {
					return new String[]{};
				}
				
				@Override
				public String[] consumes() {
					return new String[]{};
				}
			};
			
			RequestCondition<?> methodCondition = getCustomMethodCondition(method);
			info = createRequestMappingInfo(methodAnnotation, methodCondition);
			RequestMapping typeAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
			if (typeAnnotation != null) {
				RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
				info = createRequestMappingInfo(typeAnnotation, typeCondition).combine(info);
			}
		}
		return info;
	}
	
}
