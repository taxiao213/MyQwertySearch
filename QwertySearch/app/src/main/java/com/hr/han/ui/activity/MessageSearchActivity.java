package com.hr.han.ui.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hr.han.R;
import com.hr.han.base.BaseActivity;
import com.hr.han.bean.ItemContacts;
import com.hr.han.ui.adapter.SearchAdapter;
import com.hr.han.ui.view.ClearEditText;
import com.hr.han.utils.pinyin.PinYinUtils;
import com.hr.han.utils.text.RegularUtil;
import com.qwertysearch.model.PinyinSearchUnit;
import com.qwertysearch.util.PinyinUtil;
import com.qwertysearch.util.QwertyUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Han on 2017/4/22.
 */

public class MessageSearchActivity extends BaseActivity {

    @Bind(R.id.search_back_iv)
    ImageView searchBackIv;
    @Bind(R.id.search_et)
    ClearEditText searchEt;
    @Bind(R.id.search_rl)
    RelativeLayout searchRl;
    @Bind(R.id.search_contacts_rv)
    RecyclerView searchContactsRv;
    @Bind(R.id.search_contacts_ll)
    LinearLayout searchContactsLl;
    @Bind(R.id.search_message_rv)
    RecyclerView searchMessageRv;
    @Bind(R.id.search_message_ll)
    LinearLayout searchMessageLl;
    @Bind(R.id.search_result_ll)
    LinearLayout searchResultLl;
    @Bind(R.id.search_normal_ll)
    LinearLayout searchNormalLl;
    @Bind(R.id.search_more_rl)
    RelativeLayout searchMoreRl;
    @Bind(R.id.search_result_more_rl)
    RelativeLayout searchResultMoreRl;
    @Bind(R.id.search_contacts_more_rv)
    RecyclerView searchContactsMoreRv;
    @Bind(R.id.search_more_tv)
    TextView searchMoreTv;

