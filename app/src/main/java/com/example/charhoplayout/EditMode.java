package com.example.charhoplayout;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.example.charhoplayout.EarconManager.deleteChar;
import static com.example.charhoplayout.EarconManager.selectEarcon;

public class EditMode {

    static boolean editMode=false;

    static int nav_index;

    int edit_suggestions_index;
    static int insertion_index;
    int deleteion_index;

    char alpha;

    Context context;
    static String decision="";

    public EditMode(Context context) {
        this.context = context;
    }

    ArrayList<String> edit_suggestions;
    ArrayAdapter<String> edit_suggestionsAdapter;

    public void edModeInitialise(TextToSpeech tts,String alreadyTyped,Context context)
    {
        if(alreadyTyped.length()==0) {
            tts.playEarcon(selectEarcon, TextToSpeech.QUEUE_FLUSH, null, null);
            tts.speak("No character selected nothing to edit", TextToSpeech.QUEUE_FLUSH, null, null);
            return;
        }

            edit_suggestions = new ArrayList<>();
            edit_suggestions.add("Insert");
            edit_suggestions.add("Replace");
            edit_suggestionsAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,edit_suggestions);
            edit_suggestionsAdapter.notifyDataSetChanged();

            edit_suggestions_index = 0;

            editMode=true;
            MainActivity.allowSearchScan=true;
            MainActivity.toggle = 1;
            nav_index=0;
            tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
            tts.speak("Edit "+alreadyTyped,TextToSpeech.QUEUE_FLUSH,null,null);
    }

    public void edModeForwardNav(TextToSpeech tts, String alreadyTyped)
    {
        if(alreadyTyped.isEmpty())
        {
            tts.speak("Empty Syntagm",TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else
        {
            if(nav_index==alreadyTyped.length())
            {
                nav_index=0;
            }
            if(nav_index<0)
            {
                nav_index=alreadyTyped.length()-1;
            }
            alpha = alreadyTyped.charAt(nav_index);
            if(alpha == ' ')
            {
                tts.speak("space",TextToSpeech.QUEUE_FLUSH,null,null);
            }
            else if(alpha == '#')
            {
                tts.speak("Hash",TextToSpeech.QUEUE_FLUSH,null,null);
            }
            else
            {
                tts.speak(Character.toString(alpha),TextToSpeech.QUEUE_FLUSH,null,null);
            }
            nav_index++;
        }
    }

    public void edModeDecisionNav(TextToSpeech tts)
    {
        if(edit_suggestions_index==edit_suggestions.size())
        {
            edit_suggestions_index=0;
        }
        if(edit_suggestions_index<0)
        {
            edit_suggestions_index=edit_suggestions.size()-1;
        }
        tts.speak(edit_suggestions.get(edit_suggestions_index),TextToSpeech.QUEUE_FLUSH,null,null);
        edit_suggestions_index++;
    }

    public void edModeDecisionSelection(TextToSpeech tts, String alredyTyped)
    {
        insertion_index=nav_index-1;
        decision = edit_suggestions.get(edit_suggestions_index-1);
        if(decision.equals("Insert"))
        {
            if(alredyTyped.charAt(insertion_index) == ' ')
            {
                tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
                tts.speak(" Insert at space" , TextToSpeech.QUEUE_ADD, null, null);
            }
            else
            {
                tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
                tts.speak(" Insert at \t"+alredyTyped.charAt(insertion_index) , TextToSpeech.QUEUE_ADD, null, null);
            }
            MainActivity.allowSearchScan=false;
            editMode=true;
        }
        else if(decision.equals("Replace")) {
            if (alredyTyped.charAt(insertion_index) == ' ') {
                tts.playEarcon(selectEarcon, TextToSpeech.QUEUE_FLUSH, null, null);
                tts.speak(" Replace at space", TextToSpeech.QUEUE_ADD, null, null);
            } else {
                tts.playEarcon(selectEarcon, TextToSpeech.QUEUE_FLUSH, null, null);
                tts.speak(" Replace at \t" + alredyTyped.charAt(insertion_index), TextToSpeech.QUEUE_ADD, null, null);
            }
            MainActivity.allowSearchScan = false;
            editMode = true;
        }

        AlphabetMode.head_point = 0;
    }

    public String edModeDeletion(TextToSpeech tts, String alredyTyped)
    {
        if(alredyTyped.isEmpty())
        {
            tts.speak("No Character to Delete", TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else
        {
            deleteion_index=nav_index-1;
            if(alredyTyped.charAt(deleteion_index) == ' ')
            {
                StringBuilder sb_deletion = new StringBuilder(alredyTyped);
                sb_deletion.deleteCharAt(deleteion_index);

                tts.speak("Space Deleted between Syntagm",TextToSpeech.QUEUE_FLUSH,null,null);
                tts.playEarcon(deleteChar,TextToSpeech.QUEUE_FLUSH,null,null);
                alredyTyped=sb_deletion.toString();
            }
            else
            {
                char delete_char_between = alredyTyped.charAt(deleteion_index);

                StringBuilder sb_deletion = new StringBuilder(alredyTyped);
                tts.speak(String.valueOf(sb_deletion.charAt(deleteion_index)),TextToSpeech.QUEUE_ADD,null,null);

                sb_deletion.deleteCharAt(deleteion_index);
                //tts.speak(delete_char_between+" Deleted between Syntagm",TextToSpeech.QUEUE_FLUSH,null,null);
                tts.playEarcon(deleteChar,TextToSpeech.QUEUE_FLUSH,null,null);
                alredyTyped=sb_deletion.toString();
            }
            nav_index=nav_index-1;//Adjust the Navigation --> Otherwise it skips one alphabet to navigate
        }
        return alredyTyped;
    }

    public static String insertInEdit(TextToSpeech tts,String alredyTyped,String searchValue, int insertion_index)
    {
        tts.setPitch(1.0f);
        StringBuilder sb = new StringBuilder(alredyTyped);
        sb.insert(insertion_index,searchValue);
        alredyTyped=sb.toString();
        Log.i("Word",alredyTyped);
        //MainActivity.allowSearchScan=true;
        EditMode.nav_index=0;
        return alredyTyped;
    }

    public static String replaceInEdit(TextToSpeech tts, String alredyTyped,String searchValue,int insertion_index)
    {
        tts.setPitch(1.0f);
        StringBuilder sb = new StringBuilder(alredyTyped);
        sb.setCharAt(insertion_index,searchValue.charAt(0));
        //alredyTyped = alredyTyped.replace(alredyTyped.charAt(insertion_index),searchValue.charAt(0));
        alredyTyped = sb.toString();
        Log.i("Word",alredyTyped);
        //MainActivity.allowSearchScan=true;
        EditMode.nav_index=0;
        return alredyTyped;
    }

    public static void speakInsertedAtSpace(TextToSpeech tts,String searchValue)
    {
        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
        tts.speak(searchValue + " Inserted at space" , TextToSpeech.QUEUE_ADD, null, null);
    }

    public static void speakInsertedAtCharacter(TextToSpeech tts,String searchValue,char ch)
    {
        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
        tts.speak(searchValue + " Inserted at \t"+ch , TextToSpeech.QUEUE_ADD, null, null);
    }

    public static void speakReplacedAtSpace(TextToSpeech tts,String searchValue)
    {
        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
        tts.speak(searchValue + " Replaced at space" , TextToSpeech.QUEUE_ADD, null, null);
    }

    public static void speakReplacedAtCharacter(TextToSpeech tts,String searchValue,char ch)
    {
        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
        tts.speak(searchValue + " Replaced at \t"+ch , TextToSpeech.QUEUE_ADD, null, null);
    }
}
