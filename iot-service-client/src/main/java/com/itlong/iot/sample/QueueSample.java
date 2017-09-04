package com.itlong.iot.sample;


import com.google.gson.Gson;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.*;

/**
 * <desc>
 *     Azure服务总线队列业务处理类
 * </desc>
 *
 * @createDate 2017/9/1
 */
public class QueueSample {
    private static String namespace="SampleIot";
    private static String sasKeyName="RootManageSharedAccessKey";
    private static String sasUrl=".servicebus.chinacloudapi.cn";
    private static String sasKey="G1E2lmxNcSqT8a0OMFhKlyxFoCY2yqCHigNIGIJPi7A=";
    public static void main(String[] args) {
//        createQueue();
        receiveQueue();
    }

    /**
     * <desc>
     *     创建队列
     * </desc>
     *
     * @author Jiaqi.X
     * @version 1.0
     * @createDate 2017/9/4
     */
    public static void createQueue(){
        Configuration config =
                ServiceBusConfiguration.configureWithSASAuthentication(
                        namespace,
                        sasKeyName,
                        sasKey,
                        sasUrl
                );
        ServiceBusContract service = ServiceBusService.create(config);
        //设置最大空间
        long maxSizeInMegabytes = 5120;
        //设置队列名
        QueueInfo queueInfo = new QueueInfo("testqueque");
        queueInfo.setMaxSizeInMegabytes(maxSizeInMegabytes);
        try
        {
            CreateQueueResult result = service.createQueue(queueInfo);
            System.out.println(new Gson().toJson(result));
        }
        catch (ServiceException e)
        {
            System.out.print("ServiceException encountered: ");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * <desc>
     *     从队列终结点读取消息
     * </desc>
     *
     * @author Jiaqi.X
     * @version 1.0
     * @createDate 2017/9/4
     */
    public static void receiveQueue(){
        try
        {
            Configuration config =
                    ServiceBusConfiguration.configureWithSASAuthentication(
                            namespace,
                            sasKeyName,
                            sasKey,
                            sasUrl
                    );
            ServiceBusContract service = ServiceBusService.create(config);
            ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
            opts.setReceiveMode(ReceiveMode.PEEK_LOCK);
            while(true)  {
                ReceiveQueueMessageResult resultQM =
                        service.receiveQueueMessage("testqueque", opts);
                BrokeredMessage message = resultQM.getValue();
                if (message != null && message.getMessageId() != null)
                {
                    System.out.println("MessageID: " + message.getMessageId());
                    // Display the queue message.
                    System.out.print("From queue: ");
                    byte[] b = new byte[200];
                    String s = null;
                    int numRead = message.getBody().read(b);
                    while (-1 != numRead)
                    {
                        s = new String(b);
                        s = s.trim();
                        System.out.print(s);
                        numRead = message.getBody().read(b);
                    }
                    System.out.println();
                    System.out.println("Custom Property: " +
                            message.getProperty("level"));
                    // Remove message from queue.
                    System.out.println("Deleting this message.");
                    service.deleteMessage(message);
                }
                else
                {
                    System.out.println("Finishing up - no more messages.");
                    break;
                    // Added to handle no more messages.
                    // Could instead wait for more messages to be added.
                }
            }
        }
        catch (ServiceException e) {
            System.out.print("ServiceException encountered: ");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        catch (Exception e) {
            System.out.print("Generic exception encountered: ");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
}
