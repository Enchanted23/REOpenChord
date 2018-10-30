package cn.edu.ustc.onlinerate;


import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;
import java.io.*;

public class DateRecord {
	private TreeMap<Calendar,Vector<Date>> oneDay;
	private long total;  //总记录数
    private double percent;  //在线率

    public DateRecord(){
		oneDay = new TreeMap<Calendar,Vector<Date>>();
		total =0 ;
		percent = 0.0;
		File file = new File("uptime_record.dat");
		if(!file.exists()){
			try {
				file.createNewFile();
				FileOutputStream outStream = new FileOutputStream("uptime_record.dat"); //map存哪需要再思考?
			    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
			    objectOutputStream.writeObject(oneDay);
				objectOutputStream.writeObject(total);
				objectOutputStream.writeObject(percent);
			    outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
		    FileInputStream freader;
	    	try{
			   freader = new FileInputStream("uptime_record.dat");
			   ObjectInputStream objectInputStream = new ObjectInputStream(freader);
		       oneDay = (TreeMap<Calendar,Vector<Date>>)objectInputStream.readObject();
			   total = (long)objectInputStream.readObject();
			   percent = (double)objectInputStream.readObject();
			   objectInputStream.close();
           } catch (IOException e) {
           // TODO Auto-generated catch block
               e.printStackTrace();
          }catch(ClassNotFoundException e){
        	   e.printStackTrace();
          }
		}
    }

	public int Getpercent(){
		return (int)(percent*100);
	}

    public void insert(Date date){
    	Calendar day = Calendar.getInstance();
        day.setTime(date);
    	if(oneDay.isEmpty() || (!oneDay.isEmpty() && day != oneDay.lastKey())){
    		Vector<Date> temp = new Vector<Date>();
    		oneDay.put(day,temp);
    	}
    	oneDay.lastEntry().getValue().add(date);
		total++;

	/*	NavigableSet<Calendar> it = oneDay.descendingKeySet();          //TODO需要删除一些旧的记录
		int i=0;
		long lastthree=0;


		while(it.hasnext()&&i<3){
		 temp[i] = it.next();
     	 i++;
	     lastthree +=  oneDay.get(temp[i]).size();
		}
		if(i==2&&temp[0]-1==temp[1]&&temp[1]-1==temp[2]){
			if(lastthree*2 >= total){
				Iterator it2 = oneDay.KeyIterator();
				while((Calendar ob = it2.next())<temp[2]){
					total -= oneDay.get(ob).size();
					oneDay.remove(ob);
				}
			}
		}*/
		//更新上机率
		Date begin = oneDay.firstEntry().getValue().get(0);
		Date end = oneDay.lastEntry().getValue().get(oneDay.lastEntry().getValue().size()-1);
		long time_begin = begin.getTime();
		long time_end = end.getTime();
		long idealtotal = (time_end - time_begin)/(60*1000);
		percent = (float)(total)/(idealtotal);
		//将map写入到文件中
		try{
			FileOutputStream outStream = new FileOutputStream("uptime_record.dat"); //map存哪需要再思考?	
		    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
		    objectOutputStream.writeObject(oneDay);
			objectOutputStream.writeObject(total);
			objectOutputStream.writeObject(percent);
		    outStream.close();
		}catch (FileNotFoundException e) {
           e.printStackTrace();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

}
