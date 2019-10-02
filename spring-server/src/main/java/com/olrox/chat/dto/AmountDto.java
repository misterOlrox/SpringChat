package com.olrox.chat.dto;

public class AmountDto {
    private String info;
    private Long amount;

    public AmountDto(Long amount, String info) {
        this.amount = amount;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
