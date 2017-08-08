package io.bxbxbai.zhuanlan.bean;

import android.os.Parcel;
import android.os.Parcelable;
import orm.db.annotation.Column;
import orm.db.annotation.PrimaryKey;
import orm.db.annotation.Table;
import orm.db.annotation.Unique;
import orm.db.enums.AssignType;

/**
 * Created by xuebin on 15/9/21.
 *
 * 用户实体类，具有许多的用户属性字段，
 * 实现了序列化保存
 *
 * 这是一个可以用来存入orm数据库的类
 */
@Table("_user")
public class UserEntity implements Parcelable {


    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("_id")
    public int id;//主键

    @Column("followersCount")
    private int followerCount;//关注人数量

    @Column("description")
    private String description;//描述，简述

    @Column("avatarId")
    private String avatarId;//头像（不知道是不是这个意思），另外译为阿凡达

    @Column("avatarTemplate")
    private String avatarTemplate;//

    @Column("authorName")
    private String authorName;//作者名字

    @Unique
    @Column("href")
    private String href;

    @Unique
    @Column("slug")
    private String slug;

    @Column("name")
    private String name;

    @Column("url")
    private String url;

    @Column("postsCount")
    private int postCount;


    public int getFollowerCount() {
        return followerCount;
    }

    public String getDescription() {
        return description;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public String getAvatarTemplate() {
        return avatarTemplate;
    }

    public String getHref() {
        return href;
    }

    public String getSlug() {
        return slug;
    }

    public String getZhuanlanName() {
        return name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getUrl() {
        return url;
    }

    public int getPostCount() {
        return postCount;
    }

    //setter

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public void setAvatarTemplate(String avatarTemplate) {
        this.avatarTemplate = avatarTemplate;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setZhuanlanName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

//    parcelable序列化，把要序列化的变量写入parcel容器
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.followerCount);
        dest.writeString(this.description);
        dest.writeString(this.avatarId);
        dest.writeString(this.avatarTemplate);
        dest.writeString(this.authorName);
        dest.writeString(this.href);
        dest.writeString(this.slug);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeInt(this.postCount);
    }

    public UserEntity() {
    }

//    保护型构造函数，不知道目的？？这个构造函数可以用于下面的Creator方法
//    读入序列化对象，具有顺序的读入，
    protected UserEntity(Parcel in) {
        this.id = in.readInt();
        this.followerCount = in.readInt();
        this.description = in.readString();
        this.avatarId = in.readString();
        this.avatarTemplate = in.readString();
        this.authorName = in.readString();
        this.href = in.readString();
        this.slug = in.readString();
        this.name = in.readString();
        this.url = in.readString();
        this.postCount = in.readInt();
    }
//序列化重写的方法，CREATOR变量名字不能变
    public static final Parcelable.Creator<UserEntity> CREATOR = new Parcelable.Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel source) {
            return new UserEntity(source);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };
}
