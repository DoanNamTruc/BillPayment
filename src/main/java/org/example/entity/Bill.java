package org.example.entity;

import java.math.BigDecimal;

public class Bill {
    public static enum State {
        NOT_PAID,
        PAID
    }

    private Long billNo;
    private BigDecimal amount;

    private String type;

    private Long dueDate;
    private Long createdDate;
    private State state;
    private String provider;

    public Bill( String type, BigDecimal amount, Long dueDate, Long createdDate, State state, String provider) {
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.createdDate = createdDate;
        this.state = state;
        this.provider = provider;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getBillNo() {
        return billNo;
    }

    public void setBillNo(Long billNo) {
        this.billNo = billNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
