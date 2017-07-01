package com.example.kaildyhoang.storagedemo;

import java.util.Date;

public class ShowUserDataItemsActivity {
    private String Image_Title, Image_Url;
    private String _idUser;
    private String userName;
    private String name;
    private String email;
    private String password;
    private String address;
    private String description;
    private String avatar;
    private String gender;
    private String idFriendsList;
    private String idBookmarksList;
    private int countOfPosts ,countOfFriends, countOfBookmarks;
    private String birthday, createDay;

    public ShowUserDataItemsActivity(String image_Title, String image_Url) {
        Image_Title = image_Title;
        Image_Url = image_Url;
    }

    public ShowUserDataItemsActivity(String _idUser, String name, String userName, String email, String password,
                                 String address, String description, String avatar, String gender,
                                 String idFriendsList, String idBookmarksList, int countOfPosts,
                                 int countOfFriends, int countOfBookmarks, String birthday, String createDay) {
        this._idUser = _idUser;
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.description = description;
        this.avatar = avatar;
        this.gender = gender;
        this.idFriendsList = idFriendsList;
        this.idBookmarksList = idBookmarksList;
        this.countOfPosts = countOfPosts;
        this.countOfFriends = countOfFriends;
        this.countOfBookmarks = countOfBookmarks;
        this.birthday = birthday;
        this.createDay = createDay;
    }

    public ShowUserDataItemsActivity() {
//        Require a empty constructor
    }

    public String getImage_Title() {
        return Image_Title;
    }

    public void setImage_Title(String image_Title) {
        Image_Title = image_Title;
    }

    public String getImage_Url() {
        return Image_Url;
    }

    public void setImage_Url(String image_Url) {
        Image_Url = image_Url;
    }

    public String get_idUser() {
        return _idUser;
    }

    public void set_idUser(String _idUser) {
        this._idUser = _idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdFriendsList() {
        return idFriendsList;
    }

    public void setIdFriendsList(String idFriendsList) {
        this.idFriendsList = idFriendsList;
    }

    public String getIdBookmarksList() {
        return idBookmarksList;
    }

    public void setIdBookmarksList(String idBookmarksList) {
        this.idBookmarksList = idBookmarksList;
    }

    public int getCountOfPosts() {
        return countOfPosts;
    }

    public void setCountOfPosts(int countOfPosts) {
        this.countOfPosts = countOfPosts;
    }

    public int getCountOfFriends() {
        return countOfFriends;
    }

    public void setCountOfFriends(int countOfFriends) {
        this.countOfFriends = countOfFriends;
    }

    public int getCountOfBookmarks() {
        return countOfBookmarks;
    }

    public void setCountOfBookmarks(int countOfBookmarks) {
        this.countOfBookmarks = countOfBookmarks;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }
}
