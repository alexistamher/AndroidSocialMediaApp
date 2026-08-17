package dev.spooky.socialmediaapp.presentation.screens

import dev.spooky.socialmediaapp.data.dto.AuthResponse
import dev.spooky.socialmediaapp.data.dto.UserInfoResponse
import dev.spooky.socialmediaapp.data.repository.AuthRepositoryImpl
import dev.spooky.socialmediaapp.data.repository.SessionRepository
import dev.spooky.socialmediaapp.data.util.SessionHelper
import dev.spooky.socialmediaapp.data.util.httpClient
import dev.spooky.socialmediaapp.domain.usecase.auth.RegisterUseCase
import dev.spooky.socialmediaapp.presentation.screens.register.RegisterViewModel
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
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class RegisterViewModelTest {
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var sessionRepository: SessionRepository
    lateinit var sessionHelper: SessionHelper

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        sessionRepository = mockk<SessionRepository>()
        coEvery { sessionRepository.setAuthPreferences(any()) } returns Unit
        coEvery { sessionRepository.setUserInfoPreferences(any()) } returns Unit
        coEvery { sessionRepository.getAuthDataPreferences() } returns null
        coEvery { sessionRepository.getUserInfoPreferences() } returns null

        sessionHelper = spyk(SessionHelper(sessionRepository))
    }

    @Test
    fun `should register successfully`() {
        val client = MockEngine.create {
            dispatcher = testDispatcher
            addHandler { request ->
                val relativeUrl = request.url.encodedPath
                when (relativeUrl) {
                    "/api/auth/register" -> respond(
                        content = Json.encodeToString(AuthResponse("token", "token")),
                        HttpStatusCode.Created,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )

                    else -> respond(
                        content = Json.encodeToString(UserInfoResponse.empty()),
                        HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }.run { httpClient(this) }
        val spyRepo = spyk(AuthRepositoryImpl(client, "http://demo/api", sessionHelper))
        val useCase = RegisterUseCase(spyRepo)
        val viewModel = RegisterViewModel(useCase)
        viewModel.onRegisterSuccess = {}
        val spyViewModel = spyk(viewModel)

        assertEquals(ScreenState.Idle, viewModel.state.value)

        spyViewModel.register("jperez", "JuanPe", "jperez@mail.com", "Qwerty123")

        coVerify { spyRepo.register(any(), any(), any(), any()) }
        coVerify { spyRepo.getInfo() }
        assertEquals(ScreenState.Success(Unit), viewModel.state.value)
        coVerify { spyViewModel.onRegisterSuccess }
    }

    @Test
    fun `should fail on register and set viewmodel state to error`() {
        val client = MockEngine.create {
            dispatcher = testDispatcher
            addHandler { request ->
                val relativeUrl = request.url.encodedPath
                when (relativeUrl) {
                    "/api/auth/register" -> respond(
                        content = ByteReadChannel("something went wrong"),
                        HttpStatusCode.BadRequest,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )

                    else -> respond(
                        content = Json.encodeToString(
                            UserInfoResponse.empty()
                        ),
                        HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }.run { httpClient(this) }

        val mockSessionHelper = mockk<SessionHelper>()
        val repo = AuthRepositoryImpl(client, "http://demo/api", mockSessionHelper)
        val spyRepo = spyk(repo)
        val useCase = RegisterUseCase(spyRepo)
        val viewModel = RegisterViewModel(useCase)
        viewModel.onRegisterSuccess = {}
        val spyViewModel = spyk(viewModel)

        assertEquals(ScreenState.Idle, viewModel.state.value)

        spyViewModel.register("jperez", "JuanPe", "jperez@mail.com", "Qwerty123")

        coVerify { spyRepo.register(any(), any(), any(), any()) }
        coVerify(exactly = 0) { spyRepo.getInfo() }
        assertEquals(ScreenState.Error("error on register attempting"), viewModel.state.value)
        coVerify(exactly = 0) { spyViewModel.onRegisterSuccess }
    }


    @Test
    fun `should fail on getting user info and set viewmodel state to error`() {
        val client = MockEngine.create {
            dispatcher = testDispatcher
            addHandler { request ->
                val relativeUrl = request.url.encodedPath
                when (relativeUrl) {
                    "/api/auth/register" -> respond(
                        content = Json.encodeToString(AuthResponse("token", "token")),
                        HttpStatusCode.Created,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )

                    else -> respond(
                        content = ByteReadChannel("something went wrong"),
                        HttpStatusCode.BadRequest,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }.run { httpClient(this) }

        val repo = AuthRepositoryImpl(client, "http://demo/api", sessionHelper)
        val spyRepo = spyk(repo)
        val useCase = RegisterUseCase(spyRepo)
        val viewModel = RegisterViewModel(useCase)
        viewModel.onRegisterSuccess = {}
        val spyViewModel = spyk(viewModel)

        assertEquals(ScreenState.Idle, viewModel.state.value)

        spyViewModel.register("jperez", "JuanPe", "jperez@mail.com", "Qwerty123")

        coVerify { spyRepo.register(any(), any(), any(), any()) }
        coVerify { spyRepo.getInfo() }
        assertEquals(ScreenState.Error("user data not available"), viewModel.state.value)
        coVerify(exactly = 0) { spyViewModel.onRegisterSuccess }
    }
}

private fun UserInfoResponse.Companion.empty() = UserInfoResponse("", "", "", "", 0UL, 0UL, null)