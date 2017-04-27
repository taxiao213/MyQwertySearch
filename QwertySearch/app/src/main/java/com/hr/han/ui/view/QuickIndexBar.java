package com.hr.han.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.hr.han.R;

/**
 * Created by yin13 on 2016/12/15.
 */

public class QuickIndexBar extends LinearLayout {
    Paint paint = null;
    int ColorDefault = Color.GRAY;
    int ColorPress = Color.BLUE;
    //    private String[] indexArr = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
    //            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private String[] indexArr;


    public QuickIndexBar(Context context, String[] index) {
        this(context, null, index);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, String[] index) {
        this(context, attrs, 0, index);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr, String[] index) {
        super(context, attrs, defStyleAttr);
        this.indexArr = index;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
        paint.setColor(ColorDefault);
        //获取dimens的值
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.text_size);
        paint.setTextSize(dimensionPixelSize);
        //文字的默认起点在文字左下角,CENTER表示底边的正中心,BaseLine 基准线
        paint.setTextAlign(Paint.Align.CENTER);
    }

    float cellHeight;//一个格子的高度 (精准)

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //一个格子的高度 (精准)
        cellHeight = getMeasuredHeight() * 1f / indexArr.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //遍历绘制26个字母
        for (int i = 0; i < indexArr.length; i++) {
            String text = indexArr[i];
            //x为整个宽度的一半,设置为居中
            float x = getMeasuredWidth() / 2;
            //格子的高度一半+文字高度一半+i*格子的高度
            int textHeight = getTextHeight(text);
            float y = cellHeight / 2 + cellHeight * i + textHeight / 2;
            //变色
            paint.setColor(index == i ? ColorPress : ColorDefault);
            canvas.drawText(text, x, y, paint);

        }

    }

    private int getTextHeight(String text) {

        Rect bounds = new Rect();
        //获取文本框
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    //拿到Y坐标/格子的高度,得到索引
    int index = -1;//记录触摸的索引

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float downY = event.getY();
                int temp = (int) (downY / cellHeight);
                if (temp != index) {
                    index = temp;
                    //对index进行安全的检查
                    if (index >= 0 && index < indexArr.length) {
                        String name = indexArr[index];
                        if (mlistener != null) {
                            mlistener.getWord(name);
                        }
                    }
                }
                this.setBackgroundColor(getResources().getColor(R.color.grey_64));

                break;
            case MotionEvent.ACTION_UP:
                //抬起时重置
                index = -1;
                this.setBackgroundColor(getResources().getColor(R.color.grey_ef));
                break;
        }
        //按下时变色,重新绘制
        invalidate();
        return true;
    }

    //写个接口 对外提供暴露的文字
    private OnQuickIndex mlistener;

    public void SetOnQuickIndex(OnQuickIndex listener) {
        this.mlistener = listener;
    }

    public interface OnQuickIndex {
        void getWord(String name);
    }
}
