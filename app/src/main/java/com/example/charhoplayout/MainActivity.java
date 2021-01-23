package com.example.charhoplayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tapwithus.sdk.TapListener;
import com.tapwithus.sdk.TapSdk;
import com.tapwithus.sdk.TapSdkFactory;
import com.tapwithus.sdk.airmouse.AirMousePacket;
import com.tapwithus.sdk.bluetooth.BluetoothManager;
import com.tapwithus.sdk.bluetooth.TapBluetoothManager;
import com.tapwithus.sdk.mouse.MousePacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    /*
     * Variable Declaration
     * */
    public static TextToSpeech tts;         // Text To Speech Engine

    static boolean allowSearchScan=false;

    /*
     * Variable Declaration for Number Mode
     * */
    boolean isNumberMode=false;
    int numberModeToggle=0;

    boolean isDatabaseMode=false;

    /*
     * Variable Declaration for Special Character Mode
     * */
    boolean isspecialCharMode=false;
    int spModeToggle=0;
    static int toggle;

    /*
     * Variable Declaration for Auto-Suggestions Mode
     * */
    static boolean isAutoSuggestionMode=false;
    static int autoSuggestionModeToggle=0;
    ArrayList<String> SuggestionsResult = new ArrayList<String>();

    /*
    * Buttons and TextView Declaration
    * */
    Button btnGetInfo,btnResetInfo;
    TextView tvInfo;

    /*
    * Count Total Taps Declaration
    * */
    CountTotalTaps countTotalTaps;

    int inNumberMode=0 ;
    int inSpecialCharMode=0;
    int inEditMode = 0;
    int inAutoSuggestionMode=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothManager bluetoothManager = new BluetoothManager(getApplicationContext(), BluetoothAdapter.getDefaultAdapter()); // Initialise Bluetooth Manager
        TapBluetoothManager tapBluetoothManager = new TapBluetoothManager(bluetoothManager);
        TapSdk sdk = new TapSdk(tapBluetoothManager);     // Connect Bluetooth Manager with Tap Strap SDK

        TapSdkFactory.getDefault(getApplicationContext());
        sdk.registerTapListener(mTap);

        tts = new TextToSpeech(this,MainActivity.this);

        btnGetInfo = findViewById(R.id.btnGetInfo);
        btnResetInfo = findViewById(R.id.btnReset);
        tvInfo = findViewById(R.id.tvInfo);

        countTotalTaps = new CountTotalTaps(new HashMap<String, Integer>());

        btnGetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res = countTotalTaps.displayTotalTapsCount();
                tvInfo.setText(res);
                tvInfo.setTextColor(Color.RED);
            }
        });

        btnResetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTotalTaps = countTotalTaps.resetCounting();
                String res = countTotalTaps.displayTotalTapsCount();
                tvInfo.setText(res);
                tvInfo.setTextColor(Color.RED);
            }
        });
    }

    public TapListener mTap =new TapListener() {

        AlphabetMode alMode = new AlphabetMode(MainActivity.this);
        NumbersMode nmMode = new NumbersMode(MainActivity.this);
        SpecialCharactersMode specialCharMode = new SpecialCharactersMode(MainActivity.this);
        TypedString tyString = new TypedString();
        EditMode edMode = new EditMode(MainActivity.this);
        AutoSuggestionsMode autoSuggestionsMode = new AutoSuggestionsMode();
        HashMap<String, Runnable> commands = new HashMap<>();


        @Override
        public void onBluetoothTurnedOn() {
        }

        @Override
        public void onBluetoothTurnedOff() {

        }

        @Override
        public void onTapStartConnecting(@NonNull String tapIdentifier) {

        }

        @Override
        public void onTapConnected(@NonNull String tapIdentifier) {
            alMode.speakOut(tts,"Tap Strap connected to the phone   You can start keyflow");
            alMode.alModeInitialise();
            nmMode.nmModeInitialise();
            specialCharMode.spModeInitialise();
            tyString.typedStringInitialise();
            //edMode.edModeInitialise(tts,tyString.alreadyTyped,getApplicationContext());

            EarconManager earconManager = new EarconManager();
            earconManager.setupEarcons(MainActivity.tts,getApplicationContext().getPackageName());

            commands = loadAlphabletModeCommands(commands,tyString.alreadyTyped,tyString.word);

        }

        @Override
        public void onTapDisconnected(@NonNull String tapIdentifier) {
            alMode.speakOut(tts,"Tap strap not connected to the phone");

        }

        @Override
        public void onTapResumed(@NonNull String tapIdentifier) {

        }

        @Override
        public void onTapChanged(@NonNull String tapIdentifier) {

        }

        @Override
        public void onControllerModeStarted(@NonNull String tapIdentifier) {

        }

        @Override
        public void onTextModeStarted(@NonNull String tapIdentifier) {

        }

        @Override
        public void onTapInputReceived(@NonNull String tapIdentifier, int data) {
            EarconManager earconManager = new EarconManager();
            earconManager.setupEarcons(MainActivity.tts,getApplicationContext().getPackageName());

            try
            {
                switch (data)
                {
                    case 2:
                        commands.get("forward").run();
                        break;

                    case 4:
                        commands.get("backward").run();
                        break;

                    case 6:
                        commands.get("hopping").run();
                        break;

                    case 1:
                        if(inEditMode == 1)
                        {
                            commands.get("selection").run();
                            EditMode.editMode = true;
                            commands.clear();
                            commands = loadAlphabletModeCommands(commands,tyString.alreadyTyped,tyString.word);
                            inEditMode =0;
                        }
                        else
                        {
                            commands.get("selection").run();
                            if (EditMode.editMode)
                            {
                                commands.clear();
                                commands = loadEditModeCommands(commands,tyString.alreadyTyped,tyString.word);
                            }
                        }
                        break;

                    case 16:
                        commands.get("deletion").run();
                        break;

                    case 30:
                        commands.get("speakout").run();
                        break;

                    case 8:
                        commands.get("reset").run();
                        break;

                    case 3:
                        //Enter Number Mode
                        if(inNumberMode == 0)
                        {
                            inNumberMode = 1;
                            tts.speak("Enter Number Mode",TextToSpeech.QUEUE_FLUSH,null,null);
                            commands = loadNumberModeCommands(commands,tyString.alreadyTyped,tyString.word);
                        }
                        //Exit Number Mode
                        else
                        {
                            commands = loadAlphabletModeCommands(commands,tyString.alreadyTyped,tyString.word);
                            tts.speak("Exit Number Mode",TextToSpeech.QUEUE_FLUSH,null,null);
                            inNumberMode = 0;
                        }
                        break;

                    case 5:
                        //Enter Special Character Mode
                        if(inSpecialCharMode == 0)
                        {
                            inSpecialCharMode = 1;
                            tts.speak("Enter Special Character Mode",TextToSpeech.QUEUE_FLUSH,null,null);
                            commands = loadSpecialCharacterModeCommands(commands,tyString.alreadyTyped,tyString.word);
                        }
                        //Exit Special Character Mode
                        else
                        {
                            inSpecialCharMode = 0;
                            commands = loadAlphabletModeCommands(commands,tyString.alreadyTyped,tyString.word);
                            tts.speak("Exit Special Character Mode",TextToSpeech.QUEUE_FLUSH,null,null);
                        }
                        break;
                    case 14:
                        //Enter Edit Mode
                        if(inEditMode == 0 & EditMode.editMode==false)
                        {
                            inEditMode = 1;
                            if(tyString.word.length() == 0)
                            {
                                //tyString.alreadyTyped = tyString.alreadyTyped;
                            }
                            else if(!tyString.alreadyTyped.contains(tyString.word))
                            {
                                if(tyString.alreadyTyped.length() == 0)
                                {
                                    tyString.alreadyTyped = tyString.word;
                                }
                                else
                                {
                                    tyString.alreadyTyped = tyString.alreadyTyped +" "+tyString.word;
                                }
                            }
                            tyString.word = "";
                            Log.d("TypedWord",tyString.word);
                            Log.d("TypedString",tyString.alreadyTyped);

                            edMode.edModeInitialise(tts,tyString.alreadyTyped,getApplicationContext());

                            commands = loadEditModeCommands(commands,tyString.alreadyTyped,tyString.word);
                        }
                        //Exit Edit Mode
                        else if (EditMode.editMode==true)
                        {
                            inEditMode = 0;
                            EditMode.editMode = false;
                            commands = loadAlphabletModeCommands(commands,tyString.alreadyTyped,tyString.word);
                            tts.speak("Exit Edit Mode",TextToSpeech.QUEUE_FLUSH,null,null);
                        }
                        break;

                    case 9:
                        //Enter Autosuggestion Mode
                        if(inAutoSuggestionMode == 0)
                        {
                            SuggestionsResult = autoSuggestionsMode.fetchAutoSuggestions(getApplicationContext(),tts,tyString.word);
                            commands = loadAutoSuggestionCommands(commands,SuggestionsResult);
                            inAutoSuggestionMode = 1;
                        }
                        //Exit Autosuggestion Mode
                        else if(inAutoSuggestionMode == 1)
                        {
                            tts.speak("Exit Autosuggestion Mode",TextToSpeech.QUEUE_FLUSH,null,null);
                            commands = loadAlphabletModeCommands(commands,tyString.alreadyTyped,tyString.word);
                            inAutoSuggestionMode = 0;
                        }
                        break;

                    default:
                        //No Gesture found
                        tts.speak("No Such Gesture",TextToSpeech.QUEUE_FLUSH,null,null);
                }
            }
            catch(NullPointerException npe)
            {
                tts.speak("Gesture Not Recognised",TextToSpeech.QUEUE_FLUSH,null,null);
            }


//            // alMode -> Forward
//            if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data==2)
//            {
//                alMode.alModeForward(tts);
//                //commands.get("forward").run();
//                countTotalTaps.performCounting("alModeForward");
//            }
//            // alMode -> Backward
//            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data == 4)
//            {
//                alMode.alModeBackward(tts);
//                countTotalTaps.performCounting("alModeBackward");
//            }
//            // alMode -> Hopping
//            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data==6)
//            {
//                alMode.alModeHopping(tts);
//                countTotalTaps.performCounting("alModeHopping");
//            }
//            // alMode -> Selection
//            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data==1)
//            {
//                //tyString.alreadyTyped = alMode.alModeSelect(tts,tyString.alreadyTyped,tyString.word);
//                ArrayList<String> results;
//                results = alMode.alModeSelect(tts,tyString.alreadyTyped,tyString.word);
//                tyString.word = results.get(0);
//                tyString.alreadyTyped = results.get(1);
//
//                Log.d("TypedWord",tyString.word);
//                Log.d("TypedString",tyString.alreadyTyped);
//
//                countTotalTaps.performCounting("alModeSelection");
//            }
//            // alMode -> SpeakOut
//            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data == 30)
//            {
//                alMode.alModeSpeakOut(tts,tyString.alreadyTyped+" "+tyString.word);
//                countTotalTaps.performCounting("alModeSpeakOut");
//            }
//            // alMode ->Deletion
//            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data==16)
//            {
//                ArrayList<String> results;
//                results = alMode.alModeDelete(tts,tyString.alreadyTyped,tyString.word);
//                tyString.word = results.get(0);
//                tyString.alreadyTyped = results.get(1);
//
//                Log.d("TypedWord",tyString.word);
//                Log.d("TypedString",tyString.alreadyTyped);
//
//                countTotalTaps.performCounting("alMOdeDeletion");
//            }
//            // alMode -> Reset
//            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data == 8)
//            {
//                alMode.alModeReset(tts);
//                countTotalTaps.performCounting("alModeReset");
//            }
//            /*
//             *  ###########Number Mode Coding Starts Here
//             * */
//            // nmMode -> Enter
//            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data==3)
//            {
//                tts.speak("Entered Number Mode",TextToSpeech.QUEUE_FLUSH,null,null);
//                isNumberMode=true;
//                numberModeToggle=1;
//                countTotalTaps.performCounting("nmModeEnter");
//            }
//            //nmMode -> Exit
//            else if(!allowSearchScan & isNumberMode==true & numberModeToggle==1 & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data==3) // nmMode -> Exit
//            {
//                tts.speak("Exit Number Mode",TextToSpeech.QUEUE_FLUSH,null,null);
//                isNumberMode=false;
//                numberModeToggle=0;
//                nmMode.numberHeadPoint = 0;
//                countTotalTaps.performCounting("nmModeExit");
//            }
//            // nmMode -> Forward
//            else if(!allowSearchScan & isNumberMode==true & numberModeToggle==1 & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data==2)
//            {
//                nmMode.nmModeForward(tts);
//                countTotalTaps.performCounting("nmModeForward");
//            }
//            // nmMode -> Backward
//            else if(!allowSearchScan & isNumberMode==true & numberModeToggle==1 & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data==4)
//            {
//                nmMode.nmModeBackward(tts);
//                countTotalTaps.performCounting("nmModeBackward");
//            }
//            // nmMode -> Selection
//            else if(!allowSearchScan & isNumberMode==true & numberModeToggle==1 & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data==1)
//            {
//                //tyString.alreadyTyped = nmMode.nmModeSelection(tts,tyString.alreadyTyped);
//                ArrayList<String> results;
//                results = nmMode.nmModeSelection(tts,tyString.alreadyTyped,tyString.word);
//                tyString.word = results.get(0);
//                tyString.alreadyTyped = results.get(1);
//
//                Log.d("TypedWord",tyString.word);
//                Log.d("TypedString",tyString.alreadyTyped);
//
//                countTotalTaps.performCounting("nmModeSelection");
//            }
//            // nmMode -> Delete
//            else if(!allowSearchScan & isNumberMode==true & numberModeToggle==1 & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data==16)
//            {
//                ArrayList<String> results;
//                results = nmMode.nmModeDeletion(tts,tyString.alreadyTyped,tyString.word);
//                tyString.word = results.get(0);
//                tyString.alreadyTyped = results.get(1);
//
//                Log.d("TypedWord",tyString.word);
//                Log.d("TypedString",tyString.alreadyTyped);
//
//                countTotalTaps.performCounting("nmModeDeletion");
//            }
//            /*
//             *  ###########Special Characters Mode Coding Starts Here
//             * */
//            // spMode -> Enter
//            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data==5)
//            {
//                tts.speak("Entered Special Characters Mode",TextToSpeech.QUEUE_FLUSH,null,null);
//                isspecialCharMode=true;
//                spModeToggle=1;
//                countTotalTaps.performCounting("specialCharModeEnter");
//            }
//            // spMode -> Exit
//            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspecialCharMode==true & spModeToggle==1  & isAutoSuggestionMode==false & data==5)
//            {
//                tts.speak("Exit Special Characters Mode",TextToSpeech.QUEUE_FLUSH,null,null);
//                isspecialCharMode=false;
//                spModeToggle=0;
//                specialCharMode.spHeadPoint=0;
//                countTotalTaps.performCounting("specialCharModeExit");
//            }
//            // spMode -> Forward
//            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspecialCharMode==true & spModeToggle==1 & isAutoSuggestionMode==false & data==2)
//            {
//                specialCharMode.spModeForward(tts);
//                countTotalTaps.performCounting("specialCharModeForward");
//            }
//            // spMode -> Backward
//            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspecialCharMode==true & spModeToggle==1 & isAutoSuggestionMode==false & data==4)
//            {
//                specialCharMode.spModeBackward(tts);
//                countTotalTaps.performCounting("specialCharModeBackward");
//            }
//            // spMode -> Selection
//            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspecialCharMode==true & spModeToggle==1 & isAutoSuggestionMode==false & data==1)
//            {
//                //tyString.alreadyTyped = specialCharMode.spModeSelection(tts,tyString.alreadyTyped,tyString.word);
//                ArrayList<String> results;
//                results = specialCharMode.spModeSelection(tts,tyString.alreadyTyped,tyString.word);
//                tyString.word = results.get(0);
//                tyString.alreadyTyped = results.get(1);
//
//                Log.d("TypedWord",tyString.word);
//                Log.d("TypedString",tyString.alreadyTyped);
//
//                countTotalTaps.performCounting("specialCharModeSelection");
//            }
//            // spMode -> Deletion
//            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspecialCharMode==true & spModeToggle==1 & isAutoSuggestionMode==false & data==16)
//            {
//                ArrayList<String> results;
//                results = specialCharMode.spModeDeletion(tts,tyString.alreadyTyped,tyString.word);
//                tyString.word = results.get(0);
//                tyString.alreadyTyped = results.get(1);
//
//                Log.d("TypedWord",tyString.word);
//                Log.d("TypedString",tyString.alreadyTyped);
//
//                countTotalTaps.performCounting("specialCharModeDeletion");
//            }
//            /*
//             *  ###########Edit Mode Coding Starts Here
//             * */
//            //Index + Middle + Ring Finger ==> Enter In Edit Mode
//            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data== 14 & toggle==0)
//            {
//                if(tyString.word.length() == 0)
//                {
//                    //tyString.alreadyTyped = tyString.alreadyTyped;
//                }
//                else if(!tyString.alreadyTyped.contains(tyString.word))
//                {
//                    tyString.alreadyTyped = tyString.alreadyTyped +" "+tyString.word;
//                }
//                tyString.word = "";
//
//                Log.d("TypedWord",tyString.word);
//                Log.d("TypedString",tyString.alreadyTyped);
//
//                edMode.edModeInitialise(tts,tyString.alreadyTyped,getApplicationContext());
//                countTotalTaps.performCounting("editModeEnter");
//            }
//            //Index + Middle + Ring ==> Exit Edit Mode
//            else if(/*allowSearchScan==true &*/ isNumberMode==false & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data== 14 & toggle==1 & EditMode.editMode)
//            {
//                tts.speak("Exit Edit Mode ",TextToSpeech.QUEUE_FLUSH,null,null);
//                toggle=0;
//                allowSearchScan=false;
//                EditMode.editMode=false;
//                countTotalTaps.performCounting("editModeExit");
//            }
//            //Index Finger to Navigate in Selected Word
//            else if(allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false &  data == 2)
//            {
//                edMode.edModeForwardNav(tts,tyString.alreadyTyped);
//                countTotalTaps.performCounting("editModeForwardNav");
//            }
//            //RIng Finger for Decision Making
//            else if(allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data==8)
//            {
//                edMode.edModeDecisionNav(tts);
//                countTotalTaps.performCounting("editModeDecisionNav");
//            }
//            // Decision Selection in Edit Mode
//            else if(allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data ==1)
//            {
//                edMode.edModeDecisionSelection(tts,tyString.alreadyTyped);
//                countTotalTaps.performCounting("editModeDecisionSelection");
//            }
//            //Deletion in Edit Mode allowSearch Scan
//            else if(allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & data==16)
//            {
//                tyString.alreadyTyped = edMode.edModeDeletion(tts,tyString.alreadyTyped);
//                countTotalTaps.performCounting("editModeDeletion");
//            }
//            /*
//             *   #####Autosuggestions Mode
//             * */
//            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==false & autoSuggestionModeToggle ==0 & data==9)
//            {
//                Log.d("Reaching Here","Reached");
//                SuggestionsResult = autoSuggestionsMode.fetchAutoSuggestions(getApplicationContext(),tts,tyString.word);
//                //isAutoSuggestionMode=true;
//
//            }
//            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==true & autoSuggestionModeToggle ==1 & data==9)
//            {
//                tts.speak("Exit AutoSuggestions Mode ",TextToSpeech.QUEUE_FLUSH,null,null);
//                isAutoSuggestionMode=false;
//                autoSuggestionModeToggle=0;
//            }
//            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==true & autoSuggestionModeToggle ==1 & data==2) // Forward Navigation in AutoSuggestion Mode
//            {
//                autoSuggestionsMode.forwardNavigateSuggestions(tts,SuggestionsResult);
//            }
//            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspecialCharMode==false & isAutoSuggestionMode==true & autoSuggestionModeToggle ==1 & data==1)//Selection in AutoSuggestion Mode
//            {
//                //tyString.word = "";
//                tyString.word = autoSuggestionsMode.selectAutoSuggestion(tts);
//                Log.d("TypedWord",tyString.word);
//                Log.d("TypedString",tyString.alreadyTyped);
//            }
//
//            /*
//             *  ###########Database Mode Coding Starts Here
//             * */
        }

        @Override
        public void onMouseInputReceived(@NonNull String tapIdentifier, @NonNull MousePacket data) {

        }

        @Override
        public void onAirMouseInputReceived(@NonNull String tapIdentifier, @NonNull AirMousePacket data) {

        }

        @Override
        public void onError(@NonNull String tapIdentifier, int code, @NonNull String description) {

        }

        private HashMap<String,Runnable> loadNumberModeCommands(HashMap<String,Runnable> commands, String alredyTyped, String word)
        {
            commands.clear();

            commands.put("forward", ()-> nmMode.nmModeForward(tts));
            commands.put("backward", () -> nmMode.nmModeBackward(tts));

            commands.put("selection", () -> {
                ArrayList<String> results;
                results = nmMode.nmModeSelection(tts, alredyTyped, word);
                tyString.word = results.get(0);
                tyString.alreadyTyped = results.get(1);
            });

            commands.put("deletion", () -> {
                ArrayList<String> results;
                results = nmMode.nmModeDeletion(tts, alredyTyped, word);
                tyString.word = results.get(0);
                tyString.alreadyTyped = results.get(1);
            });

            return commands;
        }

        private HashMap<String, Runnable> loadSpecialCharacterModeCommands(HashMap<String,Runnable> commands,String alredyTyped, String word)
        {
            commands.clear();
            commands.put("forward", () -> specialCharMode.spModeForward(tts));
            commands.put("backward", () -> specialCharMode.spModeBackward(tts));

            commands.put("selection", () -> {
                ArrayList<String> results;
                results = specialCharMode.spModeSelection(tts,alredyTyped,word);
                tyString.word = results.get(0);
                tyString.alreadyTyped = results.get(1);
            });

            commands.put("deletion", () -> {
                ArrayList<String> results;
                results = specialCharMode.spModeDeletion(tts,alredyTyped,word);
                tyString.word = results.get(0);
                tyString.alreadyTyped = results.get(1);
            });
            return commands;
        }

        private HashMap<String, Runnable> loadAlphabletModeCommands(HashMap<String,Runnable> commands,String alredyTyped,String word)
        {
            commands.clear();
            commands.put("forward", () -> alMode.alModeForward(tts));
            commands.put("backward", () -> alMode.alModeBackward(tts));
            commands.put("hopping", () -> alMode.alModeHopping(tts));

            commands.put("selection", () -> {
                ArrayList<String> results;
                results = alMode.alModeSelect(tts, tyString.alreadyTyped, tyString.word);
                tyString.word = results.get(0);
                tyString.alreadyTyped = results.get(1);
            });

            commands.put("deletion", () -> {
                ArrayList<String> results;
                results = alMode.alModeDelete(tts, tyString.alreadyTyped, tyString.word);
                tyString.word = results.get(0);
                tyString.alreadyTyped = results.get(1);
            });
            commands.put("speakout", () -> alMode.alModeSpeakOut(tts,tyString.alreadyTyped+" "+tyString.word));
            commands.put("reset", () -> alMode.alModeReset(tts));
            return commands;
        }

        private HashMap<String,Runnable> loadEditModeCommands(HashMap<String,Runnable> commands,String alredyTyped,String word)
        {
            inEditMode = 1;

            commands.clear();

            commands.put("forward",() -> edMode.edModeForwardNav(tts,tyString.alreadyTyped));

            commands.put("reset", () -> edMode.edModeDecisionNav(tts));

            commands.put("selection", () -> edMode.edModeDecisionSelection(tts,tyString.alreadyTyped));

            return commands;
        }

        private HashMap<String, Runnable> loadAutoSuggestionCommands(HashMap<String,Runnable> commands, ArrayList<String> SuggestionsResult)
        {
            inAutoSuggestionMode = 1;
            commands.clear();
            commands.put("forward",()-> autoSuggestionsMode.forwardNavigateSuggestions(tts,SuggestionsResult));

            commands.put("selection", () -> tyString.word = autoSuggestionsMode.selectAutoSuggestion(tts));

            return commands;

        }
    };


    @Override
    public void onInit(int status) {
        if(status==TextToSpeech.SUCCESS)
        {
            int result= tts.setLanguage(Locale.US);
            tts.setSpeechRate(1.5f);
            tts.setPitch(1.0f);

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS","This Language is not Supported");
            }
        }
        else
        {
            Log.e("TTS","Initialization Failed! Activity");
        }
    }
}
