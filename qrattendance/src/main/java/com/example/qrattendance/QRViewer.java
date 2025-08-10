package com.example.qrattendance;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;

public class QRViewer {

    public static void main(String[] args) throws Exception {
        String endpoint = "http://localhost:8080/session/start";
        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        InputStream response = conn.getInputStream();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(response, Map.class);

        String base64 = data.get("qrCodeImage");
        byte[] imageBytes = Base64.getDecoder().decode(base64);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

        JFrame frame = new JFrame("QR Code");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 350);

        JLabel label = new JLabel(new ImageIcon(image));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