    private ArrayList<ItemContacts> contactslist;
    private ArrayList<ItemContacts> itemlist;
    private ArrayList<ItemContacts> contacts_more_list;
    private SearchAdapter searchContacts_Adapter;
    private SearchAdapter contactsAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_message_search;
    }

    @Override
    protected void init() {
        initView();
    }

    private void initView() {

        // 初始化聊天记录列表
        searchMessageView();

        // 初始化联系人列表
        searchContactsView();

    }

    /**
     * 初始化聊天记录列表
     */
    private void searchMessageView() {

    }

    /**
     * 初始化联系人列表
     */
    private void searchContactsView() {
        contactslist = new ArrayList<>();
        contacts_more_list = new ArrayList<>();
        //显示3个条目
        searchContactsRv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        //显示全部的条目
        searchContactsMoreRv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));

        //显示全部的条目Adapter
        searchContacts_Adapter = new SearchAdapter(mActivity, contactslist);
        //显示全部的条目
        searchContactsMoreRv.setAdapter(searchContacts_Adapter);
        //显示3条条目的Adapter
        contactsAdapter = new SearchAdapter(mActivity, contacts_more_list);
        //显示3条条目
        searchContactsRv.setAdapter(contactsAdapter);

    }


    @OnTextChanged(R.id.search_et)
    public void onTextChange(final CharSequence cs) {
        final String text = cs.toString().trim();
        if (text.length() > 0) {
            searchResultLl.setVisibility(View.VISIBLE);
            searchNormalLl.setVisibility(View.GONE);
            searchMoreRl.setVisibility(View.GONE);
            contactslist.clear();
            contacts_more_list.clear();

            //将输入的汉字转换为字母
            Observable.create(new Observable.OnSubscribe<List<ItemContacts>>() {
                @Override
                public void call(Subscriber<? super List<ItemContacts>> subscriber) {
                    itemlist = getItemlist();

                    //是数字
                    if (RegularUtil.isNumber(text)) {

                        for (int i = 0; i < itemlist.size(); i++) {
                            ItemContacts itemContacts = itemlist.get(i);
                            String itemNumber = itemContacts.getNumber();
                            if (itemNumber.contains(text)) {
                                //   Log.d("call", "数字===" + text + "..." + itemNumber);
                                itemContacts.setColorNumberName(text);
                                contactslist.add(itemContacts);
                            }
                        }
                    }

                    //汉字 直接用字去匹配，不需要转全拼
                    if (RegularUtil.isChinese(text)) {

                        for (int i = 0; i < itemlist.size(); i++) {
                            ItemContacts itemContacts = itemlist.get(i);
                            String itemName = itemContacts.getName();
                            if (itemName.contains(text)) {
                                //   Log.d("call", "汉字== " + itemName + "..." + text);
                                itemContacts.setColorName(text);
                                contactslist.add(itemContacts);
                            }
                        }
                    }


                    //字母
                    if (RegularUtil.isWord(text)) {
                        //转变成小写字母
                        String lowerCase = text.toLowerCase();
                        PinyinSearchUnit namePinyinSearchUnit;
                        String string;

                        for (int i = 0; i < itemlist.size(); i++) {
                            ItemContacts itemCont = itemlist.get(i);
                            String itemWordName = itemCont.getName();
                            //获取每个汉字首字母
                            String headName = PinYinUtils.getPinYinHeadChar(itemWordName);

                            //1.用Searcy初始化 PinyinSearchUnit
                            namePinyinSearchUnit = new PinyinSearchUnit(itemWordName);
                            //2.初始化 初始化之前先new PinyinSearchUnit
                            PinyinUtil.parse(namePinyinSearchUnit);
                            //3.去匹配，输出汉字
                            if (headName.contains(lowerCase.substring(0, 1))) {
                                if (QwertyUtil.match(namePinyinSearchUnit, text)) {//search by name;
                                    string = namePinyinSearchUnit.getMatchKeyword().toString();
                                    itemCont.setColorName(string);
                                    // Log.d("call", "查询...namePinyinSearchUnit===" + string + "....lowerCase===" + lowerCase + "..." + text);
                                    contactslist.add(itemCont);

                                }
                            }
                        }
                    }



                    if (contactslist != null && contactslist.size() > 3) {
                        contacts_more_list.clear();
                        for (int i = 0; i < 3; i++) {
                            contacts_more_list.add(i, contactslist.get(i));

                        }
                    } else {
                        contacts_more_list.clear();
                        contacts_more_list.addAll(contactslist);
                    }

                    subscriber.onNext(contactslist);

                }
            })
                    .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                    .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                    .subscribe(new Observer<List<ItemContacts>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<ItemContacts> departmentInfos) {

                            //三个条目的adapter
                            contactsAdapter.notifyDataSetChanged();

                            if (contactslist != null && contactslist.size() > 3) {
                                // 显示 查看更多联系人
                                searchMoreRl.setVisibility(View.VISIBLE);
                                // 显示 搜索结果界面
                                searchResultLl.setVisibility(View.VISIBLE);
                                // 隐藏 默认的界面
                                searchNormalLl.setVisibility(View.GONE);
                                //隐藏全部的条目
                                searchContactsMoreRv.setVisibility(View.GONE);

                                //点击查看更多联系人
                                searchMoreRl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //隐藏 查看更多联系人
                                        searchMoreRl.setVisibility(View.GONE);
                                        //隐藏 聊天记录
                                        searchMessageLl.setVisibility(View.GONE);
                                        //显示 搜索结果
                                        searchResultMoreRl.setVisibility(View.VISIBLE);
                                        //隐藏 3条条目的adapter
                                        searchContactsRv.setVisibility(View.GONE);
                                        //显示 全部的条目
                                        searchContactsMoreRv.setVisibility(View.VISIBLE);
                                        //全部条目的adapter
                                        searchContacts_Adapter.notifyDataSetChanged();
                                    }
                                });

                                //点击搜索结果
                                searchResultMoreRl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //显示 查看更多联系人
                                        searchMoreRl.setVisibility(View.VISIBLE);
                                        //显示 聊天记录
                                        searchMessageLl.setVisibility(View.VISIBLE);
                                        //隐藏 搜索结果
                                        searchResultMoreRl.setVisibility(View.GONE);
                                        //隐藏 全部的条目
                                        searchContactsMoreRv.setVisibility(View.GONE);
                                        //显示3个条目的adapter
                                        searchContactsRv.setVisibility(View.VISIBLE);
                                    }
                                });

                            } else {
                                searchMoreRl.setVisibility(View.GONE);
                            }

                            if (contacts_more_list.size() < 0) {
                                searchMoreRl.setVisibility(View.GONE);
                                //隐藏 全部的条目
                                searchContactsMoreRv.setVisibility(View.GONE);
                                //显示3个条目的adapter
                                searchContactsRv.setVisibility(View.GONE);
                                searchNormalLl.setVisibility(View.VISIBLE);
                            }

                        }
                    });

        } else {
            searchResultLl.setVisibility(View.GONE);
            searchNormalLl.setVisibility(View.VISIBLE);
            searchResultMoreRl.setVisibility(View.GONE);
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < itemlist.size(); i++) {
                        ItemContacts itemCont = itemlist.get(i);
                        itemCont.setColorName("");
                        itemCont.setColorNumberName("");
                    }
                }
            }.start();
        }

    }


    /**
     * 获取手机通讯录联系人
     *
     * @return
     */
    private ArrayList<ItemContacts> getItemlist() {
        ArrayList<ItemContacts> items = new ArrayList<>();
        ItemContacts item;
        Log.e("come in", "getItemlist: " + "con");
        if (items != null)
            items.clear();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, // 用户名
                        ContactsContract.CommonDataKinds.Phone.NUMBER, // 电话
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID},
                null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                String number = cursor.getString(1);
                int contactsId = cursor.getInt(2);
                item = new ItemContacts(name, number, contactsId);
                items.add(item);
            }
        }
        Collections.sort(items);
        cursor.close();

        Log.e("come off", "getItemlist: " + "con");
        return items;
    }


    @OnClick(R.id.search_back_iv)
    public void onClick() {
        finish();
    }
}