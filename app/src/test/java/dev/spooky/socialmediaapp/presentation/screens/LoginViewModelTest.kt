package dev.spooky.socialmediaapp.presentation.screens

import dev.spooky.socialmediaapp.data.dto.AuthResponse
import dev.spooky.socialmediaapp.data.dto.UserInfoResponse
import dev.spooky.socialmediaapp.data.models.AuthData
import dev.spooky.socialmediaapp.data.repository.AuthRepositoryImpl
import dev.spooky.socialmediaapp.data.repository.SessionRepository
import dev.spooky.socialmediaapp.data.util.SessionHelper
import dev.spooky.socialmediaapp.data.util.httpClient
import dev.spooky.socialmediaapp.domain.usecase.auth.LoginUseCase
import dev.spooky.socialmediaapp.presentation.screens.login.LoginViewModel
import dev.spooky.socialmediaapp.presentation.util.ScreenState
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import kotlin.test.assertEquals
import dev.spooky.socialmediaapp.domain.models.AuthData as DomainAuthData

class LoginViewModelTest : AutoCloseKoinTest() {
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var sessionRepository: SessionRepository
    private lateinit var sessionHelper: SessionHelper

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val auth = AuthData("token", "token")

        sessionRepository = mockk<SessionRepository>()
        coEvery { sessionRepository.setAuthPreferences(any()) } returns Unit
        coEvery { sessionRepository.setUserInfoPreferences(any()) } returns Unit
        coEvery { sessionRepository.getAuthDataPreferences() } returns null
        coEvery { sessionRepository.getUserInfoPreferences() } returns null

        sessionHelper = spyk(SessionHelper(sessionRepository))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should login successfully`() =
        runBlocking {
            val client =
                MockEngine
                    .create {
                        dispatcher = testDispatcher
                        addHandler { request ->
                            val relativeUrl = request.url.encodedPath
                            when (relativeUrl) {
                                "/api/auth/login" -> {
                                    val mockAuth = mockk<DomainAuthData>()
                                    coEvery { mockAuth getProperty "accessToken" } returns "token"
                                    coEvery { sessionHelper.getAuth() } returns mockAuth
                                    respond(
                                        content = Json.encodeToString(AuthResponse("token", "token")),
                                        HttpStatusCode.OK,
                                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                                    )
                                }

                                else -> {
                                    respond(
                                        content = Json.encodeToString(UserInfoResponse.empty()),
                                        HttpStatusCode.OK,
                                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                                    )
                                }
                            }
                        }
                    }.run { httpClient(this) }
            val repo = AuthRepositoryImpl(client, "http://demo/api", sessionHelper)
            val spyRepo = spyk(repo)
            val useCase = LoginUseCase(spyRepo)
            val viewModel = LoginViewModel(useCase, sessionHelper)
            viewModel.onLoginSuccess = {}
            val spyViewModel = spyk(viewModel)

            assertEquals(ScreenState.Idle, viewModel.state.value)

            spyViewModel.login("sample@mail.com", "sample")

            coVerify { spyRepo.login(any(), any()) }
            coVerify { spyRepo.getInfo() }
            assertEquals(ScreenState.Success(Unit), viewModel.state.value)
            coVerify { spyViewModel.onLoginSuccess }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should fail on login and set viewmodel state to error`() =
        runBlocking {
            val client =
                MockEngine
                    .create {
                        dispatcher = testDispatcher
                        addHandler { request ->
                            val relativeUrl = request.url.encodedPath
                            when (relativeUrl) {
                                "/api/auth/login" -> {
                                    respond(
                                        content = ByteReadChannel("""something went wrong"""),
                                        HttpStatusCode.BadRequest,
                                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                                    )
                                }

                                else -> {
                                    respond(
                                        content = Json.encodeToString(UserInfoResponse.empty()),
                                        HttpStatusCode.OK,
                                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                                    )
                                }
                            }
                        }
                    }.run { httpClient(this) }
            val repo = AuthRepositoryImpl(client, "http://demo/api", sessionHelper)
            val spyRepo = spyk(repo)
            val useCase = LoginUseCase(spyRepo)
            val viewModel = LoginViewModel(useCase, sessionHelper)
            viewModel.onLoginSuccess = {}
            val spyViewModel = spyk(viewModel)

            assertEquals(ScreenState.Idle, viewModel.state.value)

            spyViewModel.login("sample@mail.com", "sample")

            coVerify { spyRepo.login(any(), any()) }
            coVerify(exactly = 0) { spyRepo.getInfo() }
            assertEquals(ScreenState.Error("invalid credentials"), viewModel.state.value)
            coVerify(exactly = 0) { spyViewModel.onLoginSuccess }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should fail on getting user info and set viewmodel state to error `() =
        runBlocking {
            val mockAuth = mockk<DomainAuthData>()
            coEvery { mockAuth getProperty "accessToken" } returns "token"
            coEvery { sessionHelper.getAuth() } returns mockAuth
            val client =
                MockEngine
                    .create {
                        dispatcher = testDispatcher
                        addHandler { request ->
                            val relativeUrl = request.url.encodedPath
                            when (relativeUrl) {
                                "/api/auth/login" -> {
                                    respond(
                                        content = Json.encodeToString(AuthResponse("token", "token")),
                                        HttpStatusCode.OK,
                                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                                    )
                                }

                                else -> {
                                    respond(
                                        content = ByteReadChannel("something went wrong"),
                                        HttpStatusCode.BadRequest,
                                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                                    )
                                }
                            }
                        }
                    }.run { httpClient(this) }
            val repo = AuthRepositoryImpl(client, "http://demo/api", sessionHelper)
            val spyRepo = spyk(repo)
            val useCase = LoginUseCase(spyRepo)
            val viewModel = LoginViewModel(useCase, sessionHelper)
            viewModel.onLoginSuccess = {}
            val spyViewModel = spyk(viewModel)

            assertEquals(ScreenState.Idle, viewModel.state.value)

            spyViewModel.login("sample@mail.com", "sample")

            coVerify { spyRepo.login(any(), any()) }
            coVerify { spyRepo.getInfo() }
            assertEquals(ScreenState.Error("user data not available"), viewModel.state.value)
            coVerify(exactly = 0) { spyViewModel.onLoginSuccess }
        }
}

private fun UserInfoResponse.Companion.empty() = UserInfoResponse("", "", "", "", 0UL, 0UL, null)
