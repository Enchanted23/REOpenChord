package cn.edu.ustc.chordinit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;
import java.util.concurrent.Future;

import cn.edu.ustc.center.ApiProtos;
import cn.edu.ustc.center.BlockChainGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Request handler for handling everything, including those inside and
 * outside Chord, transfered to this node via the protocol based on UDP.
 *
 * @author Qiang Xu
 */
public class ReqHandler {
	private static final int PORT = 5001;

    private static HashMap<String, Request> responseSet = new HashMap<String, Request>();
    
   private static ArrayList<ApiProtos.response_inquiry> RIList;
    
    private static ArrayList<ApiProtos.response_push> RPList;	
    
    private static HashMap<String, Request> RSSet = new HashMap<String,Request>();

    private ReqHandler() {
        // nothing here
    }

    public static void handle(Object obj) {

        Request req = (Request) obj;

        if (req.getRequestType() == MethodConstants.RESPONSE) {
        	switch(req.getRequestMethod()){
        	      case MethodConstants.RETRIEVE_SHARD:{
                        synchronized (responseSet) {
                            responseSet.put(req.getShardhash(), req);
                        }
                        break;
        	      }
        	      case MethodConstants.TRANSPORT_RI:{
        	    	  synchronized(RIList){
        	    		  RIList.add(req.getRespond_i());
        	    	  }
        	      }
        	      case MethodConstants.TRANSPORT_RP:{
        	    	  synchronized(RPList){
        	    		  RPList.add(req.getResponse_p());
        	    	  }
        	      }
        	      case MethodConstants.TRANSPORT_RS:{
        	    	  synchronized(RSSet){
        	    		  RSSet.put(req.getReplyTo(), req);
        	    	  }
        	      }
        	}
        } else {
            int method = req.getRequestMethod();
            switch (method) {
                case MethodConstants.TRANSPORT_SHARD: {
                    try {
                        ChordLogging.getDB().InsertFragment(req.getShardhash(), req.getShard());
                    } catch (IOException e) {

                    }
                    break;
                }
                case MethodConstants.RETRIEVE_SHARD: {
                    try {
                        byte[] requestedShard
                            = ChordLogging.getDB().RetrieveFragment(req.getShardhash());
                        Request response = new Request(MethodConstants.RESPONSE,
                            req.getRequestMethod(), null);
                        response.setShardhash(req.getShardhash());
                        response.setShard(requestedShard);
                        ChordLogging.getSender().transfer_message(response, req.getReplyTo());
                    //     Future<Boolean> result = ChordLogging.getSender()
                    //         .transfer_message(response, req.getReplyTo());
                    //     while (!result.isDone())
                    //         ;
                    } catch (IOException e) {

                    }
                    break;
                }
                case MethodConstants.TRANSPORT_RP:{
                	try{
                    	ManagedChannel	channel = ManagedChannelBuilder.forAddress("localhost", PORT)
                  	        .usePlaintext(true)
                  	        .build();
                    	BlockChainGrpc.BlockChainBlockingStub blockingStub = BlockChainGrpc.newBlockingStub(channel);
                    	ApiProtos.response_push respond = blockingStub.receiveRequestPush(req.getRP());
                    
                    	ApiProtos.response_push.Builder builder = ApiProtos.response_push.newBuilder();
                    	builder.setId(ChordLogging.getID()).setConfirm(respond.getConfirm());
                    
                       ApiProtos.response_push new_respond = builder.build();
                    
                       Request re = new Request(MethodConstants.RESPONSE,req.getRequestMethod(),null);
                       re.setResponse_p(new_respond);
                       
                       ChordLogging.getSender().transfer_message(re, req.getReplyTo());
               //     Future<Boolean> result = ChordLogging.getSender()
               //         .transfer_message(re, req.getReplyTo());
               //     while (!result.isDone())
               //         ;
                        break;
                	}catch(IOException e){

                	}
                }
                case MethodConstants.TRANSPORT_RI:{
                	try{
                    	ManagedChannel	channel = ManagedChannelBuilder.forAddress("localhost", PORT)
                  	        .usePlaintext(true)
                  	        .build();
                    	BlockChainGrpc.BlockChainBlockingStub blockingStub = BlockChainGrpc.newBlockingStub(channel);
                    	ApiProtos.response_inquiry respond = blockingStub.receiveRequestInquiry(req.getRI());
                    
                    	ApiProtos.response_inquiry.Builder builder = ApiProtos.response_inquiry.newBuilder();
                    	builder.setId(ChordLogging.getID()).setForking(respond.getForking()).addAllHashes(respond.getHashesList());
                    
                       ApiProtos.response_inquiry new_respond = builder.build();
                    
                       Request re = new Request(MethodConstants.RESPONSE,req.getRequestMethod(),null);
                       re.setRespond_i(new_respond);
                       
                       ChordLogging.getSender().transfer_message(re, req.getReplyTo());
               //     Future<Boolean> result = ChordLogging.getSender()
               //         .transfer_message(re, req.getReplyTo());
               //     while (!result.isDone())
               //         ;
                        break;
                	}catch(IOException e){

                	}
                }
                case MethodConstants.TRANSPORT_RS:{
                	try{
                		ManagedChannel	channel = ManagedChannelBuilder.forAddress("localhost", PORT)
                      	        .usePlaintext(true)
                      	        .build();
                        BlockChainGrpc.BlockChainBlockingStub blockingStub = BlockChainGrpc.newBlockingStub(channel);
                       Iterator< ApiProtos.response_syn> response_s = blockingStub.receiveRequestSyn(req.getRS());
                       
                       Request re = new Request(MethodConstants.RESPONSE,req.getRequestMethod(),ChordLogging.getID());
                       while(response_s.hasNext()){
                    	   re.setResponse_s(response_s.next());
                       }
                       
                       ChordLogging.getSender().transfer_message(re, req.getReplyTo());
               //     Future<Boolean> result = ChordLogging.getSender()
               //         .transfer_message(re, req.getReplyTo());
               //     while (!result.isDone())
               //         ;
                	}catch(IOException e){

                	}
                }
            }
        }

        // if (MethodConstants.isForChord(methodType))
        //     UDPEndpoint.handle((Request)obj);
        // else
        //     ShardReqHandler.handle((Request)obj);
    }

    public static boolean isReceived(String id) {
        synchronized (responseSet) {
            return responseSet.containsKey(id);
        }
    }

    public static Request retrieveResponse(String id) {
        synchronized (responseSet) {
            return responseSet.remove(id);
        }
    }

    public static void clearResponse() {
        synchronized (responseSet) {
            responseSet.clear();
        }
    }
    public static int RIStreamLength(){
    	synchronized(RIList){
    		return RIList.size();
    	}
    }
    
    public static ArrayList<ApiProtos.response_inquiry> getRIList(){
    	synchronized(RIList){
    		return RIList;
    	}
    }
    
    public static int RPStreamLength(){
    	synchronized(RPList){
    		return RPList.size();
    	}
    }
    
    public static ArrayList<ApiProtos.response_push> getRPList(){
    	synchronized(RPList){
    		return RPList;
    	}
    }
    
    public static boolean isgetRS(String id){
    	synchronized(RSSet){
    		return RSSet.containsKey(id);
    	}
    }
    
    public static Request getRS(String id){
    	synchronized(RSSet){
    		return RSSet.remove(id);
    	}
    }
}

