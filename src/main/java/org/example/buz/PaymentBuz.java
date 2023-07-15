package org.example.buz;

import org.example.database.Database;
import org.example.entity.Payment;
import org.example.queue.SchedulePaymentMsg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaymentBuz {

    private Database database = Database.getInstance();


    public synchronized Payment addPayment(Long userId, Payment payment) {
        Payment paymentDB = database.getPayment().get(payment.getBillId());
        if (paymentDB == null) {
            payment.setPaymentNo(Long.valueOf(database.getPaymentNo()));
            ArrayList<Long> userPayment = database.getUserPayment().get(userId);
            if (userPayment == null) userPayment = new ArrayList<>();
            userPayment.add(payment.getBillId());
            database.getUserPayment().put(userId, userPayment);
            database.getPayment().put(payment.getBillId(), payment);
            paymentDB = payment;
        }
        return paymentDB;
    }

    public List getUserPayment(Long userId) {
        List<Long> list = database.getUserPayment().get(userId);
        if (list == null || list.isEmpty()) {
            return new ArrayList();
        }
        List<Payment> results = new ArrayList<>();
        for (Long id : list) {
            results.add(database.getPayment().get(id));
        }
        Collections.sort(results, (s1, s2) ->
                Long.compare(s2.getPaymentDate(), s1.getPaymentDate()));
        return results;
    }

    public void pushSchedulePaymentList(SchedulePaymentMsg job) {
        database.getSchedulePaymentList().add(job);
    }

    public void delSchedulePayment(Long billNo) {
        database.getSchedulePaymentList().remove(billNo);
    }

    public List getSchedulePaymentList() {
        return database.getSchedulePaymentList().stream().toList();
    }
}
