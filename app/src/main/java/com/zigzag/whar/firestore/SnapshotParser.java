package com.zigzag.whar.firestore;

import com.google.firebase.firestore.DocumentSnapshot;
import com.zigzag.whar.common.firebase.BaseSnapshotParser;

/**
 * Base interface for a {@link BaseSnapshotParser} for {@link DocumentSnapshot}.
 */
public interface SnapshotParser<T> extends BaseSnapshotParser<DocumentSnapshot, T> {}
