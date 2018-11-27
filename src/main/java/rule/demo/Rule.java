package rule.demo;

import java.util.Optional;
import java.util.function.Supplier;

public interface Rule<T> extends Supplier<Optional<T>> {
}
