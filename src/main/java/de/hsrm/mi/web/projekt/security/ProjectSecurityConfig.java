package de.hsrm.mi.web.projekt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration @EnableWebSecurity
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private ProjektUserDetailService userDetailsService;

    @Bean PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();   
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder authmanagerbuilder) throws Exception {
        PasswordEncoder pwenc = passwordEncoder();

        authmanagerbuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());

        authmanagerbuilder.inMemoryAuthentication()
            .withUser("friedfert")
            .password(pwenc.encode("dingdong"))
            .roles("USER")
        .and()
            .withUser("joghurta")
            .password(pwenc.encode("chefin")) 
            .roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {        
        http.authorizeRequests()
            .antMatchers("/registrieren").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers("/api/**").permitAll()
            .antMatchers("/stompbroker").permitAll()
            .antMatchers("/error").permitAll()
        .and() 
            .formLogin()
            .defaultSuccessUrl("/benutzerprofil")
            .permitAll()
        .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .permitAll();

        http.csrf().ignoringAntMatchers("/h2-console/**");
        http.headers().frameOptions().disable();
    }
}
