package com.example.rest.api.stresstest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.example.rest.api.stresstest.auth.AuthTokenService;
import com.example.rest.api.stresstest.entity.StressResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Caller of ApiAsyncService
 */
@Service
public class ApiRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(ApiRequestService.class);

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private ApiAsyncService apiAsyncService;

    @Value("${auth0.enable}")
    private boolean isAuth;

    /**
     * Call a same uri in x times Sequencially
     * @param uri
     * @param repeat
     * @return
     */
    public List<StressResult> apiSeqCaller(String uri, int repeat) {
        LOG.debug("apiSeqCaller Started");
        List<CompletableFuture<StressResult>> completableFutures = new ArrayList<>();
        List<StressResult> results = new ArrayList<StressResult>();

        String token = null;
        if(isAuth){
            token = authTokenService.retrieveAccessToken();
        }

        for (int i = 0; i < repeat; i++) {
            CompletableFuture<StressResult> pr = apiAsyncService.apiSeqCaller(uri, token);
            completableFutures.add(pr);
            pr.thenAccept(result -> results.add(result));
        }

        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();

        LOG.debug("apiSeqCaller Ended");
        return results;
    }

    /**
     * Call a same uri in x times Asynchronous
     * @param uri
     * @param repeat
     * @return
     */
    public List<StressResult> apiCaller(String uri, int repeat) {
        LOG.debug("apiCaller Started: repeat = " + repeat);
        List<CompletableFuture<StressResult>> completableFutures = new ArrayList<>();
        List<StressResult> results = new ArrayList<StressResult>();

        String token = null;
        if(isAuth){
            token = authTokenService.retrieveAccessToken();
        }

        for (int i = 0; i < repeat; i++) {
            CompletableFuture<StressResult> pr = apiAsyncService.apiAsyncCaller(uri, token);
            completableFutures.add(pr);
            pr.thenAccept(result -> results.add(result));
        }

        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();

        LOG.debug("apiCaller Ended");
        return results;
    }

    /**
     * Call a list of uri
     * @param uris
     * @return
     */
    public List<StressResult> apiCaller(List<String> uris) {
        LOG.debug("apiCaller Started: List");
        List<CompletableFuture<StressResult>> completableFutures = new ArrayList<>();
        List<StressResult> results = new ArrayList<StressResult>();

        String token = null;
        if(isAuth){
            token = authTokenService.retrieveAccessToken();
        }

        for (String url : uris) {
            CompletableFuture<StressResult> pr = apiAsyncService.apiAsyncCaller(url, token);
            completableFutures.add(pr);
            pr.thenAccept(result -> results.add(result));
        }

        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();

        LOG.debug("apiCaller Ended");
        return results;
    }

    /**
     * Call a uri
     * @param uri
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public StressResult apiCaller(String uri) throws InterruptedException, ExecutionException {
        LOG.debug("apiCaller Started");

        String token = null;
        if(isAuth){
            token = authTokenService.retrieveAccessToken();
        }

        CompletableFuture<StressResult> pr = apiAsyncService.apiAsyncCaller(uri, token);

        CompletableFuture.allOf(pr).join();

        LOG.debug("apiCaller Ended");
        return pr.get();
    }
}

