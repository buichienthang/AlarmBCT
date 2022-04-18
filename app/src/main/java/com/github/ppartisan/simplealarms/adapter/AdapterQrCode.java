package com.github.ppartisan.simplealarms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.model.Qrcode;

import java.util.List;

public class AdapterQrCode extends BaseAdapter {

    Context context;
    int layout;
    List<Qrcode> list;

    public AdapterQrCode(Context context, int layout, List<Qrcode> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(layout, null);

        TextView tvNameQrcode = view.findViewById(R.id.name_qr_code);
        tvNameQrcode.setText(list.get(i).getNameQrcode());

        return view;
    }
}
