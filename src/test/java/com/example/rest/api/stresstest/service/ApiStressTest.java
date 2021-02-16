package com.example.rest.api.stresstest.service;

import static com.example.rest.api.stresstest.util.TestUtil.printClassEnd;
import static com.example.rest.api.stresstest.util.TestUtil.printClassStart;
import static com.example.rest.api.stresstest.util.TestUtil.printEnd;
import static com.example.rest.api.stresstest.util.TestUtil.printStart;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.rest.api.stresstest.entity.StressResult;
import com.example.rest.api.stresstest.util.RandomName;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "")
public class ApiStressTest {

    private static final Logger LOG = LoggerFactory.getLogger(ApiStressTest.class);

    @Autowired
    private ApiRequestService apiRequestService;

    @BeforeAll
    public static void classStart() {
        printClassStart();
    }

    /** Print end of test class. */
    @AfterAll
    public static void classEnd() {
        printClassEnd();
    }

    @Test
    public void testSingleApiCall() {
        printStart();
        String uri = "http://localhost:3000/findName?name=John";

        try {
            StressResult pr = apiRequestService.apiCaller(uri);
            LOG.info(pr.toString());
        } catch (InterruptedException e) {
            LOG.error("InterruptedException: [{}]", e);
        } catch (ExecutionException e) {
            LOG.error("ExecutionException: [{}]", e);
        }

        printEnd();
    }

    @Test
    public void testSequencialApiCall() {
        printStart();
        String uri = "http://localhost:3000/findName?name=John";

        List<StressResult> results = apiRequestService.apiSeqCaller(uri, 10);

        for (StressResult StressResult : results) {
            LOG.info(StressResult.toString());
        }
        assertTrue(results.size() == 10);

        printEnd();
    }

    @Test
    public void testConcurrentApiCall() {
        printStart();
        String uri = "http://localhost:3000/findName?name=John";

        List<StressResult> results = apiRequestService.apiCaller(uri, 10);

        for (StressResult StressResult : results) {
            LOG.info(StressResult.toString());
        }
        assertTrue(results.size() == 10);

        printEnd();
    }

    @Test
    public void testRandomNameConcurrentApiCall() {
        printStart();

        // Setting
        int count = 10;

        String uri = "http://localhost:3000/findName?name=";

        List<String> uris = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            String name = RandomName.randomName();
            uris.add(uri + name);
        }

        List<StressResult> results = apiRequestService.apiCaller(uris);

        for (StressResult StressResult : results) {
            LOG.info(StressResult.toString());
        }
        assertTrue(results.size() == 10);

        printEnd();
    }

}

