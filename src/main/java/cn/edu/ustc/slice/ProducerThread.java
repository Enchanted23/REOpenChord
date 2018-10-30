package cn.edu.ustc.slice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import com.backblaze.erasure.ReedSolomon;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yxynha
 */
public class ProducerThread  implements Runnable {

     private PublicResource resource;
     private String fileName;
     private int total_shards;
     private int needed_shards;
     private int flag;

     public ProducerThread(PublicResource resource,int total_shards,int needed_shards,String fileName,int flag) {
        this.resource = resource;
        this.needed_shards=needed_shards;
        this.fileName=fileName;
        this.total_shards=total_shards;
        this.flag=flag;
     }

     @Override
     public void run()
     {try{
         splitfile(fileName,total_shards,needed_shards,resource,flag);
     }catch(Exception e){
         System.out.println(e);
     }

     }
        /**
     * @param fileName including path
     * @param total_shards
     * @param needed_shards
     * @exception IOException
     */
   public static void splitfile(String fileName,int total_shards,int needed_shards ,PublicResource resource,
   int flag)

    throws IOException {
         int DATA_SHARDS = needed_shards;
         int PARITY_SHARDS = total_shards-needed_shards;
         int TOTAL_SHARDS = total_shards;
        int BYTES_IN_INT = 4;
      if(flag==1){
        File f = new File(fileName);
         if (!f.exists()) {
            System.out.println("Cannot read input file: " +f);
            return;
        }//exist or not

        FileInputStream fs = new FileInputStream(f);
        String result = null;
        int piecenum=0;
        long fslength=fs.available();
        int filesize=1024*1024*8;
        byte[] b = new byte[filesize];//8M
         int storedSize = filesize + BYTES_IN_INT;
         int shardSize = (storedSize + DATA_SHARDS - 1) / DATA_SHARDS;
          int bufferSize = shardSize * DATA_SHARDS;

          //
        for(;fslength>filesize;fslength=fslength-filesize){
        int readCount = 0; // 
        while (readCount < filesize)
        readCount += fs.read(b, readCount, filesize- readCount);
        //
         byte [] allBytes = new byte[bufferSize];
        ByteBuffer.wrap(allBytes).putInt(filesize);
        java.io.ByteArrayInputStream  input=new java.io.ByteArrayInputStream(b);

        int bytesRead = input.read(allBytes, BYTES_IN_INT, filesize);
        if (bytesRead != filesize) {
            throw new IOException("not enough bytes read");
        }
        input.close();
        // Make the buffers to hold the shards.
        byte [] [] shards = new byte [TOTAL_SHARDS] [shardSize];

        // Fill in the data shards
        for (int i = 0; i < DATA_SHARDS; i++) {
            System.arraycopy(allBytes, i * shardSize, shards[i], 0, shardSize);
        }

        // Use Reed-Solomon to calculate the parity.
        ReedSolomon reedSolomon = new ReedSolomon(DATA_SHARDS, PARITY_SHARDS);
        reedSolomon.encodeParity(shards, 0, shardSize);//ec

        resource.increase(shards,false);

        piecenum++;


        }
        //last part
        int readCount = 0; // 
        while (readCount < fslength)
        readCount += fs.read(b, readCount, (int)(fslength - readCount));//
        //TODO
         storedSize = (int)fslength + BYTES_IN_INT;
         shardSize = (storedSize + DATA_SHARDS - 1) / DATA_SHARDS;
         bufferSize = shardSize * DATA_SHARDS;
        byte [] allBytes = new byte[bufferSize];
        ByteBuffer.wrap(allBytes).putInt((int)fslength);
        java.io.ByteArrayInputStream  input=new java.io.ByteArrayInputStream(b);

        int bytesRead = input.read(allBytes, BYTES_IN_INT,(int)fslength );
        if (bytesRead != (int)fslength) {
            throw new IOException("not enough bytes read");
        }
        input.close();
        // Make the buffers to hold the shards.
        byte [] [] shards = new byte [TOTAL_SHARDS] [shardSize];

        // Fill in the data shards
        for (int i = 0; i < DATA_SHARDS; i++) {
            System.arraycopy(allBytes, i * shardSize, shards[i], 0, shardSize);
        }

        // Use Reed-Solomon to calculate the parity.
        ReedSolomon reedSolomon = new ReedSolomon(DATA_SHARDS, PARITY_SHARDS);
        reedSolomon.encodeParity(shards, 0, shardSize);

         resource.increase(shards,true);
      }
      
      
    else {
        java.io.ByteArrayInputStream  fs=new java.io.ByteArrayInputStream(fileName.getBytes());
        String result = null;
        int piecenum=0;
        long fslength=fs.available();
        int filesize=1024*1024*8;
        byte[] b = new byte[filesize];//8M
         int storedSize = filesize + BYTES_IN_INT;
         int shardSize = (storedSize + DATA_SHARDS - 1) / DATA_SHARDS;
          int bufferSize = shardSize * DATA_SHARDS;

          //
        for(;fslength>filesize;fslength=fslength-filesize){
        int readCount = 0; // 
        while (readCount < filesize)
        readCount += fs.read(b, readCount, filesize- readCount);
        //
         byte [] allBytes = new byte[bufferSize];
        ByteBuffer.wrap(allBytes).putInt(filesize);
        java.io.ByteArrayInputStream  input=new java.io.ByteArrayInputStream(b);

        int bytesRead = input.read(allBytes, BYTES_IN_INT, filesize);
        if (bytesRead != filesize) {
            throw new IOException("not enough bytes read");
        }
        input.close();
        // Make the buffers to hold the shards.
        byte [] [] shards = new byte [TOTAL_SHARDS] [shardSize];

        // Fill in the data shards
        for (int i = 0; i < DATA_SHARDS; i++) {
            System.arraycopy(allBytes, i * shardSize, shards[i], 0, shardSize);
        }

        // Use Reed-Solomon to calculate the parity.
        ReedSolomon reedSolomon = new ReedSolomon(DATA_SHARDS, PARITY_SHARDS);
        reedSolomon.encodeParity(shards, 0, shardSize);//ec

        resource.increase(shards,false);

        piecenum++;


        }
        //last part
        int readCount = 0; // 
        while (readCount < fslength)
        readCount += fs.read(b, readCount, (int)(fslength - readCount));//
        //TODO
         storedSize = (int)fslength + BYTES_IN_INT;
         shardSize = (storedSize + DATA_SHARDS - 1) / DATA_SHARDS;
         bufferSize = shardSize * DATA_SHARDS;
        byte [] allBytes = new byte[bufferSize];
        ByteBuffer.wrap(allBytes).putInt((int)fslength);
        java.io.ByteArrayInputStream  input=new java.io.ByteArrayInputStream(b);

        int bytesRead = input.read(allBytes, BYTES_IN_INT,(int)fslength );
        if (bytesRead != (int)fslength) {
            throw new IOException("not enough bytes read");
        }
        input.close();
        // Make the buffers to hold the shards.
        byte [] [] shards = new byte [TOTAL_SHARDS] [shardSize];

        // Fill in the data shards
        for (int i = 0; i < DATA_SHARDS; i++) {
            System.arraycopy(allBytes, i * shardSize, shards[i], 0, shardSize);
        }

        // Use Reed-Solomon to calculate the parity.
        ReedSolomon reedSolomon = new ReedSolomon(DATA_SHARDS, PARITY_SHARDS);
        reedSolomon.encodeParity(shards, 0, shardSize);

         resource.increase(shards,true);
    }
    }
}
