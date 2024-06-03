package `in`.knightcoder.wallpaper.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://api.pexels.com/v1/"

    val api: ApiService by lazy {

        val httpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor).connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)

        httpClient.addInterceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "563492ad6f917000010000011069c623c7ad4919b7f934771d8adbe9")

            val request = requestBuilder.build()

            try {
                return@addInterceptor chain.proceed(request)
            } catch (exception: Exception) {
                when (exception) {
                    is SocketTimeoutException -> {
                        Log.e("NetworkInterceptor", "SocketTimeoutException: ${exception.message}")
                    }
                    is SocketException -> {
                        Log.e("NetworkInterceptor", "SocketException: ${exception.message}")
                    }
                    is IOException -> {
                        Log.e("NetworkInterceptor", "IOException: ${exception.message}")
                    }
                    else -> {
                        Log.e("NetworkInterceptor", "Exception: ${exception.message}")
                    }
                }

                return@addInterceptor chain.proceed(
                    originalRequest.newBuilder().build()
                )
            }
        }

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
