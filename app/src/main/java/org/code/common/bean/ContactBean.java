package org.code.common.bean;

public class ContactBean {
    public String nickname;
    public String phone;
    public String homePhone;
    public String companyPhone;

    public ContactBean() {
    }

    public ContactBean(String nickname, String phone, String homePhone, String companyPhone) {
        this.nickname = nickname;
        this.phone = phone;
        this.homePhone = homePhone;
        this.companyPhone = companyPhone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }
}
