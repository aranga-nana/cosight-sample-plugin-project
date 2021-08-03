package au.com.cosight.sample;

import au.com.cosight.sdk.annotation.EnableCosightRuntimeContext;
import au.com.cosight.sdk.auth.external.oauth.ExternalOAuth2Credentials;
import au.com.cosight.sdk.auth.external.oauth.ExternalOauth2Token;
import au.com.cosight.sdk.plugin.runtime.CosightExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
@EnableCosightRuntimeContext
public class PluginApp implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger("sample-plugin");

    @Autowired
    private CosightExecutionContext executionContext;

    public static void main(String[] args)  {
        logger.info("{}", UUID.randomUUID().toString());
        SpringApplication.run(PluginApp.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("plugin starting..");
        // execution context
        logger.info("EXECUTION CONTEXT {}",executionContext.getRuntimeInfo());

        // External OAuth2 provider integration
        ExternalOAuth2Credentials credentials = ExternalOAuth2Credentials.getInstance();
        if (credentials.isValid()) {
            logger.info("CLIENT ID{}",credentials.getOauth2Config().getClientId());
            ExternalOauth2Token token = credentials.getToken();
            logger.info("ACCESS TOKEN {}",token.getAccessToken());
            logger.info("plugin end");
        }


    }
}
