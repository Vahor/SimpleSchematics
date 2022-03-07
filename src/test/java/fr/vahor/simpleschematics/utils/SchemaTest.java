package fr.vahor.simpleschematics.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchemaTest {

    @Test
    void isValidFolderName() {
        String separator = ".";
        Schema.generatePattern(separator);
        assertTrue(Schema.isValidFolderName("trees.big.truc.a", separator));
        assertFalse(Schema.isValidFolderName("..", separator));
        assertFalse(Schema.isValidFolderName(" . . ", separator));
        assertFalse(Schema.isValidFolderName("../../truc", separator));
        assertFalse(Schema.isValidFolderName("..t.r.u.c.t.t.t.t", separator));
    }
}