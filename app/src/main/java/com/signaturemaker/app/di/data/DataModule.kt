package com.signaturemaker.app.di.data

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.signaturemaker.app.BuildConfig
import com.signaturemaker.app.R
import com.signaturemaker.app.data.datasource.SignatureServices
import com.signaturemaker.app.data.repositories.FilesRepositoryImp
import com.signaturemaker.app.data.repositories.SignatureRepositoryImp
import com.signaturemaker.app.domain.models.Changelog
import com.signaturemaker.app.domain.models.ChangelogDto
import com.signaturemaker.app.domain.repository.FilesRepository
import com.signaturemaker.app.domain.repository.SignatureRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit

val dataModule = module {

    // Moshi
    single {
        Moshi.Builder().build()
    }

    // HttpLoggingInterceptor
    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    // SignatureServices API
    single {
        get<Retrofit>().create(SignatureServices::class.java)
    }

    // Changelog
    single {
        provideChangelog(androidContext(), get())
    }

    // Repositories
    single<FilesRepository> {
        FilesRepositoryImp(androidContext())
    }

    single<SignatureRepository> {
        SignatureRepositoryImp(get())
    }
}

// Helper functions
private fun provideChangelog(context: android.content.Context, moshi: Moshi): List<Changelog> {
    val jsonAdapter: JsonAdapter<ChangelogDto> = moshi.adapter(ChangelogDto::class.java)
    val changelogDto: ChangelogDto? = jsonAdapter.fromJson(getJsonStringChangelog(context))
    return changelogDto?.changelog ?: listOf()
}

private fun getJsonStringChangelog(context: android.content.Context): String =
    readTextFile(context.resources.openRawResource(R.raw.changelog))

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

