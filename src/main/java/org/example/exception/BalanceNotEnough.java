package org.example.exception;

public class BalanceNotEnough extends Exception
{
    public BalanceNotEnough()
    {
        super("Sorry! Not enough fund to proceed with payment.\n");
    }
}