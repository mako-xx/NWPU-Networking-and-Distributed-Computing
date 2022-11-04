package server;

import bean.Meeting;
import bean.User;
import rface.MeetingInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @className: MeetingManager
 * @description: 会议管理类，实现MeetingInterface接口
 * @author: Yang Xixuan
 **/
public class MeetingManager extends UnicastRemoteObject implements MeetingInterface {

    /**
     * 用户列表
     */
    private Vector<User> users = new Vector<>();

    /**
     * 当前登录的用户
     */
    private User currentUser;

    /**
     * 会议列表
     */
    private Vector<Meeting> meetings = new Vector<>();

    /**
     * 用于时间转换格式
     */
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm");

    public MeetingManager() throws RemoteException {
        super();
    }

    /**
     * @param name:
     * @param pwd:
     * @return String
     * @author yangxixuan
     * @description 用户注册
     */
    @Override
    public String userRegister(String name, String pwd) throws Exception {
//        情况1：用户已存在
        for (User u : users) {
            if (u.getName().equals(name)) {
                return "register FAIL: user name already exists";
            }
        }
//        情况2：用户不存在，注册成功
        users.add(new User(name, pwd));
        return "register SUCCESS";
    }

    /**
     * @param name:
     * @param pwd:
     * @return String
     * @description 用户登录
     */
    @Override
    public String userLogin(String name, String pwd) throws Exception {
        int flag = 0; //标志位，用于判断用户是否存在
        for (User u : users) {
            if (u.getName().equals(name)) {
                flag = 1;
//                情况1：用户存在，密码正确
                if (u.getPassword().equals(pwd)) {
                    currentUser = u;
                    return "login SUCCESS";
                }
            }
        }
//        情况2：用户不存在
        if (flag == 0) {
            return "login FAIL: user not exists";
        }
//        情况3：用户存在，密码错误
        return "login FAIL: password error";
    }

    /**
     * @param :
     * @return null
     * @author yangxixuan
     * @description 用户注销
     */
    @Override
    public String userLogout() throws Exception {
//        情况1：用户未登陆
        if (currentUser == null) {
            return "logout FAIL: user not exists";
        }
//        情况2：用户已登陆，注销成功
        currentUser = null;
        return "logout SUCCESS";
    }

    /**
     * @param participant:
     * @param start:
     * @param end:
     * @param title:
     * @return String
     * @author yangxixuan
     * @description 用户创建会议
     */
    @Override
    public String createMeeting(String participant, String start, String end, String title) throws Exception {
//        情况1：用户未登陆
        if (currentUser == null) {
            return "create meeting FAIL: user not exists";
        }
//        情况2：参与人不存在
        int flag = 0;
        for (User u : users) {
            if (u.getName().equals(participant)) {
                flag = 1;
            }
        }
        if (flag == 0) {
            return "create meeting FAIL: participant not exists";
        }
//        情况3：参会人与当前用户相同
        if (currentUser.getName().equals(participant)) {
            return "create meeting FAIL: participant is the same as the creator";
        }
//        情况4：用户的新会议本身时间不合法
        Date startDate = simpleDateFormat.parse(start);
        Date endDate = simpleDateFormat.parse(end);
        if (startDate.after(endDate)) {
            return "create meeting FAIL: start time is after end time";
        }
//        情况5：用户的新会议与已经存在的会议出现时间重叠
        for (Meeting m : meetings) {
//            当前用户或参会人的议程中有重叠，返回错误信息
            if (m.getHost().equals(currentUser.getName())
                    || m.getParticipant().equals(currentUser.getName())
                    || m.getHost().equals(participant)
                    || m.getParticipant().equals(participant)) {
//                开始时间在另一个会议的开始和结束之间或结束时间在另一个会议的开始和结束之间，并考虑时间相等的情况
                if (startDate.after(m.getStartDate()) && startDate.before(m.getEndDate())
                        || endDate.after(m.getStartDate()) && endDate.before(m.getEndDate())
                        || startDate.equals(m.getStartDate())
                        || endDate.equals(m.getEndDate())) {
                    return "create meeting FAIL: time conflict";
                }
            }
        }
//        情况6：成功创建会议
        Meeting m = new Meeting(currentUser.getName(), participant, startDate, endDate, title);
        meetings.add(m);
        return "create meeting SUCCESS:\nyour meeting info: \n" + m.toString();
    }

