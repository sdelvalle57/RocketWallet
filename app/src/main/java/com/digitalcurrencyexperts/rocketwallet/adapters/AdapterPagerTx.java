package com.digitalcurrencyexperts.rocketwallet.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;

/**
 * Created by santiagp on 4/08/2017.
 */

public class AdapterPagerTx extends PagerAdapter {
    private Vector<View> resources;

    public AdapterPagerTx(Vector<View> resources) {
        this.resources=resources;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = resources.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return resources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



}
