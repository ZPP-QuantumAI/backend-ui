package pl.mimuw.zpp.quantumai.backendui.model;

import java.util.List;

public enum Status {
    WAITING,
    SUCCESS,
    FAILED;

    public static Status statusFromMany(List<Status> statuses) {
        if (statuses.contains(WAITING)) return WAITING;
        if (statuses.contains(FAILED)) return FAILED;
        return SUCCESS;
    }
}
