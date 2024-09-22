package com.example.quanlynhansu.Items;

public class Counter {
    private int count;

    public Counter() {
        // Bắt buộc cho Firebase
    }

    public Counter(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
