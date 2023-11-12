package com.wzfry.client;

import java.io.FileOutputStream;

public class Test {
    public static void main(String[] args) throws Exception {
        FileOutputStream fos = new FileOutputStream("./test.txt");
        fos.write("helloworld".getBytes(), 0, "helloworld".length());
    }
}
