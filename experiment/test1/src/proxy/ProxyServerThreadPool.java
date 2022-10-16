package proxy;

import com.sun.deploy.net.proxy.ProxyHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @className: ProxyServerThreadPool
 * @description: 使用线程池实现多线程处理客户端请求
 * @author: Yang Xixuan
 **/
public class ProxyServerThreadPool {
    //          一个线程池对象
//        public ThreadPoolExecutor(int corePoolSize,
//                                  int maximumPoolSize,
//                                  long keepAliveTime,
//                                  TimeUnit unit,
//                                  BlockingQueue<Runnable> workQueue,
//                                  RejectedExecutionHandler handler)
//    参数一：指定线程池的线程数量（核心线程）： corePoolSize
//    参数二：指定线程池可支持的最大线程数： maximumPoolSize
//    参数三：指定临时线程的最大存活时间： keepAliveTime
//    参数四：指定存活时间的单位(秒、分、时、天)： unit
//    参数五：指定任务队列： workQueue
//    参数六：指定用哪个线程工厂创建线程： threadFactory
//    参数七：指定线程忙，任务满的时候，新任务来了怎么办： handler
    private static ExecutorService pool = new ThreadPoolExecutor(3, 5, 6, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    //    代理服务器端口号
    private static final int PORT = 8000;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("proxy server start listening " + PORT + " port");
            while (true) {
                Socket socket = serverSocket.accept();
                pool.execute(new ProxyServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
