package com.beiing.gifmaker.supports;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by chenliu on 2016/5/25.<br/>
 * 描述：自定义广播接收者
 * </br>
 */
public class DefaultReceiver extends BroadcastReceiver {
    public interface ReceiverListener{
        void onReceive(Context context, Intent intent);
    }

    private ReceiverListener iReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(iReceiver != null)
            iReceiver.onReceive(context, intent);
        else
            throw  new NullPointerException("iReceiver is null, Please invoke setReceiver!");
    }

    public void setReceiver(ReceiverListener iReceiver) {
        this.iReceiver = iReceiver;
    }
}
