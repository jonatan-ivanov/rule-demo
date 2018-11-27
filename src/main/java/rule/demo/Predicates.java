package rule.demo;

import lombok.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Predicates {

    public static <T> Predicate<T> always() {
        return constant(true);
    }

    public static <T> Predicate<T> never() {
        return constant(false);
    }

    public static <T> Predicate<T> constant(boolean answer) {
        return obj -> answer;
    }

    public static <T> Predicate<T> eq(T value) {
        return obj -> Objects.equals(obj, value);
    }

    @SafeVarargs
    public static <T> Predicate<T> allOf(@NonNull Predicate<T>... predicates) {
        return allOf(List.of(predicates));
    }

    public static <T> Predicate<T> allOf(@NonNull Iterable<Predicate<T>> predicates) {
        return stream(predicates).reduce(constant(true), Predicate::and); // lazy :)
    }

    @SafeVarargs
    public static <T> Predicate<T> anyOf(@NonNull Predicate<T>... predicates) {
        return anyOf(List.of(predicates));
    }

    public static <T> Predicate<T> anyOf(@NonNull Iterable<Predicate<T>> predicates) {
        return stream(predicates).reduce(constant(false), Predicate::or); // lazy :)
    }

    @SafeVarargs
    public static <T> Predicate<T> noneOf(@NonNull Predicate<T>... predicates) {
        return noneOf(List.of(predicates));
    }

    public static <T> Predicate<T> noneOf(@NonNull Iterable<Predicate<T>> predicates) {
        return anyOf(predicates).negate();
    }

    public static <T> Predicate<Iterable<T>> allMatch(@NonNull Predicate<? super T> predicate) {
        return iterable -> stream(iterable).allMatch(predicate);
    }

    public static <T> Predicate<Iterable<T>> anyMatch(@NonNull Predicate<? super T> predicate) {
        return iterable -> stream(iterable).anyMatch(predicate);
    }

    public static <T> Predicate<Iterable<T>> noneMatch(@NonNull Predicate<? super T> predicate) {
        return iterable -> stream(iterable).noneMatch(predicate);
    }

    @SafeVarargs
    public static <T> Predicate<T> in(@NonNull T... values) {
        return in(List.of(values));
    }

    public static <T> Predicate<T> in(@NonNull Iterable<T> iterable) {
        return obj -> stream(iterable).anyMatch(eq(obj));
    }

    private static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
