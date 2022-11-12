package syntax;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

class EnumExceptionTest {


    @Test
    void test_enum_notfound_name() {
        String name = "CC";
        TestEnum.from(name);
    }
}

enum TestEnum {
    AA,
    BB
    ;
    private static final Map<String, TestEnum> map =
            Arrays.stream(TestEnum.values()).collect(Collectors.toMap(TestEnum::name, Function.identity()));

    public static TestEnum from(String key) {
        return ofNullable(map.get(key))
                .orElseThrow(() -> new IllegalArgumentException());
    }

}
