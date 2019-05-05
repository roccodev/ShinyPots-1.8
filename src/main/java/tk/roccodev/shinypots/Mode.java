package tk.roccodev.shinypots;

public enum Mode {
    DISABLED("Disabled"),
    NORMAL("Normal"),
    COLOR("Colored");

    private String display;

    Mode(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
