
package com.tom.xshop.util;

import com.tom.xshop.config.PrefConfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PrefUtil {
    private static SharedPreferences mPrefs = null;

    public static void init(Context context)
    {
        if (PrefConfig.prefConfigMap.size() < 1)
        {
            PrefConfig.init();
        }
        
        if (null == mPrefs)
        {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }
    
    public static String getStringValue(String key)
    {
        String defVal = (String)PrefConfig.prefConfigMap.get(key);
        return mPrefs.getString(key, defVal);
    }
    
    public static void putStringValue(String key, String val)
    {
        Editor editor = mPrefs.edit();
        editor.putString(key, val);
        editor.commit();
    }
    
    public static Boolean getBooleanValue(String key)
    {
        Boolean defVal = (Boolean)PrefConfig.prefConfigMap.get(key);
        return mPrefs.getBoolean(key, defVal);
    }
    
    public static void putBooleanValue(String key, Boolean val)
    {
        Editor editor = mPrefs.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }
    
    public static void clearValue(String key)
    {
        Editor editor = mPrefs.edit();
        editor.putString(key, (String)PrefConfig.prefConfigMap.get(key));
        editor.commit();
    }
}
