package co.edu.unbosque.payrollsystem.component;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Data
public class MailComponent {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Async
    public void sendEmailHtml(final String MailTo, final String subject, final Map<String, Object> props, final String templete) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Context context = new Context();
            context.setVariables(props);
            helper.setTo(MailTo);
            helper.setText(templateEngine.process(templete, context), true);
            helper.setSubject(subject);
            helper.setFrom(MailTo);
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
