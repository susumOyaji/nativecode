package com.example.nativecode;


import android.telecom.Call;
//https://developer.android.com/guide/topics/connectivity/telecom?hl=ja#replacePhoneApp
//https://developer.android.com/guide/topics/connectivity/telecom/selfManaged?hl=ja
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

public final class OngoingCall {
    public  static final BehaviorSubject<Integer> state;
    private static final Call.Callback callback;
    private static Call call;

   
    public final BehaviorSubject getState() {
        return state;
    }


   
    public final void setCall(Call value) {
        if (call != null) {
            //コールバックの登録を解除しますCall。    
            call.unregisterCallback(callback);
        }

        if (value != null) {
            //コールバックを登録しますCall。
            value.registerCallback(callback);
            state.onNext(value.getState());
        }
        
        call = value;
    }

    // Anwser the call
    //STATE_RINGING Callに答えるように指示します。
    public static void answer() {
        call.answer(0);
    }

    // Hangup the call
    //Callに切断するように指示します。
    public static void hangup() {
       call.disconnect();
    }

    static {
        // Create a BehaviorSubject to subscribe
        state = BehaviorSubject.create();
        callback = new Call.Callback() {
            public void onStateChanged(Call call, int newState) {
                Timber.d(call.toString());
                // Change call state
                OngoingCall.state.onNext(newState);
            }
        };
    }
}
