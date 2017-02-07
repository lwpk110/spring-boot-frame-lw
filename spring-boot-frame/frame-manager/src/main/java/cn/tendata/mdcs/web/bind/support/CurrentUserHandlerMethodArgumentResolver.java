package cn.tendata.mdcs.web.bind.support;

import cn.tendata.cas.client.security.core.userdetails.LoginUser;
import cn.tendata.mdcs.data.domain.User;
import cn.tendata.mdcs.service.UserService;
import cn.tendata.mdcs.web.bind.annotation.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;

public class CurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserService userService;

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        if (this.supportsParameter(methodParameter)) {
            Principal principal = webRequest.getUserPrincipal();
            LoginUser loginUser = (LoginUser) ((Authentication) principal).getPrincipal();
            if (loginUser != null) {
                return userService.findById(loginUser.getUserId());
            }
        }
        return WebArgumentResolver.UNRESOLVED;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(CurrentUser.class) != null
                && methodParameter.getParameterType().equals(User.class);
    }
}
