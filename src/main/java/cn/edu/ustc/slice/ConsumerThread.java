package cn.edu.ustc.slice;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.google.protobuf.ByteString;

import java.util.ArrayList;
import java.util.List;

import cn.edu.ustc.center.ApiProtos;
import cn.edu.ustc.center.Key;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import cn.edu.ustc.center.Value;
import cn.edu.ustc.chordinit.ChordLogging;
import cn.edu.ustc.chordinit.MethodConstants;
import cn.edu.ustc.chordinit.Request;
import cn.edu.ustc.chordinit.Node;

public class ConsumerThread implements  Callable<ApiProtos.FS_Response>{
	private PublicResource resource;
	private ApiProtos.Type type;
	private Info info = new Info();
	private Chord chord;
	private Value v;
	private Key key;

	public ConsumerThread(PublicResource resource, ApiProtos.Type type, Chord chord, Value v, Key key){
		this.resource = resource;
		this.type = type;
		this.chord = chord;
		this.v = v;
		this.key = key;
	}

	@Override
	public  ApiProtos.FS_Response call(){
		MessageDigest complete = null;
		try {
			complete = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		MerkleTree mt = new MerkleTree();
		while(true){
			boolean finish = resource.decrease(info);
                        if(finish == true){
				break;
	                }
			System.out.println("get shard\n");


			ArrayList<Future<Boolean>> result = new ArrayList<Future<Boolean>>();

            for(int i =0 ; i< v.getParamN() ; i++){
            	complete.update((info.getShard()[i])) ;
            	String hash = complete.digest().toString();
            	v.addshard(hash);
            	mt.addHash(hash);
               Node node = ChordLogging.getSender();
               Request request = new  Request(MethodConstants.REQUEST,MethodConstants.TRANSPORT_SHARD,ChordLogging.getID());
               request.setShard(info.getShard()[i]);
               request.setShardhash(hash);
               try{
            	node.transfer_message(request ,FindPeer.findpeer(v.getIDSet(),v.getDistrTable(),i+1));
               }catch(IOException e){
				   e.printStackTrace();
               }
            }

            //check weather the transportation success
            //if not , do it again
  /*         boolean transporting;
           do{
        	   transporting = false;
               for(int i = 0;i<v.getParamN();i++){
            	   try{
                	  if(result.get(i).get().booleanValue() == false){
                	  	Request request = new Request(MethodConstants.REQUEST,MethodConstants.TRANSPORT_SHARD,ChordLogging.getID() );
                	  	request.setShard(info.getShard()[i]);
                          request.setShardhash(v.getShardsorder().get(i));
                          Node node = ChordLogging.getSender();
                          result.add(node.transfer_message(request ,FindPeer.findpeer(v.getIDSet(),v.getDistrTable(),i+1)));
                	  	transporting = true;
                	  }
                  }catch(Exception e){

                  }
               }
           }while(transporting);*/
                    

		}

		if(key == null){
			key = new Key(mt.getRoot());
		}

		try {
			System.out.println("IDSET="+v.getIDSet());
			chord.insert(key, v);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		ApiProtos.FS_Response.Builder builder = ApiProtos.FS_Response.newBuilder();
		builder.setType(type)
		.setResult(ApiProtos.Result.OK);
		
		List<ByteString> v = new ArrayList<ByteString>();
		v.add(ByteString.copyFromUtf8(key.getKey()));
		
		builder.addAllPayload(v);
		ApiProtos.FS_Response response = builder.build();
		
	    return response;
		
	}


}

class FindPeer{
	public static String findpeer(ArrayList<String> idSet, ArrayList<Integer> distrTable, int count){
		int num = count;
		int i;
		for( i = 0 ;num > 0 ;i++){
			int temp = distrTable.get(i).intValue();
			num = num - temp;
		}
		return idSet.get(i-1);
	}

}
