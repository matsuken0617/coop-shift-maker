package com.example.coop.exception;

/**
 * COOPアプリ例外
 */
public class CoopException extends RuntimeException {

    public static final int NO_SUCH_Worker_EXISTS = 101;
    public static final int Worker_ALREADY_EXISTS = 102;

    public static final int NO_SUCH_WORK_SHIFT_EXISTS = 201;
    public static final int WORK_SHIFT_ALREADY_EXISTS = 202;

    public static final int NO_SUCH_KEY_SHIFT_EXISTS = 301;
    public static final int KEY_SHIFT_ALREADY_EXISTS = 302;

    int code;

    public CoopException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CoopException(int code, String message, Exception cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
