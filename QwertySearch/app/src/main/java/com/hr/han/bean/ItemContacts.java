package com.hr.han.bean;


import com.hr.han.utils.pinyin.PinYinUtils;

public class ItemContacts implements Comparable<ItemContacts> {
    public String headpinyin;//获取首字母
    public String name;// 名字
    public String number;// 电话号码
    public int contactsId;// 联系人的id
    public String colorName;//带颜色的字
    public String colorNumberName;//带颜色的手机号码

    public void setColorNumberName(String colorNumberName) {
        this.colorNumberName = colorNumberName;
    }

    public String getColorNumberName() {

        return colorNumberName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorName() {

        return colorName;
    }

    public ItemContacts(String name, String number, int contactsId) {
        super();
        this.name = name;
        this.number = number;
        this.contactsId = contactsId;
        this.headpinyin = PinYinUtils.getUppercasePinYinHeadChar(name);
    }

    @Override
    public int compareTo(ItemContacts o) {
        //汉字能不能排序
        return this.headpinyin.compareTo(o.headpinyin);
    }

    public void setHeadpinyin(String headpinyin) {
        this.headpinyin = headpinyin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setContactsId(int contactsId) {
        this.contactsId = contactsId;
    }

    public String getHeadpinyin() {
        return headpinyin;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public int getContactsId() {
        return contactsId;
    }

    @Override
    public String toString() {
        return "ItemContacts{" +
                "headpinyin='" + headpinyin + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", contactsId=" + contactsId +
                '}';
    }
}
