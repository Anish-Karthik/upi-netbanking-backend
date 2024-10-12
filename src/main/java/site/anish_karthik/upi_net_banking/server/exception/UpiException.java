package site.anish_karthik.upi_net_banking.server.exception;

import lombok.Getter;

@Getter
public class UpiException extends Exception {
    private final int statusCode;

    public UpiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}