package com.bio.sequencing.corelibs.U2Core.models;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Document {

    private final UUID id;

    private Path url;

    private final DocumentFormat format;

    private final DocumentInfo info;

    private final List<DocumentObject> objects =
            new ArrayList<>();

    private final ReentrantReadWriteLock lock =
            new ReentrantReadWriteLock();

    private volatile DocumentState state =
            DocumentState.NEW;

    private volatile boolean modified;

    private volatile boolean locked;

    public Document(
            Path url,
            DocumentFormat format,
            DocumentInfo info) {

        this.id = UUID.randomUUID();
        this.url = Objects.requireNonNull(url);
        this.format = Objects.requireNonNull(format);
        this.info = Objects.requireNonNull(info);
    }

    public UUID getId() {
        return id;
    }

    public Path getUrl() {
        return url;
    }

    public DocumentFormat getFormat() {
        return format;
    }

    public DocumentInfo getInfo() {
        return info;
    }

    public DocumentState getState() {
        return state;
    }

    public boolean isModified() {
        return modified;
    }

    public boolean isLocked() {
        return locked;
    }

    public List<DocumentObject> getObjects() {

        lock.readLock().lock();

        try {
            return List.copyOf(objects);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void addObject(DocumentObject object) {

        lock.writeLock().lock();

        try {
            ensureEditable();

            objects.add(Objects.requireNonNull(object));

            markModified();

        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean removeObject(UUID objectId) {

        lock.writeLock().lock();

        try {
            ensureEditable();

            boolean removed = objects.removeIf(
                    object -> object.getId().equals(objectId)
            );

            if (removed) {
                markModified();
            }

            return removed;

        } finally {
            lock.writeLock().unlock();
        }
    }

    public DocumentObject getObject(UUID objectId) {

        lock.readLock().lock();

        try {
            return objects.stream()
                    .filter(object ->
                            object.getId().equals(objectId))
                    .findFirst()
                    .orElse(null);

        } finally {
            lock.readLock().unlock();
        }
    }

    public List<DocumentObject> getObjectsByType(
            ObjectType type) {

        lock.readLock().lock();

        try {
            return objects.stream()
                    .filter(object ->
                            object.getType() == type)
                    .toList();

        } finally {
            lock.readLock().unlock();
        }
    }

    public void markModified() {
        modified = true;
        state = DocumentState.MODIFIED;
    }

    public void lockDocument() {
        locked = true;
        state = DocumentState.LOCKED;
    }

    public void unlockDocument() {
        locked = false;
        state = modified
                ? DocumentState.MODIFIED
                : DocumentState.LOADED;
    }

    public void save() throws IOException {

        lock.writeLock().lock();

        try {
            ensureEditable();

            if (!format.supportsWriting()) {
                throw new UnsupportedOperationException(
                        "Document format does not support writing"
                );
            }

            state = DocumentState.SAVING;

            format.save(url, List.copyOf(objects));

            modified = false;
            state = DocumentState.LOADED;

        } catch (IOException | RuntimeException e) {

            state = DocumentState.ERROR;
            throw e;

        } finally {
            lock.writeLock().unlock();
        }
    }

    public void saveAs(Path newPath) throws IOException {

        lock.writeLock().lock();

        try {
            ensureEditable();

            if (!format.supportsWriting()) {
                throw new UnsupportedOperationException(
                        "Document format does not support writing"
                );
            }

            state = DocumentState.SAVING;

            format.save(newPath, List.copyOf(objects));

            url = newPath;
            modified = false;
            state = DocumentState.LOADED;

        } catch (IOException | RuntimeException e) {

            state = DocumentState.ERROR;
            throw e;

        } finally {
            lock.writeLock().unlock();
        }
    }

    private void ensureEditable() {

        if (locked) {
            throw new IllegalStateException(
                    "Document is locked"
            );
        }

        if (state == DocumentState.CLOSED) {
            throw new IllegalStateException(
                    "Document is closed"
            );
        }
    }

    public void close() {

        lock.writeLock().lock();

        try {
            objects.clear();
            state = DocumentState.CLOSED;
        } finally {
            lock.writeLock().unlock();
        }
    }
}