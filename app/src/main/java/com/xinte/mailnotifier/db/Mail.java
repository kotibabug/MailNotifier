package com.xinte.mailnotifier.db;

import android.arch.persistence.room.ColumnInfo;

/**
 * Created by Koti on 18-02-2018.
 */

public class Mail {
    @ColumnInfo(name = "mail_count")
    private long mailCount;

    public long getMailCount() {
        return mailCount;
    }

    public void setMailCount(long mailCount) {
        this.mailCount = mailCount;
    }
}
