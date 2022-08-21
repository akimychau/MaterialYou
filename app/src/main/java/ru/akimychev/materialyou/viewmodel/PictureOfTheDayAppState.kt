package ru.akimychev.materialyou.viewmodel

import ru.akimychev.materialyou.model.dto.PODServerResponseData

sealed class PictureOfTheDayAppState {
    data class Success(val serverResponseData: PODServerResponseData) : PictureOfTheDayAppState()
    data class Error(val error: Throwable) : PictureOfTheDayAppState()
    data class Loading(val progress: Int?) : PictureOfTheDayAppState()
}
