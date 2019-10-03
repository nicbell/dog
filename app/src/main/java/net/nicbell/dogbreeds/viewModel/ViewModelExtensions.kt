package net.nicbell.dogbreeds.viewModel

import androidx.lifecycle.MutableLiveData
import net.nicbell.dogbreeds.api.ApiResponse
import retrofit2.HttpException
import java.io.IOException

object ViewModelExtensions {
    /**
     * Handle API call:
     * Do your API call in the body and errors will be caught and handled.
     */
    inline fun <T> handleApiCall(
        error: MutableLiveData<Event<String>>,
        apiCall: () -> ApiResponse<T>
    ): ApiResponse<T>? {
        try {
            return apiCall.invoke()
        } catch (ex: HttpException) {
            error.postValue(Event(("${ex.code()} - ${ex.message()}")))
        } catch (ex: IOException) {
            error.postValue(Event(ex.message ?: "Unknown IOException"))
        }

        return null
    }
}