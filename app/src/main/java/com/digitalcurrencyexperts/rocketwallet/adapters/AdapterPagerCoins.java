package com.digitalcurrencyexperts.rocketwallet.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digitalcurrencyexperts.rocketwallet.R;

import java.util.Vector;

/**
 * Created by Santiago Del Valle on 1/04/2017.
 * Company name Zombytes Devs
 * Contact sdelvalle57@gmail.com
 */

public class AdapterPagerCoins extends PagerAdapter {
    private Vector<Integer> resources;
    private Context context;

    public AdapterPagerCoins(Vector<Integer> resources, Context context) {
        this.resources=resources;
        this.context = context;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        int resource = resources.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.image_coin, container, false);
        AppCompatImageView imageView = (AppCompatImageView) view.findViewById(R.id.image_coin);
        imageView.setImageResource(resource);
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
