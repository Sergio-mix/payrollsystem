package co.edu.unbosque.payrollsystem.conf.security;

import co.edu.unbosque.payrollsystem.component.JWTTokenHelperComponent;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * The type Jwt authentication filter.
 */
@Data
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    /**
     * The {@link UserDetailsService}
     */
    @Autowired
    private final UserDetailsService user;

    /**
     * The {@link JWTAuthenticationFilter} constructor
     */
    @Autowired
    private final JWTTokenHelperComponent tokenHelper;

    /**
     * filter token
     */
    @Override
    protected void doFilterInternal(final @NotNull HttpServletRequest request, final @NotNull HttpServletResponse response, final @NotNull FilterChain filterChain) throws ServletException, IOException {
        final String authToken = tokenHelper.getToken(request);//get token from request
        final var userDetails = (null == authToken) ? null : user.loadUserByUsername(tokenHelper.getUsernameFromToken(authToken));//get user from username
        final var validateToken = userDetails != null && tokenHelper.validateToken(authToken, userDetails);//validate token

        if (validateToken) {
            final var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());// create authentication token
            authentication.setDetails(new WebAuthenticationDetails(request));// set details
            getContext().setAuthentication(authentication);// set security context
        }

        filterChain.doFilter(request, response);// continue filter chain
    }
}