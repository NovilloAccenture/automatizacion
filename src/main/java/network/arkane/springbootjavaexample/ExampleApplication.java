package network.arkane.springbootjavaexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;

@SpringBootApplication
@EnableOAuth2Client
public class ExampleApplication extends WebSecurityConfigurerAdapter {

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
            .csrf().disable()
            .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    private Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter arkaneFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/arkane");
        OAuth2RestTemplate facebookTemplate = arkaneTemplate();
        arkaneFilter.setRestTemplate(facebookTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(arkaneResource().getUserInfoUri(), arkane().getClientId());
        tokenServices.setRestTemplate(facebookTemplate);
        arkaneFilter.setTokenServices(tokenServices);
        return arkaneFilter;
    }

    @Bean
    public OAuth2RestTemplate arkaneTemplate() {
        return new OAuth2RestTemplate(arkane(), oauth2ClientContext);
    }

    @Bean
    @ConfigurationProperties("arkane.client")
    public AuthorizationCodeResourceDetails arkane() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("arkane.resource")
    public ResourceServerProperties arkaneResource() {
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }
}

