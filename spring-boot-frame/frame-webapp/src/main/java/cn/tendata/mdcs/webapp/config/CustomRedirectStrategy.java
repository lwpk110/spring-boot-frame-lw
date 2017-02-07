package cn.tendata.mdcs.webapp.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.DefaultRedirectStrategy;

public class CustomRedirectStrategy extends DefaultRedirectStrategy {

    private final static String REQUESTED_HEADER = "XMLHttpRequest";
    private final static String ACCEPT_HEADER = "application/json";
    private final static int EXPIRED_CODE = 102;

    private final static String TPL = "{\"status\":\"%1$s\",\"success\":false}";

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        if (request.getHeader("accept").contains(ACCEPT_HEADER) || (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").contains(REQUESTED_HEADER))) {
            response.getWriter().write(this.getMsg(EXPIRED_CODE));
            response.flushBuffer();
            if (logger.isDebugEnabled()) {
                logger.debug("Redirecting to bower");
            }
        } else {
            super.sendRedirect(request, response, url);
        }
    }

    public String getMsg(int code) {
        return String.format(TPL, code);
    }
}
