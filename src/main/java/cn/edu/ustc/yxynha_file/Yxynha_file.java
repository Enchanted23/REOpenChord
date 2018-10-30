package cn.edu.ustc.yxynha_file;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import com.backblaze.erasure.ReedSolomon;
import cn.edu.ustc.slice.*;
import cn.edu.ustc.merge.*;
/**
 *
 * @author yxynha
 *.*/
public class Yxynha_file 
 {
     //
    /*public void split(PublicResource resource,int total_shards,int needed_name,String fileName)
    throws IOException{
        new Thread(new ProducerThread(resource), total_shards, needed_name, fileName).start();  
    }
    
    //
   public void mergement(Merge mergeresource){
    new Thread(new ConsumerThread(mergeresource)).start();  
   }
    /**
     * @param wantedname :
     * @param total_shards
     * @param needed_shards
     * @param shards
     * @param shardPresent
     * @exception IOException
     */
   public static String mergefile(int total_shards,int needed_shards,String wantedname,byte[][] shards,boolean[] shardPresent) 
   throws IOException{
       int DATA_SHARDS = needed_shards;
       int PARITY_SHARDS = total_shards-needed_shards;
       int TOTAL_SHARDS = total_shards;
       int BYTES_IN_INT = 4;
       
        // Read in any of the shards that are present.
       
        int shardSize = 0;
        int shardCount = 0;
        
         
        for (int i = 0; i < total_shards; i++) {
            if (shardPresent[i]) {
                shardCount += 1;
                shardSize=shards[i].length;
            }
        }
         // We need at least DATA_SHARDS to be able to reconstruct the file.
        if (shardCount < DATA_SHARDS) {
            throw new IOException("not enough shards");
        }

        // Make empty buffers for the missing shards.
        for (int i = 0; i < TOTAL_SHARDS; i++) {
            if (!shardPresent[i]) {
                shards[i] = new byte [shardSize];
            }
        }

        // Use Reed-Solomon to fill in the missing shards
        ReedSolomon reedSolomon = new ReedSolomon(DATA_SHARDS, PARITY_SHARDS);
        reedSolomon.decodeMissing(shards, shardPresent, 0, shardSize);
        
        byte [] allBytes = new byte [shardSize * DATA_SHARDS];
        for (int i = 0; i < DATA_SHARDS; i++) {
            System.arraycopy(shards[i], 0, allBytes, shardSize * i, shardSize);
        }

        // Extract the file length
       // int fileSize = ByteBuffer.wrap(allBytes).getInt();
      if (wantedname!=null){
      byte [] needbytes = new byte [allBytes.length-4];
      System.arraycopy(allBytes, 4, needbytes, 0, allBytes.length-4);
      String con=new String (allBytes);
      String  content=con.substring(4,con.length());
          try {
            // 
            RandomAccessFile randomFile = new RandomAccessFile(wantedname, "rw");
            // 
            long fileLength = randomFile.length();
            //
            randomFile.seek(fileLength);
            randomFile.write(needbytes);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";}
     else {
         String  con=new String (allBytes);
      String  content=con.substring(4);
         return content;
     }
   }

}
    

