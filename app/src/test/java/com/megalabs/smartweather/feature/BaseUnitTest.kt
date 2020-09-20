package com.megalabs.smartweather.feature

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

abstract class BaseUnitTest {
    lateinit var mockServer: MockWebServer

    @Before
    open fun before() {
        mockServer = MockWebServer()
        mockServer.start()
    }

    @After
    open fun after() {
        mockServer.shutdown()
    }

    fun mockResponseFromFile(fileName: String, responseCode: Int) = MockResponse()
        .setResponseCode(responseCode)
        .setBody(loadJson(fileName) ?: "")

    private fun loadJson(path: String): String? {
        val inputStream = this.javaClass.classLoader?.getResourceAsStream(path)
        return inputStream?.bufferedReader().use { it?.readText() }
    }
}