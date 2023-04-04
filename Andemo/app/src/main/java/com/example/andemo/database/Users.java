package com.example.andemo.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity(nameInDb = "user_info")
public class Users {

    @Id(autoincrement = true)
    private Long id;

    private String name;
    private String password;
    private String tel;
    @Generated(hash = 269658758)
    public Users(Long id, String name, String password, String tel) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.tel = tel;
    }
    @Generated(hash = 2146996206)
    public Users() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getTel() {
        return this.tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
   

}