    /**
     * @param start:
     * @param end:
     * @return String
     * @author yangxixuan
     * @description 用户查询会议
     */
    @Override
    public String queryMeeting(String start, String end) throws Exception {
//        情况1：用户未登陆
        if (currentUser == null) {
            return "query meeting FAIL: user not exists";
        }
//        情况2：用户的查询时间不合法
        Date startDate = simpleDateFormat.parse(start);
        Date endDate = simpleDateFormat.parse(end);
        if (startDate.after(endDate)) {
            return "query meeting FAIL: start time is after end time";
        }
//        情况3：在此时间区间没有查询到符合条件的会议
        List<Meeting> result = new ArrayList<>();
        int flag = 0;
        for (Meeting m : meetings) {
//            当前用户是主持人或是参会人
            if (m.getHost().equals(currentUser.getName())
                    || m.getParticipant().equals(currentUser.getName())) {
//                考虑时间相等的情况
                if (startDate.before(m.getStartDate()) && endDate.after(m.getEndDate())
                        || startDate.equals(m.getStartDate()) && endDate.after(m.getEndDate())
                        || startDate.before(m.getStartDate()) && endDate.equals(m.getEndDate())
                        || startDate.equals(m.getStartDate()) && endDate.equals(m.getEndDate())) {
                    result.add(m);
                    flag = 1;
                }
            }
        }
        if (flag == 0) {
            return "query meeting FAIL: no meeting in this time";
        }
//        情况4：成功查询到会议，返回的结果列表按时间排序
        Collections.sort(result, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append("query meeting SUCCESS:\n");
        for (Meeting m : result) {
            sb.append(m.toString() + "\n");
        }
        return sb.toString();
    }

    /**
     * @param uuid:
     * @return String
     * @author yangxixuan
     * @description 用户删除会议
     */
    @Override
    public String deleteMeeting(String uuid) throws Exception {
//        情况1：用户未登陆
        if (currentUser == null) {
            return "delete meeting FAIL: user not exists";
        }
        for (Meeting m : meetings) {
            if (m.getUuid().equals(uuid)) {
//                情况2：会议主人不是当前用户
                if (!m.getHost().equals(currentUser.getName())) {
                    return "delete meeting FAIL: you are not the creator of this meeting";
                }
//                情况3：成功删除会议
                else {
                    meetings.remove(m);
                    return "delete meeting SUCCESS";
                }
            }
        }
//        情况4：会议不存在
        return "delete meeting FAIL: meeting not exists";
    }

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 用户清空会议
     */
    @Override
    public String clearMeeting() throws Exception {
//        情况1：用户未登陆
        if (currentUser == null) {
            return "clear meeting FAIL: user not exists";
        }
        int flag = 0;
        for (Meeting m : meetings) {
            if (m.getHost().equals(currentUser.getName())) {
                meetings.remove(m);
                flag = 1;
                break;
            }
        }
//        情况2：当前用户没有创建会议
        if (flag == 0) {
            return "clear meeting FAIL: you have no meeting";
        }
//        情况3：成功清空会议
        return "clear meeting SUCCESS";
    }

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 获取用户列表，只有管理员才能执行此操作
     */
    @Override
    public String getUserList() throws Exception {
//        判断是否是管理员
        if (currentUser == null || !currentUser.getName().equals("admin")) {
            return "get user list FAIL: you are not admin";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("user list:\n");
        for (User u : users) {
            sb.append(u.toString() + "\n");
        }
        return sb.toString();
    }

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 获取会议列表，只有管理员才能执行此操作
     */
    @Override
    public String getMeetingList() throws Exception {
//        判断是否是管理员
        if (currentUser == null || !currentUser.getName().equals("admin")) {
            return "get meeting list FAIL: you are not admin";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("meeting list:\n");
        for (Meeting m : meetings) {
            sb.append(m.toString() + "\n");
        }
        return sb.toString();
    }

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 获取当前用户状态
     */
    @Override
    public String getCurrentStatus() throws Exception {
        if (currentUser == null) {
            return "current status: no user login";
        }
        return "current status: " + currentUser.toString();
    }

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 获取帮助信息
     */
    @Override
    public String getHelp() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("help:\n");
        sb.append("time format: yyyy/MM/dd/HH:mm\n");
        sb.append("1. register -u username -p password\n");
        sb.append("2. login -u username -p password\n");
        sb.append("3. logout\n");
        sb.append("4. add -p participator -s startTime -e endTime -t title\n");
        sb.append("5. query -s startTime -e endTime\n");
        sb.append("6. delete -u uuid\n");
        sb.append("7. clear\n");
        sb.append("8. listUsers [[only for admin]]\n");
        sb.append("9. listMeetings [[only for admin]]\n");
        sb.append("10. currentStatus\n");
        sb.append("11. help\n");
        return sb.toString();
    }
}
