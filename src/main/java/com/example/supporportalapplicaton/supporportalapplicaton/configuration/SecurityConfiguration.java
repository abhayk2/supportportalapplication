package com.example.supporportalapplicaton.supporportalapplicaton.configuration;
import com.example.supporportalapplicaton.supporportalapplicaton.filter.JWTActionDeniedHandler;
import com.example.supporportalapplicaton.supporportalapplicaton.filter.JWTAuthenticationEntryPoint;
import com.example.supporportalapplicaton.supporportalapplicaton.filter.JWTAuthorizationFilter;
import com.example.supporportalapplicaton.supporportalapplicaton.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.supporportalapplicaton.supporportalapplicaton.constant.SecurityConstant.PUBLIC_URLS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JWTAuthorizationFilter jWTAuthorizationFilter;
    private JWTTokenProvider jwtTokenProvider;
   private JWTActionDeniedHandler jwtActionDeniedHandler;
   private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
   private UserDetailsService userDetailsService;
   private BCryptPasswordEncoder bCryptPasswordEncoder;

   @Autowired
   public SecurityConfiguration(JWTTokenProvider jwtTokenProvider, JWTActionDeniedHandler jwtActionDeniedHandler, JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint, @Qualifier("userDetailService") UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, JWTAuthorizationFilter jWTAuthorizationFilter) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtActionDeniedHandler = jwtActionDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
       this.jWTAuthorizationFilter = jWTAuthorizationFilter;
   }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and().authorizeRequests().antMatchers(PUBLIC_URLS).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(jwtActionDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .addFilterBefore(jWTAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }
}
