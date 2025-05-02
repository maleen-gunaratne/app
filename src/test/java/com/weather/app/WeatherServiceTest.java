package com.weather.app;

import com.weather.app.model.WeatherResponse;
import com.weather.app.service.WeatherService;
import com.weather.app.service.provider.WeatherProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherServiceTest {

    @Test
    void testGetWeatherFallback() {
        WeatherProvider primary = mock(WeatherProvider.class);
        WeatherProvider fallback = mock(WeatherProvider.class);
        when(primary.getWeatherInfo("melbourne")).thenThrow(new RuntimeException());
        when(fallback.getWeatherInfo("melbourne")).thenReturn(new WeatherResponse(25, 15));

        WeatherService service = new WeatherService(primary, fallback);
        WeatherResponse response = service.fallbackWeather("melbourne", new RuntimeException());

        assertEquals(25, response.getTemperatureDegrees());
        assertEquals(15, response.getWindSpeed());
    }
}
