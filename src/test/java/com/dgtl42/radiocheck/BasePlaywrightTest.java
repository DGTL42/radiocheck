package com.dgtl42.radiocheck;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import config.TestConfig;

abstract class BasePlaywrightTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page pagePoll;
    protected Page pageArticle;
    protected PollForm pollForm;

    @BeforeEach
    void setUp() {

        boolean headless =
                Boolean.parseBoolean(
                        System.getProperty("headless", TestConfig.get("browser.headless")));

        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(headless ? 0 : 100));

        // Clean browser context for each test. This gives isolation without trying to bypass application rules.
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1440, 1000));

        pageArticle = context.newPage();
        pageArticle.navigate(TestConfig.get("base.url.clanek"));

        pagePoll = context.newPage();
        pollForm = new PollForm(pagePoll);
        pagePoll.navigate(TestConfig.get("base.url.poll"));

    }

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    protected void writePollLogLine(
            int oldVoterCount,
            int selectedRadioIndex,
            String selectedRadioName,
            int newVoterCount
    ) {

        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String logLine = String.format(
                "%s | old voters: %d | selected index: %d | selected name: %s | new voters: %d%n",
                timestamp,
                oldVoterCount,
                selectedRadioIndex,
                selectedRadioName,
                newVoterCount
        );

        java.nio.file.Path logFile = java.nio.file.Path.of("target", "poll-vote-log.txt");

        try {
            java.nio.file.Files.createDirectories(logFile.getParent());

            java.nio.file.Files.writeString(
                    logFile,
                    logLine,
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void waitForNextUse(int testIdx) {
        int minMinutes = 10;
        int maxMinutes = 30;
        int randomSeconds = java.util.concurrent.ThreadLocalRandom.current().nextInt(10, 60);
        int randomMinutes = java.util.concurrent.ThreadLocalRandom.current().nextInt(minMinutes, maxMinutes);
        writePollLogLine(randomMinutes,randomSeconds,"waiting", testIdx);
        pagePoll.waitForTimeout(randomSeconds * 1000L);
        pagePoll.waitForTimeout(randomMinutes * 60_000L);
    }
}
