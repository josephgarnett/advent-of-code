#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end
import com.google.common.base.Preconditions;
import io.garnett.adventofcode.Application;
import io.garnett.adventofcode.utils.InputUtils;
import lombok.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.function.ToLongFunction;
#parse("File Header.java")
public class ${NAME} implements ToLongFunction<List<String>> {
    
    @Override
    public long applyAsLong(
            @NonNull final List<String> input) {
        return 1L;
    }

    private static List<String> getInput() throws IOException {
        return InputUtils.readLines(
                        "${AOC_YEAR}/${AOC_DAY}/input.txt")
                .toList();
    }

    public static void main(final String[] args) {
       final var result = Application.challenge(
            "${AOC_YEAR}/${AOC_DAY}",
             () -> new ${Name}()
                .applyAsLong(${Name}.getInput()));

        Preconditions.checkArgument(result == 1L);
    }
}