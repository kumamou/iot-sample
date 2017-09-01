package com.itlong.iot.sample;

import com.google.gson.Gson;
import com.microsoft.azure.sdk.iot.service.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * <desc>
 *     发送云到设备的消息
 * </desc>
 *
 * @createDate 2017/9/1
 */
public class MessageSendC2DSample {
    private static final String connectionString = "HostName=SampleIot.azure-devices.cn;SharedAccessKeyName=iothubowner;SharedAccessKey=vNpfjEQ6aH8XxLIrJ5P+cEU7JDf8WlaP4iGyRdLzP/g=";
    private static final String deviceId = "sampleDevice";
    private static final IotHubServiceClientProtocol protocol = IotHubServiceClientProtocol.AMQPS;

    /**
     * <desc>
     *     连接到 IoT 中心
     *     将消息发送到设备
     *     并等待设备已接收并处理消息的通知
     * </desc>
     *
     * @author Jiaqi.X
     * @param args
     * @throws IOException
     * @throws URISyntaxException
     * @throws Exception
     */
    public static void main(String[] args) throws IOException,
            URISyntaxException, Exception {
        ServiceClient serviceClient = ServiceClient.createFromConnectionString(
                connectionString, protocol);
        if (serviceClient != null) {
            serviceClient.open();
            FeedbackReceiver feedbackReceiver = serviceClient
                    .getFeedbackReceiver();
            if (feedbackReceiver != null) feedbackReceiver.open();
            Map<String,Object> params=new HashMap<String, Object>();
            params.put("id",deviceId);
            params.put("message","发送云消息");
            Message messageToSend = new Message(new Gson().toJson(params));
            messageToSend.setDeliveryAcknowledgement(DeliveryAcknowledgement.Full);
            messageToSend.setMessageId("abcdefg");
            serviceClient.send(deviceId, messageToSend);
            System.out.println("Message sent to device");

            FeedbackBatch feedbackBatch = feedbackReceiver.receive(10000);
            if (feedbackBatch != null) {
                System.out.println("Message feedback received, feedback time: "
                        + feedbackBatch.getEnqueuedTimeUtc().toString());
            }

            if (feedbackReceiver != null) feedbackReceiver.close();
            serviceClient.close();
        }
    }
}
