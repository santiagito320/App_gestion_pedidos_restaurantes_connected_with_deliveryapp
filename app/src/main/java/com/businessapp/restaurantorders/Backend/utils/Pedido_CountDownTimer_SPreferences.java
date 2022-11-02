package com.businessapp.restaurantorders.Backend.utils;

public class Pedido_CountDownTimer_SPreferences {

    public static String SharedPreferenceJsonTimersName = "PedidosTimers";
    private String numeroPedido;

   // private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = 10000;
    private long mEndTime = -1;
    private long remainingTimeInMillis = 10000;


    public Pedido_CountDownTimer_SPreferences() {
    }

    public Pedido_CountDownTimer_SPreferences(String numeroPedido, boolean mTimerRunning, long mTimeLeftInMillis, long mEndTime, long remainingTimeInMillis) {
        this.numeroPedido = numeroPedido;
        this.mTimerRunning = mTimerRunning;
        this.mTimeLeftInMillis = mTimeLeftInMillis;
        this.mEndTime = mEndTime;
        this.remainingTimeInMillis = remainingTimeInMillis;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public boolean ismTimerRunning() {
        return mTimerRunning;
    }

    public void setmTimerRunning(boolean mTimerRunning) {
        this.mTimerRunning = mTimerRunning;
    }

    public long getmTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }

    public void setmTimeLeftInMillis(long mTimeLeftInMillis) {
        this.mTimeLeftInMillis = mTimeLeftInMillis;
    }

    public long getmEndTime() {
        return mEndTime;
    }

    public void setmEndTime(long mEndTime) {
        this.mEndTime = mEndTime;
    }

    public long getRemainingTimeInMillis() {
        return remainingTimeInMillis;
    }

    public void setRemainingTimeInMillis(long remainingTimeInMillis) {
        this.remainingTimeInMillis = remainingTimeInMillis;
    }


}
