package com.cometchatworkspace.components.shared.primaryComponents.soundManager;

import com.cometchatworkspace.R;

public enum Sound {
    incomingCall(R.raw.incoming_call),
    outgoingCall(R.raw.outgoing_call),
    incomingMessage(R.raw.incoming_message),
    outgoingMessage(R.raw.outgoing_message),
    incomingMessageFromOther(R.raw.incoming_message_other);

    private final int rawFile;
    Sound(int rawFile) {
        this.rawFile = rawFile;
    }

    public int getRawFile() {
        return rawFile;
    }
}