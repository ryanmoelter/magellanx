package com.ryanmoelter.magellanx.doggos.utils

import androidx.compose.runtime.Composable
import com.ryanmoelter.magellanx.doggos.utils.Loadable.Failure
import com.ryanmoelter.magellanx.doggos.utils.Loadable.Loading
import com.ryanmoelter.magellanx.doggos.utils.Loadable.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed interface Loadable<ValueType> {
  data class Success<ValueType>(val value: ValueType) : Loadable<ValueType>
  data class Loading<ValueType>(val previousValue: ValueType? = null) : Loadable<ValueType>
  data class Failure<ValueType>(val throwable: Throwable, val message: String? = null) :
    Loadable<ValueType>
}

fun <ReturnType> wrapInLoadableFlow(
  action: suspend () -> ReturnType
): Flow<Loadable<ReturnType>> = flow {
  emit(Loading())
  try {
    emit(Success(action()))
  } catch (throwable: Throwable) {
    emit(Failure(throwable))
  }
}

@Composable
fun <ValueType> Loadable<ValueType>.Unwrap() {

}

fun <StartingType, TargetType> Loadable<StartingType>.map(
  action: (StartingType) -> TargetType
): Loadable<TargetType> = when (this) {
  is Success -> Success(action(value))
  is Loading -> if (previousValue != null) {
    Loading(action(previousValue))
  } else {
    Loading(null)
  }
  is Failure -> Failure(throwable)
}

