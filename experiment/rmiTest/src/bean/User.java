package bean;

/**
 * @className: User
 * @description: 用户类，用于存储用户信息，包括用户名、密码等
 * @author: Yang Xixuan
 **/
public class User {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "[username: " + username + ",password:" + password + "]";
    }
}
