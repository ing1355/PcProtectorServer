package oms.pc_protector.securityConfig;

import lombok.RequiredArgsConstructor;
import oms.pc_protector.jwt.ClientPrincipalDetailService;
import oms.pc_protector.jwt.JwtAuthenticationFilter;
import oms.pc_protector.jwt.JwtAuthorizationFilter;
import oms.pc_protector.jwt.ManagerPrincipalDetailsService;
import oms.pc_protector.restApi.client.service.ClientService;
import oms.pc_protector.restApi.login.mapper.LoginMapper;
import oms.pc_protector.restApi.login.service.LoginService;
import oms.pc_protector.restApi.manager.service.ManagerService;
import org.springframework.boot.autoconfigure.security.servlet.SpringBootWebSecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginMapper loginMapper;

    private final ManagerPrincipalDetailsService managerPrincipalDetailsService;
    private final ClientPrincipalDetailService clientPrincipalDetailService;
    private final ManagerService managerService;

   /* public SecurityConfig(ManagerService managerService,
                          ManagerPrincipalDetailsService managerPrincipalDetailsService) {
        this.managerService = managerService;
        this.managerPrincipalDetailsService = managerPrincipalDetailsService;
    }*/


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.clientPrincipalDetailService);
        daoAuthenticationProvider.setUserDetailsService(this.managerPrincipalDetailsService);

        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // remove csrf and state in session because in jwt we do not need them
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // add jwt filters (1. authentication, 2. authorization)
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), this.managerService, this.loginMapper))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(),  this.managerService, this.loginMapper))
                .authorizeRequests()
                // configure access rules
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/login/client").permitAll()
                .antMatchers(HttpMethod.PUT, "/v1/manager/firstlogin").permitAll()
                .antMatchers(HttpMethod.PUT, "/v1/manager/lock").permitAll()
//                .antMatchers(HttpMethod.GET,"/v1/client").hasRole("MANAGER")
                .antMatchers("/v1/client/**").hasRole("CLIENT")
//                .antMatchers("/v1/client/**").permitAll()
                .antMatchers("/v1/**").hasRole("MANAGER")
                .antMatchers("/v1/**").authenticated();
//                .antMatchers("/api/public/admin/*").hasRole("ADMIN")
//                .anyRequest().authenticated();
//                .anyRequest().permitAll();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
