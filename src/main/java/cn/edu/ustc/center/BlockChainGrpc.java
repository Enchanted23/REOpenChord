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
 * <pre>
 *说明：
 *receive_request_inquiry是第一种需要广播的模型
 *调用过程是 发起请求的python端构造request_inquiry, 调用JavaForward的reques_inquiry_forward, java传给对面的java之后，java调用BlockChain的receive_request_inquiry, python会返回一个respond_inquiry，其中id是无效的，java接收到之后要补上id，回复给请求端的java。再之后，请求端的java要把广播得到的回复放到一个队列里，通过stream response的方式返回给python.
 *receive_request_syn是第二种指定连接对象的模型
 *调用过程是 发起请求的python端构造request_syn, 调用JavaForward的request_syn_forward, java根据request的id域指示连接指定的java, 对面的java再调用BlockChain的receive_request_syn, 返回一个stream的response_syn, 最后java负责传递stream的response到请求端的python.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 0.15.0-SNAPSHOT)",
    comments = "Source: api.proto")
public class BlockChainGrpc {

  private BlockChainGrpc() {}

  public static final String SERVICE_NAME = "center.BlockChain";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.edu.ustc.center.ApiProtos.request_inquiry,
      cn.edu.ustc.center.ApiProtos.response_inquiry> METHOD_RECEIVE_REQUEST_INQUIRY =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "center.BlockChain", "receive_request_inquiry"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.edu.ustc.center.ApiProtos.request_inquiry.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.edu.ustc.center.ApiProtos.response_inquiry.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.edu.ustc.center.ApiProtos.request_syn,
      cn.edu.ustc.center.ApiProtos.response_syn> METHOD_RECEIVE_REQUEST_SYN =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING,
          generateFullMethodName(
              "center.BlockChain", "receive_request_syn"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.edu.ustc.center.ApiProtos.request_syn.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.edu.ustc.center.ApiProtos.response_syn.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.edu.ustc.center.ApiProtos.request_push,
      cn.edu.ustc.center.ApiProtos.response_push> METHOD_RECEIVE_REQUEST_PUSH =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "center.BlockChain", "receive_request_push"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.edu.ustc.center.ApiProtos.request_push.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.edu.ustc.center.ApiProtos.response_push.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BlockChainStub newStub(io.grpc.Channel channel) {
    return new BlockChainStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BlockChainBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new BlockChainBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static BlockChainFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new BlockChainFutureStub(channel);
  }

  /**
   * <pre>
   *说明：
   *receive_request_inquiry是第一种需要广播的模型
   *调用过程是 发起请求的python端构造request_inquiry, 调用JavaForward的reques_inquiry_forward, java传给对面的java之后，java调用BlockChain的receive_request_inquiry, python会返回一个respond_inquiry，其中id是无效的，java接收到之后要补上id，回复给请求端的java。再之后，请求端的java要把广播得到的回复放到一个队列里，通过stream response的方式返回给python.
   *receive_request_syn是第二种指定连接对象的模型
   *调用过程是 发起请求的python端构造request_syn, 调用JavaForward的request_syn_forward, java根据request的id域指示连接指定的java, 对面的java再调用BlockChain的receive_request_syn, 返回一个stream的response_syn, 最后java负责传递stream的response到请求端的python.
   * </pre>
   */
  public static interface BlockChain {

    /**
     */
    public void receiveRequestInquiry(cn.edu.ustc.center.ApiProtos.request_inquiry request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_inquiry> responseObserver);

    /**
     */
    public void receiveRequestSyn(cn.edu.ustc.center.ApiProtos.request_syn request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_syn> responseObserver);

    /**
     */
    public void receiveRequestPush(cn.edu.ustc.center.ApiProtos.request_push request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_push> responseObserver);
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1469")
  public static abstract class AbstractBlockChain implements BlockChain, io.grpc.BindableService {

    @java.lang.Override
    public void receiveRequestInquiry(cn.edu.ustc.center.ApiProtos.request_inquiry request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_inquiry> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_RECEIVE_REQUEST_INQUIRY, responseObserver);
    }

    @java.lang.Override
    public void receiveRequestSyn(cn.edu.ustc.center.ApiProtos.request_syn request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_syn> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_RECEIVE_REQUEST_SYN, responseObserver);
    }

    @java.lang.Override
    public void receiveRequestPush(cn.edu.ustc.center.ApiProtos.request_push request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_push> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_RECEIVE_REQUEST_PUSH, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return BlockChainGrpc.bindService(this);
    }
  }

