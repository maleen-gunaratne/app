package com.weather.app.service;

import com.weather.app.model.WeatherResponse;
import com.weather.app.exception.WeatherProviderException;
import com.weather.app.service.provider.WeatherProvider;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.weather.app.config.CacheConfig.WEATHER_CACHE;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);


    private final WeatherProvider primary;
    private final WeatherProvider fallback;

    public WeatherService(@Qualifier("weatherStackProvider") WeatherProvider primary,
                          @Qualifier("openWeatherMapProvider") WeatherProvider fallback) {
        this.primary = primary;
        this.fallback = fallback;
    }

    @Cacheable(value = WEATHER_CACHE , key = "#city.toLowerCase()")
    @CircuitBreaker(name = "weatherService", fallbackMethod = "fallbackWeather")
    @Retry(name = "weatherService")
    public WeatherResponse getWeather(String city) {
            log.info("Starting fetching weather info from providers");
            return primary.getWeatherInfo(city);
    }

    public WeatherResponse fallbackWeather(String city, Exception  e) {
        try {
            log.info("Fallback triggered for city: {} ", city);
            return fallback.getWeatherInfo(city);
        } catch (Exception ex) {
            throw new WeatherProviderException("All providers are currently unavailable");
        }
    }
}
