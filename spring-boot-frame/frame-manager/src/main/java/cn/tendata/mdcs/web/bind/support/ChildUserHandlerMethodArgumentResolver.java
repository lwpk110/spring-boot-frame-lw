package cn.tendata.mdcs.web.bind.support;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import cn.tendata.cas.client.security.core.userdetails.LoginUser;
import cn.tendata.cas.client.security.core.userdetails.LoginUser.ChildUser;
import cn.tendata.mdcs.web.bind.annotation.ChildUserVariable;
import cn.tendata.mdcs.web.util.LoginUserUtils;

public class ChildUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        if(this.supportsParameter(methodParameter)){
            Principal principal = webRequest.getUserPrincipal();
            LoginUser loginUser = (LoginUser) ((Authentication) principal).getPrincipal();
            if (loginUser != null) {
                ChildUserVariable childUserVariable = methodParameter.getParameterAnnotation(ChildUserVariable.class);
                Map<String, String> pathVariables = getPathVariables(webRequest);
                long childUserId = NumberUtils.parseNumber(pathVariables.get(childUserVariable.value()), Long.class);
                ChildUser childUser = LoginUserUtils.getChildUser(loginUser, childUserId);
                if(childUser != null){
                    return childUser;
                }
            }
        }
        return WebArgumentResolver.UNRESOLVED;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(ChildUserVariable.class) != null
                && methodParameter.getParameterType().equals(ChildUser.class);
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, String> getPathVariables(NativeWebRequest webRequest) {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        return (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }
}
