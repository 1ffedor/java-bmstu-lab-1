package game.ui.player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MenuContext implements Serializable {
    private static final long serialVersionUID = 1L;
    private MenuState currentState;
    private final Map<String, Object> storage = new HashMap<>();

    // Управление состоянием
    public void setState(MenuState state) {
        this.currentState = state;
    }

    public void run() {
        while (currentState != null) {
            currentState.display();
            currentState.handleInput();
        }
    }

    public void finish() {
        currentState = null;
    }

    // Универсальное хранилище
    public void addToStorage(String key, Object value) {
        storage.put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        Object value = storage.get(key);
        return type.isInstance(value) ? type.cast(value) : null;
    }

    public Object get(String key) {
        return storage.get(key);
    }

    public boolean contains(String key) {
        return storage.containsKey(key);
    }

    public void remove(String key) {
        storage.remove(key);
    }

    // Специализированные методы для часто используемых типов
    public String getString(String key) {
        return get(key, String.class);
    }

    public int getInt(String key) {
        Integer value = get(key, Integer.class);
        return value != null ? value : 0;
    }

    public boolean getBoolean(String key) {
        Boolean value = get(key, Boolean.class);
        return value != null ? value : false;
    }

    // Для работы с классами (как в вашей оригинальной версии)
    public <T> void addToStorage(Class<T> type, T value) {
        addToStorage(type.getName(), value);
    }

    public <T> T get(Class<T> type) {
        return get(type.getName(), type);
    }
}