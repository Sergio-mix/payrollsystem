package co.edu.unbosque.payrollsystem.component;

import lombok.Data;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The Class RestAuthenticationEntryPoint.
 */
@Component
@Data
public class RestAuthenticationEntryPointComponent implements AuthenticationEntryPoint {

    /**
     * Handle the authentication entry point.
     *
     * @param request       The request.
     * @param response      The response.
     * @param authException The exception.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()); // 401
    }
}