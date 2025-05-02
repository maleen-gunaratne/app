package com.weather.app.service.provider;

import com.weather.app.model.WeatherResponse;

public interface WeatherProvider {

    WeatherResponse getWeatherInfo(String city);

}
