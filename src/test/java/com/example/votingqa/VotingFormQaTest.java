package com.example.votingqa;

import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VotingFormQaTest extends BasePlaywrightTest {

    @Test
    @DisplayName("User can select a radio option and submit the form once")
    void userCanSelectRadioOptionAndSubmitForm() {
        FormPage formPage = new FormPage(page);

        String radioLabel = config.getProperty("radio.label");
        String submitButtonRegex = config.getProperty("submit.button.name.regex");
        String successMessageRegex = config.getProperty("success.message.regex");

        formPage.open(baseUrl());
        formPage.selectRadioByLabel(radioLabel);

        assertThat(formPage.isRadioSelected(radioLabel))
                .as("Expected radio option to be selected before submit")
                .isTrue();

        formPage.submit(submitButtonRegex);

        PlaywrightAssertions.assertThat(formPage.messageMatching(successMessageRegex))
                .isVisible();
    }

    @Test
    @DisplayName("Application should block or clearly handle duplicate submission")
    void duplicateSubmissionShouldBeBlockedOrHandled() {
        FormPage formPage = new FormPage(page);

        String radioLabel = config.getProperty("radio.label");
        String submitButtonRegex = config.getProperty("submit.button.name.regex");
        String successMessageRegex = config.getProperty("success.message.regex");
        String duplicateMessageRegex = config.getProperty("already.submitted.message.regex");

        formPage.open(baseUrl());
        formPage.selectRadioByLabel(radioLabel);
        formPage.submit(submitButtonRegex);

        PlaywrightAssertions.assertThat(formPage.messageMatching(successMessageRegex))
                .isVisible();

        // The second action models a protection check, not a repeat-voting workflow.
        // Depending on your application, this might require navigating back, reloading,
        // or opening the same link again while keeping the same browser context/session.
        formPage.open(baseUrl());
        formPage.selectRadioByLabel(radioLabel);
        formPage.submit(submitButtonRegex);

        PlaywrightAssertions.assertThat(formPage.messageMatching(duplicateMessageRegex))
                .isVisible();
    }
}
