package com.example.rest.api.stresstest;

import static com.example.rest.api.stresstest.util.TestUtil.printClassEnd;
import static com.example.rest.api.stresstest.util.TestUtil.printClassStart;
import static com.example.rest.api.stresstest.util.TestUtil.printEnd;
import static com.example.rest.api.stresstest.util.TestUtil.printStart;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TestTemplate {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(TestTemplate.class);

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
    public void testSample() {

        printStart();

        printEnd();
    }
}
