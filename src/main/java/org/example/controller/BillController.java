package org.example.controller;



import org.example.buz.BalanceBuz;
import org.example.buz.BillBuz;
import org.example.buz.PaymentBuz;
import org.example.entity.Bill;
import org.example.entity.Payment;
import org.example.exception.BalanceNotEnough;
import org.example.exception.BillNotFound;
import org.example.queue.SchedulePaymentMsg;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BillController {

    private BalanceBuz balanceBuz;
    private BillBuz billBuz;

    private PaymentBuz paymentBuz;
    public BillController() {
        this.balanceBuz = new BalanceBuz();
        this.billBuz = new BillBuz();
        this.paymentBuz = new PaymentBuz();
    }

    public void doBuz(List<String> params) {
        Long userId = balanceBuz.getUser();
        try {
            switch (params.get(0)) {
                case "CASH_IN":
                    cashIn(params, userId);
                    break;
                case "LIST_BILL":
                    listBill(userId);
                    break;
                case "ADD_BILL":
                    addBill(params, userId);
                    break;
                case "PAY":
                    payBill(params, userId);
                    break;
                case "DUE_DATE":
                    listBillDueDate(userId);
                    break;
                case "SCHEDULE":
                    schedulePayment(params, userId);
                    break;
                case "LIST_PAYMENT":
                    listPayment(userId);
                    break;
                case "SEARCH_BILL_BY_PROVIDER":
                    listBillsByProvider(userId, params);
                    break;
                case "EXEC_SCHEDULE_PAYMENT":
                    execSchedulePayment();
                    break;
                case "SET_USER":
                    switchUser(params, userId);
                    break;
                default:
                    System.out.println("Feature not exist follow exist feature bellow :");
                    System.out.println("SET_USER 0\n" +
                            "CASH_IN 1000\n" +
                            "ADD_BILL TELE 123 28/10/2020 VNN\n" +
                            "LIST_BILL \n" +
                            "PAY 2 1 3\n" +
                            "LIST_PAYMENT\n" +
                            "SCHEDULE 1 28/10/2020\n" +
                            "SEARCH_BILL_BY_PROVIDER VNPT");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    private void switchUser(List<String> params, Long userId) {
        try {
            Long newUser = Long.parseLong(params.get(1));
            balanceBuz.setUser(newUser);
            System.out.println("switch user from " + userId + " to " + newUser);
        } catch (Exception e) {
            System.out.println("param invalid");
        }
    }
    private void execSchedulePayment() {
        try {
            Long currentTime = System.currentTimeMillis();
            List<SchedulePaymentMsg> schedulePaymentList = paymentBuz.getSchedulePaymentList().stream().toList();
            for (SchedulePaymentMsg msg  : schedulePaymentList) {
                if (msg.getPaymentDate() < currentTime) {
                    Bill bill = billBuz.getBill(msg.getBillNo());
                    if (tryPayment(bill, msg.getUserId())) {
                        paymentBuz.delSchedulePayment(msg.getBillNo());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private void schedulePayment(List<String> params, Long userId) {
        try {
            Long billNo = Long.parseLong(params.get(1));
            Bill bill = billBuz.getBill(billNo);
            if (bill == null) {
                System.out.println("BillNo " + billNo + " not exist!!");
                return;
            }
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date paymentDate = df.parse(params.get(2));

            Payment payment = new Payment(bill.getAmount(), paymentDate.getTime(), Payment.State.PENDING, bill.getBillNo());
            Payment paymentDb = paymentBuz.addPayment(userId, payment);
            if (paymentDb.getState().name().equals(Payment.State.PENDING.name())) {
                //Long billNo, Long userId, Long paymentDate
                paymentBuz.pushSchedulePaymentList(new SchedulePaymentMsg(paymentDb.getBillId(), userId, paymentDate.getTime()));
            }
        } catch (Exception e) {
            System.out.println("param invalid");
        }
    }

    private void listPayment(Long userId) {
        System.out.println("No. Amount Payment Date State Bill Id");
        List<Payment> list = paymentBuz.getUserPayment(userId);
        printPayments(list);
    }

    private void listBillDueDate(Long userId) {
        System.out.println("Bill No.    Type     Amount    Due Date State PROVIDER");
        List<Bill> bills = new BillBuz().getUserBillsWithParams(userId, Bill.State.NOT_PAID, null);
        printBills(bills);
    }

    private void listBillsByProvider(Long userId, List<String> params) {
        System.out.println("Bill No.    Type     Amount    Due Date State PROVIDER");
        List<Bill> bills = new BillBuz().getUserBillsWithParams(userId, null, params.get(1));
        printBills(bills);
    }

    private void addBill(List<String> params, Long userId) {
        Bill newBill;
        try {
            String type = params.get(1);
            BigDecimal amount = new BigDecimal(params.get(2));
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date dueDate = df.parse(params.get(3));
            String provider = params.get(4);
            newBill = new Bill(type, amount, dueDate.getTime(), System.currentTimeMillis(), Bill.State.NOT_PAID, provider);
        } catch (Exception e) {
            System.out.println("param invalid");
            return;
        }
        new BillBuz().addBillByUserId(userId, newBill);
    }

    private void cashIn(List<String> params, Long userId) {
        try {
            BigDecimal bigDecimal = new BigDecimal(params.get(1));
            System.out.println( "Your available balance: " + new BalanceBuz().addFund(userId, bigDecimal));
        } catch (Exception e) {
            System.out.println("param invalid");
        }
    }

    private void listBill(Long userId) {
        System.out.println("Bill No.    Type     Amount    Due Date State PROVIDER");
        List<Bill> bills = new BillBuz().getUserBills(userId);
        printBills(bills);
    }

    private void payBill(List<String> params, Long userId) throws BillNotFound, BalanceNotEnough {
        List<Bill> billsNeedToPay = new ArrayList<>();
        for (String param : params.subList(1, params.size())) {
            try {
                Long billNo = Long.parseLong(param);
                Bill bill = billBuz.getBill(billNo);
                if(bill == null) {
                    System.out.println("BillNo " + param + " not exist!!");
                } else {
                    billsNeedToPay.add(bill);
                }
            } catch (NumberFormatException nfe) {
                System.out.println(param + " not BillNo!!");
            }
        }
        if(billsNeedToPay.isEmpty()) {
            return;
        }

        Collections.sort(billsNeedToPay, (s1, s2) ->
                Long.compare(s2.getDueDate(), s1.getDueDate()));

        for (Bill billPay : billsNeedToPay) {
            tryPayment(billPay, userId);
        }
    }

    private synchronized boolean tryPayment(Bill billPay, Long userId) {
        try {
            if (billPay.getState().name().equals(Bill.State.NOT_PAID.name())) {
                balanceBuz.subtractFund(userId, billPay.getAmount());
                billPay.setState(Bill.State.PAID);
                Payment payment = new Payment(billPay.getAmount(), System.currentTimeMillis(), Payment.State.PROCESSED, billPay.getBillNo());
                Payment paymentDb = paymentBuz.addPayment(userId, payment);
                paymentDb.setState(Payment.State.PROCESSED);
                System.out.println(billPay.getBillNo() + " PAID !!!");

            }
        } catch (BalanceNotEnough e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private void printBills(List<Bill> list) {
        for (Bill bill : list) {
            long tmp = bill.getDueDate();
            Date d = new Date(tmp);
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            String dateText = df2.format(d);
            System.out.println(bill.getBillNo() + "          " + bill.getType() + "   " + bill.getAmount() +  "   " + dateText
                    + "   " + bill.getState().name() + "    "  +bill.getProvider());
        }
    }

    private void printPayments(List<Payment> list) {
        for (Payment entity : list) {
            long tmp = entity.getPaymentDate();
            Date d = new Date(tmp);
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            String dateText = df2.format(d);
            System.out.println(entity.getPaymentNo() + "          " + entity.getAmount() + "   " + dateText
                    + "   " + entity.getState().name() + "    "  +entity.getBillId());
        }
    }
}
