package com.dgtl42.radiocheck;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import config.TestConfig;

public class PollForm {

    private final Page pollPage;

    public PollForm(Page page) {
        this.pollPage = page;
    }

    private Locator radioButtons() {
        return pollPage.locator(TestConfig.get("radio.button"));
    }

    private Locator confirmButton() {
        return pollPage.locator(TestConfig.get("poll.confirm.button"));
    }

    private Locator submittedButton() {
        return pollPage.locator(TestConfig.get("poll.submit.button.submitted"));
    }

    private Locator voterCount() {
        return pollPage.locator(TestConfig.get("poll.voter.count"));
    }

    public int getVoterCount() {
        String countText = voterCount().innerText().trim();
        return Integer.parseInt(countText);
    }

    public void selectRadioButtonByIndex(int index) {
        validateRadioButtonIndex(index);
        radioButtons().nth(index).check();
    }

    public String getRadioButtonNameByIndex(int index) {
        validateRadioButtonIndex(index);

        Locator radioButton = radioButtons().nth(index);
        String radioButtonId = radioButton.getAttribute("id");

        if (radioButtonId == null || radioButtonId.isBlank()) {
            throw new IllegalStateException("Radio button at index " + index + " has no id attribute.");
        }

        return pollPage.locator("label[for='" + radioButtonId + "']")
                .innerText()
                .trim();
    }

    public void submitPoll() {
        confirmButton().click();
    }

    public void waitUntilSubmitted() {
        submittedButton().waitFor(
                new Locator.WaitForOptions()
                        .setTimeout(60_000)
        );
    }

    public int waitUntilVoterCountChangesFrom(int oldVoterCount) {
        pollPage.waitForCondition(
                () -> getVoterCount() != oldVoterCount,
                new Page.WaitForConditionOptions()
                        .setTimeout(60_000)
        );

        return getVoterCount();
    }

    private void validateRadioButtonIndex(int index) {
        int expectedCount = TestConfig.getInt("radio.button.count");
        int actualCount = radioButtons().count();

        if (actualCount != expectedCount) {
            throw new IllegalStateException(
                    "Radio button count mismatch. Expected: "
                            + expectedCount + ", actual: " + actualCount
            );
        }

        if (index < 0 || index >= expectedCount) {
            throw new IllegalArgumentException(
                    "Invalid radio button index: " + index
                            + ". Allowed indexes are 0 to " + (expectedCount - 1)
            );
        }
    }

    public void scrollToPoll() {
        confirmButton().scrollIntoViewIfNeeded();
    }

}