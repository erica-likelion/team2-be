package likelion.hackerthon.grocering.setting.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class GuestAuthenticationFilter implements Filter {
    private final List<String> WHITELIST = Arrays.asList(
            "/guest/session",
            "/renew/session",
            "/swagger-ui/index.html"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        HttpSession session = httpRequest.getSession(false);
        String path = httpRequest.getRequestURI();

        for (String whiteList : WHITELIST) {
            if (path.equals(whiteList)|| path.startsWith("/swagger")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        if (session == null) {
            // 401 에러를 응답하고 중단
            log.info("Guest Authentication Filter fail because session is null");
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Guest session required");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
