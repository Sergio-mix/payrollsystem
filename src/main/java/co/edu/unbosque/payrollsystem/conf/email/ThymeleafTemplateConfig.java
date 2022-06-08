package co.edu.unbosque.payrollsystem.conf.email;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import static java.nio.charset.StandardCharsets.UTF_8;

@Data
@Configuration
public class ThymeleafTemplateConfig {

    @Value("${spring.thymeleaf.prefix}")
    private String prefix;

    @Value("${spring.thymeleaf.suffix}")
    private String suffix;

    /**
     * Configure Thymeleaf template resolver
     *
     * @return SpringResourceTemplateResolver
     */
    @Bean
    public SpringTemplateEngine springTemplateEngine() {
        final var templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }

    /**
     * Configure Thymeleaf template resolver
     *
     * @return SpringResourceTemplateResolver
     */
    @Bean
    public SpringResourceTemplateResolver htmlTemplateResolver() {
        final var resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix(prefix);
        resolver.setSuffix(suffix);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(UTF_8.name());
        return resolver;
    }
}
