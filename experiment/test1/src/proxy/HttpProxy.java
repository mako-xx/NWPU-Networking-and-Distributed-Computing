package proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * @className: HttpProxy
 * @description: 负责与目标服务器的通信
 * @author: Yang Xixuan
 **/
public class HttpProxy {

    /**
     * 文件保存根目录
     */
    static private String pathname = "/Users/yangxixuan/Desktop/Exercise 2/input/";

    /**
     * default HTTP port is port 80
     */
    private static int port = 80;

    /**
     * Allow a maximum buffer size of 8192 bytes
     */
    private static int buffer_size = 8192;

    /**
     * Response is stored in a byte array.
     */
    private byte[] buffer;

    /**
     * My socket to the world.
     */
    Socket socket = null;

    /**
     * Output stream to the socket.
     */
    BufferedOutputStream ostream = null;

    /**
     * Input stream from the socket.
     */
    BufferedInputStream istream = null;

    /**
     * StringBuffer storing the header
     */
    private StringBuffer header = null;

    /**
     * StringBuffer storing the response.
     */
    private StringBuffer response = null;

    /**
     * String to represent the Carriage Return and Line Feed character sequence.
     */
    static private String CRLF = "\r\n";

    /**
     * HttpClient constructor;
     */
    public HttpProxy() {
        buffer = new byte[buffer_size];
        header = new StringBuffer();
        response = new StringBuffer();
    }

    /**
     * <em>connect</em> connects to the input host on the default http port --
     * port 80. This function opens the socket and creates the input and output
     * streams used for communication.
     */
    public void connect(String host, int port) throws Exception {

        /**
         * Open my socket to the specified host at the default port.
         */
        socket = new Socket(host, port);

        /**
         * Create the output stream.
         */
        ostream = new BufferedOutputStream(socket.getOutputStream());

        /**
         * Create the input stream.
         */
        istream = new BufferedInputStream(socket.getInputStream());
    }

    /**
     * <em>processGetRequest</em> process the input GET request.
     */
    public void processGetRequest(String request, String host) throws Exception {
        /**
         * Send the request to the server.
         */
//        TCP连接建立后，客户端发送一个HTTP请求，服务器端返回一个HTTP响应，然后客户端和服务器端的TCP连接就断开了。
//        这样，客户端就不用等待服务器端的TCP连接关闭，就可以直接发送下一个HTTP请求了。
//        如果客户端发送的HTTP请求头中不包含Connection:close，那么客户端就必须等待服务器端的TCP连接关闭后，才能发送下一个HTTP请求。
//        如果客户端和服务器端之间的通信需要发送多个HTTP请求和接收多个HTTP响应，那么就不应该在HTTP请求头中加上Connection:close，以避免浪费时间.
        request += CRLF;
        request += "Host: " + host + CRLF;
        request += "Connection: Close" + CRLF + CRLF;
        buffer = request.getBytes();
        ostream.write(buffer, 0, request.length());
        ostream.flush();
        /**
         * waiting for the response.
         */
        processResponse();
    }

    /**
     * <em>processPutRequest</em> process the input PUT request.
     */
    public void processPutRequest(String request) throws Exception {
//        找文件
        String fileDir = request.split(" ")[1];
        File file = new File(pathname + fileDir);
        if (!file.exists()) {
            System.out.println("file not exist");
            return;
        } else {
            System.out.println("file exist");
//            加入“Content-Length:项目文件大小:头部
            request += "Content-Length: " + file.length() + CRLF;
            request += CRLF;
//            发送头部
            buffer = request.getBytes();
            ostream.write(buffer, 0, request.length());
            ostream.flush();
//            发送文件
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[buffer_size];
            int len = 0; // len为每次读入缓冲区的总字节数。如果已经到达文件末尾，没有更多数据，则为-1
            while ((len = fis.read(bytes)) != -1) { //将此输入流中最多bytes.length字节的数据读取到字节数组中。
                ostream.write(bytes, 0, len);
            }
            ostream.flush();
            fis.close();
        }
        /**
         * waiting for the response.
         */
        processResponse();
    }

    /**
     * <em>processResponse</em> process the server response.
     */
    public void processResponse() throws Exception {
//        处理超时
        socket.setSoTimeout(10000);
        try {
            int last = 0, c = 0; // last表示上一个字符，c表示当前字符
            /**
             * Process the header and add it to the header StringBuffer.
             */
            boolean inHeader = true; // loop control
            while (inHeader && ((c = istream.read()) != -1)) {
                switch (c) {
                    case '\r':
                        break;
                    case '\n':
                        if (c == last) {
                            inHeader = false;
                            break;
                        }
                        last = c;
                        header.append("\n");
                        break;
                    default:
                        last = c;
                        header.append((char) c);
                }
            }
            /**
             * Read the contents and add it to the response StringBuffer.
             */
            while (istream.read(buffer) != -1) {
                response.append(new String(buffer, "iso-8859-1"));
            }

        } catch (Exception e) {
            System.out.println("Time out!");
        }
    }

    /**
     * Get the response header.
     */
    public String getHeader() {
        return header.toString();
    }

    /**
     * Get the server's response.
     */
    public String getResponse() {
        return response.toString();
    }

    /**
     * Close all open connections -- sockets and streams.
     */
    public void close() throws Exception {
        socket.close();
        istream.close();
        ostream.close();
    }

}

