package sanstech.camswitch;

import android.content.Context;
import android.view.View;

/**
 * Created by svema on 10/3/2014.
 */
public interface IViewRequest {

    public View requestViewByID(int id);
    public Context getApplicationContext();
}
