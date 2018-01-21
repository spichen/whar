package com.zigzag.whar.firestore;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.zigzag.whar.common.firebase.BaseObservableSnapshotArray;

/**
 * Subclass of {@link BaseObservableSnapshotArray} for Firestore data.
 */
public abstract class ObservableSnapshotArray<T>
        extends BaseObservableSnapshotArray<DocumentSnapshot, FirebaseFirestoreException, ChangeEventListener, T> {
    /**
     * @see BaseObservableSnapshotArray#BaseObservableSnapshotArray(BaseCachingSnapshotParser)
     */
    public ObservableSnapshotArray(@NonNull SnapshotParser<T> parser) {
        super(new CachingSnapshotParser<>(parser));
    }
}
