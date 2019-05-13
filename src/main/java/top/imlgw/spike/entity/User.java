package top.imlgw.spike.entity;

/**
 * @author imlgw.top
 * @date 2019/5/11 15:56
 */
public class User {
    private Integer id;
    private String name;
    private Integer age;
    private String password;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", password='" + password + '\'' +
                '}';
    }

    public User(Integer id, String name, Integer age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
        this.id=id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
