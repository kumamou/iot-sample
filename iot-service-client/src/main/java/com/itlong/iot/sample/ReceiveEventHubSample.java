package com.itlong.iot.sample;

import com.itlong.iot.service.EventHubClientService;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.servicebus.ServiceBusException;

import java.io.IOException;

/**
 * <desc>
 *     接收设备到云的消息示例
 * </desc>
 *
 * @createDate 2017/8/30
 */
public class ReceiveEventHubSample {
    public static void main(String[] args) throws IOException {
        // 假设已在 F1（免费）层创建 IoT 中心。 免费 IoT 中心有“0”和“1”这两个分区。
        EventHubClient client2 = EventHubClientService.receiveMessages("2");
        System.out.println("Press ENTER to exit.");
        System.in.read();
        try {
            client2.closeSync();
            System.exit(0);
        } catch (ServiceBusException sbe) {
            System.exit(1);
        }
    }


}
