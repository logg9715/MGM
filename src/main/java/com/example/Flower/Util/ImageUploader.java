package com.example.Flower.Util;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ImageUploader {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "username";
        String password = "password";

        // 파일 경로 설정
        String ypImagePath = "resource/static/Plants/YP_튤립.jpg";
        String mpImagePath = "resource/static/Plants/MP_튤립.jpg";

        try {
            // 데이터베이스에 이미지 업로드
            uploadImageToDatabase(url, username, password, ypImagePath, mpImagePath);

            System.out.println("Images uploaded successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void uploadImageToDatabase(String url, String username, String password, String ypImagePath, String mpImagePath) throws Exception {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             FileInputStream ypInputStream = new FileInputStream(ypImagePath);
             FileInputStream mpInputStream = new FileInputStream(mpImagePath)) {

            // Prepare SQL statement for inserting image data
            String sql = "INSERT INTO Plants (name, YP_picture, MP_picture) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "튤립");
            statement.setBytes(2, ypInputStream.readAllBytes());
            statement.setBytes(3, mpInputStream.readAllBytes());
            statement.executeUpdate();

            System.out.println("Image uploaded successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
