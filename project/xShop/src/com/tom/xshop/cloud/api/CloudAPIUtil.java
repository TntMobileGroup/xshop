package com.tom.xshop.cloud.api;

import com.tom.xshop.cloud.CloudAPIAsyncTask;

public class CloudAPIUtil {
    private static CloudAPIUtil instance = null;
    public static CloudAPIUtil getInstance()
    {
        if (null == instance)
        {
            instance = new CloudAPIUtil();
        }
        return instance;
    }
    
    public CloudAPIUtil() {
    }
    
//    public void getAllProductInfo(CloudAPIAsyncTask.ICloudAPITaskListener listener)
//    {
//        CloudAPIAsyncTask task = new CloudAPIAsyncTask("GetAllProductInfo", listener);
//        task.execute();
//    }
    
    public void invokeAPI(String op, CloudAPIAsyncTask.ICloudAPITaskListener listener)
    {
        CloudAPIAsyncTask task = new CloudAPIAsyncTask(op, listener);
        task.execute();
    }
}
