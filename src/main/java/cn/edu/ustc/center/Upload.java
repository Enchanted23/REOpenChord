package cn.edu.ustc.center;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.protobuf.ByteString;

import cn.edu.ustc.center.Key;
import cn.edu.ustc.center.Value;
import cn.edu.ustc.chordinit.ChordLogging;
import cn.edu.ustc.merge.Consumer_Merge;
import cn.edu.ustc.slice.*;
import cn.edu.ustc.strategy.Strategy;
import de.uniba.wiai.lspi.chord.service.Chord;

public class Upload{
	private Chord chord;
	private ApiProtos.FS_Response response;

   public Upload(ApiProtos.FS_Request api,Chord chord) throws Exception{
       this.chord = chord;
	   if(api.getType() == ApiProtos.Type.FILE_UPLOAD){
        	List<ByteString> payload = api.getPayloadList();
        	String filename = payload.get(0).toStringUtf8();
        	File f = new File(filename);
			if(f.exists()&&f.isFile()){
			     	Key key = null;
					Strategy strategy = new Strategy(chord,f.length());
	                Value v = new Value(strategy.getParamK(), strategy.getParamN(), strategy.getIDSet(), strategy.getDistrTable());
					PublicResource pr = new PublicResource();
					new Thread(new ProducerThread(pr, strategy.getParamN(), strategy.getParamK(), filename, 1)).start();

					ExecutorService exs = Executors.newCachedThreadPool();
					Future<ApiProtos.FS_Response> result;
				    result = exs.submit((new ConsumerThread(pr, api.getType(), chord, v, key)));


				    response = result.get();
					exs.shutdownNow();
			}
			else{
				// the path of the file is wrong
				ApiProtos.FS_Response.Builder builder = ApiProtos.FS_Response.newBuilder();
				builder.setType(api.getType())
				.setResult(ApiProtos.Result.ERROR)
				.setPayload(0, ByteString.EMPTY);

				List<ByteString> v = new ArrayList<ByteString>();
				v.add(ByteString.copyFromUtf8("the path of the file is wrong"));

				builder.addAllPayload(v);
			    response = builder.build();

			}
        }
	   else{
		   List<ByteString> payload = api.getPayloadList();
		   String index_hash = payload.get(0).toStringUtf8();
		   String index_content = payload.get(1).toStringUtf8();
		   Key key = new Key(index_hash);
		   Strategy strategy = new Strategy(chord, index_content.length());
		   Value v = new Value(strategy.getParamK(), strategy.getParamN(), strategy.getIDSet(), strategy.getDistrTable());
		   PublicResource pr = new PublicResource();
		   new Thread(new ProducerThread(pr, strategy.getParamN(), strategy.getParamK(), index_content, 0)).start();

		   ExecutorService exs = Executors.newCachedThreadPool();
			Future<ApiProtos.FS_Response> result;
		    result = exs.submit((new ConsumerThread(pr, api.getType(), chord, v, key)));

		    response = result.get();
			exs.shutdownNow();
	   }
   }

   public ApiProtos.FS_Response getResponse(){
	   return response;
   }

}
