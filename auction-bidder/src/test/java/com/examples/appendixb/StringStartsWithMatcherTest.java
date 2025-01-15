package com.examples.appendixb;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class StringStartsWithMatcherTest {
    @Test
    public void exampleTest() {
        String someString = "Bananas";
        String expectedPrefix = "Cheese";

        StringStartsWithMatcher matcher = startsWith(expectedPrefix);
        try {
            assertThat(someString, matcher);
        } catch (AssertionError error) {
            Description mismatchDescription = new StringDescription();
            matcher.describeMismatchSafely(someString, mismatchDescription);

            throw error;
        }
    }

    private static StringStartsWithMatcher startsWith(String prefix) {
        return new StringStartsWithMatcher(prefix);
    }
}
