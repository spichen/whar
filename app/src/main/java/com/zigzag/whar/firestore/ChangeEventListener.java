package com.zigzag.whar.firestore;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.zigzag.whar.common.firebase.BaseChangeEventListener;

/**
 * Listener for changes to a {@link FirestoreArray}.
 */
public interface ChangeEventListener extends BaseChangeEventListener<DocumentSnapshot, FirebaseFirestoreException> {}
