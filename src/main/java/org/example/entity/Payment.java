package org.example.entity;

import java.math.BigDecimal;

public class Payment {

    public static enum State {
        PROCESSED,
        PENDING
    }
    private Long paymentNo;
    private BigDecimal amount;
    private Long paymentDate;
    private State state;
    private Long billId;


    public Payment(BigDecimal amount, Long paymentDate, State state, Long billId) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.state = state;
        this.billId = billId;
    }

    public Long getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(Long paymentNo) {
        this.paymentNo = paymentNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Long paymentDate) {
        this.paymentDate = paymentDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }
}
