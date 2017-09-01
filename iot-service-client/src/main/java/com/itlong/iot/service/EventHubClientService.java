package com.itlong.iot.service;

import java.io.IOException;
import com.microsoft.azure.eventhubs.*;
import com.microsoft.azure.servicebus.*;

import java.nio.charset.Charset;
import java.time.*;
import java.util.function.*;

/**
 * <desc>
 *      iot云消息业务处理类
 * </desc>
 *
 * @createDate 2017/8/30
 */
public class EventHubClientService {
    //Endpoint:事件中心兼容终结点、EntityPath：事件中心兼容终结点名称、SharedAccessKeyName：共享访问策略名称、SharedAccessKey：iothub连接主密匙
    private static String connStr = "Endpoint=sb://iothub-ns-sampleiot-25119-2fbefaf892.servicebus.chinacloudapi.cn/;EntityPath=sampleiot;SharedAccessKeyName=iothubowner;SharedAccessKey=vNpfjEQ6aH8XxLIrJ5P+cEU7JDf8WlaP4iGyRdLzP/g=";


    /**
     * <desc>
     *      1.创建eventhub实例连接到事件中心兼容终结点
     *      2.以异步方式创建 接收器 实例
     *      3.从事件中心分区读取。 它将持续循环并输出消息详细信息，直到应用终止
     * </desc>
     * @param partitionId
     * @return
     */
    public static EventHubClient receiveMessages(final String partitionId) {
        EventHubClient client = null;
        try {
            client = EventHubClient.createFromConnectionStringSync(connStr);
        } catch (Exception e) {
            System.out.println("Failed to create client: " + e.getMessage());
            System.exit(1);
        }
        try {
            // Create a receiver using the
            // default Event Hubs consumer group
            // that listens for messages from now on.
            client.createReceiver(EventHubClient.DEFAULT_CONSUMER_GROUP_NAME, partitionId, Instant.now())
                    .thenAccept(new Consumer<PartitionReceiver>() {
                        public void accept(PartitionReceiver receiver) {
                            System.out.println("** Created receiver on partition " + partitionId);
                            try {
                                while (true) {
                                    Iterable<EventData> receivedEvents = receiver.receive(100).get();
                                    int batchSize = 0;
                                    if (receivedEvents != null) {
                                        System.out.println("Got some evenst");
                                        for (EventData receivedEvent : receivedEvents) {
                                            System.out.println(String.format("Offset: %s, SeqNo: %s, EnqueueTime: %s",
                                                    receivedEvent.getSystemProperties().getOffset(),
                                                    receivedEvent.getSystemProperties().getSequenceNumber(),
                                                    receivedEvent.getSystemProperties().getEnqueuedTime()));
                                            System.out.println(String.format("| Device ID: %s",
                                                    receivedEvent.getSystemProperties().get("iothub-connection-device-id")));
                                            System.out.println(String.format("| Message Payload: %s",
                                                    new String(receivedEvent.getBytes(), Charset.defaultCharset())));
                                            batchSize++;
                                        }
                                    }
                                    System.out.println(String.format("Partition: %s, ReceivedBatch Size: %s", partitionId, batchSize));
                                }
                            } catch (Exception e) {
                                System.out.println("Failed to receive messages: " + e.getMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            System.out.println("Failed to create receiver: " + e.getMessage());
        }
        return client;
    }
}
