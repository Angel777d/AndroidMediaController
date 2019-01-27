package ru.angelovich.mediacontroller.mediacontrollerserver;

import android.app.SearchManager;
import android.content.Intent;
import android.provider.MediaStore;

public class SearchCommands {
    private static final String SEARCH_CMD = "search?";

    public static Intent process(String cmd) {
        if (cmd.contains(SEARCH_CMD)) {
            Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
            String[] separated = cmd.split("\\?");
            separated = separated[1].split("&");

            for (String pair:separated) {
                String[] _pair = pair.split("=");
                if (_pair.length < 2)
                    continue;
                String name = _pair[0];
                String value = _pair[1];
                switch (name) {
                    case "artist":
                        intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE);
                        intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, value);
                        intent.putExtra(SearchManager.QUERY, value);
                        break;

                    case "album":
                        intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE);
                        intent.putExtra(MediaStore.EXTRA_MEDIA_ALBUM, value);
                        intent.putExtra(SearchManager.QUERY, value);
                        break;

                    case "title":
                        intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, value);
                        intent.putExtra(SearchManager.QUERY, value);
                        break;

                    case "genre":
                        intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Genres.ENTRY_CONTENT_TYPE);
                        intent.putExtra(MediaStore.EXTRA_MEDIA_GENRE, value);
                        intent.putExtra(SearchManager.QUERY, value);
                        break;

                    case "playlist":
                        intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Playlists.ENTRY_CONTENT_TYPE);
                        intent.putExtra(MediaStore.EXTRA_MEDIA_PLAYLIST, value);
                        intent.putExtra(SearchManager.QUERY, value);
                        break;

                    case "radio":
                        intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Radio.ENTRY_CONTENT_TYPE);
                        intent.putExtra(MediaStore.EXTRA_MEDIA_RADIO_CHANNEL, value);
                        intent.putExtra(SearchManager.QUERY, value);
                        break;

                    case "query":
                        intent.putExtra(SearchManager.QUERY, value);
                        break;
                }
            }
            return intent;
        }
        return null;
    }

    private static void UpdateIntentWith(Intent intent, String paramName, String paramValue) {
        if (paramValue.isEmpty())
            return;
//        paramValue = paramValue.replace("_", " ");
        switch (paramName) {
        }


    }
}