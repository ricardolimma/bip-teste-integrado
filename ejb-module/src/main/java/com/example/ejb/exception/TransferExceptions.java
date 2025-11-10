package com.example.ejb.exception;

public final class TransferExceptions {

    private TransferExceptions() {}

    public static class TransferException extends RuntimeException {
        public TransferException() { super(); }
        public TransferException(String message) { super(message); }
        public TransferException(String message, Throwable cause) { super(message, cause); }
    }

    public static class AccountNotFoundException extends TransferException {
        public AccountNotFoundException(Long accountId) {
            super("Conta não encontrada: id=" + accountId);
        }
    }

    public static class InsufficientFundsException extends TransferException {
        public InsufficientFundsException(Long accountId, String message) {
            super("Saldo insuficiente na conta id=" + accountId + ": " + message);
        }
    }

    public static class ConcurrencyException extends TransferException {
        public ConcurrencyException(String message, Throwable cause) {
            super(message, cause);
        }
        public ConcurrencyException(String message) {
            super(message);
        }
    }
}
