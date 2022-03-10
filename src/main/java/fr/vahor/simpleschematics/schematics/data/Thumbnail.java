package fr.vahor.simpleschematics.schematics.data;

import fr.vahor.simpleschematics.API;
import fr.vahor.simpleschematics.utils.ColorUtils;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Thumbnail {

    public static int MAX_THUMBNAIL_SIZE = 100;

    @Getter private List<String> cachedList;

    public Thumbnail(BufferedImage bufferedImage) {

        // todo placeholder (not found) in trads
        if (bufferedImage == null) {
            cachedList = new ArrayList<>();
            return;
        }
        bufferedImage = resize(bufferedImage);
        generateDescription(bufferedImage);
    }

    /**
     * Resize Image to Config.ThumbnailSize
     * Nothing if already at right size
     * @return Resized Image
     */
    private BufferedImage resize(BufferedImage bufferedImage) {
        if (bufferedImage.getHeight() == API.getConfiguration().getThumbnailSize()) return bufferedImage;

        BufferedImage image = new BufferedImage(
                API.getConfiguration().getThumbnailSize(),
                API.getConfiguration().getThumbnailSize(),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(bufferedImage,
                0, 0,
                API.getConfiguration().getThumbnailSize(),
                API.getConfiguration().getThumbnailSize(),
                null);
        g2d.dispose();
        return image;
    }

    /**
     * Generate lore description for items
     * @param bufferedImage
     */
    private void generateDescription(BufferedImage bufferedImage) {
        List<String> list = new ArrayList<>(bufferedImage.getHeight());
        Pattern emptyPattern = Pattern.compile("§0█");
        int lastNonEmpty = 0;

        int start1CropX = Integer.MAX_VALUE; // crop from 0 to start1CropX. start1CropX => First non empty
        int start2CropX = Integer.MIN_VALUE; // crop from start2CropX to end. start2CropX => Last non empty

        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            StringBuilder sb = new StringBuilder();

            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xff;

                String icon = getIconForAlpha(alpha);
                if (icon == null) {
                    sb.append("§0█");
                    continue;
                }
                else {
                    // Update MinX/MaxX
                    if (x < start1CropX) {
                        start1CropX = x;
                    }
                    else if (x > start2CropX) {
                        start2CropX = x;
                    }
                }


                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;

                ColorUtils.ColorData colorData = ColorUtils.getClosestColor(red, green, blue);
                sb.append(colorData.getChatColor());
                sb.append(getIconForAlpha(alpha));

            }


            String value = sb.toString();
            // If current line is "empty" ( or contains only background )
            if (emptyPattern.matcher(value).replaceAll("").length() == 0) {
                // If list is empty, continue
                // Thus, the top space is trimmed
                if (list.isEmpty()) continue;
            }
            else {
                // Else, if line is not empty
                lastNonEmpty = list.size() + 1; // note +1 as we add the value after this
            }
            list.add(sb.toString());

        }

        // Remove empty lines from lastNonEmpty to end
        // and crop in x direction. 1 pixel is 3 characters (eg : §0█)
        int finalStart1CropX = Math.max(0, start1CropX * 3 - 9);
        int finalStart2CropX = Math.min(bufferedImage.getHeight() * 3, (start2CropX+1) * 3 + 9);
        cachedList = list.subList(0, lastNonEmpty)
                .stream().map(s -> s.substring(finalStart1CropX, finalStart2CropX))
                .collect(Collectors.toList());
    }

    private String getIconForAlpha(int alpha) {
        if (alpha <= 64) {
            return null;
        }
        else if (alpha <= 128) {
            return "▒";
        }
        else if (alpha <= 192) {
            return "▒";
        }
        else {
            return "█";
        }
    }

}
