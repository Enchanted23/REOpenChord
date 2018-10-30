package cn.edu.ustc.center;

import java.io.IOException;

import cn.edu.ustc.chordinit.ChordLogging;
import de.uniba.wiai.lspi.chord.service.Chord;
import io.grpc.Server;
import io.grpc.ServerBuilder;


public class Center {
 	public static final int PORT =  5000;    //listen port
 	public static final int BLOCKCHAIN_PORT = 5001;
    private Server server;
    private Server BlockChainserver;
    private Chord chord;
    
    public Center() throws IOException{
    	ChordLogging cl = new ChordLogging();
    	chord = cl.initialize();
    }
    
    public void start() throws IOException{
    	server = ServerBuilder.forPort(PORT)
    	        .addService(FSServiceGrpc.bindService(new FSService(chord)))
    	        .build()
    	        .start();
    	BlockChainserver = ServerBuilder.forPort(BLOCKCHAIN_PORT)
    	        .addService(JavaForwardGrpc.bindService(new JavaForward(chord)))
    	        .build()
    	        .start();
    	Runtime.getRuntime().addShutdownHook(new Thread(){
    		public void run(){
    			Center.this.stop();
    		}
    	});
    }
    
    private void stop(){
    	if(server != null){
    		server.shutdown();
    	}
    	if(BlockChainserver != null){
    		BlockChainserver.shutdown();
    	}
    }
    
    public void blockUntilShutdown() throws InterruptedException{
    	if(server != null){
    		server.awaitTermination();
    	}
    	if(BlockChainserver != null){
    		BlockChainserver.awaitTermination();
    	}
    }
    
    public static void main(String[] args) throws IOException, InterruptedException{
    	final Center center = new Center();
    	center.start();
    	center.blockUntilShutdown();
    }
 	
 }


	


