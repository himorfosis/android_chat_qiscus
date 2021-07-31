package com.siklusdev.qiscuschat.module

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.siklusdev.qiscuschat.network.ResponseInterceptor
import com.siklusdev.qiscuschat.network.services.AuthServices
import com.siklusdev.qiscuschat.preferences.AccountManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    private val timeOut: Int by lazy {
        90
    }

    private val responseInterceptor by lazy {
        ResponseInterceptor
    }

    private val jsonChecker by lazy {
        object : Converter.Factory() {
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<Annotation>,
                retrofit: Retrofit
            ): Converter<ResponseBody, *>? {
                return Converter<ResponseBody, Any> { responseBody ->
                    val delegate = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
                    try {
                        delegate.convert(responseBody)
                    } catch (error: Throwable) {

                        Log.e("jsonChecker error", responseBody.string())
//                        Log.e("jsonChecker error", error.response)

                        val message = try {
                            val result = JSONObject(responseBody.string())
                            val message = result.getString("message")
                            message
                        } catch (e: JSONException) {
                            "Invalid JSON format"
                        } catch (e: IOException) {
                            "Gagal"
                        }

                        throw IOException(message)
//                        throw IOException("Gagal")

                    }
                }
            }
        }
    }

    @Provides
    @Singleton
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        accountManager: AccountManager,
        moshi: Moshi
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(jsonChecker)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()
            .also {
                responseInterceptor.retrofit = it
                responseInterceptor.getToken = accountManager.getToken()
            }
    }

    @Provides
    @Singleton
    fun providesOkhttpClient(
        accountManager: AccountManager
    ): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .addInterceptor(responseInterceptor)
            .addNetworkInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer ${accountManager.getToken()}")
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .connectTimeout(timeOut.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeOut.toLong(), TimeUnit.SECONDS)
            .writeTimeout(timeOut.toLong(), TimeUnit.SECONDS)

//        if (BuildConfig.DEBUG) {
//            builder.addInterceptor(
//                HttpLoggingInterceptor()
//                    .setLevel(HttpLoggingInterceptor.Level.BODY)
//            )
//        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun providesAuthServices(retrofit: Retrofit): AuthServices {
        return retrofit.create(AuthServices::class.java)
    }

}