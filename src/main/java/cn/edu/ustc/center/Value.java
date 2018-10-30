package cn.edu.ustc.center;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import de.uniba.wiai.lspi.chord.data.ID;

public class Value implements Serializable{
	/**
	 *use default serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;
	
	private int counter;
	private int k;
	private int n;
	private ArrayList<String> idSet = new ArrayList<String>();
    private ArrayList<Integer> distrTable = new ArrayList<Integer>();
    private Vector<String> shardsorder = new Vector<String>();
    
    public Value(int k,int n, ArrayList<String> idSet,ArrayList<Integer> distrTable){
    	counter = 1;
    	this.k = k;
    	this.n = n;
    	this.idSet = idSet;
    	this.distrTable = distrTable;
    }
    
    public void addcounter(){
    	counter++;
    }
    
    public boolean subcounter(){
    	if(counter == 1){
    		return false;
    	}
    	else{
    		counter --;
    		return true;
    	}
    }
    
    public int getParamK() {
        return k;
    }

    public int getParamN() {
        return n;
    }

    public ArrayList<String> getIDSet () {
        return idSet;
    }

    public ArrayList<Integer> getDistrTable() {
        return distrTable;
    }
    
    public void addshard(String shard_hash){
    	shardsorder.add(shard_hash);
    }
    
    public Vector<String> getShardsorder(){
    	return shardsorder;
    }
}
