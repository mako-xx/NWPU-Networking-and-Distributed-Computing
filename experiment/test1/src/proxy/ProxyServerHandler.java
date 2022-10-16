package proxy;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

/**
 * @className: ProxyServerHandler
 * @description: 线程池中的一个子线程。
 * @author: Yang Xixuan
 **/
public class ProxyServerHandler implements Runnable {
    //    与客户端通信的socket
    private Socket socket;

    //    与目标服务器通信的socket
    private static HttpProxy httpProxy = new HttpProxy();

    private static final String CRLF = "\r\n";
    private byte[] buffer;
    private BufferedOutputStream ostream;
    private BufferedInputStream istream;
    private String header = null;
    private String body = null;

    public ProxyServerHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description 从socket中获取输入输出流
     */
    public void getStream() throws IOException {
        ostream = new BufferedOutputStream(socket.getOutputStream());
        istream = new BufferedInputStream(socket.getInputStream());
    }

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 读取请求头
     */
    private String readHeader() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
        String line = null;
        try {
            line = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public void run() {
        try {
            // 从socket中获取输入输出流
            getStream();
            // 读取请求头
            String s = readHeader();
            // 解析请求头
            URL u = new URL(s);
            // 处理异常：请求头中的url不合法
            if (u == null) {
                System.out.println("URL is invalid");
                return;
            }
            // 与服务器建立连接，使用httpproxy的类
            httpProxy.connect(u.getHost(),u.getPort() == -1 ? 80 : u.getPort());
            // 发送请求头，使用HTTP1.0
            String request = "GET " + u.getFile() + " " + "HTTP/1.0";
            // 处理异常：连接超时在httpproxy.processResponse()中
            httpProxy.processGetRequest(request,u.getHost());
            // 接收服务器响应：响应头
            header = httpProxy.getHeader() + CRLF;
            // 接收服务器响应：响应体
            body = httpProxy.getResponse();
            // 将响应头转发给客户端
            buffer = header.getBytes();
            ostream.write(buffer);
            ostream.flush();
            // 将响应体转发给客户端
            buffer = body.getBytes();
            ostream.write(buffer);
            ostream.flush();
        } catch (Exception e) {
            System.out.println(e.toString());
            String exceptionInfo = e.toString();
            // 将代理服务器异常信息转发给客户端
            // 500 Internal Server Error
            String response = "HTTP/1.1 500 Internal ProxyServer Error"
                    + CRLF;
            response += "ProxyServer: " + "testProxyServer/1.0" + CRLF;
            response += "Content-type: " + "text" + CRLF;
            response += "Content-length: " + exceptionInfo.length() + CRLF
                    + CRLF;
            buffer = response.getBytes();
            try {
                ostream.write(buffer, 0, response.length());
                ostream.write(e.toString().getBytes());
                ostream.flush();
                ostream.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
