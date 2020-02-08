package ru.terrakok.cicerone.commands;

public class SwitchTo implements Command {
    private int id;

    /**
     * Creates a {@link SwitchTo} navigation command.
     *
     * @param id tabsEnum
     */
    public SwitchTo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
