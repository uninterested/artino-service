package com.artino.service;


import com.artino.service.utils.DateUtils;
import com.artino.service.utils.IDUtils;

public class Application {

    public static void main(String[] args) {
        for (int i =0; i < 10; i++) {
            System.out.println("x:" + IDUtils.shared().nextId());
        }
        System.out.println(DateUtils.getTime());
    }
}
