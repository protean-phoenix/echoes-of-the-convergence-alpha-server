package com.teamechoes.echoesoftheconvergence.zuulgateway;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.context.RequestContext;

@Component
public class ZuulAuthFilter implements Filter {
	@Autowired
	private DiscoveryClient discoveryClient;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest r1 = (HttpServletRequest) request;
		String auth_token = r1.getHeader("Authorization");

		if(auth_token != null && !r1.getRequestURI().equals("/accounts/login")) {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.TEXT_PLAIN);
			HttpEntity<String> httpEntity = new HttpEntity<String>(auth_token, headers);

			List<ServiceInstance> instances = discoveryClient.getInstances("accounts");
			int port = instances.get(0).getPort();
			RequestContext context = RequestContext.getCurrentContext();
			try {
				ResponseEntity<String> auth_res = restTemplate.postForEntity("http://localhost:"+ port +"/validate", httpEntity, String.class);
				String result = auth_res.getBody();
				JSONObject claims = new JSONObject(result);
				String user_id = claims.getString("sub");
				
				context.addZuulRequestHeader("user-id", user_id);
			}catch(Exception e) {
				e.printStackTrace();
				context.addZuulRequestHeader("user-id", null);
			}
		}
		
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}
}
