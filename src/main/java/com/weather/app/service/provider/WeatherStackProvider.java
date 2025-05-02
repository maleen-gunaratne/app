package com.weather.app.service.provider;

import com.weather.app.model.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.weather.app.exception.WeatherProviderException;

import java.util.Map;

@Service("weatherStackProvider")
public class WeatherStackProvider implements WeatherProvider {

    private static final Logger log = LoggerFactory.getLogger(WeatherStackProvider.class);

    private final RestTemplate restTemplate;

    @Value("${weather.api.weatherStack.url}")
    private String url;

    @Value("${weather.api.weatherStack.key}")
    private String apiKey;

    @Autowired
    public WeatherStackProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherResponse getWeatherInfo(String city) {

        log.info("Fetching weather info from primary provider - WeatherStack");

        try {
            String requestUrl = String.format("%s?access_key=%s&query=%s", url, apiKey, city);

            Map<String, Object> response = restTemplate.getForObject(requestUrl, Map.class);

            if (response == null) {
                log.error("Received null response from WeatherStack API");
                throw new WeatherProviderException("Null response from WeatherStack");
            }

            if (!response.containsKey("current")) {
                log.error("Missing 'current' field in WeatherStack response: {}", response.keySet());
                throw new WeatherProviderException("Invalid response from WeatherStack: missing current data");
            }

            Map<String, Object> current = (Map<String, Object>) response.get("current");

            return WeatherResponse.builder()
                    .temperatureDegrees(((Integer) current.get("temperature")))
                    .windSpeed(((Integer) current.get("wind_speed")))
                    .build();

        } catch (RestClientException ex) {
            log.error("Error fetching data from WeatherStack: {}", ex.getMessage());
            throw new WeatherProviderException("Failed to retrieve data from WeatherStack", ex);
        } catch (ClassCastException ex) {
            log.error("Error parsing WeatherStack response: {}", ex.getMessage());
            throw new WeatherProviderException("Invalid response format from WeatherStack", ex);
        }
    }
}