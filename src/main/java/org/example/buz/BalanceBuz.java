package org.example.buz;

import org.example.database.Database;
import org.example.exception.BalanceNotEnough;

import java.math.BigDecimal;
import java.util.HashMap;

public class BalanceBuz {

    private Database database = Database.getInstance();

    public void setUser(Long user) {
        database.setUser(user);
    }

    public Long getUser() {
        return database.getUser();
    }

    public BigDecimal addFund(Long userId, BigDecimal amount) {
        HashMap<Long, BigDecimal> wallets = database.getWallets();
        BigDecimal resAmount = wallets.get(userId);
        if (resAmount == null) {
            resAmount = BigDecimal.ZERO;
        }
        BigDecimal newAmount = resAmount.add(amount);
        database.getWallets().put(userId, newAmount);
        return newAmount;
    }

    public void subtractFund(Long userId, BigDecimal amount) throws BalanceNotEnough {
        HashMap<Long, BigDecimal> wallets = database.getWallets();
        BigDecimal resAmount = wallets.get(userId);
        if (resAmount == null) {
            resAmount = BigDecimal.ZERO;
        }

        if (resAmount.compareTo(amount) >= 0) {
            database.getWallets().put(userId, resAmount.subtract(amount));
        } else {
            throw new BalanceNotEnough();
        }
    }
}
