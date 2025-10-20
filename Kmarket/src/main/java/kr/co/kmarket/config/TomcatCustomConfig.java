// kr.co.kmarket.config.TomcatCustomConfig.java (예시)

package kr.co.kmarket.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class TomcatCustomConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        // Tomcat Connector 설정을 커스터마이징합니다.
        factory.addConnectorCustomizers(connector -> {
            // URI 인코딩을 UTF-8로 강제 지정합니다.
            connector.setURIEncoding(StandardCharsets.UTF_8.toString());
        });
    }
}