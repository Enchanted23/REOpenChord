package cn.edu.ustc.chordinit;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ReqServer implements Runnable {
    public void run () {
        try {
            ServerSocket serverSocket = new ServerSocket(1051);
            // ExecutorService executorService = new Executors.newFixedThreadPool(100);
            final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(200);
            ExecutorService executorService = new ThreadPoolExecutor(4, 8, 0L, TimeUnit.MILLISECONDS, queue);
            while (true) {
                Socket client = serverSocket.accept();
                // new HandlerThread(client);
                executorService.execute(new HandlerThread(client));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class HandlerThread implements Runnable {
        private Socket socket;

        public HandlerThread(Socket client) {
            socket = client;
            // new Thread(this).start();
        }

        public void run() {
            try {
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                Request req = (Request)input.readObject();
                ReqHandler.handle(req);
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        socket = null;
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
