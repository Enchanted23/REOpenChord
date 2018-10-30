package cn.edu.ustc.merge;

public class Info{
    private int n;
    private int m;
    private String wantname;
    private byte[][] shard;
    private boolean[] shardspresent;
    
    public void set(int n,int m,String wantname,byte[][] shard,boolean[] shardspresent){
        this.n = n;
        this.m = m;
        this.wantname = wantname;
        this.shard = shard;
        this.shardspresent = shardspresent;
    }
    
    public int get_n(){
        return n;
    }
    
    public int get_m(){
        return m;
    }
    
    public String get_wantedname(){
        return wantname;
    }
    
    public byte[][] get_shard(){
        return shard;
    }
    
    public boolean[] get_shardspresent(){
        return shardspresent;
    }
}