package testutils;

import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class MockRegistry {
    private static final Map<Class<?>, Object> MOCK_MAP = new HashMap<>();

    public static Object get(Class<?> type) {
        if (!MOCK_MAP.containsKey(type)) {
            MOCK_MAP.put(type, Mockito.mock(type));
        }
        return MOCK_MAP.get(type);
    }
}
