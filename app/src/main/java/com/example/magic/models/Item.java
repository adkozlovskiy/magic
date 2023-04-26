package com.example.magic.models;

import com.example.magic.R;

public enum Item {
    SHOPPING_LIST(R.drawable.apple), PILLS(R.drawable.pills), GOLDEN_APPLE(R.drawable.apple), PRODUCTS(R.drawable.apple), SNUP(R.drawable.snup), EGG(R.drawable.egg), TANTUM_VERDE(R.drawable.verde);

    private int resId;

    Item(int resId) {
        this.resId = resId;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}