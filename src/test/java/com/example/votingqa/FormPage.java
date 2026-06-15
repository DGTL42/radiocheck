package com.example.votingqa;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

public class FormPage {
    private final Page page;

    public FormPage(Page page) {
        this.page = page;
    }

    public void open(String url) {
        page.navigate(url);
    }

    public void selectRadioByLabel(String label) {
        page.getByLabel(label).check();
    }

    public boolean isRadioSelected(String label) {
        return page.getByLabel(label).isChecked();
    }

    public void submit(String buttonNameRegex) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName(Pattern.compile(buttonNameRegex, Pattern.CASE_INSENSITIVE)))
                .click();
    }

    public Locator messageMatching(String regex) {
        return page.getByText(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
    }
}
