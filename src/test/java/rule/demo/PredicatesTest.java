package rule.demo;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static rule.demo.Predicates.allMatch;
import static rule.demo.Predicates.allOf;
import static rule.demo.Predicates.always;
import static rule.demo.Predicates.anyMatch;
import static rule.demo.Predicates.anyOf;
import static rule.demo.Predicates.eq;
import static rule.demo.Predicates.in;
import static rule.demo.Predicates.never;
import static rule.demo.Predicates.noneMatch;
import static rule.demo.Predicates.noneOf;

public class PredicatesTest {

    @Test
    public void allOfTest() {
        assertThat(allOf()).accepts("");

        assertThat(allOf(always())).accepts("");
        assertThat(allOf(never())).rejects("");

        assertThat(allOf(always(), always())).accepts("");
        assertThat(allOf(always(), never())).rejects("");
        assertThat(allOf(never(), always())).rejects("");
        assertThat(allOf(never(), never())).rejects("");

        assertThat(allOf(eq("abc"))).accepts("abc");
        assertThat(allOf(eq("abc"))).rejects("");
    }

    @Test
    public void anyOfTest() {
        assertThat(anyOf()).rejects("");

        assertThat(anyOf(always())).accepts("");
        assertThat(anyOf(never())).rejects("");

        assertThat(anyOf(always(), always())).accepts("");
        assertThat(anyOf(always(), never())).accepts("");
        assertThat(anyOf(never(), always())).accepts("");
        assertThat(anyOf(never(), never())).rejects("");

        assertThat(anyOf(eq("abc"))).accepts("abc");
        assertThat(anyOf(eq("abc"))).rejects("");
    }

    @Test
    public void noneOfTest() {
        assertThat(noneOf()).accepts("");

        assertThat(noneOf(always())).rejects("");
        assertThat(noneOf(never())).accepts("");

        assertThat(noneOf(always(), always())).rejects("");
        assertThat(noneOf(always(), never())).rejects("");
        assertThat(noneOf(never(), always())).rejects("");
        assertThat(noneOf(never(), never())).accepts("");

        assertThat(noneOf(eq("abc"))).rejects("abc");
        assertThat(noneOf(eq("abc"))).accepts("");
    }

    @Test
    public void matchTest() {
        Predicate<Integer> greaterThanOne = i -> i > 1;
        Predicate<Iterable<Integer>> allGreaterThanOne = allMatch(greaterThanOne);
        Predicate<Iterable<Integer>> anyGreaterThanOne = anyMatch(greaterThanOne);
        Predicate<Iterable<Integer>> noneGreaterThanOne = noneMatch(greaterThanOne);

        assertThat(allGreaterThanOne).accepts(List.of(2, 3, 4));
        assertThat(allGreaterThanOne).rejects(List.of(1, 2, 3));

        assertThat(anyGreaterThanOne).accepts(List.of(1, 2, 3));
        assertThat(anyGreaterThanOne).rejects(List.of(-1, 0, 1));

        assertThat(noneGreaterThanOne).accepts(List.of(-1, 0, 1));
        assertThat(noneGreaterThanOne).rejects(List.of(1, 2, 3));

        assertThat(in(1, 2, 3, 4, 5)).accepts(3);
        assertThat(in(1, 2, 3, 4, 5)).rejects(7);
    }
}
