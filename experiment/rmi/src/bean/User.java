package bean;

/**
 * @className: User
 * @description: 用户类，包含用户名和密码
 * @author: Yang Xixuan
 **/
public class User {
    private String name;
    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "[name: " + name + ",password:" + password + "]";
    }
}
