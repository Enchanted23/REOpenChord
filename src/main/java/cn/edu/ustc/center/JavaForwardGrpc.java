package cn.edu.ustc.center;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 0.15.0-SNAPSHOT)",
    comments = "Source: api.proto")
public class JavaForwardGrpc {

  private JavaForwardGrpc() {}

  public static final String SERVICE_NAME = "center.JavaForward";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.edu.ustc.center.ApiProtos.request_inquiry,
      cn.edu.ustc.center.ApiProtos.response_inquiry> METHOD_REQUEST_INQUIRY_FORWARD =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING,
          generateFullMethodName(
              "center.JavaForward", "request_inquiry_forward"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.edu.ustc.center.ApiProtos.request_inquiry.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.edu.ustc.center.ApiProtos.response_inquiry.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.edu.ustc.center.ApiProtos.request_syn,
      cn.edu.ustc.center.ApiProtos.response_syn> METHOD_REQUEST_SYN_FORWARD =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING,
          generateFullMethodName(
              "center.JavaForward", "request_syn_forward"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.edu.ustc.center.ApiProtos.request_syn.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.edu.ustc.center.ApiProtos.response_syn.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.edu.ustc.center.ApiProtos.request_push,
      cn.edu.ustc.center.ApiProtos.response_push> METHOD_REQUEST_PUSH_FORWARD =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING,
          generateFullMethodName(
              "center.JavaForward", "request_push_forward"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.edu.ustc.center.ApiProtos.request_push.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.edu.ustc.center.ApiProtos.response_push.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static JavaForwardStub newStub(io.grpc.Channel channel) {
    return new JavaForwardStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static JavaForwardBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new JavaForwardBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static JavaForwardFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new JavaForwardFutureStub(channel);
  }

  /**
   */
  public static interface JavaForward {

    /**
     */
    public void requestInquiryForward(cn.edu.ustc.center.ApiProtos.request_inquiry request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_inquiry> responseObserver);

    /**
     */
    public void requestSynForward(cn.edu.ustc.center.ApiProtos.request_syn request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_syn> responseObserver);

    /**
     */
    public void requestPushForward(cn.edu.ustc.center.ApiProtos.request_push request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_push> responseObserver);
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1469")
  public static abstract class AbstractJavaForward implements JavaForward, io.grpc.BindableService {

    @java.lang.Override
    public void requestInquiryForward(cn.edu.ustc.center.ApiProtos.request_inquiry request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_inquiry> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REQUEST_INQUIRY_FORWARD, responseObserver);
    }

    @java.lang.Override
    public void requestSynForward(cn.edu.ustc.center.ApiProtos.request_syn request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_syn> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REQUEST_SYN_FORWARD, responseObserver);
    }

    @java.lang.Override
    public void requestPushForward(cn.edu.ustc.center.ApiProtos.request_push request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_push> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REQUEST_PUSH_FORWARD, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return JavaForwardGrpc.bindService(this);
    }
  }

  /**
   */
  public static interface JavaForwardBlockingClient {

    /**
     */
    public java.util.Iterator<cn.edu.ustc.center.ApiProtos.response_inquiry> requestInquiryForward(
        cn.edu.ustc.center.ApiProtos.request_inquiry request);

    /**
     */
    public java.util.Iterator<cn.edu.ustc.center.ApiProtos.response_syn> requestSynForward(
        cn.edu.ustc.center.ApiProtos.request_syn request);

    /**
     */
    public java.util.Iterator<cn.edu.ustc.center.ApiProtos.response_push> requestPushForward(
        cn.edu.ustc.center.ApiProtos.request_push request);
  }

  /**
   */
  public static interface JavaForwardFutureClient {
  }

  public static class JavaForwardStub extends io.grpc.stub.AbstractStub<JavaForwardStub>
      implements JavaForward {
    private JavaForwardStub(io.grpc.Channel channel) {
      super(channel);
    }

    private JavaForwardStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected JavaForwardStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new JavaForwardStub(channel, callOptions);
    }

    @java.lang.Override
    public void requestInquiryForward(cn.edu.ustc.center.ApiProtos.request_inquiry request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_inquiry> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_REQUEST_INQUIRY_FORWARD, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void requestSynForward(cn.edu.ustc.center.ApiProtos.request_syn request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_syn> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_REQUEST_SYN_FORWARD, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void requestPushForward(cn.edu.ustc.center.ApiProtos.request_push request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_push> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_REQUEST_PUSH_FORWARD, getCallOptions()), request, responseObserver);
    }
  }

  public static class JavaForwardBlockingStub extends io.grpc.stub.AbstractStub<JavaForwardBlockingStub>
      implements JavaForwardBlockingClient {
    private JavaForwardBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private JavaForwardBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected JavaForwardBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new JavaForwardBlockingStub(channel, callOptions);
    }

    @java.lang.Override
    public java.util.Iterator<cn.edu.ustc.center.ApiProtos.response_inquiry> requestInquiryForward(
        cn.edu.ustc.center.ApiProtos.request_inquiry request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_REQUEST_INQUIRY_FORWARD, getCallOptions(), request);
    }

