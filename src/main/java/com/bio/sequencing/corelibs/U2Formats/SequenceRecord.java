package com.bio.sequencing.corelibs.U2Formats;

public static final class SequenceRecord {

    private final String id;
    private final String sequence;
    private final String quality;
    private final SequenceFormat format;

    public SequenceRecord(
            String id,
            String sequence,
            String quality,
            SequenceFormat format) {

        this.id = id;
        this.sequence = sequence;
        this.quality = quality;
        this.format = format;
    }

    public String getId() {
        return id;
    }

    public String getSequence() {
        return sequence;
    }

    public String getQuality() {
        return quality;
    }

    public SequenceFormat getFormat() {
        return format;
    }

    public boolean hasQuality() {
        return quality != null;
    }

    public int length() {
        return sequence.length();
    }

    @Override
    public String toString() {

        return "SequenceRecord{" +
                "id='" + id + '\'' +
                ", length=" + sequence.length() +
                ", format=" + format +
                '}';
    }
}
