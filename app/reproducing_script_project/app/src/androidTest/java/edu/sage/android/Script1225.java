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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;
import android.provider.Contacts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import androidx.test.filters.SdkSuppress;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Basic example for unbundled UiAutomator.
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class Script1225 {

    private static final String PACKAGE_NAME
            = "org.gnucash.android";

    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String STRING_TO_BE_TYPED = "test";

    private UiDevice uiDevice;

    @Before
    public void startActivityFromHomeScreen() throws UiObjectNotFoundException {
        // Initialize UiDevice instance
        uiDevice = UiDevice.getInstance(getInstrumentation());

        // Start from the home screen
        uiDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        uiDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the blueprint app
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(PACKAGE_NAME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        uiDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void performGUIActions() throws UiObjectNotFoundException, IOException, RemoteException, InterruptedException {
        // Setup GUI actions
        // Click "next" three times
        uiDevice.findObject(new UiSelector().text("Next")).click();
        uiDevice.findObject(new UiSelector().text("Next")).click();
        uiDevice.findObject(new UiSelector().text("Next")).click();
        // Click option 1
        uiDevice.findObject(new UiSelector().textContains("Automatically")).click();
        // Click "next"
        uiDevice.findObject(new UiSelector().text("Next")).click();
        // Click "done"
        uiDevice.findObject(new UiSelector().text("Done")).click();
        // Bug-specific GUI actions
        // Launch settings
        Context context = getApplicationContext();
        final Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    // Clear out any previous instances
        context.startActivity(intent);
        // Click search
        TimeUnit.MILLISECONDS.sleep(1000);
        uiDevice.click(1000, 200);
        // Type "language"
        uiDevice.findObject(new UiSelector().className("android.widget.EditText")).legacySetText("language");
        // Click "language"
        uiDevice.findObject(new UiSelector().text("Language")).click();
        // Click "language"
        uiDevice.findObject(new UiSelector().text("Language")).click();
        // Click "language"
        uiDevice.findObject(new UiSelector().text("Language")).click();
        // Drag up
        uiDevice.drag(500, 1700, 500, 200, 30);
        // Click "español"
        uiDevice.findObject(new UiSelector().textContains("Español")).click();
        // Launch the blueprint app
        Context context2 = getApplicationContext();
        final Intent intent2 = context2.getPackageManager()
                .getLaunchIntentForPackage(PACKAGE_NAME);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context2.startActivity(intent2);
        // Click menu
        uiDevice.findObject(new UiSelector().clickable(true)).click();
        // Click "settings"
        uiDevice.findObject(new UiSelector().text("Ajustes")).click();
        // Click "accounts"
        uiDevice.findObject(new UiSelector().clickable(true).instance(3)).click();
        // Click "create default account"
        uiDevice.findObject(new UiSelector().clickable(true).instance(2)).click();
        // Click "create account"
        uiDevice.findObject(new UiSelector().clickable(true).instance(1)).click();
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
