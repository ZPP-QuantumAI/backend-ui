package pl.mimuw.zpp.quantumai.backendui.model;

import java.util.List;

public enum Status {
    WAITING,
    SUCCESS,
    FAILED;

    public static Status statusFromMany(List<Status> statuses) {
        for (Status status : statuses) {
            switch (status) {
                case WAITING -> {
                    return WAITING;
                }
                case FAILED -> {
                    return FAILED;
                }
            }
        }
        return SUCCESS;
    }
}
