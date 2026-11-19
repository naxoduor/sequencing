package com.bio.sequencing.corelibs.U2Core.models;


import java.util.UUID;

public interface DocumentObject {

    UUID getId();

    String getName();

    ObjectType getType();

    boolean isModified();
}

