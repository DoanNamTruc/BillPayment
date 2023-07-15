package org.example.queue;

public class SchedulePaymentMsg {

    private Long billNo;

    private Long userId;

    private Long paymentDate;

    public SchedulePaymentMsg(Long billNo, Long userId, Long paymentDate) {
        this.billNo = billNo;
        this.userId = userId;
        this.paymentDate = paymentDate;
    }

    public Long getBillNo() {
        return billNo;
    }

    public void setBillNo(Long billNo) {
        this.billNo = billNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Long paymentDate) {
        this.paymentDate = paymentDate;
    }
}
