package com.examples.appendixb;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class StringStartsWithMatcher extends TypeSafeMatcher<String> {
    private final String expectedPrefix;

    public StringStartsWithMatcher(String expectedPrefix) {
        this.expectedPrefix = expectedPrefix;
    }

    @Override
    public boolean matchesSafely(String actual) {
        return actual.startsWith(expectedPrefix);
    }

    @Override
    public void describeTo(Description matchDescription) {
        matchDescription.appendText("a string starting with ").appendValue(expectedPrefix);
    }

    protected void describeMismatchSafely(String actual, Description mismatchDescription) {
        String actualPrefix = actual.substring(0, Math.min(actual.length(), expectedPrefix.length()));
        mismatchDescription.appendText("started with ").appendValue(actualPrefix);
    }
}

