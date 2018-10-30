package cn.edu.ustc.merge;
import cn.edu.ustc.center.ApiProtos;
import cn.edu.ustc.yxynha_file.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.google.protobuf.ByteString;
public class Consumer_Merge implements  Callable<ApiProtos.FS_Response>{
    private Merge mergeresource;
    private Info info = new Info();
    private  ApiProtos.Type type;
    
public Consumer_Merge( Merge mergeresource, ApiProtos.Type type){  
        this.mergeresource = mergeresource;
        this.type = type;
    }  
     @Override  
    public ApiProtos.FS_Response call() { 
    	boolean error = false;
    	String result = "";
        while(true){
            boolean finish = mergeresource.decrease(info);
            if(finish == true){
                break;
            }
            else{
                try{
                  result = result + Yxynha_file.mergefile(info.get_n(),info.get_m() ,info.get_wantedname(),
                                info.get_shard(),info.get_shardspresent());
                } catch (IOException e) {
                   error = true;
                }
            }
        }

        ApiProtos.FS_Response response;
        
        if(error == true){
    		ApiProtos.FS_Response.Builder builder = ApiProtos.FS_Response.newBuilder();
    		builder.setType(type)
    		.setResult(ApiProtos.Result.ERROR);
    		
    		List<ByteString> v = new ArrayList<ByteString>();
    		v.add(ByteString.copyFromUtf8("the location or the hash is not correct"));
    		
    		builder.addAllPayload(v);
    		response = builder.build();
    		
        }
        else{
        	if(type == ApiProtos.Type.FILE_DOWNLOAD){
        		response = ApiProtos.FS_Response.newBuilder()
            			.setType(type)
            			.setResult(ApiProtos.Result.OK)
            			.build();
        	}
        	else{
        		ApiProtos.FS_Response.Builder builder = ApiProtos.FS_Response.newBuilder();
        		builder.setType(type)
        		.setResult(ApiProtos.Result.OK);
        		
        		List<ByteString> v = new ArrayList<ByteString>();
        		v.add(ByteString.copyFromUtf8(result));
        		
        		builder.addAllPayload(v);
        		response = builder.build();
        	}
        }
        
        return response;
        	
     }

}
