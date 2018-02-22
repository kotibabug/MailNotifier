package com.koti.mailnotifier.mvp.db;

import android.arch.persistence.room.ColumnInfo;

/**
 * Created by Koti on 18-02-2018.
 */

public class Mail {
    @ColumnInfo(name = "mail_count")
    private long mailCount;

    private String lastEmailFrom;

    public long getMailCount() {
        return mailCount;
    }

    public void setMailCount(long mailCount) {
        this.mailCount = mailCount;
    }

    public String getLastEmailFrom() {
        return lastEmailFrom;
    }

    public void setLastEmailFrom(String lastEmailFrom) {
        this.lastEmailFrom = lastEmailFrom;
    }
}
