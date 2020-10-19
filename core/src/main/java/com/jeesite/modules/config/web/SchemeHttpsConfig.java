/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.config.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * 将请求协议转换为 https
 * @author ThinkGem
 * @version 2020年1月21日
 */
@Configuration
@ConditionalOnProperty(name="server.schemeHttps", havingValue="true", matchIfMissing=false)
public class SchemeHttpsConfig {

	@Bean
	public FilterRegistrationBean<Filter> schemeFilterRegistrationBean() {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		bean.setFilter(new Filter() {
			
			@Override
			public void init(FilterConfig filterConfig) throws ServletException {}

			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
				chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) request) {
					
					@Override
					public String getScheme() {
						return "https";
					}

					@Override
					public StringBuffer getRequestURL() {
						StringBuffer sb = super.getRequestURL();
						if ("http:".equals(sb.substring(0, 5))){
							return sb.replace(0, 5, "https:");
						}else{
							return sb;
						}
					}
					
				}, response);
			}

			@Override
			public void destroy() {}
		});
		bean.addUrlPatterns("/*");
		return bean;
	}

}
