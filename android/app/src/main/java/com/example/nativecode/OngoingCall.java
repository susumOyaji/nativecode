//package customphonedialer.abror96.customphonedialer;
package com.example.nativecode;

import android.telecom.Call;
import android.telecom.VideoProfile;

import org.jetbrains.annotations.Nullable;

import io.reactivex.subjects.BehaviorSubject;


public final class OngoingCall {

    public static BehaviorSubject<Integer> state = BehaviorSubject.create();
    private static Call call;

   // public final BehaviorSubject getState() {
   //     return state;
    //}
    
    
    
    private Object callback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int newState) {
            super.onStateChanged(call, newState);
            state.onNext(newState);
        }
    };

    public final void setCall(@Nullable Call value) {
        if (call != null) {
            call.unregisterCallback((Call.Callback)callback);
        }

        if (value != null) {
            value.registerCallback((Call.Callback)callback);
            state.onNext(value.getState());
        }

        call = value;
    }

    public static void answer() {
        assert call != null;
        call.answer(VideoProfile.STATE_AUDIO_ONLY);
    }

    public static void hangup() {
        assert call != null;
        call.disconnect();
    }

}
