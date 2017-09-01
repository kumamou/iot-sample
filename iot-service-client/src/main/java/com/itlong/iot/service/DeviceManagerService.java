package com.itlong.iot.service;

import com.itlong.iot.util.SampleUtils;
import com.microsoft.azure.sdk.iot.service.Device;
import com.microsoft.azure.sdk.iot.service.RegistryManager;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

import java.io.IOException;

/**
 * <desc>
 *     设备管理业务处理类
 * </desc>
 *
 * @createDate 2017/8/30
 */
public class DeviceManagerService {

    /**
     * <desc>
     *     添加设备
     * </desc>
     *
     * @author Jiaqi.X
     * @version 1.0
     * @throws Exception
     */
    public static void addDevice() throws Exception {
        RegistryManager registryManager = RegistryManager.createFromConnectionString(SampleUtils.iotHubConnectionString);

        Device device = Device.createFromId(SampleUtils.deviceId, null, null);
        try {
            device = registryManager.addDevice(device);
            System.out.println("Device created: " + device.getDeviceId());
            System.out.println("Device key: " + device.getPrimaryKey());
        } catch (IotHubException iote) {
            iote.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <desc>
     *     获取设备信息
     * </desc>
     *
     * @author Jiaqi.X
     * @version 1.0
     * @throws Exception
     */
    public static void getDevice() throws Exception {
        RegistryManager registryManager = RegistryManager.createFromConnectionString(SampleUtils.iotHubConnectionString);

        Device returnDevice = null;
        try {
            returnDevice = registryManager.getDevice(SampleUtils.deviceId);
            System.out.println("Device: " + returnDevice.getDeviceId());
            System.out.println("Device primary key: " + returnDevice.getPrimaryKey());
            System.out.println("Device secondary key: " + returnDevice.getSecondaryKey());
            System.out.println("Device dev connection:" );
            System.out.println("Device eTag: " + returnDevice.geteTag());
        } catch (IotHubException iote) {
            iote.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <desc>
     *     更新设备信息
     * </desc>
     *
     * @atuhor Jiaqi.X
     * @version 1.0
     * @throws Exception
     */
    public static void updateDevice() throws Exception {
        String primaryKey = "[New primary key goes here]";
        String secondaryKey = "[New secondary key goes here]";

        RegistryManager registryManager = RegistryManager.createFromConnectionString(SampleUtils.iotHubConnectionString);

        Device device = Device.createFromId(SampleUtils.deviceId, null, null);
        device.getSymmetricKey().setPrimaryKey(primaryKey);
        device.getSymmetricKey().setSecondaryKey(secondaryKey);
        try {
            device = registryManager.updateDevice(device);
            System.out.println("Device updated: " + device.getDeviceId());
            System.out.println("Device primary key: " + device.getPrimaryKey());
            System.out.println("Device secondary key: " + device.getSecondaryKey());
        } catch (IotHubException iote) {
            iote.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <desc>
     *     删除设备
     * </desc>
     *
     * @ahtor Jiaqi.X
     * @version 1.0
     * @throws Exception
     */
    public static void removeDevice() throws Exception {
        RegistryManager registryManager = RegistryManager.createFromConnectionString(SampleUtils.iotHubConnectionString);

        try {
            registryManager.removeDevice(SampleUtils.deviceId);
            System.out.println("Device removed: " + SampleUtils.deviceId);
        } catch (IotHubException iote) {
            iote.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
