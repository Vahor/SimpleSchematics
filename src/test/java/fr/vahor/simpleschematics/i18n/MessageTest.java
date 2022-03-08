package fr.vahor.simpleschematics.i18n;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void loadLanguage() {

        Message.loadLanguage(new File("src/main/resources"));

        System.out.println("Message.INVENTORY_ROTATION_LORE.toString().split(\"\\n\") = " + Message.INVENTORY_ROTATION_LORE);

        assertNotEquals(1, Message.INVENTORY_ROTATION_LORE.toString().split("\n").length);
        for (Message message : Message.values()) {
            assertNotNull(message.toString());
        }
    }
}