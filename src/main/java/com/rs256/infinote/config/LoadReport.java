package com.rs256.infinote.config;

public final class LoadReport {
    private final int rawTotal;
    private final int compiledOk;
    private final int invalidBlockId;
    private final int invalidSoundId;
    private final int invalidCategory;

    public LoadReport(int rawTotal, int compiledOk, int invalidBlockId, int invalidSoundId, int invalidCategory) {
        this.rawTotal = rawTotal;
        this.compiledOk = compiledOk;
        this.invalidBlockId = invalidBlockId;
        this.invalidSoundId = invalidSoundId;
        this.invalidCategory = invalidCategory;
    }

    public int skipped() {
        return invalidBlockId + invalidSoundId + invalidCategory;
    }

    public int rawTotal() {
        return rawTotal;
    }

    public int compiledOk() {
        return compiledOk;
    }

    public int invalidBlockId() {
        return invalidBlockId;
    }

    public int invalidSoundId() {
        return invalidSoundId;
    }

    public int invalidCategory() {
        return invalidCategory;
    }

}
