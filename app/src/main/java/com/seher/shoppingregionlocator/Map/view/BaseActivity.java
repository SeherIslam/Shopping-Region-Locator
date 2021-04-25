package com.seher.shoppingregionlocator.Map.view;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.seher.shoppingregionlocator.Map.app.MyApplication;
import com.seher.shoppingregionlocator.Map.data.api.OnHttpResponse;

/**
 * Created by chiranjitbardhan on 20/01/18.
 */
public abstract class BaseActivity extends AppCompatActivity implements OnHttpResponse
{
    public static final String TAG = BaseActivity.class.getSimpleName();

    public MyApplication mController;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.mController = MyApplication.getInstance();
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(int requestCode, int responseCode, String response) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        try
        {
            if (getCurrentFocus() != null)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if(imm != null)
                {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return super.dispatchTouchEvent(ev);
    }
}