package com.example.testapp.di

import com.example.testapp.BuildConfig
import com.example.testapp.data.remote.ApiConfig
import com.example.testapp.data.remote.SessionStore
import com.example.testapp.data.remote.TernurinesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(session: SessionStore): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            // BASIC no imprime el cuerpo, así que las contraseñas y el token no quedan en el log.
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val token = session.token
                chain.proceed(
                    if (token == null) request
                    else request.newBuilder().header("Authorization", "Bearer $token").build()
                )
            }
            .addInterceptor(logging)
            // Lambda puede tardar unos segundos en responder la primera vez (arranque en frío).
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(client: OkHttpClient, config: ApiConfig): TernurinesApi {
        // Sin URL configurada se usa una dirección de relleno: el API nunca se llama (modo local).
        val baseUrl = if (config.isConfigured) config.baseUrl.trimEnd('/') + "/" else "http://localhost/"
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TernurinesApi::class.java)
    }
}