  /**
   * <pre>
   *说明：
   *receive_request_inquiry是第一种需要广播的模型
   *调用过程是 发起请求的python端构造request_inquiry, 调用JavaForward的reques_inquiry_forward, java传给对面的java之后，java调用BlockChain的receive_request_inquiry, python会返回一个respond_inquiry，其中id是无效的，java接收到之后要补上id，回复给请求端的java。再之后，请求端的java要把广播得到的回复放到一个队列里，通过stream response的方式返回给python.
   *receive_request_syn是第二种指定连接对象的模型
   *调用过程是 发起请求的python端构造request_syn, 调用JavaForward的request_syn_forward, java根据request的id域指示连接指定的java, 对面的java再调用BlockChain的receive_request_syn, 返回一个stream的response_syn, 最后java负责传递stream的response到请求端的python.
   * </pre>
   */
  public static interface BlockChainBlockingClient {

    /**
     */
    public cn.edu.ustc.center.ApiProtos.response_inquiry receiveRequestInquiry(cn.edu.ustc.center.ApiProtos.request_inquiry request);

    /**
     */
    public java.util.Iterator<cn.edu.ustc.center.ApiProtos.response_syn> receiveRequestSyn(
        cn.edu.ustc.center.ApiProtos.request_syn request);

    /**
     */
    public cn.edu.ustc.center.ApiProtos.response_push receiveRequestPush(cn.edu.ustc.center.ApiProtos.request_push request);
  }

  /**
   * <pre>
   *说明：
   *receive_request_inquiry是第一种需要广播的模型
   *调用过程是 发起请求的python端构造request_inquiry, 调用JavaForward的reques_inquiry_forward, java传给对面的java之后，java调用BlockChain的receive_request_inquiry, python会返回一个respond_inquiry，其中id是无效的，java接收到之后要补上id，回复给请求端的java。再之后，请求端的java要把广播得到的回复放到一个队列里，通过stream response的方式返回给python.
   *receive_request_syn是第二种指定连接对象的模型
   *调用过程是 发起请求的python端构造request_syn, 调用JavaForward的request_syn_forward, java根据request的id域指示连接指定的java, 对面的java再调用BlockChain的receive_request_syn, 返回一个stream的response_syn, 最后java负责传递stream的response到请求端的python.
   * </pre>
   */
  public static interface BlockChainFutureClient {

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.edu.ustc.center.ApiProtos.response_inquiry> receiveRequestInquiry(
        cn.edu.ustc.center.ApiProtos.request_inquiry request);

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.edu.ustc.center.ApiProtos.response_push> receiveRequestPush(
        cn.edu.ustc.center.ApiProtos.request_push request);
  }

  public static class BlockChainStub extends io.grpc.stub.AbstractStub<BlockChainStub>
      implements BlockChain {
    private BlockChainStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BlockChainStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BlockChainStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BlockChainStub(channel, callOptions);
    }

