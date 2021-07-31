package com.siklusdev.qiscuschat.network

import android.util.Log
import com.siklusdev.qiscuschat.model.data.ErrorResponse
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.cert.CertificateException

object ResponseInterceptor: Interceptor {

    var retrofit: Retrofit? = null

    var getToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val headers = request.headers.values("@")
        if (headers.contains("Auth")) {
            val builder = request.newBuilder()
            getToken?.let {
                builder.addHeader("Authorization", "Bearer $it")
                builder.addHeader("Accept", "application/json")
                builder.addHeader("Content-Type", "application/json")
            }
            request = builder.removeHeader("@").build()
        }
        val response = try {
            chain.proceed(request)
        } catch (error: Exception) {
            val message = when (error) {
                is UnknownHostException -> "Tidak dapat terhubung dengan server"
                is SocketTimeoutException -> "Koneksi timeout"
                is CertificateException -> "Membutuhkan sambungan koneksi terpercaya"
                else -> "Something went wrong"
            }
            throw IOException(message)
        }
        throwIfError(response)

        return response
    }

    private fun throwIfError(response: Response) {
        if (response.code in 200..299) {
            return
        }
        val errorBody = getError<ErrorResponse>(response.body)

        if (errorBody != null) {
            val success = errorBody.success
            val message = errorBody.message
            val exception = IOException(message)
            isLog("throwIfError message : $message")
            throw exception
        }
    }

    private inline fun <reified T> getError(body: ResponseBody?): T? = body?.let {
        retrofit?.responseBodyConverter<T>(T::class.java, T::class.java.annotations)
            ?.convert(it)
    }

    private fun isLog(msg: String) {
        Log.e("ResponseInterceptor", msg)
    }

}