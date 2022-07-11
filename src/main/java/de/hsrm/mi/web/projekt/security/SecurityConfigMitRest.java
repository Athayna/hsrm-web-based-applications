package de.hsrm.mi.web.projekt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import de.hsrm.mi.web.projekt.jwt.JwtAuthorizationFilter;

@EnableWebSecurity
public class SecurityConfigMitRest {
    /**
     * Prio 1: Konfiguration für REST-API, nur für Endpunkte unter /api/**, stateless
     *         und Authentifizierung per JWT-Token über JwtAuthorizationFilter
     */
    @Configuration
    @Order(1)
    public static class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        JwtAuthorizationFilter jwtAuthorizationFilter;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // http.antMatcher() (Matcher, nicht Matcher*s*) beschränkt diese Konfiguration 
            // ausschließlich auf Pfade unterhalb /api
            http.antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().denyAll()
            .and()
                // keine Security-Sessions für zustandsloses REST-APIs (anders als bei z.B. WebMVC)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                // eigenen jwtAuthorizationFilter (s.o.) in Spring-Filterkette 
                // (z.B.) vor Standardfilter UsernamePasswordAuthenticationFilter einfügen
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                // CSRF-Schutz-Tokens stören für REST-APIs
                .csrf().disable()
            ;
        }
    }

    /**
     * Prio 2: Konfiguration für übrige Aspekte und insb. WebMVC-Anwendung
     *         hier können Sie Ihre bisherige Sicherheitskonfiguration integrieren
     */
    @Configuration
    @Order(2)
    public static class ProjektSecurityConfig extends WebSecurityConfigurerAdapter {

        @Bean PasswordEncoder passwordEncoder() { // @Bean -> Encoder woanders per @Autowired abrufbar
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Autowired
        private ProjektUserDetailService userDetailsService;

        
        @Override
        protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
            PasswordEncoder pwenc = passwordEncoder();

            authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

            authenticationManagerBuilder.inMemoryAuthentication()
                .withUser("friedfert")
                .password(pwenc.encode("dingdong"))
                .roles("USER")
            .and()
                .withUser("joghurta")
                .password(pwenc.encode("chefin")) 
                .roles("ADMIN");
            
        }

        @Override
        protected void configure (HttpSecurity http) throws Exception {
            http.authorizeRequests()
                .antMatchers("/benutzerprofil/**").authenticated()
                .antMatchers("/registrieren").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/stompbroker").permitAll()
                .antMatchers("/error").permitAll()
                .anyRequest().permitAll()
            .and()
                .formLogin()
                .defaultSuccessUrl("/benutzerprofil")
                .permitAll()
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
            .and()
                .headers().frameOptions().disable()
            .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")
            ;
        }

        
        /*
         * "AuthenticationManager" über Factory-Methode @Autowired'bar machen,
         * wird z.B. in JwtLoginController verwendet;
         * also in Oberklasse abrufbaren Auth.Manager als injizierbare 
         * @Bean auch anderen Komponenten verfügbar machen.
         */
        @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }
}