package com.ga2230.networking;

public interface OnConnect {
    void onConnect(Dialog dialog);

    void onDisconnect(Dialog dialog);

    void onPipe(Dialog dialog);
}
