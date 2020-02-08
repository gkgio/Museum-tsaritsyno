package ru.terrakok.cicerone;

/**
 * Screen is class for title application screen.
 */
public abstract class Screen {
    protected String screenKey = getClass().getCanonicalName();

    public String getScreenKey() {
        return screenKey;
    }
}
