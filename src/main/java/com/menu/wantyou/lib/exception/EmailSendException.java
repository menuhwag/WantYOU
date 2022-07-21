package com.menu.wantyou.lib.exception;

public class EmailSendException extends RuntimeException{
    public EmailSendException() {
        super();
    }
    public EmailSendException(String message) {
        super(message);
    }
}
