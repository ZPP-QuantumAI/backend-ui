package pl.mimuw.zpp.quantumai.backendui.error;

import lombok.Getter;

public class PackageNotFoundException extends RuntimeException {
    @Getter private final String packageId;

    public PackageNotFoundException(String packageId) {
        super();
        this.packageId = packageId;
    }
}
