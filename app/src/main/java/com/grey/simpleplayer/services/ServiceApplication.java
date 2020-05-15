package com.grey.simpleplayer.services;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.grey.simpleplayer.MusicInter;
import com.grey.simpleplayer.adapter.PlayerUtils;
import com.grey.simpleplayer.modeles.PlayBack;

import java.util.Arrays;
import java.util.WeakHashMap;

public class ServiceApplication {

    private static final String TAG = "PlayerServices";
    public static MusicInter mRemot = null;
    private static final WeakHashMap<Context, ServiceBinder> mHashMap;
    private static long[] emptyList = null;

    //Intégration des Services........................Début

    static {
        mHashMap = new WeakHashMap<>();
    }

    public static final ServiceToken bindToService(Context context, ServiceConnection serviceConnection){
        Activity realActivity = ((Activity) context).getParent();
        if (realActivity == null){
            realActivity = (Activity) context;
        }
        ContextWrapper mWrapper = new ContextWrapper(realActivity);
        mWrapper.startService(new Intent(mWrapper, MusicService.class));
        ServiceBinder binder = new ServiceBinder(serviceConnection, mWrapper.getApplicationContext());

        if (mWrapper.bindService(new Intent().setClass(mWrapper, MusicService.class), binder, 0)){
            mHashMap.put(mWrapper, binder);
            return new ServiceToken(mWrapper);
        }
        return null;
    }

    public static void unbindToService(ServiceToken token){
        if(token == null){
            return;
        }

        ContextWrapper mWrapper = token.contextWrapper;
        ServiceBinder binder = mHashMap.remove(mWrapper);
        if(binder == null){
            return;
        }

        mWrapper.unbindService(binder);
        if(mHashMap.isEmpty()){
            binder = null;
        }
    }

    public static class ServiceToken{
        private ContextWrapper contextWrapper;

        public ServiceToken(ContextWrapper contextWrapper) {
            this.contextWrapper = contextWrapper;
        }
    }

    public static final class ServiceBinder implements ServiceConnection {

        private ServiceConnection mService;
        private Context mContext;

        public ServiceBinder(ServiceConnection mService, Context mContext) {
            this.mService = mService;
            this.mContext = mContext;
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mRemot = MusicInter.Stub.asInterface(iBinder);
            if(mService != null){
                mService.onServiceConnected(componentName, iBinder);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if(mService != null){
                mService.onServiceDisconnected(componentName);
            }
            mRemot = null;
        }
    }

    //Intégration des services.....................Fin

    //.............................Définition des Méthodes.............................

    //Indique que le lecteur est actif
    public static final boolean isPlaying() {
        if (mRemot != null) {
            try {
                return mRemot.isPlaying();
            } catch (final RemoteException ignored) {
            }
        }
        return false;
    }

    //Jouer Les musiques en partant du début
    public static void playAll(long[] list, int position, long sourceId, PlayerUtils.IdType type) throws RemoteException {

            if(list.length == 0 && list == null && mRemot == null){
                return;
            }

            long audioId = getAudioId();
            int currentPosition = getCurrentPos();
            if(position == currentPosition && audioId == list[position] && position != -1){
                long[] idList = getsaveIdList();
                if(Arrays.equals(idList, list)){
                    play();
                    return;
                }
            }

            if(position < 0){
                position = 0;
            }
            mRemot.open(list,position,sourceId,type.mId);
            play();
    }

    //Récupération de la liste des ID des morceaux
    public static long[] getsaveIdList() throws RemoteException{
        if(mRemot != null){
            mRemot.getsaveIdList();
        }
        return emptyList;
    }

    //Jouer un morceau
    public static void play() throws RemoteException{
        if(mRemot != null){
            mRemot.play();
        }
    }

    //Pause
    public static void pause() throws RemoteException{
        if(mRemot != null){
            mRemot.pause();
        }
    }

    //Position actuelle lors de la lecture
    public static int getCurrentPos() throws RemoteException{
        if (mRemot != null && isPlaying()){
            return mRemot.getCurrentPos();
        }
        return -1;
    }

    //ID d'un morceau
    public static long getAudioId() throws RemoteException{
        if (mRemot != null ){
            return mRemot.getAudioId();
        }
        return -1;
    }

    //Durée d'un morceau
    public static long getSongDuration() throws RemoteException{
        if (mRemot != null ){
            return mRemot.getSongDuration();
        }
        return 0;
    }

    //Fonction play ou pause
    public static void playPause(){
        try {
            if (mRemot!=null) {

                if(mRemot.isPlaying()){
                    mRemot.pause();
                }else {
                    mRemot.play();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.d(TAG, " "+e);
        }
    }

    //Previous
    public static void prev(){
        try {
            if (mRemot!=null) {
                mRemot.prev();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.d(TAG, " "+e);
        }
    }

    //Next
    public static void next(){
        try {
            if (mRemot!=null) {
                mRemot.next();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.d(TAG, " "+e);
        }
    }

    //.............................Définition des Méthodes.............................Fin

}
