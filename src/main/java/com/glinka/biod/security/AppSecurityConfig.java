package com.glinka.biod.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String DEF_USERS_BY_USERNAME_QUERY = "select username, password, enabled " + "from users " + "where username = ?";
    public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY = "select u.username, a.authority from users u , authorities a  where u.username = ? and u.id = a.users";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(DEF_USERS_BY_USERNAME_QUERY)
                .authoritiesByUsernameQuery(DEF_AUTHORITIES_BY_USERNAME_QUERY);
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

     @Bean
     public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**").permitAll()
                .antMatchers("/login.html**", "/register**").permitAll()
                .antMatchers("/**").authenticated()
//                .antMatchers("/h2", "/h2**").permitAll()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/authenticate")
                .defaultSuccessUrl("/main.html", true)
                .permitAll()
                .and()
                .logout().permitAll()
                .deleteCookies("JSESSIONID");

        //TODO
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
//        http.csrf().disable();
//        http.headers().frameOptions().disable();

    }
}
