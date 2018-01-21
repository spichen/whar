package com.zigzag.whar.firestore;

import android.support.annotation.NonNull;


import com.google.firebase.firestore.DocumentSnapshot;
import com.zigzag.whar.common.firebase.BaseCachingSnapshotParser;
import com.zigzag.whar.common.firebase.BaseSnapshotParser;

/**
 * Implementation of {@link BaseCachingSnapshotParser} for {@link DocumentSnapshot}.
 */
public class CachingSnapshotParser<T> extends BaseCachingSnapshotParser<DocumentSnapshot, T>
        implements SnapshotParser<T> {

    public CachingSnapshotParser(@NonNull BaseSnapshotParser<DocumentSnapshot, T> parser) {
        super(parser);
    }

    @NonNull
    @Override
    public String getId(@NonNull DocumentSnapshot snapshot) {
        return snapshot.getId();
    }
}
