package com.hx.logchain.toolkit.filter;

//@Component
//@Order(-1)
//@WebFilter(urlPatterns = "/*", filterName = "logTraceFilter")
public class LogTraceFilter {//implements Filter {

//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }

//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest)servletRequest;
//        String traceId = Optional.ofNullable(request.getHeader(TRACE_ID)).orElse(MDCThreadUtil.generateTraceId());
//        MDC.put(TRACE_ID, traceId);
//        filterChain.doFilter(request,servletResponse);
//    }
//
//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }
}
