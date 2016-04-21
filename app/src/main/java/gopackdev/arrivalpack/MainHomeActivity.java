package gopackdev.arrivalpack;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;

import android.widget.FrameLayout;


import gopackdev.arrivalpack.baseactivities.DrawerBaseActivity;


public class MainHomeActivity extends DrawerBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout relativeLayout = (FrameLayout)findViewById(R.id.child_layout);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        relativeLayout.addView(layoutInflater.inflate(R.layout.content_home_page, null, false));

    }

}
