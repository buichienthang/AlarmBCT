package com.github.ppartisan.simplealarms.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.model.Qrcode;

import java.util.List;

public class AdapterQrCode extends BaseAdapter {

    Context context;
    int layout;
    List<Qrcode> list;
    int idQrcode;

    public AdapterQrCode(Context context, int layout, List<Qrcode> list, int idQrcode) {
        this.context = context;
        this.layout = layout;
        this.list = list;
        this.idQrcode = idQrcode;
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
        TextView tvSelect = view.findViewById(R.id.tv_select);
        if (list.get(i).getId() == idQrcode) {
            tvSelect.setText("Đang chọn");
            tvSelect.setTextColor(Color.BLUE);
        }

//        tvSelect.setText("Chọn");

        return view;
    }
}
