package com.pihrit.nextflick.data;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class TestFavoriteMovieContentProvider {
    private final Context mContext = InstrumentationRegistry.getTargetContext();

    /**
     * Clean the database before each test
     */
    @Before
    public void setup() {
        FavoriteMovieDbHelper dbHelper = new FavoriteMovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, null, null);
    }

    /**
     * To see if the content provider is registered correctly in the manifest
     */

    @Test
    public void testProviderRegistry() {
        String packageName = mContext.getPackageName();
        String providerClassName = FavoriteMovieContentProvider.class.getName();
        ComponentName componentName = new ComponentName(packageName, providerClassName);

        try {
            PackageManager pm = mContext.getPackageManager();
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            String actualAuthority = providerInfo.authority;
            String expectedAuthority = packageName;

            assertEquals("Content provider registered with authority: " + actualAuthority + " instead of expected: " + expectedAuthority, actualAuthority, expectedAuthority);
        } catch (PackageManager.NameNotFoundException nnfe) {
            fail("Content provider not registered at " + mContext.getPackageName());
        }
    }
}
