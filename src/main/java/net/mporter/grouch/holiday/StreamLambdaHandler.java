package net.mporter.grouch.holiday;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import lombok.extern.slf4j.Slf4j;
import net.mporter.grouch.holiday.error.InitializationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class StreamLambdaHandler implements RequestStreamHandler {
    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);
        } catch (ContainerInitializationException e) {
            log.error("Could not initialize Spring Boot application", e);
            throw new InitializationException("Could not initialize Spring Boot application", e);
        }
    }

    @Override
    public void handleRequest(final InputStream inputStream, final OutputStream outputStream, final Context context)
            throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }
}
