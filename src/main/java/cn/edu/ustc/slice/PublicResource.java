package cn.edu.ustc.slice;
import java.util.Vector;


public class PublicResource {
	private final static int maxSize = 5;
	
	private int number;
	private Vector<byte[][]> shards;
	private boolean finish;
	private boolean first;
    /**
     *
     */



    public PublicResource(){
      number = 0;
      finish = false;
      shards = new Vector<byte[][]>();
      first = false;
    }

    public synchronized void increase(byte shard[][],boolean finish) {
    	byte[][] shard_temp = shard;
    	boolean f = finish;
    	while(number == maxSize){
    		try{
    			this.wait();
    		}catch(InterruptedException e){
    			
    		}
    	}
        number++;
        shards.add(shard_temp);
        this.finish = f;
        this.notify();
    }

    /**
     *
     */
    public synchronized boolean decrease(Info info) {
        while (number == 0 && finish == false) {
          try {
            this.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        
        if(finish == true && number == 0){
            return true;
        }
        else{
        	 byte[][] shard = null;
             if(number != 0){
                number--;
                shard = shards.get(0);
                shards.remove(0);
             }
             info.setShard(shard);
        	return false;
        }

    }
}