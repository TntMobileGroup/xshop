
package com.tom.xshop.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class CacheUtil {
    private final static String CacheFolderName = "cloud_api";
    public final static int Key_visited = 0;
    public final static int Key_liked = 1;

    private static File rootCacheFolder = null;
    private static String curAppVersion = "1";
    private static Context appContext = null;

    private static HashMap<Integer, String> cacheMap = null;
    public static void initialize(Context context)
    {
        cacheMap = new HashMap<Integer, String>();
        appContext = context;
        getCacheFolder(context);
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pinfo;
            pinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            curAppVersion = String.valueOf(pinfo.versionCode);
        } catch (NameNotFoundException e) {
        }
    }

    private static String getCacheFileName(int key)
    {
        String apiName = "unknown_";
        switch (key)
        {
            case Key_visited:
                apiName = "visited_";
                break;
            default:
                break;
        }
        return apiName + curAppVersion + ".json";
    }

    private static File getCacheFolder(Context context)
    {
        if (null == rootCacheFolder)
        {
            File folder = context.getCacheDir();
            rootCacheFolder = new File(folder.getAbsolutePath(), CacheFolderName);
            if (!rootCacheFolder.exists() || !rootCacheFolder.isDirectory()) {
                rootCacheFolder.mkdirs();
            }
        }
        return rootCacheFolder;
    }

    public static void cache(int key, String data)
    {
        File cacheFile = new File(rootCacheFolder.getAbsolutePath(), getCacheFileName(key));
        writeDataToFile(cacheFile.getAbsolutePath(), data);
    }
    
    public static boolean needUpdate(int key, String data)
    {
        if (!cacheMap.containsKey(key))
        {
            return true;
        }
        
        return !data.equalsIgnoreCase(cacheMap.get(key));
    }
    
    public static void update(int key, String data)
    {
        if (cacheMap.containsKey(key))
        {
            cacheMap.remove(key);
        }
        cacheMap.put(key, data);
        
        cache(key, data);
    }

    public static String loadCachedData(int key)
    {
        File cacheFile = new File(rootCacheFolder.getAbsolutePath(), getCacheFileName(key));
        String data = readDataFromFile(cacheFile.getAbsolutePath());
        if (cacheMap.containsKey(key))
        {
            cacheMap.remove(key);
        }
        cacheMap.put(key, data);
        return data;
    }

    private static void writeDataToFile(String filePath, String data)
    {
        try {
            FileOutputStream fos = new FileOutputStream(filePath, false);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readDataFromFile(String filePath)
    {
        String result = "";
        try
        {
            File f = new File(filePath);
            if (f.exists()) {
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                StringBuilder sb = new StringBuilder(1024);
                String thisLine = null;
                while ((thisLine = br.readLine()) != null) {
                    sb.append(thisLine);
                }
                br.close();
                result = sb.toString();
            }
        } catch (Exception e) {
        }
        return result;
    }
}
