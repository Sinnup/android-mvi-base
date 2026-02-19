package com.capaocho.espert.ui.mvi

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base class for ViewModels following the MVI (Model-View-Intent) pattern.
 *
 * It manages a single [state], receives [MviIntent]s to modify that state,
 * and emits [sideEffect]s for one-time UI events. It also supports state
 * persistence across configuration changes using [SavedStateHandle].
 *
 * @param I The type of the Intents received by the ViewModel.
 * @param S The type of the UI State managed by the ViewModel.
 * @param E The type of Side Effects emitted for the UI.
 * @property savedStateHandle Optional handle to persist and restore the state.
 */
abstract class MviViewModel<I : MviIntent, S : MviState, E>(
    private val savedStateHandle: SavedStateHandle? = null
) : ViewModel() {

    companion object {
        private const val STATE_KEY = "mvi_state_key"
    }

    private val _state: MutableStateFlow<S>
    /**
     * Observable [state] that represents the current UI condition.
     */
    val state get() = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<E>()
    /**
     * Stream of one-time [sideEffect]s (e.g., navigation, toasts).
     */
    val sideEffect = _sideEffect.asSharedFlow()

    private val _intentChannel = Channel<I>(Channel.UNLIMITED)
    private val intentFlow = _intentChannel.receiveAsFlow()

    init {
        val initial = savedStateHandle?.get<S>(STATE_KEY) ?: createInitialState()
        _state = MutableStateFlow(initial)
        
        viewModelScope.launch {
            intentFlow.collect {
                handleIntent(it)
            }
        }
    }

    /**
     * Sends an [intent] to be processed by the ViewModel.
     */
    fun sendIntent(intent: I) {
        viewModelScope.launch {
            _intentChannel.send(intent)
        }
    }

    /**
     * Updates the current state using the provided [reducer].
     * The new state is automatically saved to the [savedStateHandle].
     */
    protected fun setState(reducer: S.() -> S) {
        val newState = state.value.reducer()
        _state.value = newState
        savedStateHandle?.set(STATE_KEY, newState)
    }

    /**
     * Emits a [effect] for the UI to consume.
     */
    protected fun sendEffect(effect: E) {
        viewModelScope.launch {
            _sideEffect.emit(effect)
        }
    }

    /**
     * Returns the initial state for this ViewModel.
     */
    protected abstract fun createInitialState(): S

    /**
     * Processes a single [intent] and updates the state or emits side effects accordingly.
     */
    protected abstract fun handleIntent(intent: I)
}
