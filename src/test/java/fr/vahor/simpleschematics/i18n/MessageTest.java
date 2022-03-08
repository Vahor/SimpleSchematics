package fr.vahor.simpleschematics.i18n;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void loadLanguage() {

        Message.loadLanguage(new File("src/main/resources"));

        for (Message message : Message.values()) {
            assertNotNull(message.toString());
        }
    }
}