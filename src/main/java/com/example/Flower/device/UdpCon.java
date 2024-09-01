package com.example.Flower.device;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Base64;

@Component
public class UdpCon {
    // UDP 연결 시작 메서드
    public String[] start(String ipAddress, int ipPort) throws IOException, UnknownHostException {
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName(ipAddress);

        // 데이터를 저장할 바이트 배열 생성
        byte[] rmsg = new byte[100];
        String senTag = "0";
        byte[] msg = senTag.getBytes();

        DatagramPacket outPacket = new DatagramPacket(msg, 1, serverAddress, ipPort);
        DatagramPacket inPacket = new DatagramPacket(rmsg, rmsg.length);

        datagramSocket.send(outPacket); // DatagramPacket을 전송
        datagramSocket.receive(inPacket); // DatagramPacket을 수신

        String receivedData = new String(inPacket.getData()).trim();
        System.out.println("recvData : " + receivedData);

        String[] parsedData = this.parseData(receivedData);
        datagramSocket.close();
        return parsedData;
    }

    // 데이터를 파싱하는 메서드
    public String[] parseData(String stringData) {
        String[] dataArr = stringData.trim().split(",");

        if (dataArr[ScForm.TAG].equals(ScForm.TAGSENSOR)) {
            // 소켓 종류가 센서일 때
            System.out.println("tag : " + dataArr[ScForm.TAG]);
            System.out.println("temp : " + dataArr[ScForm.TEMP]);
            System.out.println("wet : " + dataArr[ScForm.WET]);
            System.out.println("light : " + dataArr[ScForm.LIGHT]);
            System.out.println("daylight : " + dataArr[ScForm.DAYLIGHT]);
        } else if (dataArr[ScForm.TAG].equals(ScForm.TAGACTUATOR)) {
            // 소켓 종류가 액추에이터일 때
            System.out.println("tag : " + dataArr[ScForm.TAG]);
            System.out.println("moter : " + dataArr[ScForm.MOTERTYPE]);
            System.out.println("result : " + dataArr[ScForm.RESULT]);
        }

        return dataArr;
    }

    // 액추에이터 동작 메서드
    public String[] actuator(String ipAddress, int ipPort, String moterType) throws IOException, UnknownHostException {
        DatagramSocket datagramSocket = new DatagramSocket();// UDP 소켓 생성
        InetAddress serverAddress = InetAddress.getByName(ipAddress);// 주어진 IP 주소 문자열을 InetAddress 객체로 변환

        moterType = "1," + moterType;// 액추에이터 타입을 나타내는 메시지 생성 ("1," 접두사와 함께)
        byte[] msg = moterType.getBytes();// 메시지를 바이트 배열로 변환
        byte[] rmsg = new byte[100]; // 응답 메시지를 저장할 바이트 배열 생성 (길이 100 바이트)

        // 송신할 데이터그램 패킷 생성 (메시지 바이트 배열, 메시지 길이, 서버 주소, 포트 번호)
        DatagramPacket outPacket = new DatagramPacket(msg, msg.length, serverAddress, ipPort);
        // 수신할 데이터그램 패킷 생성 (응답 메시지 바이트 배열, 배열 길이)
        DatagramPacket inPacket = new DatagramPacket(rmsg, rmsg.length);

        datagramSocket.send(outPacket);// 데이터그램 패킷을 송신
        datagramSocket.receive(inPacket);// 데이터그램 패킷을 수신

        // 수신된 데이터(바이트 배열)를 문자열로 변환하고, 앞뒤 공백을 제거
        String receivedData = new String(inPacket.getData()).trim();
        System.out.println("recvData : " + receivedData);

        String[] parsedData = this.parseData(receivedData);// 수신된 데이터를 파싱하여 문자열 배열로 변환
        datagramSocket.close();// 소켓을 닫아 리소스를 해제
        return parsedData;// 파싱된 데이터를 반환
    }

    //스크린샷
    private String latestScreenshotBase64;

    public synchronized String getLatestScreenshotBase64() {
        return latestScreenshotBase64;
    }

    public void receiveAndProcessData() {
        try {
            // 스크린샷을 캡처합니다.
            latestScreenshotBase64 = getScreenshotFromStreamUrl("http://175.123.202.85:20800/screenshot");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getScreenshotFromStreamUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } finally {
            connection.disconnect();
        }
    }

}