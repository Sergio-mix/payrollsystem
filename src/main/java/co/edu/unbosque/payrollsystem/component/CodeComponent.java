package co.edu.unbosque.payrollsystem.component;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Data
public class CodeComponent {

    public String getRandomString(final Integer n) {
        String result = null;
        if (n != null && n > 0) {
            String uuid = UUID.randomUUID().toString();
            byte[] bytes = uuid.getBytes(StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                int index = ThreadLocalRandom.current().nextInt(0, bytes.length);
                sb.append(uuid.charAt(index));
            }
            result = sb.toString();
        }
        return result;
    }
}
