package cn.edu.ustc.center;

import java.util.concurrent.ExecutionException;

import cn.edu.ustc.center.ApiProtos.FS_Request;
import cn.edu.ustc.center.ApiProtos.FS_Response;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import io.grpc.stub.StreamObserver;

public class FSService implements FSServiceGrpc.FSService{
	static final int FILE_DOWNLOAD = ApiProtos.Type.FILE_DOWNLOAD_VALUE;
	static final int INDEX_DOWNLOAD = ApiProtos.Type.INDEX_DOWNLOAD_VALUE;
	static final int FILE_UPLOAD = ApiProtos.Type.FILE_UPLOAD_VALUE;
	static final int INDEX_UPLOAD = ApiProtos.Type.INDEX_UPLOAD_VALUE;
	static final int EXIT = ApiProtos.Type.EXIT_VALUE;

    private FS_Response response;
    private Chord chord;
    
    public FSService(Chord chord){
    	this.chord = chord;

    }
    
	@Override
	public void fSServe(FS_Request request, StreamObserver<FS_Response> responseObserver){
		switch(request.getTypeValue()){
		case FILE_DOWNLOAD:
		case INDEX_DOWNLOAD:
			Download dl = null;
			try {
				dl = new Download(request,chord);
				} catch (InterruptedException | ExecutionException | ServiceException e) {
					e.printStackTrace();
				}
			if(dl != null){
				response = dl.getResponse();
			}
			break;
			
		case FILE_UPLOAD:
		case INDEX_UPLOAD:
			Upload ul = null;
			try{
				ul = new Upload(request,chord);
			}catch (Exception e) {
				e.printStackTrace();
			}
			if(ul != null){
				response = ul.getResponse();
			}
			break;
		
		case EXIT:
			try {
				chord.leave();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
		
	
	   responseObserver.onNext(response);
		
		responseObserver.onCompleted();
	}
	
	
	
}
