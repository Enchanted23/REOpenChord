package cn.edu.ustc.chordinit;

import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Node {
    Node() {
        // nothing here
    }

    public void transfer_message(Request req, String peer) throws IOException {
        boolean sent = false;
        while (!sent) {
            try {
                Socket s = new Socket(peer, 1051);
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(req);
                out.flush();
                sent = true;
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException(e);
            }
        }
    }
}
