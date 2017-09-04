package com.itlong.iot.sample;

import com.google.gson.Gson;
import com.microsoft.azure.sdk.iot.device.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *<desc>
 *     模拟硬件设备发送消息  发送设备到云的消息
 *</desc>
 *
 * @createDate 2017/8/30
 */
public class MessageSendD2CSample {

    //连接字符串 HostName:IOT中心名称     DeviceId:设备id    SharedAccessKey:生成的设备key
    private static String connString = "HostName=SampleIot.azure-devices.cn;DeviceId=sampleDevice;SharedAccessKey=K+CjtPB+edVzVnNPq5TjxQ==";
    private static IotHubClientProtocol protocol = IotHubClientProtocol.MQTT;
    private static String deviceId = "sampleDevice";
    private static DeviceClient client;

    /**
     * <desc>
     *     设备发送消息，及接收消息
     * </desc>
     * @param args
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        //=====================向 IoT 中心发送设备到云消息的线程===================================
        client = new DeviceClient(connString, protocol);
        client.open();

        MessageSender sender = new MessageSender();

        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(sender);


        System.out.println("Press ENTER to exit.");
        System.in.read();
        executor.shutdownNow();
        client.closeNow();
        //============================================================================================
        //==================模拟设备侦听云到设备的消息=================================================
//        client = new DeviceClient(connString, protocol);
//        MessageCallback callback = new AppMessageCallback();
//        client.setMessageCallback(callback, null);
//        client.open();
        //==============================================================================================
    }

    private static class MessageSender implements Runnable {
        /**
         * <desc>
         *     将生成要发送到 IoT 中心的示例遥测数据，并在发送下一条消息之前等待确认
         *     此方法将随机发送需要服务器处理的消息，通过自定义属性来交互，level
         * </desc>
         *
         * @author Jiaqi.X
         * @version 1.0
         * @date 2017/8/30
         */
        public void run()  {
            try {
                double minTemperature = 20;
                double minHumidity = 60;
                Random rand = new Random();
                boolean lock=true;
                int i=0;
                while (lock) {
                    if(i>10){
                        break;
                    }
                    String msgStr;
                    Message msg;
                    if (new Random().nextDouble() > 0.7) {
                        msgStr = "这是一条队列消息";
                        msg = new Message(msgStr);
                        msg.setProperty("level", "critical");
                    } else {
                        double currentTemperature = minTemperature + rand.nextDouble() * 15;
                        double currentHumidity = minHumidity + rand.nextDouble() * 20;
                        TelemetryDataPoint telemetryDataPoint = new TelemetryDataPoint();
                        telemetryDataPoint.deviceId = deviceId;
                        telemetryDataPoint.temperature = i;
                        telemetryDataPoint.humidity = currentHumidity;
                        msgStr = telemetryDataPoint.serialize();
                        msg = new Message(msgStr);
                    }

                    System.out.println("Sending: " + msgStr);

                    Object lockobj = new Object();
                    EventCallback callback = new EventCallback();
                    client.sendEventAsync(msg, callback, lockobj);

                    synchronized (lockobj) {
                        lockobj.wait();
                    }
                    Thread.sleep(1000);
                    i++;
                }
            } catch (InterruptedException e) {
                System.out.println("Finished.");
            }
        }
    }

    /**
     * <desc>
     *     模拟设备发送的遥测数据
     * </desc>
     *
     * @author Jiaqi.X
     * @version 1.0
     * @createDate 2017/8/30
     */
    private static class TelemetryDataPoint {
        public String deviceId;
        public double temperature;
        public double humidity;

        public String serialize() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    /**
     * <desc>
     *      显示 IoT 中心在处理来自设备应用的消息时返回的确认状态
     *      处理消息时，此方法还会通知应用中的主线程
     * </desc>
     *
     * @author Jiaqi.X
     * @version 1.0
     * @createDate 2017/8/30
     */
    private static class EventCallback implements IotHubEventCallback {
        public void execute(IotHubStatusCode status, Object context) {
            System.out.println("IoT Hub responded to message with status: " + status.name());
            if (context != null) {
                synchronized (context) {
                    context.notify();
                }
            }
        }
    }

    /**
     * <desc>
     *     模拟设备应用接收消息
     * </desc>
     *
     * @author Jiaqi.X
     * @version 1.0
     * @createDate 2017/09/01
     */
    private static class AppMessageCallback implements MessageCallback {
        public IotHubMessageResult execute(Message msg, Object context) {
            System.out.println("Received message from hub: "
                    + new String(msg.getBytes(), Message.DEFAULT_IOTHUB_MESSAGE_CHARSET)+"    messageId=:"+msg.getMessageId());
            return IotHubMessageResult.COMPLETE;
        }
    }
}
