// MusicInter.aidl
package com.grey.simpleplayer;

// Declare any non-default types here with import statements

interface MusicInter {
        void open(in long[] list, int position, long sourceId, int type);
        void play();
        void stop();
        void pause();
        void next();
        void prev();
        boolean isPlaying();
        long getSongDuration();
        long getAudioId();
        int getCurrentPos();
        long[] getsaveIdList();
}
