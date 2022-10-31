package ssd8.socket.http;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;

/**
 * @className: HTTPServerHandler
 * @description: HTTP服务器处理器，用于处理客户端请求
 * @author: Yang Xixuan
 **/
public class HTTPServerHandler implements Runnable {

    private Socket socket; // 与客户端通信的Socket管道

    private File rootDir; // HTTP服务器根目录

    BufferedInputStream istream = null; // 输入流

    BufferedOutputStream ostream = null; // 输出流

    Writer out;

    private static String CRLF = "\r\n";

    public static final String SAVE_DIR = "/Users/yangxixuan/Desktop/Exercise2/output";

    public HTTPServerHandler(Socket socket, File rootDir) throws IOException {
        this.socket = socket;
        this.rootDir = rootDir;
    }

    public void initStream() {
        try {
            istream = new BufferedInputStream(socket.getInputStream());
            ostream = new BufferedOutputStream(socket.getOutputStream());
            out = new OutputStreamWriter(ostream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("接收到客户端的请求，开始处理...");
        initStream();

//        1.1 读取客户端请求头
        StringBuilder requestLine = new StringBuilder();
        int ch = 0;
        try {
            int c;
            while ((c = istream.read()) != -1) {
                requestLine.append((char) c);
                if (c == '\n') {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        1.2 分析请求头，获取请求资源
        String[] requestLineArray = requestLine.toString().split(" ");//reqs1->put/get reqs2->name reqs3->http version
        String requestMethod = requestLineArray[0]; // 请求方法

//        2.响应客户端请求，服务器只需成功地响应使用GET和PUT方法的请求，但对其他所有请求（有效的或无效的）的都应返回RFC指定的响应
        try {

//            2.1响应客户端GET请求
            if (requestMethod.equals("GET")) {
//                情况1：请求格式错误，发送400响应
                if (requestLineArray.length != 3) {
                    send400("HTTP/1.1");
                }
//                情况2：请求格式正确
                else {
                    String uri = requestLineArray[1]; // 请求资源
                    String httpVersion = requestLineArray[2].substring(0, requestLineArray[2].length() - 1); // HTTP版本,去掉末尾的\n
                    doGet(uri, httpVersion); // 处理GET请求
                }
            }

//            2.2响应客户端PUT请求
            else if (requestMethod.equals("PUT")) {
//                情况1：请求格式错误，发送400响应
                if (requestLineArray.length != 3) {
                    send400("HTTP/1.1");
                }
//                情况2：请求格式正确
                else {
                    String uri = requestLineArray[1]; // 请求资源
                    String httpVersion = requestLineArray[2].substring(0, requestLineArray[2].length() - 1); // HTTP版本,去掉末尾的\n
//                    获取请求体长度
                    String requestHeader = processRequest();
                    int contentLen = Integer.parseInt(requestHeader.substring(
                            requestHeader.indexOf("Content-Length: ") + 16, requestHeader.length() - 1));
                    doPut(uri, httpVersion, contentLen); // 处理PUT请求
                }
            } else {
//                情况1：请求格式错误，发送400响应
                if (requestLineArray.length != 3) {
                    send400("HTTP/1.1");
                }
//                情况2：请求格式正确
                else {
//                    情况2.1：发送501响应
                    try {
                        String httpVersion = requestLineArray[2].substring(0, requestLineArray[2].length() - 1); // HTTP版本,去掉末尾的\n
                        send501(httpVersion);
                    } catch (Exception e) {
//                        情况2.2：发送400响应
                        send505();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            send404("HTTP/1.1");
        } finally {
//            如果客户端意外关闭，确保服务器正在运行
            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * @param uri:
     * @param httpVersion:
     * @param contentLen:
     * @return void
     * @author yangxixuan
     * @description 处理PUT请求, 将请求体中的内容写入文件
     * 如果请求的资源不存在，则创建该资源，返回201响应
     * 如果请求的资源存在，则覆盖该资源，返回200响应
     * 如果http版本不为1.1，则返回505响应（http1.0不支持put）
     * 如果写入文件失败，则返回500响应
     * 响应状态码为201,响应头中包含Content-Length和Content-Type,响应体中包含请求体中的内容,响应格式为HTTP/1.1,响应头和响应体之间用一个空行隔开,响应体中的内容为请求体中的内容,响应体中的内容不包含请求头中的内容,
     */
    private void doPut(String uri, String httpVersion, int contentLen) {
        System.out.println("处理PUT请求...");
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }
        File file = new File(rootDir, uri);
        int Flags = 1; // 标志位，用于判断文件是否存在

//        1.检查http版本
//        情况1：如果不是HTTP/1.1,则返回505响应
        if (!httpVersion.startsWith("HTTP/1.1")) {
            System.out.println("http版本错误");
            send505(); // 发送505响应
            return;
        }
//        情况2：如果是HTTP/1.1,则继续处理
        else {
            try {

//                2.检查文件是否存在
//                情况1：文件不存在,则创建文件,标志为1
                if (!file.exists()) {
                    System.out.println("文件不存在");
                    file.createNewFile();
                    Flags = 1;
                }
//                情况2：文件存在,则覆盖文件,标志为2
                else {
                    System.out.println("文件存在");
                    Flags = 2;
                }

//                3.将请求体中的内容写入文件
                writeFile(file, contentLen);
            } catch (IOException e) {
                e.printStackTrace();
//                如果写入文件失败，则返回500响应
                send500();
                return;
            }
        }

//        4.根据标志位返回响应
//        情况1：文件不存在,则创建文件,返回201响应
        if (Flags == 1) {
            send201("HTTP/1.1"); // 发送201响应
        }
//        情况2：文件存在,则覆盖文件,返回200响应
        else if (Flags == 2) {
            send200("HTTP/1.1"); // 发送200响应
        }
    }

    /**
     * @param file:
     * @param contentLen:
     * @return void
     * @author yangxixuan
     * @description PUT请求写入文件, 写入的内容长度为contentLen
     */
    private void writeFile(File file, int contentLen) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len = 0;
        int total = 0;
        while ((len = istream.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            total += len;
            if (total >= contentLen) {
                break;
            }
        }
        fos.close();
    }


    /**
     * @param uri:
     * @param httpVersion:
     * @return void
     * @author yangxixuan
     * @description 处理GET请求, 返回响应
     * 如果请求的资源不存在,则返回404响应,
     * 如果请求的资源存在,则返回200响应,并将资源内容写入输出流,发送给客户端,
     * 如果http版本不能识别,则返回505响应
     */
    private void doGet(String uri, String httpVersion) throws IOException {
        System.out.println("处理GET请求...");
        httpVersion = httpVersion.toUpperCase();
        File file = null; // 判断file是否存在

//        1.检查http版本
//        情况1：http版本正确
        if (httpVersion.startsWith("HTTP/1.0") || httpVersion.startsWith("HTTP/1.1")) {
//            情况1：请求资源有"/"
            if (uri.startsWith("/")) {
                file = new File(rootDir, uri.substring(1));
            }
//            情况2：请求资源缺少"/"
            else {
                file = new File(rootDir, uri);
            }
        }
//        情况2：http版本无法识别,发送505响应
        else {
            send505();
            return;
        }

//        2.检查文件是否存在
//        情况1：文件存在,发送200响应和文件内容
        if (file.exists() && file.canRead()) {
            String contentType = getFileType(file);
            try {
                send200(httpVersion, contentType, file); // 发送200响应和文件内容
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
//        情况2：文件不存在,发送404响应
        else {
            send404(httpVersion);
        }

    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description: 505: HTTP Version Not Supported
     */
    private void send505() {
        System.out.println("发送HTTP Error 505: HTTP Version Not Supported");
//        1.生成响应体
        String errorBody = getResponseBody(
                "HTTP Version Not Supported",
                "HTTP Error 505: HTTP Version Not Supported"
        );
//        2.生成响应头
        String errorHeader = getResponseHeader(
                "HTTP/1.1",
                "505 HTTP Version Not Supported",
                "text/html; charset=utf-8",
                errorBody.length()
        );
//        3.发送响应
        try {
            ostream.write(errorHeader.getBytes());
            ostream.write(errorBody.getBytes());
            ostream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param httpVersion:
     * @return void
     * @author yangxixuan
     * @description 501: Not Implemented
     */
    private void send501(String httpVersion) {
        System.out.println("发送HTTP Error 501: Not Implemented");
//        1.生成响应体
        String errorBody = getResponseBody(
                "Not Implemented",
                "HTTP Error 501: Not Implemented"
        );
//        2.生成响应头
        String errorHeader = getResponseHeader(
                httpVersion,
                "501 Not Implemented",
                "text/html; charset=utf-8",
                errorBody.length()
        );
//        3.发送响应
        try {
            ostream.write(errorHeader.getBytes());
            ostream.write(errorBody.getBytes());
            ostream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description 500: Internal Server Error
     */
    private void send500() {
        System.out.println("发送HTTP Error 500: Internal Server Error");
//        1.生成响应体
        String errorBody = getResponseBody(
                "Internal Server Error",
                "HTTP Error 500: Internal Server Error"
        );
//        2.生成响应头
        String errorHeader = getResponseHeader(
                "HTTP/1.1",
                "500 Internal Server Error",
                "text/html; charset=utf-8",
                errorBody.length()
        );
//        3.发送响应
        try {
            ostream.write(errorHeader.getBytes());
            ostream.write(errorBody.getBytes());
            ostream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param httpVersion:
     * @return void
     * @author yangxixuan
     * @description 404 文件不存在
     */
    private void send404(String httpVersion) {
        System.out.println("发送HTTP Error 404: Not Found");
        if (httpVersion.equals("")) {
            httpVersion = "HTTP/1.1";
        }

//        1.生成响应体
        String errorBody = getResponseBody(
                "Not Found",
                "HTTP Error 404: Not Found"
        );
//        2.生成响应头
        String errorHeader = getResponseHeader(
                httpVersion,
                "404 Not Found",
                "text/html; charset=utf-8",
                errorBody.length()
        );
//        3.发送响应
        try {
            ostream.write(errorHeader.getBytes());
            ostream.write(errorBody.getBytes());
            ostream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param httpVersion:
     * @return void
     * @author yangxixuan
     * @description 400 请求格式错误
     */
    private void send400(String httpVersion) {
        System.out.println("发送HTTP Error 400: Bad Request");
//        1.生成响应体
        String errorBody = getResponseBody(
                "Bad Request",
                "HTTP Error 400: Bad Request"
        );
//        2.生成响应头
        String errorHeader = getResponseHeader(
                httpVersion,
                "400 Bad Request",
                "text/html; charset=utf-8",
                errorBody.length()
        );
//        3.发送响应
        try {
            ostream.write(errorHeader.getBytes());
            ostream.write(errorBody.getBytes());
            ostream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param httpVersion:
     * @return void
     * @author yangxixuan
     * @description PUT请求的201响应, 201 Created
     */
    private void send201(String httpVersion) {
        System.out.println("发送HTTP 201 Created");
//        1.生成响应头
        String header = getResponseHeader(
                httpVersion,
                "201 Created",
                "text/html; charset=utf-8",
                0
        );
//        2.发送响应
        try {
            ostream.write(header.getBytes());
            ostream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param httpVersion:
     * @param contentType:
     * @param file:
     * @return void
     * @author yangxixuan
     * @description GET请求的200响应, 发送文件内容
     */
    private void send200(String httpVersion, String contentType, File file) throws IOException {
        System.out.println("发送HTTP 200 OK");
        byte[] fileData = Files.readAllBytes(file.toPath());
//        1.生成响应头
        String header = getResponseHeader(
                httpVersion,
                "200 OK",
                contentType,
                fileData.length
        );
//        2.发送响应
        ostream.write(header.getBytes());
        ostream.write(fileData); //响应体
        ostream.flush();
    }

    /**
     * @param httpVersion:
     * @return void
     * @author yangxixuan
     * @description PUT请求的200响应
     */
    private void send200(String httpVersion) {
        System.out.println("发送HTTP 200 OK");
//        1.生成响应头
        String header = getResponseHeader(
                httpVersion,
                "200 OK",
                "text/html; charset=utf-8",
                0
        );
//        2.发送响应
        try {
            ostream.write(header.getBytes());
            ostream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param file:
     * @return String
     * @author yangxixuan
     * @description 获取文件类型，用于生成响应头Content-Type字段
     * 支持的文件类型：.htm, .html, .txt, .jpg, .jpg, .png, .gif
     * 如果无法识别，则返回application/octet-stream
     */
    private String getFileType(File file) {
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        String contentType = null;
        switch (fileType) {
            case "htm":
                contentType = "text/html";
                break;
            case "html":
                contentType = "text/html";
                break;
            case "txt":
                contentType = "text/plain";
                break;
            case "jpg":
                contentType = "image/jpeg";
                break;
            case "gif":
                contentType = "image/gif";
                break;
            case "png":
                contentType = "image/png";
                break;
            default:
                contentType = "application/octet-stream";
                break;
        }
        return contentType;
    }

    /**
     * @param title:
     * @param body:
     * @return String
     * @author yangxixuan
     * @description 生成响应体, html格式, 包含title和body, body中包含错误信息
     */
    private String getResponseBody(String title, String body) {
        StringBuilder sb = new StringBuilder().
                append("<!DOCTYPE html>" + CRLF).
                append("<html>" + CRLF).
                append("<head>" + CRLF).
                append("<meta charset=\"UTF-8\">" + CRLF).
                append("<title>" + title + "</title>" + CRLF).
                append("</head>" + CRLF).
                append("<body>" + CRLF).
                append("<h1>" + body + "</h1>" + CRLF).
                append("</body>" + CRLF).
                append("</html>" + CRLF);
        return sb.toString();
    }

    /**
     * @param address:
     * @return String
     * @author yangxixuan
     * @description 服务器必须能够将.jpg图像嵌入到HTML文档中，以便浏览器能够正确显示图像。
     * 生成响应体, html格式, 包含title和body, body中包含图片
     * 选择性使用，如果不使用，可以使用send200(String httpVersion, String contentType, File file)方法直接将图片文件发送给客户端
     */
    private String getResponseBody(String address) {
        StringBuilder sb = new StringBuilder().
                append("<!DOCTYPE html>" + CRLF).
                append("<html>" + CRLF).
                append("<head>" + CRLF).
                append("<meta charset=\"UTF-8\">" + CRLF).
                append("<title>Image</title>" + CRLF).
                append("</head>" + CRLF).
                append("<body>" + CRLF).
                append("<img src=\"" + address + "\"/>" + CRLF).
                append("</body>" + CRLF).
                append("</html>" + CRLF);
        return sb.toString();
    }

    /**
     * @param httpType:
     * @param state:
     * @param contentType:
     * @param length:
     * @return String
     * @author yangxixuan
     * @description 生成响应头, 返回响应头字符串
     */
    private String getResponseHeader(String httpType, String state, String contentType, int length) {
//        情况1：如果http版本为1.0,则在响应头中添加Connection: close字段
        if (httpType.equals("HTTP/1.0")) {
            return new StringBuilder().
                    append(httpType + " " + state + CRLF).
                    append("Server: MyHttpServer/1.0" + CRLF).
                    append("Content-Type: " + contentType + CRLF).
                    append("Content-Length: " + length + CRLF).
                    append("Date: " + new Date() + CRLF).
                    append("Connection: close" + CRLF).
                    append(CRLF).
                    toString();
        }
//        情况2：如果http版本为1.1,则在响应头中添加Connection: keep-alive字段
        else {
            return new StringBuilder().
                    append(httpType + " " + state + CRLF).
                    append("Server: MyHttpServer/1.0" + CRLF).
                    append("Content-Type: " + contentType + CRLF).
                    append("Content-Length: " + length + CRLF).
                    append("Date: " + new Date() + CRLF).
                    append("Connection: keep-alive" + CRLF).
                    append(CRLF).
                    toString();
        }
    }

    private String processRequest() throws IOException {
        int last = 0, c = 0;
        StringBuilder requestInfo = new StringBuilder();
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
                    requestInfo.append("\n");
                    break;
                default:
                    last = c;
                    requestInfo.append((char) c);
            }
        }
        return requestInfo.toString();
    }
}


