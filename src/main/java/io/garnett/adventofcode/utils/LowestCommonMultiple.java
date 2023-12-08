package io.garnett.adventofcode.utils;

import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LowestCommonMultiple {
    public static long isPrime(long n){
        for(int i = 2; i<=Math.sqrt(n); i++){
            if(n % i == 0)
                return 0;
        }
        return 1;
    }

    public static long[] primeFactors(long n) {
        final List<Long> result = new ArrayList<>();
        for(long i = 2; i<= n; i++){
            if(LowestCommonMultiple.isPrime(i)==1){
                long x = n;
                while(x % i ==0){
                    result.add(i);
                    x /= i;
                }
            }
        }

        return result.stream()
                .mapToLong(t -> t).toArray();
    }

    public static long lcm(@NonNull final List<Long> values) {
        final var factors = values.stream()
                .map(LowestCommonMultiple::primeFactors)
                .map(f -> Arrays.stream(f)
                        .boxed()
                        .collect(Collectors.groupingBy(
                                Function.identity(),
                                Collectors.counting())))
                .reduce(new HashMap<>(), (a, b) -> {
                    b.forEach((key, value) -> a.compute(key, (k, v) -> value > Optional.ofNullable(v).orElse(0L) ? value : v));

                    return a;
                });

        return factors.entrySet()
                .stream()
                .map(entry -> Math.pow(entry.getKey(), entry.getValue()))
                .mapToLong(Double::longValue)
                .reduce((a, b) -> a * b)
                .orElseThrow();
    }
}
