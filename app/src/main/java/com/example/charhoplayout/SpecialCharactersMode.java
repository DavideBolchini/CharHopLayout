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

    public ArrayList<String> spModeSelection(TextToSpeech tts, String alreadyTyped, String word)
    {
        if(!spPrev)
        {
            searchValue = spSuggestions.get(spHeadPoint-1);

            if(EditMode.editMode & EditMode.decision.equals("Insert"))
            {
                if(alreadyTyped.charAt(EditMode.insertion_index) == ' ')
                {
                    EditMode.speakInsertedAtSpace(tts,searchValue);
                }
                else
                {
                    EditMode.speakReplacedAtCharacter(tts,searchValue,alreadyTyped.charAt(EditMode.insertion_index));
                }
                alreadyTyped = EditMode.insertInEdit(tts,alreadyTyped,searchValue,EditMode.insertion_index);
                word = "";
            }
            else if(EditMode.editMode & EditMode.decision.equals("Replace"))
            {
                if(alreadyTyped.charAt(EditMode.insertion_index) == ' ')
                {
                    EditMode.speakReplacedAtSpace(tts,searchValue);
                }
                else
                {
                    EditMode.speakReplacedAtCharacter(tts,searchValue,alreadyTyped.charAt(EditMode.insertion_index));
                }
                alreadyTyped = EditMode.replaceInEdit(tts,alreadyTyped,searchValue,EditMode.insertion_index);
                word = "";
            }
            else
            {
                //count("Selection");
                if(searchValue.equals(" "))
                {
                    alreadyTyped = addSpaceAtEnd(tts, alreadyTyped, word, searchValue);
                    word = "";
                    //alreadyTyped = addSpaceAtEnd(tts,alreadyTyped,searchValue);
                }
                else
                {
                    word = addAtEnd(tts, word, searchValue);
                    //alreadyTyped = alreadyTyped;
                    //alreadyTyped = addAtEnd(tts,alreadyTyped,searchValue);
                }
            }
            spPrev=false;
        }
        else
        {
            if(!spFront)
            {
                searchValue = spSuggestions.get(spHeadPoint-1);

                if(EditMode.editMode & EditMode.decision.equals("Insert"))
                {
                    if(alreadyTyped.charAt(EditMode.insertion_index) == ' ')
                    {
                        EditMode.speakInsertedAtSpace(tts,searchValue);
                    }
                    else
                    {
                        EditMode.speakInsertedAtCharacter(tts,searchValue,alreadyTyped.charAt(EditMode.insertion_index));
                    }
                    alreadyTyped = EditMode.insertInEdit(tts,alreadyTyped,searchValue,EditMode.insertion_index);
                    word = "";
                }
                else if(EditMode.editMode & EditMode.decision.equals("Replace"))
                {
                    if(alreadyTyped.charAt(EditMode.insertion_index) == ' ')
                    {
                        EditMode.speakReplacedAtSpace(tts,searchValue);
                    }
                    else
                    {
                        EditMode.speakReplacedAtCharacter(tts,searchValue,alreadyTyped.charAt(EditMode.insertion_index));
                    }
                    alreadyTyped = EditMode.replaceInEdit(tts,alreadyTyped,searchValue,EditMode.insertion_index);
                    word = "";
                }
                else
                {
                    //count("Selection");
                    if(searchValue.equals(" "))
                    {
                        alreadyTyped = addSpaceAtEnd(tts, alreadyTyped, word, searchValue);
                        word = "";
                        //alreadyTyped = addSpaceAtEnd(tts,alreadyTyped,searchValue);
                    }
                    else
                    {
                        word = addAtEnd(tts, word, searchValue);
                        //alreadyTyped = alreadyTyped;
                        //alreadyTyped = addAtEnd(tts,alreadyTyped,searchValue);
                    }
                }
            }
            else
            {
                searchValue=spSuggestions.get(spHeadPoint);

                if(EditMode.editMode & EditMode.decision.equals("Insert"))
                {
                    if(alreadyTyped.charAt(EditMode.insertion_index) == ' ')
                    {
                        EditMode.speakInsertedAtSpace(tts,searchValue);
                    }
                    else
                    {
                        EditMode.speakInsertedAtCharacter(tts,searchValue,alreadyTyped.charAt(EditMode.insertion_index));
                    }
                    alreadyTyped = EditMode.insertInEdit(tts,alreadyTyped,searchValue,EditMode.insertion_index);
                    word = "";
                }
                else if(EditMode.editMode & EditMode.decision.equals("Replace"))
                {
                    if(alreadyTyped.charAt(EditMode.insertion_index) == ' ')
                    {
                        EditMode.speakReplacedAtSpace(tts,searchValue);
                    }
                    else
                    {
                        EditMode.speakReplacedAtCharacter(tts,searchValue,alreadyTyped.charAt(EditMode.insertion_index));
                    }
                    alreadyTyped = EditMode.replaceInEdit(tts,alreadyTyped,searchValue,EditMode.insertion_index);
                    word = "";
                }
                else
                {
                    //count("Selection");
                    if(searchValue.equals(" "))
                    {
                        alreadyTyped = addSpaceAtEnd(tts, alreadyTyped, word, searchValue);
                        word = "";
                        //alreadyTyped = addSpaceAtEnd(tts,alreadyTyped,searchValue);
                    }
                    else
                    {
                        word = addAtEnd(tts, word, searchValue);
                        //alreadyTyped = alreadyTyped;
                        //alreadyTyped = addAtEnd(tts,alreadyTyped,searchValue);
                    }
                }
            }
        }
        ArrayList<String> results = new ArrayList<>();
        results.add(word);
        results.add(alreadyTyped);
        return results;
    }

    public String addSpaceAtEnd(TextToSpeech tts,String alredyTyped,String word, String searchValue)
    {
        //alredyTyped = alredyTyped +" ";
        alredyTyped = alredyTyped +word + searchValue;
        tts.setPitch(2.0f);
        tts.speak("space", TextToSpeech.QUEUE_ADD, null, null);
        tts.setPitch(1.0f);
        return alredyTyped;
    }

    public String addAtEnd(TextToSpeech tts,String alreadyTyped,String searchValue)
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
        alreadyTyped = alreadyTyped + searchValue;
        return alreadyTyped;
    }

}
