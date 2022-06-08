package co.edu.unbosque.payrollsystem.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parser;

/**
 * security token helper
 */
@Data
@Component
public class JWTTokenHelperComponent {

    /**
     * name authentication
     */
    @Value("${jwt.auth.app}")
    private String appName;

    /**
     * secretKey
     */
    @Value("${jwt.auth.secret_key}")
    private String secretKey;

    /**
     * expiration time
     */
    @Value("${jwt.auth.expires_in}")
    private int expiresIn;

    /**
     * ALGORITHM JWT
     */
    private static final SignatureAlgorithm ALGORITHM;

    static {
        ALGORITHM = SignatureAlgorithm.HS512;
    }


    /**
     * get all token claims
     *
     * @param token the token to be generated
     * @return the JWT Claims
     */
    private Claims getAllClaimsFromToken(final String token) {
        return parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    /**
     * get Username From Token
     *
     * @param token the token to be generated
     * @return the JWT token
     */
    public String getUsernameFromToken(final String token) {
        String username;
        final Claims claims = this.getAllClaimsFromToken(token);//get the payload from the token
        username = claims.getSubject();//getSubject() returns the subject of the JWT
        return username;
    }

    /**
     * generate token for the user
     *
     * @param username username of the user
     * @return the JWT token
     */
    public String generateToken(final String username) {
        return builder().setIssuer(appName).setSubject(username).setIssuedAt(new Date()).setExpiration(generateExpirationDate()).signWith(ALGORITHM, secretKey).compact();//compact() returns the compact JWT
    }

    /**
     * generate expiration date
     *
     * @return the expiration date
     */
    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + expiresIn * 100_000L);
    }

    /**
     * validate token
     *
     * @param token       the token to be validated
     * @param userDetails the user details
     * @return true if the token is valid
     */
    public Boolean validateToken(final String token, final UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username != null && username.equals(userDetails.getUsername())
                && !isTokenExpired(token) && userDetails.isEnabled();
    }

    /**
     * check if the token is expired
     *
     * @param token the token to be checked
     * @return true if the token is expired
     */
    public boolean isTokenExpired(final String token) {
        return getExpirationDate(token).before(new Date());//check if the token is expired
    }

    /**
     * get the expiration date
     *
     * @param token the token to be checked
     * @return the expiration date
     */
    private Date getExpirationDate(final String token) {
        Date expireDate;
        final Claims claims = this.getAllClaimsFromToken(token);//get the payload from the token
        expireDate = claims.getExpiration();//get the expiration date from the token
        return expireDate;
    }

    /**
     * get the token from the request
     *
     * @param request the request to be checked
     * @return the token
     */
    public String getToken(final HttpServletRequest request) {
        final String authHeader = getAuthHeaderFromHeader(request);//get the authorization header from the request
        return authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    }

    /**
     * get the authorization header from the request
     *
     * @param request the request to be checked
     * @return the authorization header
     */
    public String getAuthHeaderFromHeader(final HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}