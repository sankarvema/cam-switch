package sanstech.camswitch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

public class MediaButtonIntentReceiver extends BroadcastReceiver {

    PhotoHandler photo = null;

    public MediaButtonIntentReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "OnReceive", Toast.LENGTH_SHORT).show();
        String intentAction = intent.getAction();
        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            return;
        }

        KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }

        int action = event.getAction();
//        switch (event.getKeyCode()) {
//            case KeyEvent.KEYCODE_HEADSETHOOK:
//                if (action == KeyEvent.ACTION_DOWN) {
//                    long time = SystemClock.uptimeMillis();
//                    // double click
//                    if (time - sLastClickTime < DOUBLE_CLICK_DELAY)
//                        // do something
//                        Toast.makeText(context, "BUTTON PRESSED DOUBLE!",
//                                Toast.LENGTH_SHORT).show();
//                        // single click
//                    else {
//                        // do something
//                        Toast.makeText(context, "BUTTON PRESSED!",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    sLastClickTime = time;
//                }
//                break;
//        }

        if (action == KeyEvent.ACTION_DOWN) {
            // do something
            Toast.makeText(context, "BUTTON PRESSED!", Toast.LENGTH_SHORT).show();

            if (photo==null)
                photo = new PhotoHandler(context);

            photo.takePhoto();
        }
        abortBroadcast();
    }
}
