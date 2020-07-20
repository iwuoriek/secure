package com.example.secure.dto;

public class FundsTransfer {
    private long accountToTransferTo;
    private long accountToTransferFrom;
    private double funds;

    public FundsTransfer() {
    }

    public FundsTransfer(long accountToTransferTo, double funds) {
        this.accountToTransferTo = accountToTransferTo;
        this.funds = funds;
    }

    public long getAccountToTransferTo() {
        return accountToTransferTo;
    }

    public void setAccountToTransferTo(long accountToTransferTo) {
        this.accountToTransferTo = accountToTransferTo;
    }

    public long getAccountToTransferFrom() {
        return accountToTransferFrom;
    }

    public void setAccountToTransferFrom(long accountToTransferFrom) {
        this.accountToTransferFrom = accountToTransferFrom;
    }

    public double getFunds() {
        return funds;
    }

    public void setFunds(double funds) {
        this.funds = funds;
    }
}
