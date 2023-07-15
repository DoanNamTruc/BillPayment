package org.example.schedule;

import org.example.controller.BillController;

import java.util.ArrayList;
import java.util.List;

public class PaymentBillJob {

    public static void schedulePayment(BillController controller) {
        Thread one = new Thread() {
            public void run() {
                do {
                    try {
                        Thread.sleep(30000);
                        List<String> params = new ArrayList<>();
                        params.add("EXEC_SCHEDULE_PAYMENT");
                        controller.doBuz(params);

                    } catch (Exception e) {
                        System.out.println("Nope, it doesnt...again.");
                    }
                } while (true);
            }
        };
        one.start();
    }

}
