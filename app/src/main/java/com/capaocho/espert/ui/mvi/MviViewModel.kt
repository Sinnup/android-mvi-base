package com.capaocho.espert.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class MviViewModel<I : MviIntent, S : MviState, E> : ViewModel() {

    private val _state: MutableStateFlow<S>
    val state get() = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<E>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val _intentChannel = Channel<I>(Channel.UNLIMITED)
    private val intentFlow = _intentChannel.receiveAsFlow()

    init {
        _state = MutableStateFlow(createInitialState())
        viewModelScope.launch {
            intentFlow.collect {
                handleIntent(it)
            }
        }
    }

    fun sendIntent(intent: I) {
        viewModelScope.launch {
            _intentChannel.send(intent)
        }
    }

    protected fun setState(reducer: S.() -> S) {
        _state.value = state.value.reducer()
    }

    protected fun sendEffect(effect: E) {
        viewModelScope.launch {
            _sideEffect.emit(effect)
        }
    }

    protected abstract fun createInitialState(): S
    protected abstract fun handleIntent(intent: I)
}