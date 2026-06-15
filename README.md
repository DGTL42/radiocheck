# Java Playwright Form QA Template

This is a safe QA automation template for testing a radio-button form flow on a system you own or are authorized to test.

It is **not** intended for repeatedly voting on public polls, bypassing voting restrictions, or manipulating results.

## What it covers

1. Opens a clean browser context.
2. Navigates to a configurable form URL.
3. Selects a radio button.
4. Confirms/submits the selection.
5. Verifies a success message.
6. Verifies that a duplicate submission is blocked or handled correctly.

## Requirements

- Java 17+
- Maven 3.8+

## Install Playwright browsers

```bash
mvn -q exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
```

If the above command is not available in your Maven setup, use:

```bash
mvn test
```

Playwright will normally download the browser binaries when first used.

## Run tests

```bash
mvn test -DbaseUrl="https://your-test-environment.example.com/form"
```

Run headed browser:

```bash
mvn test -DbaseUrl="https://your-test-environment.example.com/form" -Dheadless=false
```

## Adapt selectors

Open `src/test/resources/test.properties` and update:

- `radio.label`
- `submit.button.name.regex`
- `success.message.regex`
- `duplicate.message.regex`

You can also override the URL from command line with `-DbaseUrl=...`.

## Recommended QA approach

Use this template to prove both positive and protective behavior:

- A valid user can submit once.
- The selected radio option is stored or acknowledged.
- A second attempt by the same session/user/token is rejected.
- The user sees a clear duplicate-vote message.
- Server-side validation prevents duplicate submissions even if the UI is bypassed.

## Project structure

```text
java-playwright-form-qa/
├── pom.xml
├── README.md
├── src/test/java/com/example/votingqa/
│   ├── BasePlaywrightTest.java
│   ├── FormPage.java
│   └── VotingFormQaTest.java
└── src/test/resources/
    └── test.properties
```
