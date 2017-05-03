package com.alia.pokemoshow;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GalleryActivity extends SingleFragmentActivity{


        @Override
        protected Fragment createFragment() {
            return GalleryFragment.newInstance();
        }

}
