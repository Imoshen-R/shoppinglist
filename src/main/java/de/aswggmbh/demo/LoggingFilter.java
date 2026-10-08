package de.aswggmbh.demo;

import java.io.IOException;
import java.time.LocalDateTime;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebFilter;

@WebFilter( urlPatterns = {"/*"} )
public class LoggingFilter implements Filter {
  public void doFilter(ServletRequest req, ServletResponse resp, 
      FilterChain chain) throws IOException, ServletException {
    try {
      System.out.println( LocalDateTime.now() + " from " + 
                            req.getRemoteHost());
      chain.doFilter(req, resp);
    } finally {
      HttpServletResponse httpResp = (HttpServletResponse) resp;
      System.out.println( LocalDateTime.now() + " status " + 
                            httpResp.getStatus() );
} } }