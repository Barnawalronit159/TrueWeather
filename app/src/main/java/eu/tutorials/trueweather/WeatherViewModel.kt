package eu.tutorials.trueweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.tutorials.trueweather.api.Constant
import eu.tutorials.trueweather.api.NetworkResponce
import eu.tutorials.trueweather.api.RetrofitInstance
import eu.tutorials.trueweather.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel:ViewModel() {

    private val _weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult=MutableLiveData<NetworkResponce<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponce<WeatherModel>> = _weatherResult

    //a funtion which will get data from retrofit for given city
    fun getData(city:String){
        _weatherResult.value=NetworkResponce.loading
        viewModelScope.launch {
            try {
                val responce = _weatherApi.getWeather(Constant.apiKey,city)
                if(responce.isSuccessful){
                    responce.body()?.let {
                        _weatherResult.value = NetworkResponce.Success(it)
                    }
                }else{
                    _weatherResult.value=NetworkResponce.Error("Failed to load data")
                }
            }catch (e:Exception){
                _weatherResult.value=NetworkResponce.Error("Failed to load data")
            }
        }
    }
}