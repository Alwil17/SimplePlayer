package com.grey.simpleplayer.adapter;

public class PlayerUtils {

    public enum  IdType{
        NA(0);

        public final int mId;

        IdType(int mId){
            this.mId = mId;
        }

        public static  IdType getInstance(int id){
            for (IdType type:values()){
                if(type.mId == id){
                    return type;
                }
            }
            throw new IllegalArgumentException("ID incorrect ou non reconnue");
        }
    }
}
