package com.bio.sequencing.plugins.dbi_bam;

public record BAMRecord(
        String readName,
        String referenceName,
        int alignmentStart,
        int alignmentEnd,
        String cigar,
        String sequence,
        String baseQuality,
        int mappingQuality,
        int flags
) {

    public boolean isPaired() {
        return (flags & 0x1) != 0;
    }

    public boolean isProperPair() {
        return (flags & 0x2) != 0;
    }

    public boolean isUnmapped() {
        return (flags & 0x4) != 0;
    }

    public boolean isReverseStrand() {
        return (flags & 0x10) != 0;
    }

    public boolean isSecondary() {
        return (flags & 0x100) != 0;
    }

    public boolean isDuplicate() {
        return (flags & 0x400) != 0;
    }

    public boolean isSupplementary() {
        return (flags & 0x800) != 0;
    }
}
