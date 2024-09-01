package com.example.Flower.device;

public interface ScForm {
    int TAG = 0;

    String TAGSENSOR = "0";
    String TAGACTUATOR = "1";

    // 센서 인덱스
    int TEMP = 1;
    int WET = 2;
    int LIGHT = 3;
    int DAYLIGHT = 4;

    // 모터 인덱스
    int MOTERTYPE = 1;
    int RESULT = 2;

    // 모터 타입
    String ROTATER = "0";
    String SPRINKLER = "1";
    String LED = "2";
}

