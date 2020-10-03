package com.example.charhoplayout;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.charhoplayout.EarconManager.deleteChar;
import static com.example.charhoplayout.EarconManager.selectEarcon;

public class NumbersMode {

    Context context;

    ArrayList<String> numSuggestions;
    ArrayAdapter<String> numSuggestionsAdapter;



    String searchValue;

    int numberHeadPoint;
    boolean numberFront,numberBack,numberPrev;

    String del_char;

    public NumbersMode(Context context)
    {

        this.context = context;
    }

    public void nmModeInitialise()
    {
        //Adding Numbers into a Different Plane
        String numbers = "0,1,2,3,4,5,6,7,8,9";
        numSuggestions = new ArrayList<>(Arrays.asList(numbers.split(",")));
        numSuggestionsAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,numSuggestions);
        numSuggestionsAdapter.notifyDataSetChanged();

        numberHeadPoint=0;
        numberFront=false;
        numberBack=false;
        numberPrev=false;  //###########

    }

    public void nmModeForward(TextToSpeech tts)
    {
        if(numberFront)
        {
            numberHeadPoint = numberHeadPoint + 1;
            numberFront=false;
        }

        if(numberHeadPoint==10)
        {
            numberHeadPoint = 0;
        }

        tts.speak(numSuggestions.get(numberHeadPoint), TextToSpeech.QUEUE_FLUSH,null,null);
        numberHeadPoint++;
        numberBack=true;
    }

    public void nmModeBackward(TextToSpeech tts)
    {
        if (numberBack) {
            numberHeadPoint = numberHeadPoint - 2;
            numberBack = false;
        }
        else
        {
            numberHeadPoint--;
        }

        if(numberHeadPoint < 0 )
        {
            numberHeadPoint =9;
        }

        tts.speak(numSuggestions.get(numberHeadPoint),TextToSpeech.QUEUE_FLUSH,null,null);
        numberFront=true;
        numberPrev=true;
    }

    public String nmModeSelection(TextToSpeech tts, String alreadyTyped)
    {
        if(!numberPrev)
        {
            searchValue = numSuggestions.get(numberHeadPoint-1);

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
            }
            else
            {
                //count("Selection");
                if(searchValue.equals(" "))
                {
                    alreadyTyped = addSpaceAtEnd(tts,alreadyTyped,searchValue);
                }
                else
                {
                    alreadyTyped = addAtEnd(tts,alreadyTyped,searchValue);
                }
            }
            numberPrev=false;
        }
        else
        {
            if(!numberFront)
            {
                searchValue = numSuggestions.get(numberHeadPoint-1);

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
                }
                else
                {
                    if(searchValue.equals(" "))
                    {
                        alreadyTyped = addSpaceAtEnd(tts,alreadyTyped,searchValue);
                    }
                    else
                    {
                        alreadyTyped = addAtEnd(tts,alreadyTyped,searchValue);
                    }
                }
            }
            else
            {
                searchValue=numSuggestions.get(numberHeadPoint);

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
                }
                else
                {
                    if(searchValue.equals(" "))
                    {
                        alreadyTyped = addSpaceAtEnd(tts,alreadyTyped,searchValue);
                    }
                    else
                    {
                        alreadyTyped = addAtEnd(tts,alreadyTyped,searchValue);
                    }
                }
            }
        }
        return alreadyTyped;
    }

    public String nmModeDeletion(TextToSpeech tts,String alreadyTyped)
    {
        if(alreadyTyped.isEmpty())
        {
            tts.speak("No Character to Delete", TextToSpeech.QUEUE_FLUSH, null, null);
            //nav_index=0;//After Deleting Entirely a word if you select a new word then it should start from first character
        }
        else
        {
            //Normal Deletion from End of the String in Number Mode
            del_char = alreadyTyped.substring(alreadyTyped.length()-1);

            if(del_char.equals(" "))
            {
                tts.speak("Space", TextToSpeech.QUEUE_ADD, null, null);
                tts.playEarcon(deleteChar,TextToSpeech.QUEUE_ADD,null,null);
            }
            else
            {
                tts.speak(del_char+"", TextToSpeech.QUEUE_ADD, null, null);
                tts.playEarcon(deleteChar,TextToSpeech.QUEUE_ADD,null,null);
            }
            alreadyTyped = alreadyTyped.substring(0,alreadyTyped.length()-1);
        }
        return alreadyTyped;
    }

    public String addAtEnd(TextToSpeech tts,String alredyTyped,String searchValue)
    {
        tts.setPitch(2.0f);
        //tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
        if(searchValue.equals("#"))
        {
            tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
            tts.speak("Hash",TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else if(searchValue.equals("$"))
        {
            tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
            tts.speak("Dollar",TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else
        {
            tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
            tts.speak(searchValue, TextToSpeech.QUEUE_ADD, null, null);
        }
        //tts.speak(searchValue, TextToSpeech.QUEUE_ADD, null, null);
        tts.setPitch(1.0f);
        alredyTyped = alredyTyped + searchValue;
        return alredyTyped;
    }

    public String addSpaceAtEnd(TextToSpeech tts,String alredyTyped,String searchValue)
    {
        alredyTyped = alredyTyped +searchValue;
        tts.setPitch(2.0f);
        tts.speak("space", TextToSpeech.QUEUE_ADD, null, null);
        tts.setPitch(1.0f);
        return alredyTyped;
    }
}
