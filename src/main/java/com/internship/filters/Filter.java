package com.internship.filters;

import com.internship.controllers.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class Filter extends OncePerRequestFilter {
    @Autowired
    private LocationService locationService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String outOfJSON = request.getHeader("key");
        if(request.getRequestURI().equals("/location"))
        {
            if(outOfJSON==null || !locationService.checkingOnAvailabilityInDB(outOfJSON))
                response.setStatus(401);
            else
                filterChain.doFilter(request,response);
        }
        else
            filterChain.doFilter(request,response);
    }
}
