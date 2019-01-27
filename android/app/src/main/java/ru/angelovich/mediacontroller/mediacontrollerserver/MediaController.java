package ru.angelovich.mediacontroller.mediacontrollerserver;

import android.app.SearchManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.AudioManager;
import android.net.UrlQuerySanitizer;
import android.provider.MediaStore;

class MediaController {


    private static final String TOGGLE = "toggle";
    private static final String NEXT = "next";
    private static final String PREV = "prev";
    private static final String VOL_UP = "volume_up";
    private static final String VOL_DOWN = "volume_down";
    private static final String SEARCH = "search";

    private static final String SERVICECMD = "com.android.music.musicservicecommand";
    private static final String CMDNAME = "command";
    private static final String CMDTOGGLEPAUSE = "togglepause";
    private static final String CMDSTOP = "stop";
    private static final String CMDPAUSE = "pause";
    private static final String CMDPLAY = "play";
    private static final String CMDPREVIOUS = "previous";
    private static final String CMDNEXT = "next";

    private ContextWrapper context;

    MediaController(ContextWrapper context) {
        this.context = context;
    }

    void OnCommand(String cmd) {
        switch (cmd) {
            case TOGGLE:
                MedaiPlayerIntent(CMDTOGGLEPAUSE);
                break;
            case NEXT:
                MedaiPlayerIntent(CMDNEXT);
                break;
            case PREV:
                MedaiPlayerIntent(CMDPREVIOUS);
                break;
            case VOL_UP:
                VolumeUp();
                break;
            case VOL_DOWN:
                VolumeDown();
                break;
            default:
                ReadParams(cmd);
                break;
        }
    }

    private void MedaiPlayerIntent(String commandName) {
        Intent i = new Intent(SERVICECMD);
        i.putExtra(CMDNAME, commandName);
        context.sendBroadcast(i);
    }

    private void VolumeUp() {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
    }

    private void VolumeDown() {
        AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
    }

    private void ReadParams(String cmd) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);

        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(cmd);

        for (UrlQuerySanitizer.ParameterValuePair pair : sanitizer.getParameterList()) {
            UpdateIntentWith(intent, pair.mParameter, pair.mValue);
        }

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }

    }

//    /**
//     * The name of the Intent-extra used to define the artist
//     */
//    public static final String EXTRA_MEDIA_ARTIST = "android.intent.extra.artist";
//    /**
//     * The name of the Intent-extra used to define the album
//     */
//    public static final String EXTRA_MEDIA_ALBUM = "android.intent.extra.album";
//    /**
//     * The name of the Intent-extra used to define the song title
//     */
//    public static final String EXTRA_MEDIA_TITLE = "android.intent.extra.title";
//    /**
//     * The name of the Intent-extra used to define the genre.
//     */
//    public static final String EXTRA_MEDIA_GENRE = "android.intent.extra.genre";
//    /**
//     * The name of the Intent-extra used to define the playlist.
//     */
//    public static final String EXTRA_MEDIA_PLAYLIST = "android.intent.extra.playlist";
//    /**
//     * The name of the Intent-extra used to define the radio channel.
//     */
//    public static final String EXTRA_MEDIA_RADIO_CHANNEL = "android.intent.extra.radio_channel";
//    /**
//     * The name of the Intent-extra used to define the search focus. The search focus
//     * indicates whether the search should be for things related to the artist, album
//     * or song that is identified by the other extras.
//     */
//    public static final String EXTRA_MEDIA_FOCUS = "android.intent.extra.focus";


//    public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/audio";
//    public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/genre";
//    public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/playlist";
//    public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/artist";
//    public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/album";
//    public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/radio";

    private void UpdateIntentWith(Intent intent, String paramName, String paramValue) {
        if (paramValue.isEmpty())
            return;
//        paramValue = paramValue.replace("_", " ");
        switch (paramName) {
            case "focus":
                //intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE);
                intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, "vnd.android.cursor.item/" + paramValue);
                break;
            case "artist":
            case "album":
            case "title":
            case "genre":
            case "playlist":
            case "radio":
                // intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, paramValue);
                intent.putExtra("android.intent.extra." + paramName, paramValue);
                break;
            case "query":
                intent.putExtra(SearchManager.QUERY, paramValue);
                break;
            default:
                intent.putExtra(SearchManager.QUERY, paramName + " " + paramValue);
                break;
        }


    }
}
