package com.example.magic.models;

import com.example.magic.R;

public enum Item {
    SHOPPING_LIST(R.drawable.apple), GOLDEN_APPLE(R.drawable.apple), PRODUCTS(R.drawable.apple), SNUP(R.drawable.apple), EGG(R.drawable.apple), TANTUM_VERDE(R.drawable.apple);

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