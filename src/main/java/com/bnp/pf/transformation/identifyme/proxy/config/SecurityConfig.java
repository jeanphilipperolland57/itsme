package com.bnp.pf.transformation.identifyme.proxy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ClientRegistrationRepository clientRegistrationRepository;

    private final String requestUri;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository, @Value("${spring.security.oauth2.client.registration.itsme.requestUri}") String requestUri) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.requestUri = requestUri;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //.antMatchers("/","/login","/login/itsme", "/h2-console").permitAll()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .authorizationRequestResolver(
                        new CustomAuthorizationRequestResolver(
                                this.clientRegistrationRepository,requestUri))
                .and()
                .loginPage("/login")
                .defaultSuccessUrl("/login/oauth2/code/itsme")
                .failureUrl("/error")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .csrf().disable();
    }

   /* @Bean
    public OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver() {
        var defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorize/{registrationId}");
        return new CustomAuthorizationRequestResolver(defaultResolver, requestUri);
    }*/
}
