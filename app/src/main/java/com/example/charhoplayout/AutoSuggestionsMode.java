package com.example.charhoplayout;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.ArrayList;

import static com.example.charhoplayout.EarconManager.selectEarcon;

public class AutoSuggestionsMode {

    private ArrayList SuggestionsResult = new ArrayList();
    int autoSuggestionHeadPoint = 0;

    public ArrayList<String> fetchAutoSuggestions(Context context,TextToSpeech tts,String alreadyTyped)
    {
        if (alreadyTyped.length() < 2)
        {
            tts.speak("Syntagm is too small for Autosuggestion", TextToSpeech.QUEUE_ADD, null, null);
        }
        else
        {
            tts.speak("Fetching Auto Suggestions for "+alreadyTyped,TextToSpeech.QUEUE_FLUSH,null,null);
            MainActivity.isAutoSuggestionMode=true;
            MainActivity.autoSuggestionModeToggle=1;
            autoSuggestionHeadPoint=0;
            AutoSuggestionsParser SGP = new AutoSuggestionsParser();
            SuggestionsResult = (ArrayList) SGP.autoSuggestions(context,alreadyTyped);
            Log.d("Auto Suggestions",SuggestionsResult.toString());

        }
        if(SuggestionsResult.size() == 0)
        {
            tts.speak("No Suggestions Available",TextToSpeech.QUEUE_ADD,null,null);
            MainActivity.isAutoSuggestionMode = false;
            MainActivity.autoSuggestionModeToggle=0;
        }
        return SuggestionsResult;
    }

    public void forwardNavigateSuggestions(TextToSpeech tts,ArrayList<String> suggestionsResult)
    {
        if(autoSuggestionHeadPoint==SuggestionsResult.size())
        {
            autoSuggestionHeadPoint=0;
        }

        tts.speak(suggestionsResult.get(autoSuggestionHeadPoint),TextToSpeech.QUEUE_ADD,null,null);
        autoSuggestionHeadPoint++;
    }

    public String selectAutoSuggestion(TextToSpeech tts)
    {
        String searchValue="";
        if(autoSuggestionHeadPoint == 0)
        {
            tts.speak("Navigate to Listen to Autosuggestions",TextToSpeech.QUEUE_ADD,null,null);
        }
        else
        {
            searchValue = (String) SuggestionsResult.get(autoSuggestionHeadPoint - 1);
            tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
            tts.speak(searchValue, TextToSpeech.QUEUE_ADD, null, null);
        }
        autoSuggestionHeadPoint=0;
        MainActivity.isAutoSuggestionMode=false;
        MainActivity.autoSuggestionModeToggle=0;

        return searchValue;
    }
}
