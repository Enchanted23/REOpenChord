package cn.edu.ustc.center;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.edu.ustc.merge.*;

import com.google.protobuf.ByteString;

import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;

public class Download {
	private Chord chord;
	private ApiProtos.FS_Response response;


	public Download(ApiProtos.FS_Request api, Chord chord) throws ServiceException, InterruptedException, ExecutionException{
		this.chord = chord;
		String location = null;
		Value v;
		Merge merge;
		Key key;
		Set<Serializable>  set;


		if(api.getType() == ApiProtos.Type.FILE_DOWNLOAD){
			List<ByteString> payload = api.getPayloadList();
			String file_hash = payload.get(0).toStringUtf8();
			 location = payload.get(1).toStringUtf8();

			 key = new Key(file_hash);

			 File f = new File(location);
			 if(f.exists()){
				 f.delete();
			 }
		}

		else{
			List<ByteString> payload = api.getPayloadList();
			String index_hash = payload.get(0).toStringUtf8();

			 key = new Key(index_hash);
		}
		 set = chord.retrieve(key);
		 v = (Value) set.iterator().next();

		merge = new Merge(v.getParamK(),v.getParamN(),location);
        new Thread(new Producer_Merge(merge, v)).start();

		ExecutorService exs = Executors.newCachedThreadPool();
		Future<ApiProtos.FS_Response> result;
	    result = exs.submit((new Consumer_Merge(merge, api.getType())));

	    response = result.get();
		exs.shutdownNow();

	}

	public ApiProtos.FS_Response getResponse(){
		return response;
	}

}
