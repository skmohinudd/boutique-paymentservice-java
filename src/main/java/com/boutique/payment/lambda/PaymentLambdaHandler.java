package com.boutique.payment.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.boutique.payment.dto.AuthorizePaymentRequest;
import com.boutique.payment.dto.PaymentResponse;
import com.boutique.payment.service.PaymentService;
import tools.jackson.databind.JsonNode;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

public final class PaymentLambdaHandler implements RequestStreamHandler {
    private final PaymentService service = LambdaSupport.bean(PaymentService.class);

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) {
        try {
            JsonNode event = LambdaSupport.readEvent(input);
            String method = LambdaSupport.method(event);
            String path = LambdaSupport.path(event);

            if ("POST".equals(method) && "/api/v1/payments/authorize".equals(path)) {
                AuthorizePaymentRequest request = LambdaSupport.validate(
                        LambdaSupport.JSON.readValue(
                                LambdaSupport.body(event),
                                AuthorizePaymentRequest.class
                        )
                );
                PaymentResponse response = service.authorize(request);
                int status = "AUTHORIZED".equals(response.status().name()) ? 201 : 402;
                LambdaSupport.respond(output, status, response);
                return;
            }

            if ("GET".equals(method) && path.startsWith("/api/v1/payments/")) {
                String rawId = LambdaSupport.pathParameter(event, "paymentId");
                if (rawId.isBlank()) rawId = path.substring("/api/v1/payments/".length());
                LambdaSupport.respond(output, 200, service.get(UUID.fromString(rawId)));
                return;
            }

            LambdaSupport.respond(output, 404, Map.of("message", "Payment route not found"));
        } catch (Throwable failure) {
            try {
                LambdaSupport.fail(output, failure, context);
            } catch (Exception responseFailure) {
                throw new RuntimeException(responseFailure);
            }
        }
    }
}
