package fr.vahor.simpleschematics.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorUtilsTest {

    @Test
    void getClosestColor() {
        assertEquals(ColorUtils.ColorData.DARK_RED, ColorUtils.getClosestColor(255,0,0));
    }
}