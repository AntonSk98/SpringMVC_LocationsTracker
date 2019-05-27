package com.internship.configs;


import com.internship.controllers.location.BeanSimulator;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;


@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("com.internship")

public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private ApplicationContext applicationContext;
    @Bean
    @Description("Create SpringResourceTemplateResolver")
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(false);
        return templateResolver;
    }
    @Bean
    @Description("Create SpringTemplateEngine")
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }


    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(HikariDataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean
    public HikariDataSource setupHikariDataSource(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("org.postgresql.Driver");
        hikariDataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/internship");
        hikariDataSource.setUsername("postgres");
        hikariDataSource.setPassword("1234");
        return hikariDataSource;
    }
    @Bean
    public JdbcTemplate setupJdbcTemplate(HikariDataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Override
    @Description("указываем расположение всех ресурсов, которые будут использоваться для страниц. Это могут быть и каскадные таблицы стилей," +
            " и java-script файлы, изображения и прочее.")
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/res/static/");
    }
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

}
