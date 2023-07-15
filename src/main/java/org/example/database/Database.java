package org.example.database;

import org.example.entity.Bill;
import org.example.entity.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

public class Database {

    private Long user = 0L; //default user
    private HashMap wallets = new HashMap<>();
    private static AtomicInteger billNo = new AtomicInteger(0);

    private static AtomicInteger paymentNo = new AtomicInteger(0);
    private HashMap<Long, Bill> bills = new HashMap<>();
    private HashMap<Long, ArrayList<Long>> userBill = new HashMap<>();

    private HashMap<Long, Payment> payment = new HashMap<>();

    private HashSet schedulePaymentList = new HashSet();

    private HashMap<Long, ArrayList<Long>> userPayment = new HashMap<>();
    private Database() {}


    private static final Database instance = new Database();

    public HashSet getSchedulePaymentList() {
        return schedulePaymentList;
    }

    public static Database getInstance(){
        return instance;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getUser() {
        return user;
    }

    public HashMap<Long, ArrayList<Long>> getUserBill() {
        return userBill;
    }

    public HashMap<Long, Payment> getPayment() {
        return payment;
    }

    public HashMap<Long, ArrayList<Long>> getUserPayment() {
        return userPayment;
    }

    public static Integer getBillNo(){
        return billNo.incrementAndGet();
    }

    public static Integer getPaymentNo(){
        return paymentNo.incrementAndGet();
    }

    public HashMap getWallets() {
        return wallets;
    }

    public HashMap<Long, Bill> getBills() {
        return bills;
    }
}
