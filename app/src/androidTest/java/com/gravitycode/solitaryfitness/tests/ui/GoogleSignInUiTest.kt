package dev.gravitycode.solitaryfitness.tests.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class GoogleSignInUiTest {

    private lateinit var device: UiDevice

    @Before
    fun setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), 5000)

        // Launch the app
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(
            "dev.gravitycode.solitaryfitness"
        ).apply {
            // Clear out any previous instances
            this!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        // Wait for the app to appear
        device.wait(Until.hasObject(By.pkg("dev.gravitycode.solitaryfitness").depth(0)), 5000)
    }

    @Test
    fun testGoogleSignInFlow() {
        device.findObject(By.res("overflow")).click()
        device.waitForIdle()
        device.findObject(By.text("Sign In with Google")).click()

        // Wait for the Google account chooser to appear
        device.wait(Until.hasObject(By.res("com.google.android.gms:id/account_name")), 10000)

        // Click on my account
        device.findObject(By.text("buickea@gmail.com")).click()

        // Wait for my profile picture to appear in the toolbar (the main indication sign in was successful)
        device.wait(Until.hasObject(By.desc("User profile picture")), 10000)
        val pfp = device.findObject(By.desc("User profile picture"))
        assertNotNull(pfp)
    }
}
