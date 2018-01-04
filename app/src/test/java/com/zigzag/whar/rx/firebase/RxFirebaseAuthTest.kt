package com.zigzag.whar.rx.firebase

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.ArgumentCaptor
import com.google.android.gms.tasks.OnCompleteListener
import org.mockito.MockitoAnnotations
import org.junit.Before
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import io.reactivex.internal.disposables.DisposableHelper.dispose
import io.reactivex.observers.TestObserver
import org.junit.Test


/**
 * Created by salah on 2/1/18.
 */
class RxFirebaseAuthTest {

    @Mock
    var mockFirebaseAuth: FirebaseAuth? = null

    @Mock
    var phoneAuthProvider : PhoneAuthProvider? = null

    @Mock
    var mockAuthCredential: AuthCredential? = null

    @Mock
    var mockAuthResult: AuthResult? = null

    @Mock
    var mockAuthResultTask: Task<AuthResult>? = null

    @Mock
    var mockSendPasswordResetEmailTask: Task<Void>? = null

    @Mock
    var mockFetchProvidersTask: Task<ProviderQueryResult>? = null

    @Mock
    var mockFirebaseUser: FirebaseUser? = null

    private var onComplete: ArgumentCaptor<OnCompleteListener<*>>? = null

    private var authStateChange: ArgumentCaptor<FirebaseAuth.AuthStateListener>? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        onComplete = ArgumentCaptor.forClass(OnCompleteListener::class.java)
        authStateChange = ArgumentCaptor.forClass(FirebaseAuth.AuthStateListener::class.java)
    }
/*

    @Test
    fun testAuthStateChanges() {
        val obs = TestObserver.create<Any>()

        phoneAuthProvider?.rxVerifyPhoneNumber(919895940989)
                ?.subscribe(obs)


        obs.assertNotComplete()
        obs.assertValueCount(1)

        obs.dispose()
    }*/

}