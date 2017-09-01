/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

package com.itlong.iot.util;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.SharedAccessBlobPermissions;
import com.microsoft.azure.storage.blob.SharedAccessBlobPolicy;

import java.security.InvalidKeyException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.EnumSet;

/**
 * <desc>
 *     示例连接iothub工具类
 * </desc>
 *
 * @creteDate 2017/8/30
 */
public class SampleUtils
{
    public static final String iotHubConnectionString = "HostName=SampleIot.azure-devices.cn;SharedAccessKeyName=iothubowner;SharedAccessKey=vNpfjEQ6aH8XxLIrJ5P+cEU7JDf8WlaP4iGyRdLzP/g=";
    public static final String storageConnectionString = "[Sample storage connection string goes here]";
    public static final String deviceId = "sampleDevice2";
    public static final String exportFileLocation = "[Insert local folder here - something like C:\\foldername\\]";

    public static String getContainerSasUri(CloudBlobContainer container) throws InvalidKeyException, StorageException
    {
        //Set the expiry time and permissions for the container.
        //In this case no start time is specified, so the shared access signature becomes valid immediately.
        SharedAccessBlobPolicy sasConstraints = new SharedAccessBlobPolicy();
        Date expirationDate = Date.from(Instant.now().plus(Duration.ofDays(1)));
        sasConstraints.setSharedAccessExpiryTime(expirationDate);
        EnumSet<SharedAccessBlobPermissions> permissions = EnumSet.of(
                SharedAccessBlobPermissions.WRITE,
                SharedAccessBlobPermissions.LIST,
                SharedAccessBlobPermissions.READ,
                SharedAccessBlobPermissions.DELETE);
        sasConstraints.setPermissions(permissions);

        //Generate the shared access signature on the container, setting the constraints directly on the signature.
        String sasContainerToken = container.generateSharedAccessSignature(sasConstraints, null);

        //Return the URI string for the container, including the SAS token.
        return container.getUri() + "?" + sasContainerToken;
    }
}
