package com.tom.xshop.cloud;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.tom.xshop.util.CacheUtil;
import com.tom.xshop.util.JSONUtil;

import android.os.AsyncTask;

public class CloudAPIAsyncTask extends AsyncTask<String, Void, String> {

    private final static String API_URL = "http://115.28.107.154/xshop/api/admin/api.php?op=";
    
    private String mOp = "";
	public interface ICloudAPITaskListener {
		public void onFinish(int returnCode, String strResult);
	}

	private ICloudAPITaskListener mAPIListener = null;

    public CloudAPIAsyncTask(String op, ICloudAPITaskListener apiListener) {
        super();
        mAPIListener = apiListener;
        mOp = op;
    }

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
	       String url = API_URL + mOp;

	        HttpGet getReq = new HttpGet(url);
	        try {
	            HttpResponse httpResponse = new DefaultHttpClient().execute(getReq);
	            if (httpResponse.getStatusLine().getStatusCode() == 200) {
	                String strResult = EntityUtils.toString(httpResponse
	                        .getEntity());
	                return strResult;
	            } else {
	                return "";
	            }
	        } catch (Exception e) {
	            return "";
	        }
	}

	@Override
	protected void onPostExecute(String strResult) {
		super.onPostExecute(strResult);
		if (null != mAPIListener) {
			mAPIListener.onFinish(0, strResult);
		}
	}
}