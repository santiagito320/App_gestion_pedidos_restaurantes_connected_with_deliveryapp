package com.businessapp.restaurantorders.Backend.utils;

import android.os.CountDownTimer;

public class PedidoCountDownTimer {
    private String numeroPedido;
    private CountDownTimer countDownTimer;

    public PedidoCountDownTimer() {
    }

    public PedidoCountDownTimer(String numeroPedido, CountDownTimer countDownTimer) {
        this.numeroPedido = numeroPedido;
        this.countDownTimer = countDownTimer;
    }


    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }
}
