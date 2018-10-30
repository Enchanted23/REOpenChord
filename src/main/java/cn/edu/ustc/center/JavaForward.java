package cn.edu.ustc.center;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import cn.edu.ustc.center.ApiProtos.request_inquiry;
import cn.edu.ustc.center.ApiProtos.request_push;
import cn.edu.ustc.center.ApiProtos.request_syn;
import cn.edu.ustc.center.ApiProtos.response_inquiry;
import cn.edu.ustc.center.ApiProtos.response_inquiry;
import cn.edu.ustc.center.ApiProtos.response_push;
import cn.edu.ustc.center.ApiProtos.response_syn;
import cn.edu.ustc.chordinit.ChordLogging;
import cn.edu.ustc.chordinit.MethodConstants;
import cn.edu.ustc.chordinit.Request;
import cn.edu.ustc.chordinit.*;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import io.grpc.stub.StreamObserver;

public class JavaForward implements JavaForwardGrpc.JavaForward{
	private Chord chord;

	public JavaForward(Chord chord){
		this.chord = chord;
	}

	@Override
	public void requestInquiryForward(request_inquiry request, StreamObserver<response_inquiry> responseObserver) {
		List<String> id_list = null;
		try{

		id_list = chord.getRefs();
		}catch(ServiceException e){

		}
		 Iterator<String> i = id_list.iterator();
		 Node node = ChordLogging.getSender();

		 ArrayList<Future<Boolean>> result = new ArrayList<Future<Boolean>>();
		 while(i.hasNext()){
			 Request r = new Request(MethodConstants.REQUEST,MethodConstants.TRANSPORT_RI,ChordLogging.getID());
			 r.setRI(request);
			 try{
//				 result.add(node.transfer_message(r,i.next()));
				 node.transfer_message(r,i.next());
			 }catch(IOException e){

             }
		 }

		 while(ReqHandler.RIStreamLength() != id_list.size()){
			;
		 }

		ArrayList<ApiProtos.response_inquiry> RIList = ReqHandler.getRIList();

		for(int count=0;count<RIList.size();count++){
			responseObserver.onNext(RIList.get(count));
		}
		responseObserver.onCompleted();

	}

	@Override
	public void requestSynForward(request_syn request, StreamObserver<response_syn> responseObserver) {

		Request r = new Request(MethodConstants.REQUEST,MethodConstants.TRANSPORT_RS,ChordLogging.getID());
		r.setRS(request);

		Node node = ChordLogging.getSender();
		Future<Boolean> result;
		try{
//			result = node.transfer_message(r,request.getId());
//			if(result.get().booleanValue()){
//				//TODO 从ReqHandler处取response_syn
//			}
			node.transfer_message(r,request.getId());

		}catch(IOException e){

        }

		while(ReqHandler.isgetRS(request.getId()) == false){
			;
		}

		Request want = ReqHandler.getRS(request.getId());
		ArrayList<ApiProtos.response_syn> syn = want.getRSList();

		for(int count = 0 ; count < syn.size() ; count++){
			responseObserver.onNext(syn.get(count));
		}
		responseObserver.onCompleted();



	}


	@Override
	public void requestPushForward(request_push request, StreamObserver<response_push> responseObserver) {
		List<String> id_list = null;
		try{

		id_list = chord.getRefs();
		}catch(ServiceException e){

		}
		 Iterator<String> i = id_list.iterator();
		 Node node = ChordLogging.getSender();

		 ArrayList<Future<Boolean>> result = new ArrayList<Future<Boolean>>();
		 while(i.hasNext()){
			 Request r = new Request(MethodConstants.REQUEST,MethodConstants.TRANSPORT_RP,ChordLogging.getID());
			 r.setRP(request);
			 try{
//				 result.add(node.transfer_message(r,i.next()));
				 node.transfer_message(r,i.next());
			 }catch(IOException e){

            }
		 }

		 while(ReqHandler.RPStreamLength() != id_list.size()){
			;
		 }

		ArrayList<ApiProtos.response_push> RPList = ReqHandler.getRPList();

		for(int count=0;count<RPList.size();count++){
			responseObserver.onNext(RPList.get(count));
		}
		responseObserver.onCompleted();

	}

}
