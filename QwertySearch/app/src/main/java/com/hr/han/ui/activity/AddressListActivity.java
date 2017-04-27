package com.hr.han.ui.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hr.han.R;
import com.hr.han.base.BaseActivity;
import com.hr.han.bean.ItemContacts;
import com.hr.han.ui.adapter.SearchAdapter;
import com.hr.han.ui.view.ClearEditText;
import com.hr.han.ui.view.QuickIndexBar;
import com.hr.han.utils.UIUtil;
import com.hr.han.utils.pinyin.PinYinUtils;
import com.hr.han.utils.text.RegularUtil;
import com.qwertysearch.model.PinyinSearchUnit;
import com.qwertysearch.util.PinyinUtil;
import com.qwertysearch.util.QwertyUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Han on 2017/4/20.
 */

public class AddressListActivity extends BaseActivity {
    @Bind(R.id.back_iv)
    ImageView backIv;
    @Bind(R.id.search_tv)
    TextView searchTv;
    @Bind(R.id.address_rv)
    RecyclerView addressRv;
    @Bind(R.id.bg_tv)
    TextView bgTv;
    @Bind(R.id.rl)
    RelativeLayout rl;
    @Bind(R.id.search_et)
    ClearEditText searchEt;
    @Bind(R.id.search_rl)
    RelativeLayout searchRl;
    @Bind(R.id.search_address_rv)
    RecyclerView searchAddressRv;
    @Bind(R.id.address_rl)
    RelativeLayout address_rl;
    @Bind(R.id.ll)
    LinearLayout llt;

