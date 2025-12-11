package com.jsh.erp.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class ImageUtil {

  public static byte[] generateThumbnail(byte[] imageBytes, int width) throws IOException {
    ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
    BufferedImage original = ImageIO.read(bis);

    if(original == null) return null;

    int originalWidth = original.getWidth();
    int originalHeight = original.getHeight();
    int height = (originalHeight * width) / originalWidth;

    Image scaled = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);

    BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = thumbnail.createGraphics();
    g2d.drawImage(scaled, 0, 0, null);
    g2d.dispose();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(thumbnail, "jpg", baos);

    return baos.toByteArray();
  }
}
