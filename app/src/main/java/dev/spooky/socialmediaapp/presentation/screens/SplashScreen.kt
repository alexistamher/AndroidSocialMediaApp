package dev.spooky.socialmediaapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.spooky.socialmediaapp.R
import dev.spooky.socialmediaapp.data.util.SessionHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = koinViewModel<SplashViewModel>(),
) {
    LaunchedEffect(Unit) {
        viewModel.onValidSession = onNavigateToHome
        viewModel.onInvalidSession = onNavigateToLogin
        viewModel.startCheck()
    }

    Scaffold { mainPadding ->
        Box(
            Modifier
                .padding(mainPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Text(
                    stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displaySmall,
                )

                CircularProgressIndicator()
                Text(
                    "loading...",
                    style =
                        MaterialTheme.typography.titleLarge.run {
                            copy(color = color.copy(alpha = 0.6f))
                        },
                )
            }
        }
    }
}

internal class SplashViewModel(
    private val sessionHelper: SessionHelper,
) : ViewModel() {
    lateinit var onInvalidSession: () -> Unit
    lateinit var onValidSession: () -> Unit

    fun startCheck() {
        viewModelScope.launch {
            val validSession = sessionHelper.validateSession()
            // in order to make splash screen visible
            delay(2000L.milliseconds)
            if (validSession && ::onValidSession.isInitialized) {
                onValidSession()
                return@launch
            }
            if (::onInvalidSession.isInitialized) {
                onInvalidSession
            }
        }
    }
}
