package com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StrikethroughSpan;

import com.guadou.lib_baselib.utils.CommUtils;
import com.guadou.lib_baselib.view.span.CustomTypefaceSpan;
import com.guadou.lib_baselib.view.span.MiddleIMarginImageSpan;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.Stack;

/**
 * 支持的标签为
 * <del>xxx</del>  中划线
 * <size value='16'>xxx</size>  自定义大小文本
 * <face>xxx</face>       自定义字体
 */
public class CustomerLableHandler implements Html.TagHandler {

    private Typeface typeface;
    private int imgRes;

    public CustomerLableHandler(Typeface typeface, int imgRes) {
        this.typeface = typeface;
        this.imgRes = imgRes;
    }

    /**
     * html 标签的开始下标，为了支持多个标签，使用栈管理开始下标
     */
    private Stack<Integer> startIndex;

    /**
     * html的标签的属性值
     */
    private Stack<String> propertyValue;

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (opening) {
            handlerStartTAG(tag, output, xmlReader);
        } else {
            handlerEndTAG(tag, output);
        }
    }

    /**
     * 处理开始的标签位
     */
    private void handlerStartTAG(String tag, Editable output, XMLReader xmlReader) {
        if (tag.equalsIgnoreCase("del")) {
            handlerStartDEL(output);
        } else if (tag.equalsIgnoreCase("size")) {
            handlerStartSIZE(output, xmlReader);
        }else if (tag.equalsIgnoreCase("face")){
            handleStartFACE(output);
        }else if (tag.equalsIgnoreCase("icon")){
            handleStartICON(output);
        }
    }

    /**
     * 处理结尾的标签位
     */
    private void handlerEndTAG(String tag, Editable output) {
        if (tag.equalsIgnoreCase("del")) {
            handlerEndDEL(output);
        } else if (tag.equalsIgnoreCase("size")) {
            handlerEndSIZE(output);
        }else if (tag.equalsIgnoreCase("face")){
            handleEndFACE(output);
        }else if (tag.equalsIgnoreCase("icon")){
            handleEndICON(output);
        }
    }

    // =======================  自定义Icon begin ↓ =========================

    private void handleStartICON(Editable output) {
        if (startIndex == null) {
            startIndex = new Stack<>();
        }
        startIndex.push(output.length());
    }

    private void handleEndICON(Editable output) {

        Drawable drawable = CommUtils.getDrawable(imgRes);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        MiddleIMarginImageSpan imageSpan = new MiddleIMarginImageSpan(drawable, 4, 0, 0);

        output.setSpan(imageSpan, startIndex.pop(), output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    // =======================  自定义Icon end ↑ =========================



    // =======================  自定义字体 begin ↓ =========================

    private void handleStartFACE(Editable output) {
        if (startIndex == null) {
            startIndex = new Stack<>();
        }
        startIndex.push(output.length());
    }

    private void handleEndFACE(Editable output) {
        output.setSpan(new CustomTypefaceSpan(typeface), startIndex.pop(), output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    // =======================  自定义字体 end ↑ =========================


    // =======================  中划线处理 begin ↓ =========================

    private void handlerStartDEL(Editable output) {
        if (startIndex == null) {
            startIndex = new Stack<>();
        }
        startIndex.push(output.length());
    }

    //中划线的Span
    private void handlerEndDEL(Editable output) {
        output.setSpan(new StrikethroughSpan(), startIndex.pop(), output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    // =======================  中划线处理 end ↑ =========================

    // =======================  文本大小设置 begin ↓ =========================

    private void handlerStartSIZE(Editable output, XMLReader xmlReader) {
        if (startIndex == null) {
            startIndex = new Stack<>();
        }
        startIndex.push(output.length());

        if (propertyValue == null) {
            propertyValue = new Stack<>();
        }
        //获取自定义标签内部的属性值
        propertyValue.push(getProperty(xmlReader, "value"));
    }

    private void handlerEndSIZE(Editable output) {

        if (!propertyValue.isEmpty()) {
            try {
                int value = Integer.parseInt(propertyValue.pop());
                output.setSpan(new AbsoluteSizeSpan(CommUtils.dip2px(value)), startIndex.pop(), output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // =======================  文本大小设置 end ↑ =========================

    /**
     * 利用反射获取html标签的属性值
     */
    private String getProperty(XMLReader xmlReader, String property) {
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[]) dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer) lengthField.get(atts);

            for (int i = 0; i < len; i++) {
                // 这边的property换成你自己的属性名就可以了
                if (property.equals(data[i * 5 + 1])) {
                    return data[i * 5 + 4];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
