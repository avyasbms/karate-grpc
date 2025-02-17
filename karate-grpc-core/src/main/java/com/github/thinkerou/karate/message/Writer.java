package com.github.thinkerou.karate.message;

import java.util.List;
import java.util.logging.Logger;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import io.grpc.stub.StreamObserver;

/**
 * Writer
 *
 * A StreamObserver which writes the contents of the received messages to an Output.
 * The messages are writting in a newline-separated json format.
 *
 * @author thinkerou
 */
public final class Writer<T extends Message> implements StreamObserver<T> {

    private static final Logger logger = Logger.getLogger(Writer.class.getName());

    private final JsonFormat.Printer jsonPrinter;
    private final List<Object> output;

    /**
     * Creates a new Writer which writes the messages it sees to the supplied Output.
     */
    public static <T extends Message> Writer<T> create(List<Object> output, JsonFormat.TypeRegistry registry) {
        return new Writer<>(JsonFormat.printer().usingTypeRegistry(registry), output);
    }

    Writer(JsonFormat.Printer jsonPrinter, List<Object> output) {
        this.jsonPrinter = jsonPrinter;
        this.output = output;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onNext(T message) {
        try {
            output.add(jsonPrinter.print(message));
        } catch (InvalidProtocolBufferException e) {
            logger.warning(e.getMessage());
        }
    }

}
