package com.dgtl42.radiocheck;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PollFormTest extends BasePlaywrightTest {

    @RepeatedTest(100)
    @DisplayName("User can select a radio option and submit the form once")
    void shouldSubmitPollAndLogVoterCountChange(RepetitionInfo repetitionInfo) {

        //int selectedRadioIndex = 5;
        int selectedRadioIndex =
                (repetitionInfo.getCurrentRepetition() % 2 != 0) ? 5 :
                        java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 9);

        pollForm.scrollToPoll();
        int oldVoterCount = pollForm.getVoterCount();
        String selectedRadioName = pollForm.getRadioButtonNameByIndex(selectedRadioIndex);
        pollForm.selectRadioButtonByIndex(selectedRadioIndex);
        pollForm.submitPoll();

        int newVoterCount = pollForm.waitUntilVoterCountChangesFrom(oldVoterCount);

        assertTrue(
                newVoterCount > oldVoterCount,
                "Expected voter count to increase after submitting the poll"
        );

        writePollLogLine(
                oldVoterCount,
                selectedRadioIndex,
                selectedRadioName,
                newVoterCount
        );

        waitForNextUse(repetitionInfo.getCurrentRepetition());
    }



}
