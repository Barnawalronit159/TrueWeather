🌦️ Weather App

A simple and modern Android weather application built using Kotlin and Jetpack Compose.
It fetches real-time weather data for any city using a weather API and displays temperature, conditions, humidity, wind speed, and more — all in a clean Compose UI.

🚀 Features

- 🔍 Search weather by city name

- 📍 Get current location weather (if implemented)

- 🌡️ Real-time temperature

- ☁️ Current weather condition (Cloudy, Sunny, Rainy, etc.)

- 💨 Wind speed & humidity

- 🎨 Modern UI with Jetpack Compose

- ⚡ Fast API calls using Retrofit / Ktor (whichever you used)

🛠 Tech Stack

- Kotlin

- Jetpack Compose

- Material 3

- Retrofit (or Ktor — edit based on your actual project)

- ViewModel + StateFlow

- Location Services (if used)

- Coil (for weather icons, if you used it)

🧠 What I Learned

- Making API requests with Retrofit

- Parsing JSON responses cleanly

- Managing loading/error/success UI states

- Using ViewModel + state in Compose

- Designing UI entirely with composables

- Handling user input & text fields in real-time

🔧 How to Run the Project

1. Clone the repo:

    git clone https://github.com/Barnawalronit159/YourWeatherAppRepo.git


2. Open in Android Studio Hedgehog or newer

3. Add your API key in local.properties or inside your WeatherApiService

4. Build & run on emulator/device

🗝️ API Key Setup (Important)

 If you are using WeatherAPI.com:

1. Visit https://www.weatherapi.com/

2. Create a free account

3. Generate your API key

4. Add the key in one of these places:

    - In local.properties

         WEATHER_API_KEY=your_api_key_here


    - OR directly inside your WeatherApiService.kt (not recommended for production)

⚠️ Important:

- Never upload your API key to GitHub

- Add local.properties to your .gitignore (Android already does this by default)


## 📬 Author

**Ronit Barnawal**  
🔗 [LinkedIn](https://www.linkedin.com/in/ronitbarnawal)



⭐ Support

If you found this project helpful, star the repo — it motivates and helps visibility.
