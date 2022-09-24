import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @program: Socket
 * @description: 客户端
 * @author: Yang Xixuan
 * @create: 2022-09-20 19:53
 **/
public class FileClient {

    private static int TCP_PORT = 2021;

    private static Socket socket;

    private static DatagramSocket datagramSocket;// udp接收端

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description tcp连接，发送命令
     */
    public static void tcpConnect() {
        try {
            System.out.println("======客户端启动======");
//            1.创建socket通信管道请求服务器的连接
//            public Socket(String host, int port)
//            参数一：服务端的IP地址
//            参数二：服务端的端口
            socket = new Socket(InetAddress.getLocalHost(), TCP_PORT);
//            System.out.println("======客户端连接成功======");
//            2.接收服务端连接成功的消息
            InputStream is = null;
            try {
                is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String str = br.readLine();
                System.out.println(str);
            } catch (IOException e) {
                throw new RuntimeException("警告：服务器连接失败！");
            }
//            3.发送消息给服务端
            communicate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description 判断命令，与服务端交互
     */
    public static void communicate() {
//        先打开udp连接，准备接收文件
        udpConnect();
//        1.1 从socket通信管道中得到一个字节输出流 负责发送数据
        OutputStream os = null;
        try {
            os = socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        1.2 从socket通信管道中得到一个字节输入流
        InputStream is = null;
        try {
            is = socket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        2.1 把低级的字节流包装成打印流
        PrintStream ps = new PrintStream(os);
//        2.2 把字节输入流包装成缓冲字符输入流
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        3.1 创建键盘输入流
        Scanner scanner = new Scanner(System.in);
//        3.2 按照行读取消息
        while (true) {
            System.out.println("请输入要发送的信息：");
            String msg = scanner.nextLine();
//            情况1：客户端发送bye
            if (msg.startsWith("bye")) {
//                发送消息
                ps.println(msg);
                ps.flush();
//                关闭资源
                datagramSocket.close();
                break;
            }
//            情况2：客户端发送get，使用udp传送文件
            else if (msg.startsWith("get ")) {
//                发送文件信息
                ps.println(msg);
                ps.flush();
//                接收消息，服务端返回格式为"fileInfo:"+文件名+文件内容+oneTaskFinish"
                String str = null;
                try {
                    while ((str = br.readLine()) != null) {
//                        情况1：服务端返回内容为所有文件传输完毕
                        if (str.equals("end")) {
                            break;
                        }
//                        情况2：服务端返回内容为文件
                        else if (str.startsWith("fileInfo:")) {
//                            1.获得文件名
                            String all[] = str.substring(9).split("\\s+");
                            String filename = all[0];
//                            2.udp接收文件
                            System.out.println("开始接收文件，文件名为：" + filename);
                            System.out.println("----------------");
                            udpReceive(filename);
                        }
//                        情况3：其他情况
                        else {
                            System.out.println(str);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
//            情况3：客户端发送cd或ls，与服务器交互
            else {
//                发送消息
                ps.println(msg);
                ps.flush();
//                接收消息
                String str = null;
                try {
//                    如果服务器发送end,说明此次传输结束
                    while ((str = br.readLine()) != null) {
                        if (str.equals("end")) {
                            break;
                        }
                        System.out.println(str);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description 打开udp连接，准备接收文件
     */
    public static void udpConnect() {
        try {
            System.out.println("======udp连接启动======");
//            1.创建接收端对象：注册端口（人）
            datagramSocket = new DatagramSocket(2020);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param fileName:
     * @return void
     * @author yangxixuan
     * @description 使用udp接收文件
     */
    public static void udpReceive(String fileName) {
        try {
//            1.创建文件
            File file = new File(fileName);
//            2.定义写流
            FileOutputStream fos = new FileOutputStream(file);
//            3.定义包装写流
            BufferedOutputStream bos = new BufferedOutputStream(fos);
//            4.定义数组
            byte[] b = new byte[1024];
//            5.定义数据包
            DatagramPacket dp = null;
//            6.边读边写
            while (true) {
                dp = new DatagramPacket(b, b.length);
//                开始接收
                datagramSocket.receive(dp);
//                判断接收来的结束包
                if (new String(b).contains("oneTaskFinish")) {
                    break;
                }
//                开始写入本地
                bos.write(b);
//                刷新
                bos.flush();
            }
//            7.关闭资源
            System.out.println("文件传输成功，文件名为:" + fileName + "   文件路径为:" + file.getAbsolutePath());
            bos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        tcpConnect(); // TCP连接
    }
}