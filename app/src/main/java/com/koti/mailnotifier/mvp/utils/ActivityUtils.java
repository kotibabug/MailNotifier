package com.koti.mailnotifier.mvp.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Koti on 2/22/2018.
 */

public class ActivityUtils {
    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int fragmentId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragmentId, fragment);
        fragmentTransaction.commit();
    }
}
