package eu.tutorials.trueweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.tutorials.trueweather.api.Constant
import eu.tutorials.trueweather.api.NetworkResponse
import eu.tutorials.trueweather.api.RetrofitInstance
import eu.tutorials.trueweather.api.WeatherModel
import kotlinx.coroutines.launch
import org.json.JSONObject

class WeatherViewModel:ViewModel() {

    private val _weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult=MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city:String){
        val trimmedCity = city.trim()
        Log.d("WeatherViewModel", "Searching for city: '$trimmedCity'")
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = _weatherApi.getWeather(Constant.apiKey, trimmedCity)
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    } ?: run {
                        _weatherResult.value = NetworkResponse.Error("Empty response from server")
                    }
                }else{
                    val errorBody = response.errorBody()?.string()
                    Log.e("WeatherViewModel", "API Error: ${response.code()} Body: $errorBody")
                    val errorMsg = try {
                        val json = JSONObject(errorBody ?: "")
                        json.getJSONObject("error").getString("message")
                    } catch (e: Exception) {
                        when(response.code()){
                            401 -> "Invalid API Key"
                            400 -> "City not found"
                            else -> "Server error: ${response.code()}"
                        }
                    }
                    _weatherResult.value = NetworkResponse.Error(errorMsg)
                }
            }catch (e:Exception){
                Log.e("WeatherViewModel", "Execution Exception: ${e.javaClass.simpleName}", e)
                _weatherResult.value = NetworkResponse.Error("Request failed: ${e.localizedMessage}")
            }
        }
    }
}