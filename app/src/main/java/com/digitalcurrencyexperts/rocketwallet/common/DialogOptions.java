package com.digitalcurrencyexperts.rocketwallet.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.digitalcurrencyexperts.rocketwallet.R;
import com.digitalcurrencyexperts.rocketwallet.adapters.ToolsAdapter;

import java.util.ArrayList;

/**
 * Created by santiagp on 23/08/2017.
 */

public class DialogOptions extends Dialog implements Interfaces.OptionsListCallback {

    private Context context;
    private RecyclerView recyclerView;
    private String phrase;
    private Interfaces.OptionsListCallback optionsListCallback;

    public DialogOptions(@NonNull Context context) {
        super(context);
        this.context = context;
        this.phrase = phrase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_options);
        getWindow().getAttributes().windowAnimations = R.style.customAnimations_slide;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        setAdapter();
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    public void setOptionsListCallback(Interfaces.OptionsListCallback optionsListCallback) {
        this.optionsListCallback = optionsListCallback;
    }

    private void setAdapter() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        ToolsAdapter toolsAdapter = new ToolsAdapter(getTittles(), getImages());
        toolsAdapter.setOptionsListCallback(this);
        recyclerView.setAdapter(toolsAdapter);
    }

    private ArrayList<String> getTittles() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(context.getString(R.string.backup_wallet));
        arrayList.add(context.getString(R.string.restore_wallet));
        return arrayList;
    }

    private ArrayList<Integer> getImages() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.drawable.vector_backup);
        arrayList.add(R.drawable.vector_wallet);
        return arrayList;
    }

    @Override
    public void onOptionClick(int pos) {
        dismiss();
        if (optionsListCallback!=null) optionsListCallback.onOptionClick(pos);
    }
}
