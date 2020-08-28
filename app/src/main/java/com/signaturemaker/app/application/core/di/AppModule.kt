package com.signaturemaker.app.application.core.di

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import com.signaturemaker.app.R
import com.signaturemaker.app.domain.models.Changelog
import com.signaturemaker.app.domain.models.ChangelogDto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideChangelog(@ApplicationContext appContext: Context): List<Changelog> =
        Gson().fromJson(getJsonStringChangelog(appContext), ChangelogDto::class.java).changelog

    private fun getJsonStringChangelog(appContext: Context) =
        readTextFile(appContext.resources.openRawResource(R.raw.changelog))

    private fun readTextFile(inputStream: InputStream): String {
        val outputStream = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        var len: Int
        try {
            while (inputStream.read(buf).also { len = it } != -1) {
                outputStream.write(buf, 0, len)
            }
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            Crashlytics.log("Error al cargar ek changelog")
        }
        return outputStream.toString()
    }
}


