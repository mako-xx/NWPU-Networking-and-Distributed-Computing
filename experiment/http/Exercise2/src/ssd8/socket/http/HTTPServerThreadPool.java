package ssd8.socket.http;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @className: HTTPServerThreadPool
 * @description:
 * @author: Yang Xixuan
 **/
public class HTTPServerThreadPool {
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

    // HTTP服务器根目录
    private static File root;

    //    HTTP服务器端口号
    private static final int PORT = 80;

    public static void main(String[] args) {
        System.out.println("======服务器启动======");
//        1.服务器启动时传递root目录参数
        System.out.println("根目录参数：>" + args[0]);
//        2.验证root目录是否有效
        File rootDir = checkDirExist(args[0]);
//        3.根目录验证通过后，启动服务器
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//            4.接收到客户端的Socket管道连接后，将Socket管道连接交给线程池处理
            while (true) {
                Socket socket = serverSocket.accept();
                Runnable task = new HTTPServerHandler(socket, rootDir);
                pool.execute(task);
            }
        } catch (IOException e) {
            e.printStackTrace();}
    }

    private static File checkDirExist(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
//            1.检查一个对象是否是文件夹
            if(file.isDirectory()) {
                System.out.println("经校验该目录有效。");
                try {
//                    2. 返回文件的标准形式,路径将是根目录中的绝对唯一路径
                    file = file.getCanonicalFile();
                    return file;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("警告：该目录无效！");
            }
        }else{
            throw new RuntimeException("警告：该目录无效！");
        }
        return null;
    }
}
