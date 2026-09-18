package com.rs256.infinote.config;

public final class LoadReport {
    private final int rawTotal;
    private final int compiledOk;
    private final int invalidBlockId;
    private final int invalidSoundId;
    private final int invalidCategory;
    private final int bypassTotal;
    private final int bypassOk;

    public LoadReport(int rawTotal, int compiledOk, int invalidBlockId, int invalidSoundId, int invalidCategory, int bypassTotal, int bypassOk) {
        this.rawTotal = rawTotal;
        this.compiledOk = compiledOk;
        this.invalidBlockId = invalidBlockId;
        this.invalidSoundId = invalidSoundId;
        this.invalidCategory = invalidCategory;
        this.bypassTotal = bypassTotal;
        this.bypassOk = bypassOk;
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

    public int bypassTotal() {
        return bypassTotal;
    }

    public int bypassOk() {
        return bypassOk;
    }

}
