package com.example.andemo.data.DataBases;

public class User {

    private String name;            //用户名
    private String password;        //密码
    private  Integer tel;
    private  Integer id;

    public User(Integer id,String name, String password,Integer tel) {
        this.id=id;
        this.name = name;
        this.password = password;
        this.tel=tel;

    }
    public Integer getId() {
        return id;
    }
    public void setID(Integer id) {
        this.id = id;
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
    public Integer getTel() {
        return tel;
    }
    public void setTel(Integer tel) {
        this.tel = tel;
    }
}



