package com.arnoagape.lokavelo.di

import com.arnoagape.lokavelo.data.network.nominatim.NominatimApi
import com.arnoagape.lokavelo.data.network.nominatim.NominatimRepositoryImpl
import com.arnoagape.lokavelo.data.repository.GeocodingRepository
import dagger.Module
import dagger.Provides
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNominatimApi(): NominatimApi =
        Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .client(
                OkHttpClient.Builder()
                    .addInterceptor {
                        val request = it.request().newBuilder()
                            .header(
                                "User-Agent",
                                "LokaveloApp/1.0 (crockile@hotmail.fr)"
                            )
                            .build()
                        it.proceed(request)
                    }
                    .build()
            )
            .build()
            .create(NominatimApi::class.java)

    @Provides
    @Singleton
    fun provideGeocodingRepository(
        api: NominatimApi
    ): GeocodingRepository =
        NominatimRepositoryImpl(api)
}