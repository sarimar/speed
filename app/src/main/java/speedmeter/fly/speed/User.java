package speedmeter.fly.speed;

/**
 * Created by user on 19/06/2018.
 */

public class User {

    public String id;
    public String username;
    public String password;
    public String name;
    public String surename;
    public String age;
    public Double weight;

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double height;
    public String email;

    public User(String id,
            String username,
            String password,
            String name,
            String surename,
            String age,
            double weight,
            String email

    )
    {
        this.id=id;
        this.username=username;
        this.password=password;
        this.name=name;
        this.surename=surename;
        this.age=age;
        this.weight=weight;
        this.email=email;

    }
    public User()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurename() {
        return surename;
    }

    public void setSurename(String surename) {
        this.surename = surename;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


}
