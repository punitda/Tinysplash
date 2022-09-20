package dev.punitd.unplashapp.data

import app.cash.turbine.test
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import dev.punitd.unplashapp.model.*
import dev.punitd.unplashapp.util.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
class UnsplashRepositoryImplTest {
    private lateinit var webServer: MockWebServer
    private lateinit var unsplashApi: UnsplashApi

    private val client = OkHttpClient.Builder().build()
    private val moshi: Moshi = Moshi.Builder().build()

    @Before
    fun createServer() {
        webServer = MockWebServer()

        unsplashApi = Retrofit.Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .baseUrl(webServer.url("/"))
            .build()
            .create(UnsplashApi::class.java)
    }

    @After
    fun shutdown() {
        webServer.shutdown()
    }

    @Test
    fun `success photos response is parsed into correct result`() = runTest {
        val response = MockResponse()
            .setBody(photosSuccessfulResponse)
            .setHeader(firstPageHeaders.name(0), firstPageHeaders.value(0))
            .setResponseCode(200)

        webServer.enqueue(response)
        val unsplashRepository = UnsplashRepositoryImpl(unsplashApi = unsplashApi)

        val result = unsplashRepository.getPhotos(page = 1, perPage = Constants.ITEM_PER_PAGE)
        assert(result is Success)
        val data = (result as Success).data
        assertEquals(images, data.images)
    }

    @Test
    fun `error photos response is parsed into correct result during bad request`() = runTest {
        val response = MockResponse()
            .setBody(errorResponse)
            .setResponseCode(400)

        webServer.enqueue(response)
        val unsplashRepository = UnsplashRepositoryImpl(unsplashApi = unsplashApi)

        val result = unsplashRepository.getPhotos(page = 1, perPage = Constants.ITEM_PER_PAGE)
        assert(result is Error)
        assertEquals(errorMessage, (result as Error).message)
    }

    @Test
    fun `error photos response is parsed into correct result during auth error`() = runTest {
        val response = MockResponse()
            .setBody(authErrorResponse)
            .setResponseCode(401)

        webServer.enqueue(response)
        val unsplashRepository = UnsplashRepositoryImpl(unsplashApi = unsplashApi)

        val result = unsplashRepository.getPhotos(page = 1, perPage = Constants.ITEM_PER_PAGE)
        assert(result is Error)
        assertEquals(authErrorMessage, (result as Error).message)
    }

    @Test
    fun `success search response is parsed into correct result`() = runTest {
        val response = MockResponse()
            .setBody(searchSuccessfulResponse)
            .setResponseCode(200)

        webServer.enqueue(response)
        val unsplashRepository = UnsplashRepositoryImpl(unsplashApi = unsplashApi)

        unsplashRepository.search(query = "q", perPage = Constants.ITEM_PER_PAGE)
            .test {
                val result = awaitItem()
                assert(result is Success)
                val data = (result as Success).data
                assertEquals(images, data.images)
                cancelAndIgnoreRemainingEvents()
            }

    }

    @Test
    fun `error search response is parsed into correct result during bad request`() = runTest {
        val response = MockResponse()
            .setBody(errorResponse)
            .setResponseCode(400)

        webServer.enqueue(response)
        val unsplashRepository = UnsplashRepositoryImpl(unsplashApi = unsplashApi)

        unsplashRepository.search(query = "q", perPage = Constants.ITEM_PER_PAGE)
            .test {
                val result = awaitItem()
                assert(result is Error)
                assertEquals(errorMessage, (result as Error).message)
                cancel()
            }

    }

    @Test
    fun `error search response is parsed into correct result during auth error`() = runTest {
        val response = MockResponse()
            .setBody(authErrorResponse)
            .setResponseCode(401)

        webServer.enqueue(response)
        val unsplashRepository = UnsplashRepositoryImpl(unsplashApi = unsplashApi)

        unsplashRepository.search(query = "q", perPage = Constants.ITEM_PER_PAGE)
            .test {
                val result = awaitItem()
                assert(result is Error)
                assertEquals(authErrorMessage, (result as Error).message)
                cancelAndIgnoreRemainingEvents()
            }
    }

}