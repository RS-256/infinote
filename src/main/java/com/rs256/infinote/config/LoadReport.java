package com.rs256.infinote.config;

public record LoadReport(
        int rawTotal,
        int compiledOk,
        int invalidBlockId,
        int invalidSoundId,
        int invalidCategory
) {
    public int skipped() {
        return invalidBlockId + invalidSoundId + invalidCategory;
    }
}
