/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.sage.android;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.os.RemoteException;
import android.provider.Contacts;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import androidx.test.filters.SdkSuppress;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Basic example for unbundled UiAutomator.
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class Script1216{

    private static final String BASIC_SAMPLE_PACKAGE
            = "me.ccrama.redditslide";
    private static final String AndroidOS = "com.android.packageinstaller";
    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String STRING_TO_BE_TYPED = "UiAutomator";

    private UiDevice mDevice;
    public void sleepOneSecond(){
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the blueprint app
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void testChangeText_sameActivity() {

        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Permissions
        UiObject2 next = mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE, "next")),2000);
        next.click();

        next = mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE, "next")),2000);
        next.click();

        sleepOneSecond();
        sleepOneSecond();
        UiObject2 allow = mDevice.wait(Until.findObject(By.res("com.android.permissioncontroller", "permission_allow_button")),2000);
        allow.click();

        UiObject2 open = mDevice.wait(Until.findObject(By.desc( "Open")),2000);
        open.click();

        UiObject2 settings = mDevice.wait(Until.findObject(By.text( "Settings")),2000);
        settings.click();
        sleepOneSecond();
        mDevice.swipe(170, 1450, 170, 450, 10);

        UiObject2 data = mDevice.wait(Until.findObject(By.text( "Data saving")),2000);
        data.click();

        data = mDevice.wait(Until.findObject(By.text( "Enable datasaving settings")),2000);
        data.click();

        data = mDevice.wait(Until.findObject(By.text( "Mobile data and Wi-Fi")),2000);
        data.click();

        data = mDevice.wait(Until.findObject(By.text( "Image quality when Data Saving enabled")),2000);
        data.click();

        data = mDevice.wait(Until.findObject(By.textStartsWith( "Load low quality")),2000);
        data.click();


        mDevice.pressBack();
        sleepOneSecond();
        mDevice.pressBack();

        open = mDevice.wait(Until.findObject(By.desc( "Open")),2000);
        open.click();

        UiObject2 gotoSub = mDevice.wait(Until.findObject(By.text( "Go to subreddit")),2000);
        gotoSub.setText("test");

        gotoSub = mDevice.wait(Until.findObject(By.text( "Go to test")),2000);
        gotoSub.click();
        sleepOneSecond();
        open = mDevice.wait(Until.findObject(By.desc( "More options")),2000);
        open.click();

        UiObject2 search = mDevice.wait(Until.findObject(By.text( "Search")),2000);
        search.click();

        UiObject2 input = mDevice.wait(Until.findObject(By.text( "What are you searching for?")),2000);
        input.setText("Slide Imgur Test");

        search = mDevice.wait(Until.findObject(By.text( "SEARCH TEST")),2000);
        search.click();

        search = mDevice.wait(Until.findObject(By.textEndsWith( "touzainanboku ")),2000);
        search.click();

        mDevice.click(124, 1323);
        mDevice.click(124, 1323);
    }



    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}