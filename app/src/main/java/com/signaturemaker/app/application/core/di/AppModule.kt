package com.signaturemaker.app.application.core.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.signaturemaker.app.BuildConfig
import com.signaturemaker.app.R
import com.signaturemaker.app.data.datasource.SignatureServices
import com.signaturemaker.app.data.repositories.SignatureRepositoryImp
import com.signaturemaker.app.domain.models.Changelog
import com.signaturemaker.app.domain.models.ChangelogDto
import com.signaturemaker.app.domain.usecase.SendSuggest
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi.Builder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideChangelog(@ApplicationContext appContext: Context): List<Changelog> {

        val jsonAdapter: JsonAdapter<ChangelogDto> = Builder().build().adapter(ChangelogDto::class.java)
        val changelogDto: ChangelogDto? = jsonAdapter.fromJson(getJsonStringChangelog(appContext))
        return changelogDto?.changelog ?: listOf()
    }

    @Provides
    @Singleton
    fun provideSendSuggest(dataSource: SignatureRepositoryImp): SendSuggest = SendSuggest(dataSource)

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
            FirebaseCrashlytics.getInstance().log("Error al cargar el changelog")
        }
        return outputStream.toString()
    }

    @Provides
    @Singleton
    fun createNetworkClient() = retrofitClient(httpClient())

    @Provides
    @Singleton
    fun provideServices(): SignatureServices = createNetworkClient().create(SignatureServices::class.java)

    private fun httpClient(): OkHttpClient {
        // val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        val clientBuilder = OkHttpClient.Builder()

        clientBuilder.addInterceptor(
            HttpLoggingInterceptor().setLevel(
                getLogLevel()
            )
        )

        clientBuilder.addNetworkInterceptor(StethoInterceptor())
        clientBuilder.readTimeout(30, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(30, TimeUnit.SECONDS)
        return clientBuilder.build()
    }

    private fun retrofitClient(httpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private fun getLogLevel(): HttpLoggingInterceptor.Level {
        return if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.HEADERS
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
}