    @java.lang.Override
    public java.util.Iterator<cn.edu.ustc.center.ApiProtos.response_syn> requestSynForward(
        cn.edu.ustc.center.ApiProtos.request_syn request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_REQUEST_SYN_FORWARD, getCallOptions(), request);
    }

    @java.lang.Override
    public java.util.Iterator<cn.edu.ustc.center.ApiProtos.response_push> requestPushForward(
        cn.edu.ustc.center.ApiProtos.request_push request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_REQUEST_PUSH_FORWARD, getCallOptions(), request);
    }
  }

  public static class JavaForwardFutureStub extends io.grpc.stub.AbstractStub<JavaForwardFutureStub>
      implements JavaForwardFutureClient {
    private JavaForwardFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private JavaForwardFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected JavaForwardFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new JavaForwardFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_REQUEST_INQUIRY_FORWARD = 0;
  private static final int METHODID_REQUEST_SYN_FORWARD = 1;
  private static final int METHODID_REQUEST_PUSH_FORWARD = 2;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final JavaForward serviceImpl;
    private final int methodId;

    public MethodHandlers(JavaForward serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REQUEST_INQUIRY_FORWARD:
          serviceImpl.requestInquiryForward((cn.edu.ustc.center.ApiProtos.request_inquiry) request,
              (io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_inquiry>) responseObserver);
          break;
        case METHODID_REQUEST_SYN_FORWARD:
          serviceImpl.requestSynForward((cn.edu.ustc.center.ApiProtos.request_syn) request,
              (io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_syn>) responseObserver);
          break;
        case METHODID_REQUEST_PUSH_FORWARD:
          serviceImpl.requestPushForward((cn.edu.ustc.center.ApiProtos.request_push) request,
              (io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_push>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
        METHOD_REQUEST_INQUIRY_FORWARD,
        METHOD_REQUEST_SYN_FORWARD,
        METHOD_REQUEST_PUSH_FORWARD);
  }

  public static io.grpc.ServerServiceDefinition bindService(
      final JavaForward serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          METHOD_REQUEST_INQUIRY_FORWARD,
          asyncServerStreamingCall(
            new MethodHandlers<
              cn.edu.ustc.center.ApiProtos.request_inquiry,
              cn.edu.ustc.center.ApiProtos.response_inquiry>(
                serviceImpl, METHODID_REQUEST_INQUIRY_FORWARD)))
        .addMethod(
          METHOD_REQUEST_SYN_FORWARD,
          asyncServerStreamingCall(
            new MethodHandlers<
              cn.edu.ustc.center.ApiProtos.request_syn,
              cn.edu.ustc.center.ApiProtos.response_syn>(
                serviceImpl, METHODID_REQUEST_SYN_FORWARD)))
        .addMethod(
          METHOD_REQUEST_PUSH_FORWARD,
          asyncServerStreamingCall(
            new MethodHandlers<
              cn.edu.ustc.center.ApiProtos.request_push,
              cn.edu.ustc.center.ApiProtos.response_push>(
                serviceImpl, METHODID_REQUEST_PUSH_FORWARD)))
        .build();
  }
}
