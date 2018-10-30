/************************************************************
 *
 * 				ChordLogging.java
 * date			:05.02.2016
 * copyright	:2016 Distributed File Systems Group
 *
 ************************************************************/
 package cn.edu.ustc.chordinit;

 import java.util.Enumeration;
 import java.util.UUID;
 import java.util.Properties;
 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.PrintWriter;
 import java.io.FileReader;
 import java.io.BufferedReader;
 import java.io.IOException;
 import java.net.InetAddress;
 import java.net.NetworkInterface;

 import de.uniba.wiai.lspi.chord.data.URL;
 import de.uniba.wiai.lspi.chord.service.Chord;
 import de.uniba.wiai.lspi.chord.service.ServiceException;

 // import com.barchart.udt.ExceptionUDT;
 //
 // import nodetest.NodeException;
 // import nodetest.PackException;

/**
 * Provides all interfaces (create, join, leave) for the
 * client to launch a Chord network.
 *
 * @author Wallflower
 * @author Qiang Xu
 */
public class ChordLogging{

	private static String DCOLON = ":";
	private static String SLASH = "/";
	private static String DCOLON_SLASHES = DCOLON + SLASH + SLASH;

	private static String addr2;

    private static Node sender;

    private static DBOperation shardDB;

	public ChordLogging()throws IOException {
        addr2=this.getLocalIP();
        // File uuid = new File("uuid");
        // if (!uuid.exists()) {
        //     // addr2 = UUID.randomUUID().toString();
        //     // PrintWriter fout = new PrintWriter(new FileOutputStream(uuid));
        //     // fout.write(addr2);
        //     // fout.close();
        // } else {
        //     BufferedReader fin = new BufferedReader(new FileReader(uuid));
        //     addr2 = fin.readLine();
        //     fin.close();
        // }
		System.out.println("the local host IP is " + addr2); //for test
	}

	/**
	 * to judge whether the operating system is Windows or not
	 * @return boolean
	 */

	 public static boolean isWindowsOS(){
		     boolean isWindowsOS = false;
		     String osName = System.getProperty("os.name");
		     if(osName.toLowerCase().indexOf("windows")>-1){
		      isWindowsOS = true;
		     }

		     return isWindowsOS;
	}

	/**
	 * to get the local IP
	 * @return String
	 */
	 public  String getLocalIP(){
	     String sIP = "";
	     InetAddress ip = null;
	     try {
	      // if it is the windows OS
	      if(isWindowsOS()){
	       ip = InetAddress.getLocalHost();
	       sIP = ip.getHostAddress();
	      }
	      // if not
	      else{
	       boolean bFindIP = false;
	       Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>)
	    		   NetworkInterface.getNetworkInterfaces();
	       while (netInterfaces.hasMoreElements()) {
	        if(bFindIP){
	         break;
	        }

	        NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();

	        // to traverse all IPs
	        Enumeration<InetAddress> ips = ni.getInetAddresses();
	        while (ips.hasMoreElements()) {
	         ip = (InetAddress) ips.nextElement();
	         if(  !ip.isLoopbackAddress()
	                    && ip.getHostAddress().indexOf(":")==-1){
	             bFindIP = true;
	             sIP = ip.getHostAddress();
	                break;
	            }
	        }
	       }
	      }
	     }
	     catch (Exception e) {
	      e.printStackTrace();
	     }

	     return sIP;
	 }

    public Chord initialize() throws IOException {
        // Properties properties = new Properties();
        // // InputStream input = null;

        // try {
        //     // input = new FileInputStream("mediator.properties");
        //     properties.load(ChordLogging.class.getClassLoader().getResourceAsStream("mediator.properties"));
		// } catch (IOException e) {
		// 	throw new RuntimeException(e);
		// }
        // try {
        //     sender = new nodetest.Node(addr2, properties.getProperty("host"), 23333);
        // } catch (ExceptionUDT | PackException | NodeException e) {
        //     throw new RuntimeException(e);
        // }

        try {
            shardDB = new DBOperation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ReqServer reqServer = new ReqServer();
        new Thread(reqServer).start();

        sender = new Node();

        File uuid = new File("bootstrapURL");
        if (!uuid.exists()) {
            // addr2 = UUID.randomUUID().toString();
            // PrintWriter fout = new PrintWriter(new FileOutputStream(uuid));
            // fout.write(addr2);
            // fout.close();
            return create();
        } else {
            BufferedReader fin = new BufferedReader(new FileReader(uuid));
            String addr3 = fin.readLine();
            fin.close();
            return join(addr3);
        }

    }

	// to create a chord network when there is no nodes
	private Chord create(){

		de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
		URL localURL = null;
		try {
			localURL = new URL(protocol + DCOLON_SLASHES + addr2 + SLASH);
		} catch (Exception e){
			throw new RuntimeException(e);
		}

		System.out.println("the URL has been created");
		System.out.println("localURL is " + localURL);//for test

		Chord chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl();
		try{
			chord.create(localURL);
		} catch (ServiceException e){
			throw new RuntimeException("Could not create DHT!", e);
		}

		System.out.println("the create has been done!"); //for test
        return chord;

	}

	// to join a chord network when some nodes already exist
	private Chord join(String addr3){

		de.uniba.wiai.lspi.chord.service.PropertiesLoader.loadPropertyFile();
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
		URL localURL = null;
		try {
			localURL = new URL(protocol + DCOLON_SLASHES + addr2 + SLASH);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		URL bootstrapURL = null;
		try {
			bootstrapURL = new URL(protocol + DCOLON_SLASHES + addr3 + SLASH);
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		Chord chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl();
		try{
			chord.join(localURL, bootstrapURL);
		} catch (ServiceException e){
			throw new RuntimeException("Could not join DHT!", e);
		}
        return chord;
	}

    public static Node getSender() {
        return sender;
    }

    public static String getID() {
        return addr2;
    }

    public static DBOperation getDB() {
        return shardDB;
    }

}
