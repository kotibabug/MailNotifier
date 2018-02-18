package com.xinthe.mailnotifier.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.xinthe.mailnotifier.db.dao.AccountDao;

/**
 * Created by Koti on 18-02-2018.
 */

@Database(entities = {Account.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract AccountDao accountDao();
}
