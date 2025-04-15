package ru.aasmc.grpc;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.stereotype.Component;

@GlobalServerInterceptor
@Component
public class LoggingInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call,
                                                                 Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {
        return new LoggingListener<>(next.startCall(call, headers), call);
    }

    static class LoggingListener<ReqT, RespT> extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {

        private final ServerCall<ReqT, RespT> call;

        LoggingListener(ServerCall.Listener<ReqT> delegate,
                        ServerCall<ReqT, RespT> call) {
            super(delegate);
            this.call = call;
        }

        @Override
        public void onMessage(ReqT message) {
            log.info("Intercepted message {}", message);
            super.onMessage(message);
        }

        @Override
        public void onReady() {
            log.info("gRPC Server started");
            super.onReady();
        }
    }
}
