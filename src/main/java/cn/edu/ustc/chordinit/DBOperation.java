/*************************************************
 *
 * 				DBOperation.java
 * date			:05.02.2016
 * copyright	:2016 Distributed File Systems Group
 *
 ************************************************************/
package cn.edu.ustc.chordinit;

import org.mapdb.*;
import java.io.IOException;

/**
 * @author Wallflower
 *
 */
public class DBOperation {

	private DB db;
	private HTreeMap<String,byte[]> map;
	private HTreeMap<String,Integer> map1;

	public DBOperation()throws IOException {
		db = DBMaker.fileDB("file.db").closeOnJvmShutdown().make();
		map= (HTreeMap<String, byte[]>) db.hashMap("map0").createOrOpen();
		map1= (HTreeMap<String, Integer>) db.hashMap("map1").createOrOpen();
	}

	/**
	 * To store a file fragment in the database
	 *
	 * @param key
     * @param value
	 * @throws IOException
	 */
	public void InsertFragment(String key,byte[] value) throws IOException{
		 Integer counter=0;
		 synchronized(this.map){
			 synchronized(this.map1){
				 if(map.containsKey(key)){
						 counter=map1.get(key);
						 map1.remove(key);
						 counter++;
						 map1.put(key, counter);
				 }
				else{
					map.put(key, value);
					map1.put(key, 1);
					}
			 }
		 }

		//  System.out.println("the insertion has been done!");
	}

	/**
	 * To remove a file fragment in the database
	 *
	 * @param key
	 * @throws IOException
	 */
	public void RemoveFragment(String key) throws IOException{
		 Integer counter=0;
		 synchronized(this.map){
			 synchronized(this.map1){
					    counter=map1.get(key);
						if(counter==1){
							map.remove(key);
							map1.remove(key);
						}
						else{
							map1.remove(key);
							counter--;
							map1.put(key, counter);
						}
			 }
		 }

	    //   System.out.println("the removal has been done!");
	}

	/**
	 * To retrieve a file fragment in the database
	 *
	 * @param key
	 * @throws IOException
	 */
	public byte[] RetrieveFragment(String key) throws IOException{
		 byte[] value2;
		 synchronized(this.map){
			  value2= map.get(key);
		 }

	    // System.out.println("the retrieval has been done!");
		return value2;
	}

}
