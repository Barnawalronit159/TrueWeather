package eu.tutorials.trueweather.api

//with T , we are referring to  weathermodel
sealed class NetworkResponce<out T> {
    data class Success<out T>(val data:T): NetworkResponce<T>()
    data class Error(val message:String): NetworkResponce<Nothing>()
    object loading: NetworkResponce<Nothing>()
}