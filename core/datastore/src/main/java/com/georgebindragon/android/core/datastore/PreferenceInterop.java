package com.georgebindragon.android.core.datastore;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;

final class PreferenceInterop {
    private PreferenceInterop() {
    }

    static Preferences.Key<String> stringKey(String name) {
        return PreferencesKeys.stringKey(name);
    }

    static Preferences.Key<Integer> intKey(String name) {
        return PreferencesKeys.intKey(name);
    }

    static Preferences.Key<Long> longKey(String name) {
        return PreferencesKeys.longKey(name);
    }

    static Preferences.Key<Boolean> booleanKey(String name) {
        return PreferencesKeys.booleanKey(name);
    }

    static Preferences.Key<Float> floatKey(String name) {
        return PreferencesKeys.floatKey(name);
    }

    static Preferences.Key<java.util.Set<String>> stringSetKey(String name) {
        return PreferencesKeys.stringSetKey(name);
    }

    static Preferences emptyPreferences() {
        return new MutablePreferences();
    }
}
