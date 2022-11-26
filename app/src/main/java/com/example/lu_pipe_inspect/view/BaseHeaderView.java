package com.example.lu_pipe_inspect.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lu_pipe_inspect.R;
import com.example.lu_pipe_inspect.util.MyApplication;


/**
 * 自定义header头部
 * ①返回按钮   返回文字
 * ②title
 * ③保存按钮   保存文字
 *
 */

public class BaseHeaderView extends LinearLayout {
    private AttributeSet attrs;

    private TextView tviewTittle;//标题
    private String tvTitle;//标题
    private ImageView ivviewLeft;//图片
    private boolean isTittle;//标题是否显示
    private boolean isleftIv;//图标是否显示

    public void setTvTitle(String tvTitle) {
        tviewTittle.setText(tvTitle);
        this.tvTitle = tvTitle;
    }

    public BaseHeaderView(Context context) {
        super(context);
        initAttributes();
    }

    public BaseHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs=attrs;
        initAttributes();
    }

    /**
     * 初始化属性
     */
    private void initAttributes() {
        if(attrs!=null){
            TypedArray typedArray = MyApplication.context.obtainStyledAttributes(attrs, R.styleable.myHeaderView);
            if (typedArray != null) {
                tvTitle = typedArray.getString(R.styleable.myHeaderView_text_header_title);
                isTittle=typedArray.getBoolean(R.styleable.myHeaderView_text_header_is_title_visiable,true);
                isleftIv=typedArray.getBoolean(R.styleable.myHeaderView_text_header_is_left_iv_visiable,true);
                typedArray.recycle();
            }
        }
        initView();
    }

    /**
     * 初始化view
     */
    public void initView(){
        LayoutInflater.from(MyApplication.context).inflate(R.layout.iyem_myheaderview, this, true);
        tviewTittle= (TextView) findViewById(R.id.tvHeaderTitle);
        ivviewLeft=(ImageView) findViewById(R.id.ivHeaderTitle);
        if(isTittle){
            tviewTittle.setVisibility(VISIBLE);
            tviewTittle.setText(tvTitle);
        }else{
            tviewTittle.setVisibility(GONE);
        }
        if(isleftIv){
            ivviewLeft.setVisibility(VISIBLE);
        }else {
            ivviewLeft.setVisibility(GONE);
        }
    }
}