    @java.lang.Override
    public void receiveRequestInquiry(cn.edu.ustc.center.ApiProtos.request_inquiry request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_inquiry> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_RECEIVE_REQUEST_INQUIRY, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void receiveRequestSyn(cn.edu.ustc.center.ApiProtos.request_syn request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_syn> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_RECEIVE_REQUEST_SYN, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void receiveRequestPush(cn.edu.ustc.center.ApiProtos.request_push request,
        io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_push> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_RECEIVE_REQUEST_PUSH, getCallOptions()), request, responseObserver);
    }
  }

  public static class BlockChainBlockingStub extends io.grpc.stub.AbstractStub<BlockChainBlockingStub>
      implements BlockChainBlockingClient {
    private BlockChainBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BlockChainBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BlockChainBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BlockChainBlockingStub(channel, callOptions);
    }

    @java.lang.Override
    public cn.edu.ustc.center.ApiProtos.response_inquiry receiveRequestInquiry(cn.edu.ustc.center.ApiProtos.request_inquiry request) {
      return blockingUnaryCall(
          getChannel(), METHOD_RECEIVE_REQUEST_INQUIRY, getCallOptions(), request);
    }

    @java.lang.Override
    public java.util.Iterator<cn.edu.ustc.center.ApiProtos.response_syn> receiveRequestSyn(
        cn.edu.ustc.center.ApiProtos.request_syn request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_RECEIVE_REQUEST_SYN, getCallOptions(), request);
    }

    @java.lang.Override
    public cn.edu.ustc.center.ApiProtos.response_push receiveRequestPush(cn.edu.ustc.center.ApiProtos.request_push request) {
      return blockingUnaryCall(
          getChannel(), METHOD_RECEIVE_REQUEST_PUSH, getCallOptions(), request);
    }
  }

  public static class BlockChainFutureStub extends io.grpc.stub.AbstractStub<BlockChainFutureStub>
      implements BlockChainFutureClient {
    private BlockChainFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BlockChainFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BlockChainFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BlockChainFutureStub(channel, callOptions);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<cn.edu.ustc.center.ApiProtos.response_inquiry> receiveRequestInquiry(
        cn.edu.ustc.center.ApiProtos.request_inquiry request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_RECEIVE_REQUEST_INQUIRY, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<cn.edu.ustc.center.ApiProtos.response_push> receiveRequestPush(
        cn.edu.ustc.center.ApiProtos.request_push request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_RECEIVE_REQUEST_PUSH, getCallOptions()), request);
    }
  }

  private static final int METHODID_RECEIVE_REQUEST_INQUIRY = 0;
  private static final int METHODID_RECEIVE_REQUEST_SYN = 1;
  private static final int METHODID_RECEIVE_REQUEST_PUSH = 2;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BlockChain serviceImpl;
    private final int methodId;

    public MethodHandlers(BlockChain serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RECEIVE_REQUEST_INQUIRY:
          serviceImpl.receiveRequestInquiry((cn.edu.ustc.center.ApiProtos.request_inquiry) request,
              (io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_inquiry>) responseObserver);
          break;
        case METHODID_RECEIVE_REQUEST_SYN:
          serviceImpl.receiveRequestSyn((cn.edu.ustc.center.ApiProtos.request_syn) request,
              (io.grpc.stub.StreamObserver<cn.edu.ustc.center.ApiProtos.response_syn>) responseObserver);
          break;
        case METHODID_RECEIVE_REQUEST_PUSH:
          serviceImpl.receiveRequestPush((cn.edu.ustc.center.ApiProtos.request_push) request,
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
        METHOD_RECEIVE_REQUEST_INQUIRY,
        METHOD_RECEIVE_REQUEST_SYN,
        METHOD_RECEIVE_REQUEST_PUSH);
  }

  public static io.grpc.ServerServiceDefinition bindService(
      final BlockChain serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          METHOD_RECEIVE_REQUEST_INQUIRY,
          asyncUnaryCall(
            new MethodHandlers<
              cn.edu.ustc.center.ApiProtos.request_inquiry,
              cn.edu.ustc.center.ApiProtos.response_inquiry>(
                serviceImpl, METHODID_RECEIVE_REQUEST_INQUIRY)))
        .addMethod(
          METHOD_RECEIVE_REQUEST_SYN,
          asyncServerStreamingCall(
            new MethodHandlers<
              cn.edu.ustc.center.ApiProtos.request_syn,
              cn.edu.ustc.center.ApiProtos.response_syn>(
                serviceImpl, METHODID_RECEIVE_REQUEST_SYN)))
        .addMethod(
          METHOD_RECEIVE_REQUEST_PUSH,
          asyncUnaryCall(
            new MethodHandlers<
              cn.edu.ustc.center.ApiProtos.request_push,
              cn.edu.ustc.center.ApiProtos.response_push>(
                serviceImpl, METHODID_RECEIVE_REQUEST_PUSH)))
        .build();
  }
}
