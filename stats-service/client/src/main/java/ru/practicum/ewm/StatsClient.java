package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void postStats(Hit hit) {
        post(hit);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris,
                                    Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", String.join(",", uris),
                "unique", unique
        );

        ResponseEntity<Object> responseEntity = get(parameters);

        List<ViewStats> forRet = new ArrayList<>();


        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            Object responseBody = responseEntity.getBody();
            if (responseBody instanceof List) {

                List<?> responseList = (List<?>) responseBody;

                for (Object item : responseList) {

                    ViewStats viewStats = new ViewStats();

                    if (item instanceof Map) {

                        Map<?, ?> responseMap = (Map<?, ?>) item;

                        Object hits = responseMap.get("hits");

                        Object uri = responseMap.get("uri");

                        Object app = responseMap.get("app");

                        if (hits instanceof Number) {
                            viewStats.setHits(((Number) hits).longValue());
                        }

                        if (uri instanceof String) {

                            viewStats.setUri(((String) uri).substring(0, 1));
                        }

                        if (app instanceof String) {

                            viewStats.setApp(((String) app).substring(0, 1));
                        }
                    }

                    forRet.add(viewStats);
                }

            }

        }
        return forRet;
    }

}