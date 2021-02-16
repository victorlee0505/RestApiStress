package com.example.rest.api.stresstest.service;

import java.util.concurrent.CompletableFuture;

import com.example.rest.api.stresstest.entity.StressResult;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * Async RestTemplate
 */
@Service
public class ApiAsyncService {

    private static final Logger LOG = LoggerFactory.getLogger(ApiAsyncService.class);

    /**
     * Call an uri in Sequencial
     * @param uri
     * @param token
     * @return
     */
    public CompletableFuture<StressResult> apiSeqCaller(String uri, String token){
        return apiAsyncCaller(uri, token);
    }

    /**
     * Call an uri in Async
     * @param uri
     * @param token
     * @return
     */
    @Async("apiExecutor")
    public CompletableFuture<StressResult> apiAsyncCaller(String uri, String token) {

        LOG.debug("apiCaller Started");

        StressResult result = new StressResult();

        result.setUrl(uri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if(token !=null){
            headers.setBearerAuth(token);
        }

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        RestTemplate restTemplate = new RestTemplate();

        StopWatch stopwatch = StopWatch.create();
        stopwatch.start();

        ResponseEntity<String> res = null;
        try {
            res = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        } catch (HttpStatusCodeException e) {
            LOG.error("ApiAsyncService Exception: [{}]", e);
            res = ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        }

        stopwatch.stop();

        long elapsedTime = stopwatch.getTime();

        result.setElapsedTime(elapsedTime + "ms");
        result.setHttpStatus(res.getStatusCode());

        LOG.debug("apiCaller Ended");

        return CompletableFuture.completedFuture(result);
    }
}
