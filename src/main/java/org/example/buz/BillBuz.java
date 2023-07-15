package org.example.buz;

import org.example.database.Database;
import org.example.entity.Bill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BillBuz {

    private Database database = Database.getInstance();

    public void addBillByUserId(Long userId, Bill bill) {
        bill.setBillNo(Long.valueOf(database.getBillNo()));
        ArrayList<Long> userBills = database.getUserBill().get(userId);
        if(userBills == null) {
            userBills = new ArrayList<>();
            database.getUserBill().put(userId, userBills);
        }
        userBills.add(bill.getBillNo());
        database.getBills().put(bill.getBillNo(), bill);
    }
    public List getUserBills(Long userId) {
        return getBillByUserId(userId);
    }

    public Bill getBill(Long billNo) {
        return database.getBills().get(billNo);
    }

    private List getBillByUserId(Long userId, Bill.State state, String provider) {
        List<Long> listBill = database.getUserBill().get(userId);
        if(listBill == null || listBill.isEmpty()) {
            return new ArrayList();
        }
        List<Bill> userBill = new ArrayList<>();
        for (Long id : listBill) {
            if(state != null && state.name() != database.getBills().get(id).getState().name()) {
                continue;
            }
            if (provider != null && !provider.equals(database.getBills().get(id).getProvider())) {
                continue;
            }
            userBill.add(database.getBills().get(id));
        }
        Collections.sort(userBill, (s1, s2) ->
                Long.compare(s2.getDueDate(), s1.getDueDate()));
        return userBill;
    }

    public List getUserBillsWithParams(Long userId, Bill.State state, String provider) {
        return getBillByUserId(userId, state, provider);
    }

    public List getBillByUserId(Long userId) {
        return getBillByUserId(userId, null, null);
    }
}
