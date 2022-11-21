package com.google.code.kaptcha.servlet;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

/**
 * This servlet uses the settings passed into it via the Producer api.
 *
 * @author testvoogd@hotmail.com
 * @author jon
 * @author cliffano
 */
public class KaptchaServlet extends HttpServlet {

    private final Properties props = new Properties();

    private Producer kaptchaProducer = null;

    private String sessionKeyValue = null;

    private String sessionKeyDateValue = null;

    private boolean initialized = false;

    public KaptchaServlet() {
    }

    public KaptchaServlet(Config config) {
        init(config);
    }

    @Override
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        // ensure container do not override manual invoked init method result
        if (!initialized) {
            Enumeration<?> initParams = conf.getInitParameterNames();
            while (initParams.hasMoreElements()) {
                String key = (String) initParams.nextElement();
                String value = conf.getInitParameter(key);
                this.props.put(key, value);
            }
            init(Config.builder().from(this.props).build());
        }
    }

    public void init(Config config) {
        initialized = true;
        // Switch off disk based caching.
        ImageIO.setUseCache(false);
        this.kaptchaProducer = config.getProducerImpl();
        this.sessionKeyValue = config.getSessionKey();
        this.sessionKeyDateValue = config.getSessionDate();
    }

    /**
     *
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Set to expire far in the past.
        resp.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        resp.setHeader("Pragma", "no-cache");

        // return a jpeg
        resp.setContentType("image/jpeg");

        // create the text for the image
        String capText = this.kaptchaProducer.createText();

        // store the text in the session
        req.getSession().setAttribute(this.sessionKeyValue, capText);

        // store the date in the session so that it can be compared
        // against to make sure someone hasn't taken too long to enter
        // their kaptcha
        req.getSession().setAttribute(this.sessionKeyDateValue, new Date());

        // create the image with the text
        BufferedImage bi = this.kaptchaProducer.createImage(capText);

        ServletOutputStream out = resp.getOutputStream();

        // write the data out
        ImageIO.write(bi, "jpg", out);
    }
}
