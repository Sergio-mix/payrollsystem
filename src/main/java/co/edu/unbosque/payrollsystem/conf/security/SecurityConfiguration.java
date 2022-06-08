package co.edu.unbosque.payrollsystem.conf.security;

import co.edu.unbosque.payrollsystem.component.JWTTokenHelperComponent;
import co.edu.unbosque.payrollsystem.service.CustomUserServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;

import static org.springframework.security.crypto.password.NoOpPasswordEncoder.*;

/**
 * security settings
 */
@Configuration
@EnableWebSecurity
@Data
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * user service
     */
    @Autowired
    private CustomUserServiceImpl userService;

    /**
     * authentication entry point
     */
    @Autowired
    private JWTTokenHelperComponent jWTTokenHelper;

    /**
     * authentication entry point
     */
    @Autowired
    private AuthenticationEntryPoint entryPoint;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    /**
     * password encoder
     *
     * @return password encoder
     */
    @Bean
    protected PasswordEncoder passwordEncoder() {
        final HashMap<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("noop", getInstance());
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put(null, new MessageDigestPasswordEncoder("MD5"));
        encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
        return new DelegatingPasswordEncoder("bcrypt", encoders);
    }

    /**
     * authentication manager
     *
     * @return authentication manager
     * @throws Exception exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * http security settings
     *
     * @param http http
     * @throws Exception exception
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
                .authenticationEntryPoint(entryPoint).and()
                .authorizeRequests((request) -> request.antMatchers("/auth/api/v1/*", "/", "/assets/*").permitAll()
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated())
                .addFilterBefore(new JWTAuthenticationFilter(userService, jWTTokenHelper),
                        UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable().cors().and().headers().frameOptions().disable();
    }
}