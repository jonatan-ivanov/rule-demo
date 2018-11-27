package rule.demo;

import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;
import static rule.demo.Predicates.always;

@NoArgsConstructor(access = PRIVATE)
public class BDRule<T, R> implements Rule<R> {
    private T input;
    private Predicate<? super T> predicate = always();
    private Function<? super T, ? extends R> function = input -> (R) input; // well, not exactly :(

    public static <T, R> BDRule<T, R> of() {
        return new BDRule<>();
    }

    public BDRule<T, R> given(@NonNull Supplier<? extends T> supplier) {
        return given(supplier.get());
    }

    public BDRule<T, R> given(T input) {
        this.input = input;
        return this;
    }

    public BDRule<T, R> when(@NonNull Supplier<? extends Predicate<? super T>> supplier) {
        return when(supplier.get());
    }

    public BDRule<T, R> when(@NonNull Predicate<? super T> predicate) {
        this.predicate = predicate;
        return this;
    }

    public BDRule<T, R> then(@NonNull Supplier<? extends Function<? super T, ? extends R>> supplier) {
        return then(supplier.get());
    }

    public BDRule<T, R> then(@NonNull Function<? super T, ? extends R> function) {
        this.function = function;
        return this;
    }

    public Optional<R> or(@NonNull Supplier<? extends Optional<? extends R>> supplier) {
        return get().or(supplier);
    }

    @Override
    public Optional<R> get() {
        return Optional.ofNullable(input).filter(predicate).map(function);
    }
}
