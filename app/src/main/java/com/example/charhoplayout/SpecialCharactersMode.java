package com.example.charhoplayout;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class SpecialCharactersMode {

    Context context;

    ArrayList<String> spSuggestions;
    ArrayAdapter<String> spSuggestionsAdapter;

    boolean spFront,spBack,spPrev;
    int spHeadPoint;
    String searchValue;

    boolean editMode=false;
    String decision="";

    String alredyTyped="";

    public SpecialCharactersMode(Context context)
    {
        this.context = context;
    }

    public void spModeInitialise()
    {
        //Adding Special Characters into Different Plane
        String spCharacters = "&,.,@,!,*,#,$,%";
        spSuggestions = new ArrayList<>(Arrays.asList(spCharacters.split(",")));
        spSuggestionsAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,spSuggestions);
        spSuggestionsAdapter.notifyDataSetChanged();

        spFront=false;
        spBack=false;
        spHeadPoint=0;
        spPrev=false; //#############
    }

    public void spModeForward(TextToSpeech tts)
    {
        if(spFront)
        {
            spHeadPoint = spHeadPoint + 1;
            spFront=false;
        }

        if(spHeadPoint==spSuggestions.size())
        {
            spHeadPoint = 0;
        }

        if(spSuggestions.get(spHeadPoint).equals("#"))
        {
            tts.speak("Hash", TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else if(spSuggestions.get(spHeadPoint).equals("$"))
        {
            tts.speak("Dollar",TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else
        {
            tts.speak(spSuggestions.get(spHeadPoint),TextToSpeech.QUEUE_FLUSH,null,null);
        }

        //tts.speak(spSuggestions.get(spHeadPoint),TextToSpeech.QUEUE_FLUSH,null,null);
        spHeadPoint++;
        spBack=true;
    }

    public void spModeBackward(TextToSpeech tts)
    {
        if (spBack) {
            spHeadPoint = spHeadPoint - 2;
            spBack = false;
        }
        else
        {
            spHeadPoint--;
        }

        if(spHeadPoint < 0 )
        {
            spHeadPoint =spSuggestions.size()-1;
        }


        if(spSuggestions.get(spHeadPoint).equals("#"))
        {
            tts.speak("Hash",TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else if(spSuggestions.get(spHeadPoint).equals("$"))
        {
            tts.speak("Dollar",TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else
        {
            tts.speak(spSuggestions.get(spHeadPoint),TextToSpeech.QUEUE_FLUSH,null,null);
        }
        //tts.speak(spSuggestions.get(spHeadPoint),TextToSpeech.QUEUE_FLUSH,null,null);
        spFront=true;
        spPrev=true;
    }

    public void spModeSelection(TextToSpeech tts)
    {
        if(!spPrev)
        {
            searchValue = spSuggestions.get(spHeadPoint-1);
            if(editMode & decision.equals("Insert"))
            {
                //count("Selection in Edit");
//                if(alredyTyped.charAt(insertion_index) == ' ')
//                {
//                    tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                    tts.speak(searchValue + " Inserted at space" , TextToSpeech.QUEUE_ADD, null, null);
//                }
//                else
//                {
//                    tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                    tts.speak(searchValue + " Inserted at \t"+alredyTyped.charAt(insertion_index) , TextToSpeech.QUEUE_ADD, null, null);
//                }
//                insertInEdit();
            }
            else if(editMode & decision.equals("Replace"))
            {
                //count("Selection in Edit");
//                if(alredyTyped.charAt(insertion_index) == ' ')
//                {
//                    tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                    tts.speak(searchValue + " Replaced at space", TextToSpeech.QUEUE_ADD, null, null);
//                }
//                else
//                {
//                    tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                    tts.speak(searchValue + " Replaced at \t"+alredyTyped.charAt(insertion_index) , TextToSpeech.QUEUE_ADD, null, null);
//                }
//                replaceInEdit();
            }
            else
            {
                //count("Selection");
                if(searchValue.equals(" "))
                {
                    addSpaceAtEnd(tts);
                }
                else
                {
                    addAtEnd(tts);
                }
            }
            spPrev=false;
        }
        else
        {
            if(!spFront)
            {
                searchValue = spSuggestions.get(spHeadPoint-1);

                if(editMode & decision.equals("Insert"))
                {
//                    count("Selection in Edit");
//                    if(alredyTyped.charAt(insertion_index) == ' ')
//                    {
//                        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                        tts.speak(searchValue + " Inserted at space" , TextToSpeech.QUEUE_ADD, null, null);
//                    }
//                    else
//                    {
//                        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                        tts.speak(searchValue + " Inserted at \t"+alredyTyped.charAt(insertion_index) , TextToSpeech.QUEUE_ADD, null, null);
//                    }
//                    insertInEdit();
                }
                else if(editMode & decision.equals("Replace"))
                {
//                    count("Selection in Edit");
//                    if(alredyTyped.charAt(insertion_index) == ' ')
//                    {
//                        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                        tts.speak(searchValue + " Replaced at space", TextToSpeech.QUEUE_ADD, null, null);
//                    }
//                    else
//                    {
//                        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                        tts.speak(searchValue + " Replaced at \t"+alredyTyped.charAt(insertion_index) , TextToSpeech.QUEUE_ADD, null, null);
//                    }
//                    replaceInEdit();
                }
                else
                {
                    //count("Selection");
                    if(searchValue.equals(" "))
                    {
                        addSpaceAtEnd(tts);
                    }
                    else
                    {
                        addAtEnd(tts);
                    }
                }
            }
            else
            {
                searchValue=spSuggestions.get(spHeadPoint);
                if(editMode & decision.equals("Insert"))
                {
//                    count("Selection in Edit");
//                    if(alredyTyped.charAt(insertion_index) == ' ')
//                    {
//                        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                        tts.speak(searchValue + " Inserted at space" , TextToSpeech.QUEUE_ADD, null, null);
//                    }
//                    else
//                    {
//                        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                        tts.speak(searchValue + " Inserted at \t"+alredyTyped.charAt(insertion_index) , TextToSpeech.QUEUE_ADD, null, null);
//                    }
//                    insertInEdit();
                }
                else if(editMode & decision.equals("Replace"))
                {
//                    count("Selection in Edit");
//                    if(alredyTyped.charAt(insertion_index) == ' ')
//                    {
//                        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                        tts.speak(searchValue + " Replaced at space", TextToSpeech.QUEUE_ADD, null, null);
//                    }
//                    else
//                    {
//                        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                        tts.speak(searchValue + " Replaced at \t"+alredyTyped.charAt(insertion_index) , TextToSpeech.QUEUE_ADD, null, null);
//                    }
//                    replaceInEdit();
                }
                else
                {
                    //count("Selection");
                    if(searchValue.equals(" "))
                    {
                        addSpaceAtEnd(tts);
                    }
                    else
                    {
                        addAtEnd(tts);
                    }
                }
            }
        }
    }

    public void addSpaceAtEnd(TextToSpeech tts)
    {
        alredyTyped = alredyTyped +" ";
        tts.setPitch(2.0f);
        tts.speak("space", TextToSpeech.QUEUE_ADD, null, null);
        tts.setPitch(1.0f);
    }

    public void addAtEnd(TextToSpeech tts)
    {
        tts.setPitch(2.0f);
        //tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
        if(searchValue.equals("#"))
        {
            //tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
            tts.speak("Hash",TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else if(searchValue.equals("$"))
        {
            //tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
            tts.speak("Dollar",TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else
        {
            //tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
            tts.speak(searchValue, TextToSpeech.QUEUE_ADD, null, null);
        }
        //tts.speak(searchValue, TextToSpeech.QUEUE_ADD, null, null);
        tts.setPitch(1.0f);
        alredyTyped = alredyTyped + searchValue;
    }

}
