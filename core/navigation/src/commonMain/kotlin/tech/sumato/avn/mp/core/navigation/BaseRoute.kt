package tech.sumato.avn.mp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.koin.compose.viewmodel.koinViewModel

interface MviViewModel<S : Any, E : Any> {
    val state: StateFlow<S>
    val effects: Flow<E>
}

@Composable
inline fun <reified VM, S : Any, E : Any> BaseRoute(
    noinline onEffect: (E) -> Unit,
    noinline content: @Composable VM.(state: S) -> Unit,
) where VM : MviViewModel<S, E>, VM : ViewModel {
    val viewModel = koinViewModel<VM>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            onEffect(effect)
        }
    }

    content(viewModel, state)
}
