package com.allarphoto.server;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class ResponseHeaderFilter implements Filter {
	FilterConfig fc;

  public void doFilter(ServletRequest request, 
		  ServletResponse response, FilterChain filterChain) 
  			throws IOException, ServletException

  {  
	  HttpServletResponse httpResponse = (HttpServletResponse) response;
	  // set the provided HTTP response parameters
	  for (Enumeration e=fc.getInitParameterNames();
	      e.hasMoreElements();) 
	  {
	    String headerName = (String)e.nextElement();
	    httpResponse.addHeader(headerName,
	               fc.getInitParameter(headerName));
	  }
	  // pass the request/response on
	  filterChain.doFilter(request, httpResponse);
  }  

  public void init(FilterConfig filterConfig) {
    this.fc = filterConfig;
  }

  public void destroy() {
    this.fc = null;
  }
  
}