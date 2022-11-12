package server;

import bean.TodoItem;
import bean.User;
import server.TodoInterface;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

/**
 * @className: TodoService
 * @description: TodoService class implements TodoInterface interface
 * @author: Yang Xixuan
 **/
@WebService
public class TodoService implements TodoInterface {

    /**
     * 用户列表
     */
    private Vector<User> users = new Vector<>();

    /**
     * 当前登录的用户
     */
    private User currentUser;

    /**
     * 待办事项列表
     */
    private Vector<TodoItem> todoItems = new Vector<>();

    /**
     * 用于时间转换格式
     */
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");

    @Override
    public String userRegister(String name, String pwd) throws Exception {
//        情况1：用户名已存在，注册失败
        for (User user : users) {
            if (user.getName().equals(name)) {
                return "REGISTER FAIL: USER ALREADY EXISTS";
            }
        }
//        情况2：用户名不存在，注册成功
        users.add(new User(name, pwd));
        return "REGISTER SUCCESS";
    }

    @Override
    public String userLogin(String name, String pwd) throws Exception {
        int flag = 0; //标志位，用于判断用户是否存在
        for (User u : users) {
            if (u.getName().equals(name)) {
                flag = 1;
//                情况1：用户存在，密码正确
                if (u.getPwd().equals(pwd)) {
                    currentUser = u;
                    return "LOGIN SUCCESS";
                }
            }
        }
//        情况2：用户不存在
        if (flag == 0) {
            return "LOGIN FAIL: USER DOES NOT EXIST";
        }
//        情况3：用户存在，密码错误
        return "LOGIN FAIL: WRONG PASSWORD";
    }

    @Override
    public String addTodoItem(String start, String end, String description) throws Exception {
//        情况1：用户未登陆
        if (currentUser == null) {
            return "ADD TODO ITEM FAIL: USER NOT LOGGED IN";
        }
        try {
            Date startDate = simpleDateFormat.parse(start);
            Date endDate = simpleDateFormat.parse(end);
//            情况2：开始时间在结束时间之前，或开始时间与结束时间一致，添加失败
            if (startDate.after(endDate) || startDate.equals(endDate)) {
                return "ADD TODO ITEM FAIL: START TIME LATER THAN END TIME";
            }
//            情况3：开始时间在结束时间之后，添加成功
            todoItems.add(new TodoItem(description, currentUser, startDate, endDate));
            return "ADD TODO ITEM SUCCESS";
        }
//        情况4：时间格式不正确
        catch (Exception e) {
            return "ADD TODO ITEM FAIL: WRONG TIME FORMAT";
        }
    }

    @Override
    public String queryTodoItem(String start, String end) throws Exception {
//        情况1：用户未登陆
        if (currentUser == null) {
            return "QUERY TODO ITEM FAIL: USER NOT LOGGED IN";
        }
        try {
            Date startDate = simpleDateFormat.parse(start);
            Date endDate = simpleDateFormat.parse(end);
//            情况2：开始时间在结束时间之前，或开始时间与结束时间一致，查询失败
            if (startDate.after(endDate) || startDate.equals(endDate)) {
                return "QUERY TODO ITEM FAIL: START TIME LATER THAN END TIME";
            }
//            情况3：开始时间在结束时间之后，查询成功，返回每一项的描述和时间
            StringBuilder result = new StringBuilder();
            for (TodoItem todoItem : todoItems) {
                if (todoItem.getOwner().equals(currentUser)) {
                    if (todoItem.getStart().after(startDate) && todoItem.getEnd().before(endDate) ||
                            todoItem.getStart().equals(startDate) && todoItem.getEnd().before(endDate) ||
                            todoItem.getStart().after(startDate) && todoItem.getEnd().equals(endDate) ||
                            todoItem.getStart().equals(startDate) && todoItem.getEnd().equals(endDate)) {
                        result.append(todoItem.toString()).append("\n");
                    }
                }
            }
            return result.toString();
        }
//        情况4：时间格式不正确
        catch (Exception e) {
            return "QUERY TODO ITEM FAIL: WRONG TIME FORMAT";
        }
    }

    @Override
    public String deleteTodoItem(String uuid) throws Exception {
//        情况1：用户未登陆
        if (currentUser == null) {
            return "DELETE TODO ITEM FAIL: USER NOT LOGGED IN";
        }
        for (TodoItem todoItem : todoItems) {
            if (todoItem.getUuid().equals(uuid)) {
//                情况2：创建者不是当前用户
                if (!todoItem.getOwner().equals(currentUser)) {
                    return "DELETE TODO ITEM FAIL: NOT OWNER";
                }
//                情况3：创建者是当前用户，删除成功
                todoItems.remove(todoItem);
                return "DELETE TODO ITEM SUCCESS";
            }
        }
//        情况4：uuid不存在
        return "DELETE TODO ITEM FAIL: UUID DOES NOT EXIST";
    }

    @Override
    public String clearTodoItem() throws Exception {
//        情况1：用户未登陆
        if (currentUser == null) {
            return "CLEAR TODO ITEM FAIL: USER NOT LOGGED IN";
        }
        int flag = 0;
        Iterator<TodoItem> it = todoItems.iterator();
        while (it.hasNext()) {
            TodoItem todoItem = it.next();
//            情况2：创建者是当前用户，删除成功
            if (todoItem.getOwner().equals(currentUser)) {
                it.remove();
                flag = 1;
            }
        }
//        情况2：用户存在，但没有待办事项
        if (flag == 0) {
            return "CLEAR TODO ITEM FAIL: NO TODO ITEM";
        }
//        情况3：用户存在，且有待办事项，删除成功
        return "CLEAR TODO ITEM SUCCESS";
    }

    @Override
    public String userLogout() throws Exception {
//        情况1：用户未登陆
        if (currentUser == null) {
            return "LOGOUT FAIL: USER NOT LOGGED IN";
        }
//        情况2：用户已登陆，注销成功
        currentUser = null;
        return "LOGOUT SUCCESS";
    }

    @Override
    public String help() throws Exception {
        return "time format: yyyy/MM/dd/HH:mm:ss\n" +
                "REGISTER: register <username> <password>\n" +
                "LOGIN: login <username> <password>\n" +
                "ADD TODO ITEM: add <start> <end> <description>\n" +
                "QUERY TODO ITEM: query <start> <end>\n" +
                "DELETE TODO ITEM: delete <uuid>\n" +
                "CLEAR TODO ITEM: clear\n" +
                "LOGOUT: logout\n" +
                "HELP: help";
    }

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/webservice/TodoService", new TodoService()); // 发布服务
        System.out.println("TodoService is published"); // 输出提示信息
    }
}
