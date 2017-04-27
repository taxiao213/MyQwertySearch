package com.hr.han.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hr.han.R;
import com.hr.han.bean.ItemContacts;
import com.hr.han.ui.view.ColorTextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 搜索的条目
 * Created by Han on 2017/4/25.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ItemContacts> list;

    public SearchAdapter(Context context, ArrayList<ItemContacts> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.activity_address_search_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holde = (ViewHolder) holder;
        final ItemContacts item = list.get(position);
        //设置字体颜色
        holde.searchNameTv.showTextHighlight(item.name, item.getColorName());
        holde.searchPhoneTv.showTextHighlight(item.number, item.getColorNumberName());

        holde.searchCallIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转打电话
                Intent data = new Intent();
                data.setAction(Intent.ACTION_CALL);
                data.setData(Uri.parse("tel:" + item.number));
                //Toast.makeText(context, item.name + "..." + item.number, Toast.LENGTH_SHORT).show();
                context.startActivity(data);
            }
        });

        holde.searchMailIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转发送短信
                Uri uri = Uri.parse("smsto:" + item.number);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.search_icon_iv)
        ImageView searchIconIv;
        @Bind(R.id.search_name_tv)
        ColorTextView searchNameTv;
        @Bind(R.id.search_phone_tv)
        ColorTextView searchPhoneTv;
        @Bind(R.id.search_call_iv)
        ImageView searchCallIv;
        @Bind(R.id.search_mail_iv)
        ImageView searchMailIv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
