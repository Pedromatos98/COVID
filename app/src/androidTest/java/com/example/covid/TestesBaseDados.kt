package com.example.covid

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TestesBaseDados {
    private fun getAppContext() = InstrumentationRegistry.getInstrumentation().targetContext
    private fun getCovidOpenHelper() = BdCovidOpenHelper(getAppContext())

    @Test
    fun consegueAbrirBaseDados() {
        val db = getCovidOpenHelper().readableDatabase
        assert(db.isOpen)
        db.close()
    }

}