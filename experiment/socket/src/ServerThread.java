import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;

/**
 * @program: socket
 * @description: 线程池中的一个子线程，包含一个socket管道。
 * @author: Yang Xixuan
 * @create: 2022-09-22 19:54
 **/
public class ServerThread implements Runnable {
    private static Socket socket;//和本线程相关的Socket

    private static PrintStream ps;//输出流,输出给客户端

    private static Stack<File> rootDirStack = new Stack<>(); //目录栈

    public ServerThread(Socket socket, File rootDir) {
        this.socket = socket;
        rootDirStack.push(rootDir);
//        初始化输出流
//        1.从socket通信管道中得到一个字节输出流 负责向客户端发送数据
        OutputStream os = null;
        try {
            os = socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        2.把低级的字节流包装成打印流
        ps = new PrintStream(os);
    }

    /**
     * @param targetDir:
     * @return void
     * @author yangxixuan
     * @description 用于执行cd命令，客户端发送报文格式为："cd"+……，服务端传回报文格式为"end"
     */
    public static void cd(String targetDir) {
//        情况1：目标目录是..，返回上一级目录
        if (targetDir.equals("..")) {
            if (rootDirStack.size() <= 1) {
                ps.println("已经在根目录下，无法退回子目录");
                ps.flush();
            } else {
//                当前目录出栈
                rootDirStack.pop();
            }
        }
//        情况2：目标目录是.，返回当前目录
        else if (targetDir.equals(".")) {
            ps.println("当前目录为：" + rootDirStack.peek().getAbsolutePath());
            ps.flush();
        }
//        情况3：目标目录是其他目录，返回目标目录
        else {
//            数组表示此路径名表示的目录中的文件
            File[] files = rootDirStack.peek().listFiles();
            boolean flag = false;
            for (File file : files) {
                if (file.isDirectory() && file.getName().equals(targetDir)) {
//                    目录进栈
                    rootDirStack.push(file);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                ps.println("目标目录不存在");
                ps.flush();
            }
        }
//        发送结束包 否之接收方会一直接收处于接收状态
        ps.println("end");
    }

    /**
     * @param fileName:
     * @return void
     * @author yangxixuan
     * @description 用于执行get命令，发送端报文格式为"get"+文件名，服务端传回报文格式为"fileInfo:"+文件名+文件内容+oneTaskFinish"+……+"end"
     */
    public static void get(String fileName) {
        File[] files = rootDirStack.peek().listFiles();
        boolean flag = false;
        for (File file : files) {
//            情况1：传输文件
            if (file.isFile() && file.getName().equals(fileName)) {
                flag = true;
//                1.传输文件
                udpSend(file);
//                2.发送结束包 否之接收方会一直接收处于接收状态
                ps.println("end");
                break;
            }
//            情况2：传输文件夹，传输文件夹下所有非文件夹的文件
            else if (file.isDirectory() && file.getName().equals(fileName)) {
//                1.批量传输文件
                flag = true;
                File[] files1 = file.listFiles();
                boolean exist = false;
                for (File file1 : files1) {
                    if (!file1.isFile()) {
                        ps.println("非文件形式，无法使用get");
                    } else {
                        System.out.println("server: 文件" + file1.getName() + "存在，开始传输");
                        exist = true;
                        udpSend(file1);
                    }
                }
                if (!exist) {
                    ps.println("该文件夹为空");
                    ps.flush();
                }
//                2.发送结束包 否之接收方会一直接收处于接收状态
                ps.println("end");
                break;
            }
        }
        if (!flag) {
            ps.println("文件不存在");
//            发送结束包 否之接收方会一直接收处于接收状态
            ps.println("end");
            ps.flush();
        }
    }

    /**
     * @param file:
     * @return void
     * @author yangxixuan
     * @description 使用udp传输单个文件，报文格式："fileInfo:"+文件名+文件内容+oneTaskFinish"
     */
    public static void udpSend(File file) {
        try {
//            1.创建发送端对象：先写无参的构造函数
            DatagramSocket ds = new DatagramSocket();
//            2.文件读流
            FileInputStream fis = new FileInputStream(file);
//            3.文件包装读流
            BufferedInputStream bis = new BufferedInputStream(fis);
//            4.定义包裹
            DatagramPacket dp = null;//先等于null
//            5.定义一个byte数组
            byte[] b = new byte[1024];
//            6.传输文件
            ps.println("fileInfo:" + file.getName());
            while (true) {
                //开始读
                int n = bis.read(b);//1024个1024个的读
                if (n == -1) {
                    break;
                }
                Thread.sleep(10);//休眠 以避免发送/读取...太快发现漏包状态(丢失数据)
                //开始写
                /**
                 public DatagramPacket(byte buf[], int length,
                 InetAddress address, int port)
                 参数一：封装要发送的数据
                 参数二：发送数据的大小
                 参数三：服务端的主机IP地址
                 参数四：服务端的端口
                 */
                dp = new DatagramPacket(b, b.length, InetAddress.getLocalHost(), 2020);
                //开始发送
                ds.send(dp);
            }
//            7.发送结束包 否之接收方会一直接收处于接收状态
            dp = new DatagramPacket("oneTaskFinish".getBytes(), "oneTaskFinish".getBytes().length, InetAddress.getLocalHost(), 2020);
            ds.send(dp);
//            8.关闭流
            bis.close();
            fis.close();
            ds.close();
            System.out.println("发送完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description 用于执行ls命令，客户端发送报文格式为"ls"，服务端传回报文格式为自定义表格形式+"end"
     */
    public static void ls() {
//        1.列出路径名表示的目录中的文件
        File[] files = rootDirStack.peek().listFiles();
//        2.以表格形式返回,设置表头形式并返回
        String str = String.format("%-10s", "type") + String.format("%-40s", "name") + "  " + "size";
        ps.println(str);
        ps.flush();
//        3.按照格式返回文件名和文件大小
        for (File file : files) {
            if (file.isDirectory()) {
                str = String.format("%-10s", "<dir>") + String.format("%-40s", file.getName()) + "  " + formatSize(file);
            } else {
                str = String.format("%-10s", "<file>") + String.format("%-40s", file.getName()) + "  " + formatSize(file);
            }
            ps.println(str);
            ps.flush();
        }
//        4.发送结束包 否之接收方会一直接收处于接收状态
        ps.println("end");
    }

    /**
     * @param file:
     * @return long
     * @author yangxixuan
     * @description 用于获取文件的大小，这里的文件有可能是文件夹
     */
    public static long formatSize(File file) {
//        情况1：该文件是文件夹
        if (file.isDirectory()) {
            long size = 0;
            File[] files = file.listFiles();
//            迭代计算
            for (File f : files) {
                size += formatSize(f);
            }
            return size;
        }
//        情况2：该文件是文件
        else {
            return file.length();
        }
    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description 用于执行pwd命令，客户端发送报文格式为"pwd"，服务端传回报文格式为"当前目录为："+目录名+"end"
     */
    public static void pwd() {
        ps.println("当前目录为：" + rootDirStack.peek().getAbsolutePath());
        ps.println("end");
        ps.flush();
    }

    @Override
    public void run() {
        try {
//            向客户端发送"连接成功"的消息
            ps.println(socket.getLocalAddress() + ":" + socket.getLocalPort() + ">连接成功");
            ps.flush();
//            1.从socket通信管道中得到一个字节输入流
            InputStream is = socket.getInputStream();
//            2.把字节输入流包装成缓冲字符输入流进行消息的接收
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            3.按照行读取消息
            String msg;
            while ((msg = br.readLine()) != null) {
//                System.out.println(socket.getRemoteSocketAddress() + "说：" + msg);
//                情况1：客户端发送的消息格式为：bye
                if (msg.equals("bye")) {
                    break;
                }
//                情况2：客户端发送的消息格式为：cd dir
                else if (msg.startsWith("cd ")) {
                    String dir = msg.substring(3);
                    cd(dir);
                }
//                情况3：客户端发送的消息格式为：get file
                else if (msg.startsWith("get ")) {
                    String fileName = msg.substring(4);
                    get(fileName);
                }
//                情况4：客户端发送消息格式为：ls
                else if (msg.equals("ls")) {
                    ls();
                }
//                情况5：客户端发送消息格式为：pwd
                else if (msg.equals("pwd")) {
                    pwd();
                }
//                情况6：客户端发送消息格式不正确
                else {
                    ps.println("命令格式不正确");
//                    发送结束包 否之接收方会一直接收处于接收状态
                    ps.println("end");
                    ps.flush();
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }finally {
            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
