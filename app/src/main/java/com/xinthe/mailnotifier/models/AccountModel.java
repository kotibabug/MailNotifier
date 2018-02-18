package com.xinthe.mailnotifier.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Koti on 18-02-2018.
 */

public class AccountModel implements Parcelable {

    private String host;
    private String storeType;
    private int port;
    private String username;
    private String password;

    public AccountModel(String host, String storeType,int port, String username, String password) {
        setHost(host);
        setStoreType(storeType);
        setPort(port);
        setUsername(username);
        setPassword(password);
    }

    private AccountModel(Parcel parcel) {
        setHost(parcel.readString());
        setStoreType(parcel.readString());
        setPort(parcel.readInt());
        setUsername(parcel.readString());
        setPassword(parcel.readString());
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(host);
        parcel.writeString(storeType);
        parcel.writeInt(port);
        parcel.writeString(username);
        parcel.writeString(password);
    }

    public static final Parcelable.Creator<AccountModel> CREATOR = new Parcelable.Creator<AccountModel>() {

        @Override
        public AccountModel createFromParcel(Parcel parcel) {
            return new AccountModel(parcel);
        }

        @Override
        public AccountModel[] newArray(int i) {
            return new AccountModel[i];
        }
    };
}
