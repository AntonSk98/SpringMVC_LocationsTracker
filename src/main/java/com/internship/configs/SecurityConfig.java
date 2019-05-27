package com.internship.configs;

import com.internship.filters.Filter;
import com.internship.services.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    Filter filter;
    @Autowired
    private UserDetailsServiceImplementation userDetailsServiceImplementation;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception{
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/location", "/install", "/ui/index","/static/**","/res/static/**").permitAll()
                .antMatchers("/ui/main").hasAnyRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/ui/authorize") //the login form
                .defaultSuccessUrl("/ui/main") //default and successful page
                .usernameParameter("username") //login parameter from an html page
                .passwordParameter("password") //password parameter from an html page
                .loginPage("/ui/authorize").permitAll()
                .failureUrl("/ui/403Page").permitAll()
                .and()
                .logout();

        http.addFilterBefore(filter, ChannelProcessingFilter.class);
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

       auth.userDetailsService(userDetailsServiceImplementation).passwordEncoder(bCryptPasswordEncoder()); //тут надо добавить аутентифицированных пользователей
    }


}
