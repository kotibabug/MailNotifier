package com.xinte.mailnotifier.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.xinte.mailnotifier.db.Account;

/**
 * Created by Koti on 18-02-2018.
 */

@Dao
public interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long addAccount(Account account);

    @Update
    public int updateAccount(Account account);

    @Query("SELECT * FROM account LIMIT 1")
    public Account getAccount();

    @Delete
    public int deleteAccount(Account account);
}
