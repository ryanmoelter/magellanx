package com.ryanmoelter.magellanx.doggos.utils

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import com.ryanmoelter.magellanx.doggos.utils.Loadable.Failure
import com.ryanmoelter.magellanx.doggos.utils.Loadable.Loading
import com.ryanmoelter.magellanx.doggos.utils.Loadable.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed interface Loadable<ValueType> {
  data class Success<ValueType>(
    val value: ValueType,
  ) : Loadable<ValueType>

  data class Loading<ValueType>(
    val previousValue: ValueType? = null,
  ) : Loadable<ValueType>

  data class Failure<ValueType>(
    val throwable: Throwable,
    val message: String? = null,
  ) : Loadable<ValueType>
}

fun <ReturnType> wrapInLoadableFlow(action: suspend () -> ReturnType): Flow<Loadable<ReturnType>> =
  flow {
    emit(Loading())
    try {
      emit(Success(action()))
    } catch (throwable: Throwable) {
      emit(Failure(throwable))
    }
  }

fun <StartingType, TargetType> Loadable<StartingType>.map(
  action: (StartingType) -> TargetType,
): Loadable<TargetType> =
  when (this) {
    is Success -> Success(action(value))
    is Loading ->
      if (previousValue != null) {
        Loading(action(previousValue))
      } else {
        Loading(null)
      }

    is Failure -> Failure(throwable)
  }

@Composable
fun <T : Any> ShowLoadingAround(
  loadable: Loadable<T>,
  content: @Composable (T) -> Unit,
) {
  val blockTouches =
    when (loadable) {
      is Success, is Failure -> false
      is Loading -> true
    }
  Box(Modifier.gesturesDisabled(blockTouches)) {
    AnimatedContent(
      targetState = loadable,
      label = "",
      transitionSpec = {
        fadeIn(animationSpec = tween(220, delayMillis = 90))
          .togetherWith(fadeOut(animationSpec = tween(90)))
      },
    ) { loadable ->
      when (loadable) {
        is Success -> content(loadable.value)
        is Loading -> {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (loadable.previousValue != null) {
              Box(modifier = Modifier.alpha(0.6f)) {
                content(loadable.previousValue)
              }
            }
            CircularProgressIndicator(Modifier.align(Alignment.Center))
          }
        }

        is Failure -> {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
              loadable.throwable.toString(),
            ) // TODO: Show an error state, log to sentry, offer retry?
          }
        }
      }
    }
  }
}

@Composable
fun ShowLoadingAround(
  isLoading: Boolean,
  showSpinner: Boolean = true,
  content: @Composable () -> Unit,
) {
  Box(Modifier.gesturesDisabled(isLoading), contentAlignment = Alignment.Center) {
    content()

    AnimatedVisibility(
      visible = isLoading,
      enter = fadeIn(animationSpec = tween(300)),
      exit = fadeOut(animationSpec = tween(200)),
    ) {
      Box(
        modifier =
          Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center,
      ) {
        if (showSpinner) {
          CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
      }
    }
  }
}

fun Modifier.gesturesDisabled(disabled: Boolean) =
  if (disabled) {
    pointerInput(Unit) {
      awaitPointerEventScope {
        // we should wait for all new pointer events
        while (true) {
          awaitPointerEvent(pass = PointerEventPass.Initial)
            .changes
            .forEach(PointerInputChange::consume)
        }
      }
    }
  } else {
    this
  }
