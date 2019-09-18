package com.engineering.ai.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("users")
    @Expose
    private List<UserData> users = null;
    @SerializedName("has_more")
    @Expose
    private Boolean hasMore;

    public List<UserData> getUsers() {
        return users;
    }

    public void setUsers(List<UserData> userData) {
        this.users = userData;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

}