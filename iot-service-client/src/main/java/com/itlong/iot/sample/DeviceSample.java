package com.itlong.iot.sample;

import com.itlong.iot.service.DeviceManagerService;
import com.itlong.iot.util.SslUtils;

/**
 * <desc>
 *     设备管理示例
 * </desc>
 *
 * @createDate 2017/8/30
 */
public class DeviceSample {
    /**
     * <desc>
     *     addDevice 添加设备
     *     getDevice 获取设备信息
     *     updateDevice 更新设备信息
     *     removeDevice 删除设备信息
     * </desc>
     *
     * @param args
     * @author Jiaqi.X
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
//        System.out.println("Starting sample...");
//        SslUtils.ignoreSsl();
//        System.out.println("Add Device started");
//        DeviceManagerService.addDevice();
//        System.out.println("Add Device finished");
        System.out.println("Get Device started");
        DeviceManagerService.getDevice();
//        System.out.println("Get Device finished");
//        System.out.println("Update Device started");
//        DeviceManagerService.updateDevice();
//        System.out.println("Update Device finished");
//        System.out.println("Remove Device started");
//        DeviceManagerService.removeDevice();
//        System.out.println("Remove Device finished");
    }
}
