package com.hx.http.client.utils;

public class ObjectHashUtils {

    public static void main(String[] args) {
        String s1 = "/home/data/exefds.exls";
        String s2 = "/home/data/exefds.docx";
        String s3 = "/home/data/exefds.text";
        System.out.println("s1 = "+s1.hashCode());
        System.out.println("s2 = "+s2.hashCode());
        System.out.println("s3 = "+s3.hashCode());
    }
}
