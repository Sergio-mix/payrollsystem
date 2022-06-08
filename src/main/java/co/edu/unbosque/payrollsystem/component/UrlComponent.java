package co.edu.unbosque.payrollsystem.component;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Data
public class UrlComponent {
    /**
     * Get the url of the server
     *
     * @param request HttpServletRequest
     * @return String url
     */
    public String getCurrentUrl(final HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        url.replace(url.length() - uri.length(), url.length(), "");
        return url.toString();
    }

    /**
     * Get the IP of the client
     *
     * @param request HttpServletRequest
     * @return String ip
     */
    public String getIPAddress(final HttpServletRequest request) {
        try {
            String remoteAddress = "";
            if (request != null) {
                remoteAddress = request.getHeader("X-Forwarded-For");
                if (remoteAddress == null || "".equals(remoteAddress)) {
                    remoteAddress = request.getRemoteAddr();
                }
            }
            remoteAddress = remoteAddress != null && remoteAddress.contains(",") ?
                    remoteAddress.split(",")[0] : remoteAddress;
            return remoteAddress;
        } catch (Exception e) {
            return null;
        }
    }
}
