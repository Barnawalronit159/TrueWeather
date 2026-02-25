package eu.tutorials.trueweather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import coil.compose.AsyncImage
import eu.tutorials.trueweather.api.NetworkResponse
import eu.tutorials.trueweather.api.WeatherModel
import eu.tutorials.trueweather.ui.theme.*

@Composable
fun WeatherPage(viewModel: WeatherViewModel){
    var city by remember{ mutableStateOf("") }
    val weatherResult = viewModel.weatherResult.observeAsState()
    //to hide keyboard after searching
    var keyboardController= LocalSoftwareKeyboardController.current

    val backgroundBrush = getBackgroundBrush(weatherResult.value)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Column(
            modifier= Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    modifier=Modifier.weight(1f),
                    value = city,
                    onValueChange = {city=it},
                    label = { Text(text = "Search for City", color = Color.White)},
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White)
                )
                IconButton(
                    onClick = {
                        if(city.isNotBlank()){
                            viewModel.getData(city)
                            keyboardController?.hide()
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if(city.isNotBlank()) GlassWhite else GlassWhite.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "To search about",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when(val result = weatherResult.value){
                is NetworkResponse.Error -> {
                    Text(text = result.message, color = Color.White, fontWeight = FontWeight.SemiBold)
                }
                is NetworkResponse.Success -> {
                    WeatherDetails(data = result.data)
                }
                NetworkResponse.Loading -> {
                    CircularProgressIndicator(color = Color.White)
                }
                null -> {
                    Text(
                        text = "Search a city to see weather",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 100.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun getBackgroundBrush(result: NetworkResponse<WeatherModel>?): Brush {
    val (startColor, endColor) = when (result) {
        is NetworkResponse.Success -> {
            val condition = result.data.current.condition.text.lowercase()
            when {
                condition.contains("sunny") || condition.contains("clear") -> SunnyStart to SunnyEnd
                condition.contains("cloud") || condition.contains("overcast") || condition.contains("mist") || condition.contains("fog") -> CloudyStart to CloudyEnd
                condition.contains("rain") || condition.contains("drizzle") || condition.contains("thund") -> RainyStart to RainyEnd
                else -> NightStart to NightEnd
            }
        }
        else -> NightStart to NightEnd
    }
    return Brush.verticalGradient(colors = listOf(startColor, endColor))
}

@Composable
fun WeatherDetails(data:WeatherModel){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier=Modifier.size(40.dp),
                tint = Color.White
            )
            Text(text = data.location.name, fontSize = 30.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country, fontSize = 18.sp, color = Color.White.copy(alpha = 0.7f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${data.current.temp_c}˚C",
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White
        )

        AsyncImage(
            modifier=Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64","128x128"),
            contentDescription = "weather condition"
        )
        Text(
            text = data.current.condition.text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.9f)
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        // Glassmorphic Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(GlassWhite)
                .border(1.dp, GlassWhiteStroke, RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyValue(key = "Humidity", value = data.current.humidity)
                    WeatherKeyValue(key = "Wind Speed", value = data.current.wind_kph+" km/h")
                }
                Row(
                    modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyValue(key = "UV", value = data.current.uv)
                    WeatherKeyValue(key = "Precipitation", value = data.current.precip_mm+" mm")
                }
                Row(
                    modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyValue(key = "Local Time", value = data.location.localtime.split(" ")[1])
                    WeatherKeyValue(key = "Local Date", value = data.location.localtime.split(" ")[0])
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "powered by",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
            Text(
                text = "R.B.",
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun WeatherKeyValue(key:String,value:String){
    Column(
        modifier=Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text=value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
    }
}
