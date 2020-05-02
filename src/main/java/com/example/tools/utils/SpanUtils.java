package com.example.tools.utils;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.EmbossMaskFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

/**
 * crate by jerry
 * 富文本字符串设置工具类
 */
public class SpanUtils {

    private static SpanUtils spanUtils;
    private Builder builder;

    public static synchronized SpanUtils getInstance(String text) {
        if (null == spanUtils) {
            spanUtils = new SpanUtils();
        }
        spanUtils.setText(text);
        return spanUtils;
    }

    /**
     * 设置文本并创建SpannableString对象
     */
    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (null == builder || (null != builder
                && !builder.spannable.equals(text))) {
            builder = new Builder(text);
        }
    }

    /**
     * 设置文本的部分文字前景色，也就是对文字上色，
     *
     * @param BaseSpan
     * @param colorId  颜色的ID值
     *                 颜色设置为GREEN，start为4，end为7，应该是“陈奕迅”三个字显示为绿色
     */
    public SpanUtils setForegroundColorSpan(BaseSpan baseSpan, int colorId) {
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(colorId);
        builder.spannable.setSpan(foregroundColorSpan, baseSpan.getStart()
                , baseSpan.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文本的部分文字背景颜色，
     */
    public SpanUtils setBackgroudColorSpan(BaseSpan baseSpan, int colorId) {
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(colorId);
        builder.spannable.setSpan(backgroundColorSpan, baseSpan.getStart()
                , baseSpan.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文本的部分文字的点击事件
     */
    public SpanUtils setClickableSpan(BaseSpan baseSpan, TextView mTextView
            , OnTextClickedListener onTextClickedListener) {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (null != onTextClickedListener) {
                    onTextClickedListener.onTextClicked(widget);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        builder.spannable.setSpan(clickableSpan, baseSpan.getStart(), baseSpan.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        return this;
    }

    /**
     * EmbossMaskFilter实现浮雕效果
     * EmbossMaskFilter(float[] direction, float ambient, float specular, float blurRadius)
     * direction：float数组，定义长度为3的数组标量[x,y,z]，来指定光源的方向
     * ambient：环境光亮度，0~1
     * specular：镜面反射系数
     * blurRadius：模糊半径，必须>0
     * new EmbossMaskFilter(new float[]{10, 10, 10}, 0.5f, 1, 1)
     */
    public SpanUtils setEmbossMaskFilter(BaseSpan baseSpan, float[] direction, float ambient, float specular, float blurRadius) {
        MaskFilterSpan embossMaskFilterSpan =
                new MaskFilterSpan(new EmbossMaskFilter(direction, ambient, specular, blurRadius));
        builder.spannable.setSpan(embossMaskFilterSpan, baseSpan.getStart(), baseSpan.getEnd()
                , Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return this;
    }

    /**
     * BlurMaskFilter实现模糊效果
     * radius：模糊半径
     * style：有四个参数可选
     * BlurMaskFilter.Blur.NORMAL：内外模糊
     * BlurMaskFilter.Blur.OUTER：外部模糊
     * BlurMaskFilter.Blur.INNER：内部模糊
     * BlurMaskFilter.Blur.SOLID：内部加粗，外部模糊
     */
    public SpanUtils setBlurMaskFilter(BaseSpan baseSpan, int radius) {
        MaskFilterSpan blurMaskFilterSpan
                = new MaskFilterSpan(new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL));
        builder.spannable.setSpan(blurMaskFilterSpan, baseSpan.getStart(), baseSpan.getEnd()
                , Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体的相对大小，这里设置为TextView大小的1.5倍，看图
     */
    public SpanUtils setRelativeSizeSpan(BaseSpan baseSpan, float proportion) {
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(proportion);
        builder.spannable.setSpan(relativeSizeSpan, baseSpan.getStart(), baseSpan.getEnd()
                , Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体的相绝对大小，40表示文字大小，true表示单位为dip，若为false
     */
    public SpanUtils setAbsoluteSizeSpan(BaseSpan baseSpan, int size, boolean isDip) {
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size, isDip);
        builder.spannable.setSpan(absoluteSizeSpan, baseSpan.getStart()
                , baseSpan.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体x轴缩放，1.5表示x轴放大为1.5倍，效果如图
     */
    public SpanUtils setScaleXSpan(BaseSpan baseSpan, float proportion) {
        ScaleXSpan scaleXSpan = new ScaleXSpan(proportion);
        builder.spannable.setSpan(scaleXSpan, baseSpan.getStart()
                , baseSpan.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文字样式，如斜体、粗体
     * Typeface.BOLD
     * Typeface.ITALIC
     * Typeface.BOLD_ITALIC
     * Typeface.NORMAL
     * Typeface.STYLE_MASK
     */
    public SpanUtils setStyleSpan(SpanModel model) {
        StyleSpan boldSpan = new StyleSpan(model.getTextTypeface());
        builder.spannable.setSpan(boldSpan, model.getStart()
                , model.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文字字体类型，如monospace、serif和sans-serif等等
     */
    public SpanUtils setTypefaceSpan(BaseSpan baseSpan, String family) {
        TypefaceSpan monospace = new TypefaceSpan(family);
        builder.spannable.setSpan(monospace, baseSpan.getStart()
                , baseSpan.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文字外貌，通过style资源设置，这里使用系统的style资源
     * android.R.style.TextAppearance_Material
     */
    public SpanUtils setTextAppearanceSpan(BaseSpan baseSpan, Context mContext, int styleId) {
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(mContext, styleId);
        builder.spannable.setSpan(textAppearanceSpan, baseSpan.getStart()
                , baseSpan.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文字下划线，强调突出文字时可以使用该span
     */
    public SpanUtils setUnderlineSpan(BaseSpan baseSpan) {
        UnderlineSpan underlineSpan = new UnderlineSpan();
        builder.spannable.setSpan(underlineSpan, baseSpan.getStart()
                , baseSpan.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文字删除线
     */
    public SpanUtils setStrikethroughSpan(BaseSpan baseSpan) {
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        builder.spannable.setSpan(strikethroughSpan, baseSpan.getStart()
                , baseSpan.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 返回设置好的SpannableString对象
     */
    public SpannableString toBuild() {
        if (null == builder) {
            throw new NullPointerException("Uninitialized SpannableString");
        }
        return builder.spannable;
    }

    private static class Builder {
        public SpannableString spannable;

        public Builder(String text) {
            this.spannable = new SpannableString(text);
        }
    }

    public static class SpanModel extends BaseSpan {

        private int start;
        private int end;
        private int textTypeface;

        public SpanModel(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public SpanModel(int start, int end, int textTypeface) {
            this.start = start;
            this.end = end;
            this.textTypeface = textTypeface;
        }

        @Override
        public int getStart() {
            return start;
        }

        @Override
        public int getEnd() {
            return end;
        }

        public int getTextTypeface() {
            return textTypeface;
        }
    }

    private abstract static class BaseSpan {

        public abstract int getStart();

        public abstract int getEnd();

    }

    public interface OnTextClickedListener {
        void onTextClicked(View view);
    }
}
