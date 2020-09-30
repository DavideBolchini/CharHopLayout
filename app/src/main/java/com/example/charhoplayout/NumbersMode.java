package com.example.charhoplayout;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class NumbersMode {

    ArrayList<String> numSuggestions;
    ArrayAdapter<String> numSuggestionsAdapter;

    Context context;

    String searchValue;

    int numberHeadPoint = 0;
    boolean numberFront,numberBack,numberPrev;

    boolean editMode=false;
    String decision="";

    String alredyTyped="";

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
        boolean isNumberMode=false;
        int numberModeToggle=0;
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

    public void nmModeSelection(TextToSpeech tts)
    {
        if(!numberPrev)
        {
            searchValue = numSuggestions.get(numberHeadPoint-1);

            if(editMode & decision.equals("Insert"))
            {
                //count("Selection in Edit");
//                if(alredyTyped.charAt(insertion_index) == ' ')
//                {
//                    //tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                    tts.speak(searchValue + " Inserted at space" , TextToSpeech.QUEUE_ADD, null, null);
//                }
//                else
//                {
//                    //tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                    tts.speak(searchValue + " Inserted at \t"+alredyTyped.charAt(insertion_index) , TextToSpeech.QUEUE_ADD, null, null);
//                }
//                insertInEdit();
            }
            else if(editMode & decision.equals("Replace"))
            {
                //count("Selection in Edit");
//                if(alredyTyped.charAt(insertion_index) == ' ')
//                {
//                    //tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                    tts.speak(searchValue + " Replaced at space", TextToSpeech.QUEUE_ADD, null, null);
//                }
//                else
//                {
//                    //tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
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
            numberPrev=false;
        }
        else
        {
            if(!numberFront)
            {
                searchValue = numSuggestions.get(numberHeadPoint-1);

                if(editMode & decision.equals("Insert"))
                {
                    //count("Selection in Edit");
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
                searchValue=numSuggestions.get(numberHeadPoint);

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

    public void nmModeDeletion(TextToSpeech tts)
    {
        if(alredyTyped.isEmpty())
        {
            tts.speak("No Character to Delete", TextToSpeech.QUEUE_FLUSH, null, null);
            //nav_index=0;//After Deleting Entirely a word if you select a new word then it should start from first character
        }
        else
        {
            //Normal Deletion from End of the String in Number Mode
            del_char = alredyTyped.substring(alredyTyped.length()-1);

            if(del_char.equals(" "))
            {
                tts.speak("Space", TextToSpeech.QUEUE_ADD, null, null);
                //tts.playEarcon(deleteChar,TextToSpeech.QUEUE_ADD,null,null);
            }
            else
            {
                tts.speak(del_char+"", TextToSpeech.QUEUE_ADD, null, null);
                //tts.playEarcon(deleteChar,TextToSpeech.QUEUE_ADD,null,null);
            }
            alredyTyped = alredyTyped.substring(0,alredyTyped.length()-1);
        }
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

    public void addSpaceAtEnd(TextToSpeech tts)
    {
        alredyTyped = alredyTyped +" ";
        tts.setPitch(2.0f);
        tts.speak("space", TextToSpeech.QUEUE_ADD, null, null);
        tts.setPitch(1.0f);
    }
}
