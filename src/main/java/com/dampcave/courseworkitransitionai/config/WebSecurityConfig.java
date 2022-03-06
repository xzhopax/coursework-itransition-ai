package com.dampcave.courseworkitransitionai.config;

import com.dampcave.courseworkitransitionai.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserAuthService userAuthService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userAuthService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Autowired
    public WebSecurityConfig(PasswordEncoder passwordEncoder, UserAuthService userAuthService) {
        this.passwordEncoder = passwordEncoder;
        this.userAuthService = userAuthService;
    }

    public WebSecurityConfig(boolean disableDefaults, PasswordEncoder passwordEncoder, UserAuthService userAuthService) {
        super(disableDefaults);
        this.passwordEncoder = passwordEncoder;
        this.userAuthService = userAuthService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/scripts/**")
                .antMatchers("/styles/**")
                .antMatchers("/images/**")
                .antMatchers("/img/**")
                .antMatchers("/fonts/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/register").permitAll()
                    .antMatchers("/films/**").permitAll()
                    .antMatchers("/films/film/**").permitAll()
                    .antMatchers("/**").authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .loginPage("/films").permitAll()
                    .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                    .permitAll();
    }
}
