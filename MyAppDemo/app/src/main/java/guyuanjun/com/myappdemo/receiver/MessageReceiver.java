package guyuanjun.com.myappdemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static guyuanjun.com.myappdemo.utils.Constant.KEY_EXTRAS;
import static guyuanjun.com.myappdemo.utils.Constant.KEY_MESSAGE;
import static guyuanjun.com.myappdemo.utils.Constant.MESSAGE_RECEIVED_ACTION;

/**
 * Created by HP on 2017-4-20.
 */

public class MessageReceiver extends BroadcastReceiver {
    private final String TAG = "MessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
            String messge = intent.getStringExtra(KEY_MESSAGE);
            String extras = intent.getStringExtra(KEY_EXTRAS);
            StringBuilder showMsg = new StringBuilder();
            showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
            if (!ExampleUtil.isEmpty(extras)) {
                showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
            }
            setCostomMsg(showMsg.toString());
        }
    }

    private void setCostomMsg(String msg){
        Log.d(TAG, msg);
    }
}
