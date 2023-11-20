package pl.mimuw.zpp.quantumai.backendui.utils;

import io.vavr.control.Either;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;

public class Utils {
    public static <A, B, C, E> Either<E, C> map2(
            Callable<Either<E, A>> e1,
            Callable<Either<E, B>> e2,
            BiFunction<A, B, C> f
    ) throws Exception {
        Either<E, A> either1 = e1.call();
        if (either1.isLeft()) {
            return Either.left(either1.getLeft());
        }
        Either<E, B> either2 = e2.call();
        if (either2.isLeft()) {
            return Either.left(either2.getLeft());
        }
        return Either.right(f.apply(either1.get(), either2.get()));
    }
}
