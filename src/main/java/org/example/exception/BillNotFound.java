package org.example.exception;

public class BillNotFound extends Exception
{
    public BillNotFound()
    {
        super("Sorry! BillNo not exist.\n");
    }
}