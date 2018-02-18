package com.xinte.mailnotifier;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.xinte.mailnotifier.db.AppDatabase;

/**
 * Created by Koti on 18-02-2018.
 */

public class MailNotifier extends Application {

    AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        createDatabase();
    }

    public AppDatabase getDatabaseInstance() {
        if (db == null)
            createDatabase();
        return db;
    }

    private void createDatabase() {
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, getString(R.string.dbname)).build();
    }
}
