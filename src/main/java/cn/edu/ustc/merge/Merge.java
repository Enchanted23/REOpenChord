
package cn.edu.ustc.merge;


import java.util.Vector;
import java.io.*;

public class Merge {
	private static final int maxSize = 5;
	private int number;  
	private Vector<byte[][]> shards;
	private Vector<boolean[]> shards_present;
	private int m;
	private int n;
	private String filename;
	private boolean finish;
    /** 
     * 
     */
	public Merge(int m,int n,String filename){
		number = 0;
		this.m = m;
		this.n = n;
		this.filename = filename;
		shards = new Vector<byte[][]>();
		shards_present = new Vector<boolean[]>();
		finish = false;
	}
	
    public synchronized void increase(byte[][] shard,boolean[] p,boolean finish) {  
        if(finish == false){   
        	while(number == maxSize){
        		try{
        			wait();
        		}catch(InterruptedException e){
        			
        		}
        	}
           number++;  
           shards.add(shard);
           shards_present.add(p);
           notify();
        }
        else{
            this.finish = finish;
            notify();
        }  
    }  
    
   
  
    /** 
     * 
     * @return 
     */  
    public synchronized boolean decrease(Info info) {
        while(number == 0 && finish == false){
            try{
                wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        if(finish == true && number == 0){
            return true;
        }
    	else{
    	    if(number != 0) {  
    		  number--; 
              byte[][] shard=shards.get(0);
              boolean[] shardspresent=shards_present.get (0);
              shards.remove(0);
              shards_present.remove(0);
              int N=n ;
              int M=m;
              String wantedname=filename; 
              info.set(N,M,wantedname,shard,shardspresent);       
              notify();
             }
           return false;
          }
       }
    
}


