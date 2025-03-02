package ru.aasmc.grpc;

import io.grpc.stub.StreamObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import ru.aasmc.grpc.proto.ExampleRq;
import ru.aasmc.grpc.proto.ExampleRs;
import ru.aasmc.grpc.proto.ExampleServiceGrpc;

@Service
public class ExampleService extends ExampleServiceGrpc.ExampleServiceImplBase {

    private static Log log = LogFactory.getLog(ExampleService.class);

    @Override
    public void example(ExampleRq request, StreamObserver<ExampleRs> responseObserver) {
        log.info("Example request: " + request.getName());

        ExampleRs response = ExampleRs.newBuilder()
                .setCode(request.getCode())
                .setResponse("Response " + request.getName())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void exampleStream(ExampleRq request, StreamObserver<ExampleRs> responseObserver) {
        log.info("Example request: " + request.getName());
        int count = 0;
        while (count < 10) {
            ExampleRs response = ExampleRs.newBuilder()
                    .setCode(request.getCode())
                    .setResponse("Response " + request.getName() + " " + count)
                    .build();
            responseObserver.onNext(response);
            ++count;
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                responseObserver.onError(e);
                return;
            }
        }
        responseObserver.onCompleted();
    }
}
