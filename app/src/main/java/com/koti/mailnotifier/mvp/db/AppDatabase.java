package com.koti.mailnotifier.mvp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.koti.mailnotifier.mvp.db.dao.AccountDao;

/**
 * Created by Koti on 18-02-2018.
 */

@Database(entities = {Account.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract AccountDao accountDao();
}
