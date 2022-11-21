package com.mrx;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        Producer producer = new DefaultKaptcha();
        BufferedImage img = producer.createImage("测试");
        ImageIO.write(img, "jpg", Files.newOutputStream(Path.of("1.jpg")));
    }
    
}