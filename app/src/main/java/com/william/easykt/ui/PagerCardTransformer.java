package com.william.easykt.ui;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

/**
 * @author WilliamYang
 * @date 2021/2/19 13:22
 * Class Comment：卡片转换
 */
public class PagerCardTransformer implements ViewPager.PageTransformer {
    final float SCALE_FACTOR = 0.27F;
    final float ALPHA_FACTOR = 0.5F;

    public PagerCardTransformer() {
    }

    public void transformPage(@NotNull View page, float position) {
        float scale = position < 0.0F ? SCALE_FACTOR * position + 1.0F : -SCALE_FACTOR * position + 1.0F;
        float alpha = position < 0.0F ? ALPHA_FACTOR * position + 1.0F : -ALPHA_FACTOR * position + 1.0F;
        if (position < 0.0F) {
            page.setPivotX((float) page.getWidth());
        } else {
            page.setPivotX(0.0F);
        }
        page.setPivotY((float) (page.getHeight() / 2));

        page.setScaleX(scale);
        page.setScaleY(scale);
        page.setAlpha(Math.abs(alpha));
    }
}

