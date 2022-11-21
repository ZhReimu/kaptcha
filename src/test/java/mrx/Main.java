package mrx;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        Config config = Config.builder()
                .border(true)
                .borderColor("105,179,90")
                .textproducerFontColor("blue")
                .textproducerFontSize(100)
                .imageWidth(300)
                .imageHeight(100)
                .textproducerCharLength(4)
                .textproducerFontNames("宋体")
                .textproducerCharSpace(16)
                .noiseColor("blue")
                .textproducerCharString("0123456789")
                .build();
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
        BufferedImage image = kaptcha.createImage(kaptcha.createText());
        ImageIO.write(image, "jpg", Files.newOutputStream(Path.of("1.jpg")));
    }

}