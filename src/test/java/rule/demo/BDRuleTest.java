package rule.demo;

import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static rule.demo.Predicates.always;
import static rule.demo.Predicates.never;

public class BDRuleTest {

    @Test
    public void emptyRuleShouldProvideEmptyResult() {
        assertThat(BDRule.of().get()).isEmpty();
    }

    @Test
    public void noFilterAndNoActionShouldProvideTheInput() {
        assertThat(BDRule.of().given("abc").get()).contains("abc");
    }

    @Test
    public void matchingFilterWithActionShouldProvideActionResult() {
        assertThat(
            BDRule.<String, String>of()
                .given("abc")
                .when(always())
                .then(String::toUpperCase)
                .get()
        ).contains("ABC");
    }

    @Test
    public void notMatchingFilterShouldProvideEmptyResult() {
        assertThat(
            BDRule.<String, String>of()
                .given("abc")
                .when(never())
                .then(String::toUpperCase)
                .get()
        ).isEmpty();
    }

    @Test
    public void notMatchingFilterWithElseClauseShouldProvideElseResult() {
        assertThat(
            BDRule.<String, String>of()
                .given("abc")
                .when(never())
                .then(String::toUpperCase)
            .get().orElse("xyz")
        ).isEqualTo("xyz");
    }

    @Test
    public void matchingRuleWithMatchingFilterShouldProvideMatchingRuleActionResult() {
        assertThat(
            BDRule.<String, String>of()
                .given("abc")
                .when(never())
                .then(String::toUpperCase)
                .get()
            .or(
                BDRule.<String, String>of()
                    .given("xyz")
                    .when(always())
                    .then(String::toUpperCase)
            )
            .orElse("123")
        ).isEqualTo("XYZ");
    }

    @Test
    public void notMatchingRulesShouldProvideEmptyResult() {
        assertThat(
            BDRule.<String, String>of()
                .given("abc")
                .when(never())
                .then(String::toUpperCase)
            .or(
                BDRule.<String, String>of()
                    .given("xyz")
                    .when(never())
                    .then(String::toUpperCase)
            )
        ).isEmpty();
    }

    @Test
    public void notMatchingRulesWithElseClauseShouldProvideElseResult() {
        assertThat(
            BDRule.<String, String>of()
                .given("abc")
                .when(never())
                .then(String::toUpperCase)
            .or(
                BDRule.<String, String>of()
                    .given("xyz")
                    .when(never())
                    .then(String::toUpperCase)
            ).orElse("123")
        ).isEqualTo("123");
    }

    @Test
    public void mapperFunctionTypeShouldMatch() {
        assertThat(
            BDRule.<String, Integer>of()
                .given("abc")
                .then(String::length)
                .get()
        ).contains(3);
    }

    @Test
    public void chainedFilterAndMapperShouldTakeEffect() {
        Predicate<String> isShort = s -> s.length() < 5;
        Predicate<String> hasOddLength = s -> s.length() % 2 != 0;
        Function<String, String> appendA = s -> s + "A";

        assertThat(
            BDRule.<String, Integer>of()
                .given("abc")
                .when(isShort.and(hasOddLength))
                .then(appendA.andThen(String::toUpperCase).andThen(String::length).andThen(x -> x + 1))
                .get()
        ).contains(5);
    }
}
