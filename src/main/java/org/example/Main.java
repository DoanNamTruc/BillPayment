package org.example;

import org.example.controller.BillController;
import org.example.schedule.PaymentBillJob;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BillController controller = new BillController();
        PaymentBillJob.schedulePayment(controller);

        String demo = "SET_USER 0\n" +
                "LIST_BILL\n" +
                "ADD_BILL TELE 123 28/10/2020 VNN\n" +
                "ADD_BILL TELE 1 28/10/2020 VN4\n" +
                "ADD_BILL ELEC 3 28/10/2020 VNT\n" +
                "ADD_BILL ELEC 5 28/10/2020 VNPT\n" +
                "CASH_IN 10\n" +
                "LIST_BILL \n" +
                "PAY 2 1 3\n" +
                "LIST_BILL\n" +
                "LIST_PAYMENT\n" +
                "SCHEDULE 1 28/10/2020\n" +
                "LIST_PAYMENT\n" +
                "SEARCH_BILL_BY_PROVIDER VNPT\n" +
                "CASH_IN 1\n" +
                "ADD_BILL ELEC 500 28/10/2020 VNPT\n" +
                "CASH_IN 100\n" +
                "CASH_IN 100";

        for(String line : demo.split("\n")) {
            List<String> params = new ArrayList<>();
            System.out.println("\n" + line);
            for (String param : line.split(" ")) {
                String p = param.trim();
                if (!p.isEmpty()) {
                    params.add(p);
                }
            }
            controller.doBuz(params);
        }

        do {
            Scanner sc = new Scanner(System.in);
            List<String> params = new ArrayList<>();
            String input = sc.nextLine();
            for (String param : input.split(" ")) {
                String p = param.trim();
                if (!p.isEmpty()) {
                    params.add(p);
                }
            }
            controller.doBuz(params);
        } while (true);
    }
}