package com.example.charhoplayout;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class AlphabetMode {

    /*
     * Variable Declaration for Alphabetical Mode
     * */
    ArrayList<String> suggestions;
    ArrayAdapter<String> suggestionsAdapter;

    Context context;

    int head_point=0;
    String alredyTyped="";
    boolean front,back,prev,hopping;

    String searchValue;

    boolean editMode=false;
    String decision="";

    int nav_index=0;

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
            //uniqueLettersR.add(suggestions.get(head_point));
            //totalPathTakenByUser.add(suggestions.get(head_point));
        }
        else
        {
            tts.speak(suggestions.get(head_point),TextToSpeech.QUEUE_FLUSH,null,null);
            //uniqueLettersR.add(suggestions.get(head_point));
            //totalPathTakenByUser.add(suggestions.get(head_point));
            //Log.d("Unique Values","values "+uniqueLettersR);
            //Log.d("Length of Suggestions","suggestions ka size +"+suggestions.size());
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
            //uniqueLettersR.add(suggestions.get(head_point));
            //totalPathTakenByUser.add(suggestions.get(head_point));
        }
        else
        {
            tts.speak(suggestions.get(head_point),TextToSpeech.QUEUE_FLUSH,null,null);
            //uniqueLettersR.add(suggestions.get(head_point));
            //totalPathTakenByUser.add(suggestions.get(head_point));
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
            //uniqueLettersR.add(suggestions.get(head_point));
            //totalPathTakenByUser.add(suggestions.get(head_point));
        }
        else
        {
            tts.speak(suggestions.get(head_point),TextToSpeech.QUEUE_FLUSH,null,null);
            //uniqueLettersR.add(suggestions.get(head_point));
            //totalPathTakenByUser.add(suggestions.get(head_point));
        }
        //tts.speak(suggestions.get(head_point),TextToSpeech.QUEUE_FLUSH,null,null);
        head_point++;
        Log.d("3 Finger h_p",""+head_point);
        back=true;
        prev=false;
        hopping=true;
    }

    public void alModeSelect(TextToSpeech tts)
    {
        if (!prev)
        {
            searchValue = suggestions.get(head_point-1);
            if(alredyTyped.isEmpty())
            {
                int searchIndex = suggestions.indexOf(searchValue)+1;
                int inverseSearchIndex=27-searchIndex;
                int minOfAboveTwo=Math.min(searchIndex,inverseSearchIndex);
                //optimalPath=optimalPath+minOfAboveTwo;
                //Log.d("1","1"+optimalPath);
            }
            else
            {
                int searchIndex = Math.abs(((suggestions.indexOf(searchValue)+1) - (suggestions.indexOf(alredyTyped.substring(alredyTyped.length() - 1))+1)));
                int inverseSearchIndex=27-searchIndex;
                int minOfAboveTwo=Math.min(searchIndex,inverseSearchIndex);
                //optimalPath=optimalPath+minOfAboveTwo;
                //Log.d("1","1a"+optimalPath);
            }

            if(editMode & decision.equals("Insert"))
            {
                //count("Selection in Edit");

                //tts.setPitch(2.0f);
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
////                            tts.setPitch(1.0f);
////                            StringBuilder sb = new StringBuilder(alredyTyped);
////                            sb.insert(insertion_index,searchValue);
////                            alredyTyped=sb.toString();
////                            Log.i("Word",alredyTyped);
////                            allowSearchScan=true;
////                            nav_index=0;
            }
            else if(editMode & decision.equals("Replace"))
            {
//                count("Selection in Edit");
//
//                //tts.setPitch(2.0f);
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
////                            tts.setPitch(1.0f);
////                            alredyTyped = alredyTyped.replace(alredyTyped.charAt(insertion_index),searchValue.charAt(0));
////                            Log.i("Word",alredyTyped);
////                            allowSearchScan=true;
////                            nav_index=0;
            }
            else
            {
                //count("Selection");

                if(searchValue.equals(" "))
                {
                    addSpaceAtEnd(tts);
//                                alredyTyped = alredyTyped +" ";
//                                tts.setPitch(2.0f);
//                                tts.speak("space",TextToSpeech.QUEUE_ADD,null,null);
//                                tts.setPitch(1.0f);
                }
                else
                {
                    //Here we will right the code for appending the character
                    addAtEnd(tts);
//                                tts.setPitch(2.0f);
//                                tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                                tts.speak(searchValue, TextToSpeech.QUEUE_ADD, null, null);
//                                tts.setPitch(1.0f);
//                                alredyTyped = alredyTyped + searchValue;
                }
            }
            prev = false;
        }
        else
        {
            if(!front)
            {
                searchValue = suggestions.get(head_point-1);
                if(alredyTyped.isEmpty())
                {
                    int searchIndex = suggestions.indexOf(searchValue)+1;
                    int inverseSearchIndex=27-searchIndex;
                    int minOfAboveTwo=Math.min(searchIndex,inverseSearchIndex);
                    //optimalPath=optimalPath+minOfAboveTwo;
                    //Log.d("2","2"+optimalPath);
                }
                else
                {
                    int searchIndex = Math.abs(((suggestions.indexOf(searchValue)+1) - (suggestions.indexOf(alredyTyped.substring(alredyTyped.length() - 1))+1)) );
                    int inverseSearchIndex=27-searchIndex;
                    int minOfAboveTwo=Math.min(searchIndex,inverseSearchIndex);
                    //optimalPath=optimalPath+minOfAboveTwo;
                    //Log.d("2","2a"+optimalPath);
                }
//
//                            int searchIndex = suggestions.indexOf(searchValue)+1;
//                            int inverseSearchIndex=27-searchIndex;
//                            int maxOfAboveTwo=Math.min(searchIndex,inverseSearchIndex);
//                            optimalPath=optimalPath+maxOfAboveTwo;

                if(editMode & decision.equals("Insert"))
                {
//                    count("Selection in Edit");
//
//                    //tts.setPitch(2.0f);
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
////                                tts.setPitch(1.0f);
////                                StringBuilder sb = new StringBuilder(alredyTyped);
////                                sb.insert(insertion_index,searchValue);
////                                alredyTyped=sb.toString();
////                                Log.i("Word",alredyTyped);
////                                allowSearchScan=true;
////                                nav_index=0;
                }
                else if(editMode & decision.equals("Replace"))
                {
//                    count("Selection in Edit");
//
//                    //tts.setPitch(2.0f);
//                    if(alredyTyped.charAt(insertion_index) == ' ')
//                    {
//                        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                        tts.speak(searchValue + " Replaced at space" , TextToSpeech.QUEUE_ADD, null, null);
//                    }
//                    else
//                    {
//                        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                        tts.speak(searchValue + " Replaced at \t"+alredyTyped.charAt(insertion_index) , TextToSpeech.QUEUE_ADD, null, null);
//                    }
//                    replaceInEdit();
////                                tts.setPitch(1.0f);
////                                alredyTyped = alredyTyped.replace(alredyTyped.charAt(insertion_index),searchValue.charAt(0));
////                                Log.i("Word",alredyTyped);
////                                allowSearchScan=true;
////                                nav_index=0;
                }
                else
                {
                    //count("Selection");

                    if(searchValue.equals(" "))
                    {
                        //tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
                        //addSpaceAtEnd(tts);
                        alredyTyped = alredyTyped + " ";
                        tts.setPitch(2.0f);
                        tts.speak("space",TextToSpeech.QUEUE_ADD,null,null);
                        tts.setPitch(1.0f);
                    }
                    else
                    {
                        //Here we will right the code for appending the character
                        addAtEnd(tts);
//                                    tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                                    alredyTyped = alredyTyped + searchValue;
//                                    tts.setPitch(2.0f);
//                                    tts.speak(searchValue,TextToSpeech.QUEUE_ADD,null,null);
//                                    tts.setPitch(1.0f);
                    }
                }
            }
            else
            {
                searchValue = suggestions.get(head_point);
                Log.d("gggg","5gggg");

                if(alredyTyped.isEmpty())
                {
                    int searchIndex = suggestions.indexOf(searchValue)+1;
                    int inverseSearchIndex=27-searchIndex;
                    int minOfAboveTwo=Math.min(searchIndex,inverseSearchIndex);
                    //optimalPath=optimalPath+minOfAboveTwo;
                    //Log.d("3","3"+optimalPath);
                }
                else
                {
                    int searchIndex = Math.abs(  ((suggestions.indexOf(searchValue)+1) - (suggestions.indexOf(alredyTyped.substring(alredyTyped.length() - 1))+1)) );
                    int inverseSearchIndex=27-searchIndex;
                    int maxOfAboveTwo=Math.min(searchIndex,inverseSearchIndex);
                    //optimalPath=optimalPath+maxOfAboveTwo;
                    //Log.d("3","3a"+optimalPath);
                }

//                        int searchIndex = suggestions.indexOf(searchValue)+1;
//                        int inverseSearchIndex=27-searchIndex;
//                        int maxOfAboveTwo=Math.min(searchIndex,inverseSearchIndex);
//                        optimalPath=optimalPath+maxOfAboveTwo;

                if(editMode & decision.equals("Insert"))
                {
//                    count("Selection in Edit");
//
//                    //tts.setPitch(2.0f);
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
////                                tts.setPitch(1.0f);
////                                StringBuilder sb = new StringBuilder(alredyTyped);
////                                sb.insert(insertion_index,searchValue);
////                                alredyTyped=sb.toString();
////                                Log.i("Word",alredyTyped);
////                                allowSearchScan=true;
////                                nav_index=0;
                }
                else if(editMode & decision.equals("Replace"))
                {
//                    count("Selection in Edit");
//
//                    //tts.setPitch(2.0f);
//                    if(alredyTyped.charAt(insertion_index) == ' ')
//                    {
//                        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                        tts.speak(searchValue + " Replaced at space" , TextToSpeech.QUEUE_ADD, null, null);
//                    }
//                    else
//                    {
//                        tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                        tts.speak(searchValue + " Replaced at \t"+alredyTyped.charAt(insertion_index) , TextToSpeech.QUEUE_ADD, null, null);
//                    }
//                    replaceInEdit();
////                                tts.setPitch(1.0f);
////                                alredyTyped = alredyTyped.replace(alredyTyped.charAt(insertion_index),searchValue.charAt(0));
////                                Log.i("Word",alredyTyped);
////                                allowSearchScan=true;
////                                nav_index=0;
                }
                else
                {
                    //count("Selection");

                    if(searchValue.equals(" "))
                    {
                        addSpaceAtEnd(tts);
//                                    alredyTyped = alredyTyped +" ";
//                                    tts.setPitch(2.0f);
//                                    tts.speak("space", TextToSpeech.QUEUE_ADD, null, null);
//                                    tts.setPitch(1.0f);
                    }
                    else
                    {
                        //Here we will right the code for appending the character
                        addAtEnd(tts);
//                                  alredyTyped = alredyTyped + searchValue;
//                                  tts.setPitch(2.0f);
//                                  tts.playEarcon(selectEarcon,TextToSpeech.QUEUE_FLUSH,null,null);
//                                  tts.speak(searchValue, TextToSpeech.QUEUE_ADD, null, null);
//                                  tts.setPitch(1.0f);
                    }

                }
            }
        }
    }


    public void alModeSpeakOut(TextToSpeech tts)
    {

        speakOutSelection(tts,alredyTyped);
    }

    public void alModeReset(TextToSpeech tts)
    {
        tts.speak("Stop",TextToSpeech.QUEUE_FLUSH,null,null);
        //tts.playEarcon(flowshift,TextToSpeech.QUEUE_FLUSH,null,null);
        head_point = 0;
        front=false;
        prev=false;
    }

    public void alModeDelete(TextToSpeech tts)
    {
        //count("Deletion");

        if(alredyTyped.isEmpty())
        {
            tts.speak("No Character to Delete", TextToSpeech.QUEUE_FLUSH, null, null);
            nav_index=0;//After Deleting Entirely a word if you select a new word then it should start from first character
        }
        else
        {
            //Normal Deletion from End of the String
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
