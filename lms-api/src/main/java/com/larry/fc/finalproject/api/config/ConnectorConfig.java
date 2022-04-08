package com.larry.fc.finalproject.api.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class ConnectorConfig {

//
//        @Value("${server.port.http}")
//        private int serverPortHttp;
//
//        @Bean
//        public ServletWebServerFactory serverFactory() {
//            TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//            tomcat.addAdditionalTomcatConnectors(createStandardConnector());
//            return tomcat;
//        }
//        private Connector createStandardConnector() {
//            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//            connector.setPort(serverPortHttp);
//            return connector;
//        }
}

