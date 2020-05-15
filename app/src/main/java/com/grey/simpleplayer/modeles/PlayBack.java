package com.grey.simpleplayer.modeles;

import com.grey.simpleplayer.adapter.PlayerUtils;

public class PlayBack {
    public long mId;
    public long sourceId;
    public PlayerUtils.IdType mIdType;
    public int mCurrentPos;

    public PlayBack(long mId, long sourceId, PlayerUtils.IdType mIdType, int mCurrentPos) {
        this.mId = mId;
        this.sourceId = sourceId;
        this.mIdType = mIdType;
        this.mCurrentPos = mCurrentPos;
    }
}
