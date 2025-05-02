package com.weather.app.service.provider;

import com.weather.app.model.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service("openWeatherMapProvider")
public class OpenWeatherMapProvider implements  WeatherProvider {

    private static final Logger log = LoggerFactory.getLogger(OpenWeatherMapProvider.class);

    @Value("${weather.api.openWeatherMap.url}")
    private String url;

    @Value("${weather.api.openWeatherMap.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public OpenWeatherMapProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherResponse getWeatherInfo(String city) {

        log.info("Fetching weather info from secondary provider - OpenWeatherMap");

        String requestUrl = String.format("%s?q=%s,AU&appid=%s&units=metric", url, city, apiKey);

        Map<String, Object> response = restTemplate.getForObject(requestUrl, Map.class);

        if (response == null || !response.containsKey("main") || !response.containsKey("wind")) {
            throw new IllegalStateException("Unexpected response from weather API: missing 'main' or 'wind' data.");
        }

        Map<String, Object> main = castToMap(response.get("main"));
        Map<String, Object> wind = castToMap(response.get("wind"));

        return WeatherResponse.builder()
                .temperatureDegrees(((Number) main.get("temp")).intValue())
                .windSpeed(((Number) wind.get("speed")).intValue())
                .build();

    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castToMap(Object obj) {
        if (!(obj instanceof Map)) {
            throw new IllegalArgumentException("Expected a Map but got: " + obj);
        }
        return (Map<String, Object>) obj;
    }


}
