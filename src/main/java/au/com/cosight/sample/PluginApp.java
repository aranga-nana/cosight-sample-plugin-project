package au.com.cosight.sample;

import au.com.cosight.sdk.annotation.EnableCosightRuntimeContext;
import au.com.cosight.sdk.auth.external.oauth.ExternalOAuth2Credentials;
import au.com.cosight.sdk.auth.external.oauth.ExternalOauth2Token;
import au.com.cosight.sdk.plugin.runtime.CosightExecutionContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@EnableCosightRuntimeContext
public class PluginApp implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger("sample-plugin");

    @Autowired
    private CosightExecutionContext executionContext;

    public static void main(String[] args)  {
        logger.info("{}", UUID.randomUUID().toString());
        logger.info("TS ===>{}",new Date().getTime());
        SpringApplication.run(PluginApp.class,args);
    }

    @Override
    public void run(String... args) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        logger.info("plugin starting..");
        // execution context
        logger.info("EXECUTION CONTEXT {}",executionContext.getRuntimeInfo());

        final ExternalOAuth2Credentials credentials = ExternalOAuth2Credentials.getInstance();
        if (credentials.isValid()) {
            logger.info("CLIENT ID{}", credentials.getOauth2Config().getClientId());
            ExternalOauth2Token token = credentials.getToken();
            logger.info("ACCESS TOKEN {}", token.getAccessToken());

            try {


                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet("https://api.xero.com/connections");
                httpGet.setHeader("Authorization", "Bearer " + token.getAccessToken());
                CloseableHttpResponse response = httpClient.execute(httpGet);
                logger.info("{}",  EntityUtils.toString(response.getEntity()));

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

    }
}
