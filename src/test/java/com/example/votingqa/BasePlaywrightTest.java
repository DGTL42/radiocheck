package com.example.votingqa;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

abstract class BasePlaywrightTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected Properties config;

    @BeforeEach
    void setUp() throws IOException {
        config = loadConfig();

        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(headless ? 0 : 100));

        // Clean browser context for each test. This gives isolation without trying to bypass application rules.
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1440, 1000));
        page = context.newPage();
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

    protected String baseUrl() {
        return System.getProperty("baseUrl", config.getProperty("base.url"));
    }

    private Properties loadConfig() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.properties")) {
            if (inputStream == null) {
                throw new IOException("Missing test.properties in src/test/resources");
            }
            properties.load(inputStream);
        }
        return properties;
    }
}
