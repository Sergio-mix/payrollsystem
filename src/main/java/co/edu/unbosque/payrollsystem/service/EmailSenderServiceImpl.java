package co.edu.unbosque.payrollsystem.service;

import co.edu.unbosque.payrollsystem.component.MailComponent;
import co.edu.unbosque.payrollsystem.component.UrlComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import co.edu.unbosque.payrollsystem.model.Record;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service(value = "EmailSenderServiceImpl")
public class EmailSenderServiceImpl {
    @Autowired
    private MailComponent mail;
    @Autowired
    private UrlComponent urlComponent;

    @Autowired
    private UserHistoryServiceImpl userHistoryService;
    @Value("${auth.email.credentials.url}")
    private String credentialsUrl;

    @Value("${auth.email.credentials.subject}")
    private String credentialsSubject;

    @Value("${auth.email.credentials.templete}")
    private String credentialsTemplete;

    @Value("${auth.email.recovery_code.url}")
    private String recoveryCodeUrl;

    @Value("${auth.email.recovery_code.subject}")
    private String recoveryCodeSubject;

    @Value("${auth.email.recovery_code.templete}")
    private String recoveryCodeTemplete;

    public void sendMailCredentials(final String email, final String password, final String username, final String name, final HttpServletRequest url) {
        Map<String, Object> model = new HashMap<>();
        model.put("userName", username);
        model.put("password", password);
        model.put("name", name);
        model.put("url", urlComponent.getCurrentUrl(url) + credentialsUrl);
        mail.sendEmailHtml(email, credentialsSubject, model, credentialsTemplete);
        userHistoryService.save(username, new Record("GET", "send credentials"), null, url);//Save record
    }

    public void sendMailRecoveryCode(final String email, final String username, final Integer id, final String code, final HttpServletRequest url, final int minutes) {
        Map<String, Object> model = new HashMap<>();
        model.put("recovery", code);
        model.put("minutes", minutes);
        model.put("url", urlComponent.getCurrentUrl(url) + recoveryCodeUrl + id + "/" + email);
        mail.sendEmailHtml(email, recoveryCodeSubject, model, recoveryCodeTemplete);
        userHistoryService.save(username, new Record("GET", "password reset email"), null, url);//Save record
    }
}