    private ArrayList<ItemContacts> list;
    private MyAdapter adapter;
    private Handler handler = new Handler();
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ItemContacts> itemlist;
    private String[] strings;
    private QuickIndexBar quickIndexBar;
    private SearchAdapter searchAdapter;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_addresslist;
    }

    @Override
    protected void init() {

        //初始化view
        initView();

    }

    /**
     * 右侧快速滑动显示字母
     *
     * @param name
     */
    private void showCurrentWord(String name) {
        handler.removeCallbacksAndMessages(null);
        bgTv.setVisibility(View.VISIBLE);
        bgTv.setText(name);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bgTv.setVisibility(View.GONE);
            }
        }, 1000);

    }

    private void initView() {

        list = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        //通讯录列表
        addressRv.setLayoutManager(linearLayoutManager);
        //搜索列表
        searchAddressRv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));

        Observable.create(new Observable.OnSubscribe<ArrayList<ItemContacts>>() {
            @Override
            public void call(Subscriber<? super ArrayList<ItemContacts>> subscriber) {
                itemlist = getItemlist();
                String st = "";
                if (itemlist != null && itemlist.size() > 0) {
                    for (int i = 0; i < itemlist.size(); i++) {
                        String headpinyin = itemlist.get(i).headpinyin.charAt(0) + "";
                        st += headpinyin;
                    }
                }

                if (st != null && st.length() > 0) {
                    //获取通讯录列表的首字母并排序
                    String word = st.replaceAll("(?s)(.)(?=.*\\1)", "");
                    strings = new String[word.length()];
                    for (int i = 0; i < strings.length; i++) {
                        strings[i] = word.substring(i, i + 1);
                    }

                }
                subscriber.onNext(itemlist);

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<ItemContacts>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<ItemContacts> itemContactses) {
                        if (adapter == null) {
                            adapter = new MyAdapter(itemContactses);
                            addressRv.setAdapter(adapter);
                        }

                        //初始化快速检索
                        if (quickIndexBar == null) {
                            quickIndexBar = new QuickIndexBar(mActivity, strings);
                            quickIndexBar.setBackgroundColor(getResources().getColor(R.color.grey_ef));
                            int pxWidth = UIUtil.dip2px(22);
                            //设定每个字母height占18dp
                            int height = 18;
                            int pxHeight = UIUtil.dip2px(height * (strings.length));
                            llt.addView(quickIndexBar, pxWidth, pxHeight);
                        }

                        //联系人快速检索
                        quickIndexBar.SetOnQuickIndex(new QuickIndexBar.OnQuickIndex() {
                            @Override
                            public void getWord(final String name) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < itemlist.size(); i++) {
                                            String pinyin = itemlist.get(i).headpinyin.charAt(0) + "";
                                            if (name.equals(pinyin)) {
                                                final int finalI = i;
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //找到对应的条目后置顶
                                                        linearLayoutManager.scrollToPositionWithOffset(finalI, 0);
                                                        linearLayoutManager.setStackFromEnd(true);
                                                    }
                                                });
                                                break;
                                            }
                                        }
                                    }
                                }.start();

                                showCurrentWord(name);
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                });

        //初始化搜索Adapter
        searchAdapter = new SearchAdapter(mActivity, list);
        searchAddressRv.setAdapter(searchAdapter);

    }


    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<ItemContacts> list;

        public MyAdapter(ArrayList<ItemContacts> list) {
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.activity_address_item, null);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder holde = (ViewHolder) holder;

            final ItemContacts item = list.get(position);
            final String number = item.getNumber();
            String letter = item.headpinyin.charAt(0) + "";
            //分类栏的显示和隐藏
            if (position > 0) {
                //获取上一个条目的首字母
                String previousLetter = list.get(position - 1).headpinyin.charAt(0) + "";
                //如果当前letter和上一个的一样，那么则隐藏当前的字母View
                if (letter.equals(previousLetter)) {
                    // holde.view.setVisibility(View.GONE);
                    holde.letterTv.setVisibility(View.GONE);
                } else {
                    // holde.view.setVisibility(View.VISIBLE);
                    holde.letterTv.setVisibility(View.VISIBLE);
                    holde.letterTv.setText(letter);
                }
            } else {
                //说明是第0个，直接显示
                // holde.view.setVisibility(View.VISIBLE);
                holde.letterTv.setVisibility(View.VISIBLE);
                holde.letterTv.setText(letter);
            }
            //条目的下划线显示和隐藏
            if (position < list.size() - 1) {
                //获取下一个条目的首字母
                String previousLetter = list.get(position + 1).headpinyin.charAt(0) + "";
                if (!letter.equals(previousLetter)) {
                    holde.belowLine.setVisibility(View.GONE);
                } else {
                    holde.belowLine.setVisibility(View.VISIBLE);
                }
            }

            holde.nameTv.setText(item.name);
            holde.messageTv.setText(item.number);
            holde.callIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转打电话
                    Intent data = new Intent();
                    data.setAction(Intent.ACTION_CALL);
                    data.setData(Uri.parse("tel:" + number));
                    startActivity(data);
                }
            });

            holde.mailIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转发送短信
                    Uri uri = Uri.parse("smsto:" + number);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    startActivity(intent);
                }
            });


            //// TODO: 2017/4/21 有问题
            //  Bitmap image = getAllImage(mActivity, item.contactsId);
            //            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //
            //            image.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //            byte[] bytes = baos.toByteArray();
            //

            //使用Glide加载图片
            //                        Glide.with(holde.iconIv.getContext())
            //                                .load()
            //
            //                                .centerCrop()//设置从中间剪切
            //                               // .placeholder(R.mipmap.ic_launcher_round)//设置默认图片
            //                                .crossFade(1000)//渐变的时间
            //                                .into(holde.iconIv);//要设置的imageview

            //
            //            Bitmap allImage = getAllImage(
            //                    getApplicationContext(), item.contactsId);
            //            if (allImage != null) {
            //                // 设置头像
            //                holde.iconIv.setImageBitmap(allImage);
            //            } else {
            //                // 没有头像 也要设置默认头像 否则会复用错误
            //                holde.iconIv.setImageResource(R.mipmap.ic_launcher);
            //            }

            /*if (image != null) {
                // 圆形头像
                Bitmap bitmap = BitmapUtils.makeRoundCorner(image);

                // 设置头像
                holde.iconIv.setImageBitmap(bitmap);
            } else {
                // TODO: 2017/4/20 卡顿 
                // 没有头像 也要设置默认头像 否则会复用错误
//                Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.timg);
//                Bitmap bitmap2 = BitmapUtils.makeRoundCorner(bitmap1);
//                holde.iconIv.setImageBitmap(bitmap2);
                holde.iconIv.setImageResource(R.mipmap.ic_launcher);
            }*/
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.letter_tv)
            TextView letterTv;
            @Bind(R.id.icon_iv)
            ImageView iconIv;
            @Bind(R.id.name_tv)
            TextView nameTv;
            @Bind(R.id.message_tv)
            TextView messageTv;
            @Bind(R.id.address_rv)
            RelativeLayout addressRv;
            @Bind(R.id.call_iv)
            ImageView callIv;
            @Bind(R.id.mail_iv)
            ImageView mailIv;
            @Bind(R.id.below_line)
            View belowLine;

            ViewHolder(final View view) {
                super(view);
                ButterKnife.bind(this, view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int layoutPosition = getLayoutPosition();
                        // TODO: 2017/4/26
                    }
                });
            }
        }
    }

    ArrayList<ItemContacts> items = new ArrayList<>();
    ItemContacts item;

    /**
     * 获取手机通讯录联系人
     *
     * @return
     */
    private ArrayList<ItemContacts> getItemlist() {
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

    /*
    * 获取头像
    */
    public Uri getImageUri(Context context, int contactsId) {
        // 获取内容解析者
        ContentResolver contentResolver = context.getContentResolver();
        // 查头像要传的uri 参1 baseuri 参2 要拼接的部分
        Uri contactUri = Uri.withAppendedPath(
                ContactsContract.Contacts.CONTENT_URI, contactsId + "");
        Log.e("getImageUri: ", contactUri + "");
        //Uri uri = Uri.parse(contactUri);
        //        String contactUri = "photo:" + Uri.withAppendedPath(
        //                ContactsContract.Contacts.CONTENT_URI, contactsId + "").toString();
        //        Uri uri = Uri.parse(contactUri);

        return contactUri;

    }


    /*
     * 获取头像
     */
    public static Bitmap getAllImage(Context context, int contactsId) {
        // 获取内容解析者
        ContentResolver contentResolver = context.getContentResolver();
        // 查头像要传的uri 参1 baseuri 参2 要拼接的部分
        Uri contactUri = Uri.withAppendedPath(
                ContactsContract.Contacts.CONTENT_URI, contactsId + "");
        // 获取联系人头像的流
        InputStream iconIs = ContactsContract.Contacts
                .openContactPhotoInputStream(contentResolver, contactUri);
        //把流生成bitmap对象
        Bitmap bitmap = BitmapFactory.decodeStream(iconIs);
        return bitmap;

    }

    @OnClick({R.id.back_iv, R.id.search_tv, R.id.search_back_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.search_tv:

                searchAddressRv.setVisibility(View.VISIBLE);
                searchRl.setVisibility(View.VISIBLE);
                rl.setVisibility(View.GONE);
                searchTv.setVisibility(View.GONE);
                break;
            case R.id.search_back_iv:
                address_rl.setVisibility(View.VISIBLE);
                searchAddressRv.setVisibility(View.GONE);
                searchRl.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
                searchTv.setVisibility(View.VISIBLE);
                searchEt.setText("");
                list.clear();
                searchAdapter.notifyDataSetChanged();

                break;
        }
    }


    /**
     * 搜索通讯录
     *
     * @param cs
     */
    @OnTextChanged(R.id.search_et)
    public void onTextChange(final CharSequence cs) {
        final String text = cs.toString().trim();

        if (text.length() > 0) {
            list.clear();
            address_rl.setVisibility(View.GONE);
            searchAddressRv.setVisibility(View.VISIBLE);
            //搜索的内容标颜色识别
            // searchAdapter.selectColor = text;

            //将输入的汉字转换为字母
            Observable.create(new Observable.OnSubscribe<List<ItemContacts>>() {
                @Override
                public void call(Subscriber<? super List<ItemContacts>> subscriber) {

                    //是数字
                    if (RegularUtil.isNumber(text)) {

                        for (int i = 0; i < itemlist.size(); i++) {
                            ItemContacts itemContacts = itemlist.get(i);
                            String itemNumber = itemContacts.getNumber();
                            if (itemNumber.contains(text)) {
                                //   Log.d("call", "数字===" + text + "..." + itemNumber);
                                itemContacts.setColorNumberName(text);
                                list.add(itemContacts);
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
                                list.add(itemContacts);
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
                                    //将要改变颜色的字体设置到bean
                                    itemCont.setColorName(string);
                                    //  Log.d("call", "查询...namePinyinSearchUnit===" + string + "....lowerCase===" + lowerCase + "..." + text);
                                    list.add(itemCont);

                                }
                            }
                        }
                    }

                    subscriber.onNext(list);
                    subscriber.onCompleted();
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
                            searchAdapter.notifyDataSetChanged();
                        }
                    });
        } else {
            address_rl.setVisibility(View.VISIBLE);
            searchAddressRv.setVisibility(View.GONE);
            list.clear();
            searchAdapter.notifyDataSetChanged();
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


    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
