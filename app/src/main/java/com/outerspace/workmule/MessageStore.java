package com.outerspace.workmule;

public class MessageStore {

    private static MessageStore instance = new MessageStore();
    private static Integer index = 0;

    private MessageStore() { }

    public static MessageStore getInstance() {
        return instance;
    }

    private static String[] bunchOfMessages = {
            "Zeroth", "First", "Second", "Third", "Fourth",
            "Fifth", "Sixth", "Seventh", "Eighth", "Ninth",
            "Tenth", "Eleventh", "Twelfth", "Thirteenth", "Fourteenth",
            "Fifteenth", "Sixteenth", "Seventeenth", "Eighteenth",
            "Twentieth",
    };

    public String getNext() {
        String message = bunchOfMessages[index];
        index = index + 1 >= bunchOfMessages.length ? 0 : index + 1;
        return getCurrent();
    }

    public String getCurrent() {
        return bunchOfMessages[index];
    }

    public void reset() {
        index = 0;
    }
}
