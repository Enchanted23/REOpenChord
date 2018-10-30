package cn.edu.ustc.merge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.edu.ustc.center.Value;
import cn.edu.ustc.chordinit.ChordLogging;
import cn.edu.ustc.chordinit.MethodConstants;
import cn.edu.ustc.chordinit.ReqHandler;
import cn.edu.ustc.chordinit.Request;
import cn.edu.ustc.chordinit.*;;

public class Producer_Merge implements Runnable {
	private Merge merge;
	private Value v;

	public Producer_Merge(Merge merge, Value v){
		this.merge = merge;
		this.v = v;
	}

	@Override
    public void run(){
		for(int count = 0; count <  v.getShardsorder().size(); count = count + v.getParamN()){
			byte[][] shard = new byte[v.getParamN()][];
			boolean[] p = new boolean[v.getParamN()];
			ExecutorService exs = Executors.newCachedThreadPool();
			ArrayList<Future<Request>> result = new ArrayList<Future<Request>>();
			for(int i = 0; i < v.getParamN(); i++){
				Request request = new Request(MethodConstants.REQUEST,MethodConstants.RETRIEVE_SHARD,ChordLogging.getID());
				request.setShardhash(v.getShardsorder().get(count + i));
				Node node = ChordLogging.getSender();
				try{
				   node.transfer_message(request ,FindPeer.findpeer(v.getIDSet(),v.getDistrTable(), i + 1));
				}catch(IOException e){

				}
				p[i] = false;
				shard[i] = null;
				result.add(exs.submit(new waitThread(v.getShardsorder().get(count + i))));
			}

			int collection = 0;
			for(int num = 0;collection < v.getParamK(); num++ ){
				if(result.get(num).isDone() && p[num] == false){
					try{
					    shard[num] = result.get(num).get().getShard();
					}catch(Exception e){

					}
					collection++;
					p[num] = true;
				}
				if(num == v.getParamN() - 1){
					num = -1;
				}
			}

            exs.shutdownNow();

			merge.increase(shard, p, false);
		}
		merge.increase(null, null, true);
	}

}

class  FindPeer{
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

class  waitThread implements Callable<Request>{
	private String hash;

	public waitThread(String hash){
		this.hash = hash;
	}

	public Request call(){
		while(ReqHandler.isReceived(hash) == false){
			;
		}
		return ReqHandler.retrieveResponse(hash);
	}
}
