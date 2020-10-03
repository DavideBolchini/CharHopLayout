package com.example.charhoplayout;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.charhoplayout.EarconManager.deleteChar;
import static com.example.charhoplayout.EarconManager.flowshift;
import static com.example.charhoplayout.EarconManager.selectEarcon;

public class AlphabetMode {

    /*
     * Variable Declaration for Alphabetical Mode
     * */
    Context context;

    ArrayList<String> suggestions;
    ArrayAdapter<String> suggestionsAdapter;

    int head_point;

    boolean front,back,prev,hopping;

    String searchValue;

    String del_char;

    public AlphabetMode(Context context)
    {
        this.context = context;

    }

    public void alModeInitialise()
    {
        String s = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z, ,";
        suggestions = new ArrayList<>(Arrays.asList(s.split(",")));   // Suggested Characters from A - Z and Space
        suggestionsAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, suggestions);
        suggestionsAdapter.notifyDataSetChanged();

        head_point = 0;
        front = false;
        back = false;
        prev=false;
        hopping=false;
    }

    public void alModeForward(TextToSpeech tts)
    {
        if(front & hopping==false)
        {
            head_point = head_point + 1;
            front=false;
        }
        if(prev==true & hopping==true)
        {
            head_point = head_point + 1;
            prev=false;
        }

        if(head_point== 27)
        {
            head_point = 0;
        }

        if(suggestions.get(head_point).equals(" "))
        {
            tts.speak("space",TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else
        {
            tts.speak(suggestions.get(head_point),TextToSpeech.QUEUE_FLUSH,null,null);
        }

        head_point++;
        back=true;
        //hopping=false;
    }

    public void alModeBackward(TextToSpeech tts)
    {
        if (back) {
            head_point = head_point - 2;
            back = false;
        }
        else
        {
            head_point--;
        }

        if(head_point < 0 )
        {
            head_point =26;
        }

        if(suggestions.get(head_point).equals(" "))
        {
            tts.speak("space",TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else
        {
            tts.speak(suggestions.get(head_point),TextToSpeech.QUEUE_FLUSH,null,null);
            Log.d("backward h_p",""+head_point);
        }
        front = true;
        prev = true;
    }

    public void alModeHopping(TextToSpeech tts)
    {
        if(head_point > 0 & head_point < 5)
        {
            head_point=5;
        }
        else if(head_point > 5 & head_point < 10)
        {
            head_point=10;
        }
        else if(head_point > 10 & head_point < 15)
        {
            head_point=15;
        }
        else if(head_point >15 & head_point < 20)
        {
            head_point=20;
        }
        else if(head_point > 20 & head_point <= 26)
        {
            head_point = 26;
        }
        else
        {
            head_point = 0;
        }

        if(suggestions.get(head_point).charAt(0) == ' ')
        {
            tts.speak("space",TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else
        {
            tts.speak(suggestions.get(head_point),TextToSpeech.QUEUE_FLUSH,null,null);
        }
        //tts.speak(suggestions.get(head_point),TextToSpeech.QUEUE_FLUSH,null,null);
        head_point++;
        Log.d("3 Finger h_p",""+head_point);
        back=true;
        prev=false;
        hopping=true;
    }

    public String alModeSelect(TextToSpeech tts, String alreadyTyped)
    {
        if (!prev)
        {
            searchValue = suggestions.get(head_point-1);

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
                    alreadyTyped = addAtEnd(tts,alreadyTyped, searchValue); //Here we will right the code for appending the character
                }
            }
            prev = false;
        }
        else
        {
            if(!front)
            {
                searchValue = suggestions.get(head_point-1);

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
                        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
                        //addSpaceAtEnd(tts);
                        alreadyTyped = alreadyTyped + " ";
                        tts.setPitch(2.0f);
                        tts.speak("space",TextToSpeech.QUEUE_ADD,null,null);
                        tts.setPitch(1.0f);
                    }
                    else
                    {
                        alreadyTyped = addAtEnd(tts,alreadyTyped,searchValue); //Here we will right the code for appending the character
                    }
                }
            }
            else
            {
                searchValue = suggestions.get(head_point);
                Log.d("gggg","5gggg");

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
                        alreadyTyped = addAtEnd(tts,alreadyTyped,searchValue); //Here we will right the code for appending the character
                    }
                }
            }
        }
        return alreadyTyped;
    }


    public void alModeSpeakOut(TextToSpeech tts, String alredyTyped)
    {

        speakOutSelection(tts,alredyTyped);
    }

    public void alModeReset(TextToSpeech tts)
    {
        tts.speak("Stop",TextToSpeech.QUEUE_FLUSH,null,null);
        tts.playEarcon(flowshift,TextToSpeech.QUEUE_FLUSH,null,null);
        head_point = 0;
        front=false;
        prev=false;
    }

    public String alModeDelete(TextToSpeech tts, String alreadyTyped)
    {
        if(alreadyTyped.isEmpty())
        {
            tts.speak("No Character to Delete", TextToSpeech.QUEUE_FLUSH, null, null);
            EditMode.nav_index=0;//After Deleting Entirely a word if you select a new word then it should start from first character
        }
        else
        {
            //Normal Deletion from End of the String
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

    public void speakOutSelection(TextToSpeech tts,String text)
    {
        if(text.isEmpty())
        {
            tts.setPitch(1.5f);
            speakOut(tts,"No Character Selected");
            tts.setPitch(1.0f);
        }
        else
        {
            tts.setPitch(1.5f);
            speakOut(tts,text);
            tts.setPitch(1.0f);
        }
    }

    public void speakOut(TextToSpeech tts,String text)
    {
        if(!text.equals("STOP"))
        {
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        }
    }

    public String addSpaceAtEnd(TextToSpeech tts,String alreadyTyped,String searchValue)
    {
        //alreadyTyped = alreadyTyped +" ";
        alreadyTyped = alreadyTyped +searchValue;
        tts.setPitch(2.0f);
        tts.speak("space", TextToSpeech.QUEUE_ADD, null, null);
        tts.setPitch(1.0f);
        return alreadyTyped;
    }

    public String addAtEnd(TextToSpeech tts,String alreadyTyped, String searchValue)
    {
        tts.setPitch(2.0f);
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
        tts.setPitch(1.0f);
        alreadyTyped = alreadyTyped + searchValue;
        return alreadyTyped;
    }

}
