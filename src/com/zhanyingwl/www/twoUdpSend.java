package com.zhanyingwl.www;

import java.io.IOException;
import java.net.*;

import static com.zhanyingwl.www.doConnect.twogetTargetHost;

public class twoUdpSend extends oneUdpSend {
    @Override

    public void run() {
        try {
            DatagramSocket ds = new DatagramSocket();
            InetAddress ip = InetAddress.getByName(twogetTargetHost);
            byte[] info = UDPData.getBytes();
            DatagramPacket p = new DatagramPacket(info, UDPData.length(), ip, 80);
            while (!Thread.currentThread().isInterrupted()) {
                ds.send(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
