package cn.edu.ustc.onlinerate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GetTime  {
	private DateRecord record;
	private Timer timer;

	public GetTime(){
		record = new DateRecord();		//时间调用记录表
		timer = new Timer();
		timer.schedule(new GetNetworkTime(record),0,60*1000); //1分钟执行一次
	}

	public Double getonlinerate(){
		return (double)record.Getpercent() / 100.0;
	}

	public void closethread(){
		timer.cancel();
	}
}


class  GetNetworkTime extends TimerTask{
	private DateRecord record;
	public GetNetworkTime(DateRecord record){
		this.record = record;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
        Date date = new Date();
        String webUrl = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
        date = getWebsiteDatetime(webUrl);
 //       System.out.println(date);
        if(date != null){ //如果得到网站日期时间，即连接上网络，则将该时间调用记录存到记录表中
           record.insert(date);
//           System.out.println("insert");
        }
	}
   private static Date getWebsiteDatetime(String webUrl){
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            return date;
        } catch (MalformedURLException e) {
            // e.printStackTrace();
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return null;
    }
}
