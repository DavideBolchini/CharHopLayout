package com.example.charhoplayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.tapwithus.sdk.TapListener;
import com.tapwithus.sdk.TapSdk;
import com.tapwithus.sdk.TapSdkFactory;
import com.tapwithus.sdk.airmouse.AirMousePacket;
import com.tapwithus.sdk.bluetooth.BluetoothManager;
import com.tapwithus.sdk.bluetooth.TapBluetoothManager;
import com.tapwithus.sdk.mouse.MousePacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static com.example.charhoplayout.EarconManager.selectEarcon;

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
    boolean isspMode=false;
    int spModeToggle=0;
    static int toggle;

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

    }

    public TapListener mTap =new TapListener() {

        AlphabetMode alMode = new AlphabetMode(MainActivity.this);
        NumbersMode nmMode = new NumbersMode(MainActivity.this);
        SpecialCharactersMode spMode = new SpecialCharactersMode(MainActivity.this);
        TypedString tyString = new TypedString();
        EditMode edMode = new EditMode(MainActivity.this);

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
            spMode.spModeInitialise();
            tyString.typedStringInitialise();
            EarconManager earconManager = new EarconManager();
            earconManager.setupEarcons(MainActivity.tts,getApplicationContext().getPackageName());
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
        /*
         *  data parameter plays major role : Gestures are mapped to numbers
         * front, back and prev variables are used to Manipulate the index over suggestions
         *
         * When moving forward using Index Finger : Make back = true
         *
         * When moving backward using Middle Finger : Make front = true and prev = true
         *
         * As we start the character scanning we need to character a to be spoken out so we cannot increment first : if that is done then a will never be considered
         * So first a is spoken out and then header is incremented. So always its : header =0 a -> header 1 -> b --> header 2 ...
         *
         * As we have started forward navigation and now we want to go backward : eg. spoken is b but header=2 (c) so we need 2 steps back which is done at Backward navigation code
         * that is the special case rest of the backward navigation requires header--;
         *
         * Similar header Manipulation is required for Selection of Characters :
         *
         * The Selection Code Follows the below code strucuture :-
         *
         * if (prev == false) --> !prev --> !false (true)
         * {
         *           Character Scanning started and never done backward navigation Required get(header - 1)
         * }
         *else(prev will be true)
         * {
         *      if(front == false) --> !front --> !false (true)
         *      {
         *          Backward navigation and then forward navigation : eg: a b c d : c spoken but header at d so get(header - 1)
         *      }
         *      else
         *      {
         *          In backward direction the character need to be selected where the header is so its just get**(header)** and not get(header-1)
         *      }
         * }
         * */
        @Override
        public void onTapInputReceived(@NonNull String tapIdentifier, int data) {
            EarconManager earconManager = new EarconManager();
            earconManager.setupEarcons(MainActivity.tts,getApplicationContext().getPackageName());

            if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspMode==false & data==2) // alMode -> Forward
            {
                alMode.alModeForward(tts);
            }
            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspMode==false & data == 4) // alMode -> Backward
            {
                alMode.alModeBackward(tts);
            }
            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspMode==false & data==6) // alMode -> Hopping
            {
                alMode.alModeHopping(tts);
            }
            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspMode==false & data==1) // alMode -> Selection
            {
                tyString.alreadyTyped = alMode.alModeSelect(tts,tyString.alreadyTyped);
            }
            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspMode==false & data == 30) // alMode -> SpeakOut
            {
                alMode.alModeSpeakOut(tts,tyString.alreadyTyped);
            }
            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspMode==false & data==16) // alMode ->Deletion
            {
                tyString.alreadyTyped = alMode.alModeDelete(tts,tyString.alreadyTyped);
            }
            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspMode==false & data == 8) // alMode -> Reset
            {
                alMode.alModeReset(tts);
            }
            /*
             *  ###########Number Mode Coding Starts Here
             * */
            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspMode==false & data==3) // nmMode -> Enter
            {
                //Enter Number Mode
                tts.speak("Entered Number Mode",TextToSpeech.QUEUE_FLUSH,null,null);
                isNumberMode=true;
                numberModeToggle=1;
            }
            else if(!allowSearchScan & isNumberMode==true & numberModeToggle==1 & isDatabaseMode==false & isspMode==false & data==3) // nmMode -> Exit
            {
                //Exit Number Mode
                tts.speak("Exit Number Mode",TextToSpeech.QUEUE_FLUSH,null,null);
                isNumberMode=false;
                numberModeToggle=0;
                nmMode.numberHeadPoint = 0;
            }
            else if(!allowSearchScan & isNumberMode==true & numberModeToggle==1 & isDatabaseMode==false & isspMode==false & data==2) // nmMode -> Forward
            {
                nmMode.nmModeForward(tts);
            }
            else if(!allowSearchScan & isNumberMode==true & numberModeToggle==1 & isDatabaseMode==false & isspMode==false & data==4) // nmMode -> Backward
            {
                nmMode.nmModeBackward(tts);
            }
            else if(!allowSearchScan & isNumberMode==true & numberModeToggle==1 & isDatabaseMode==false & isspMode==false & data==1) // nmMode -> Selection
            {
                tyString.alreadyTyped = nmMode.nmModeSelection(tts,tyString.alreadyTyped);
            }
            else if(!allowSearchScan & isNumberMode==true & numberModeToggle==1 & isDatabaseMode==false & isspMode==false & data==16) // nmMode -> Delete
            {
                tyString.alreadyTyped = nmMode.nmModeDeletion(tts,tyString.alreadyTyped);
            }
            /*
             *  ###########Special Characters Mode Coding Starts Here
             * */
            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspMode==false & data==5) // spMode -> Enter
            {
                tts.speak("Entered Special Characters Mode",TextToSpeech.QUEUE_FLUSH,null,null);
                isspMode=true;
                spModeToggle=1;
            }
            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspMode==true & spModeToggle==1  & data==5) // spMode -> Exit
            {
                tts.speak("Exit Special Characters Mode",TextToSpeech.QUEUE_FLUSH,null,null);
                isspMode=false;
                spModeToggle=0;
                spMode.spHeadPoint=0;
            }
            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspMode==true & spModeToggle==1 & data==2) // spMode -> Forward
            {
                spMode.spModeForward(tts);
            }
            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspMode==true & spModeToggle==1 & data==4) // spMode -> Backward
            {
                spMode.spModeBackward(tts);
            }
            else if(!allowSearchScan & isNumberMode==false & numberModeToggle==0 & isDatabaseMode==false & isspMode==true & data==1) // spMode -> Selection
            {
                tyString.alreadyTyped = spMode.spModeSelection(tts,tyString.alreadyTyped);
            }
            /*
             *  ###########Edit Mode Coding Starts Here
             * */
            else if(!allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspMode==false & data== 14 & toggle==0)//Index + Middle Finger ==> Enter In Edit Mode
            {
                edMode.edModeInitialise(tts,tyString.alreadyTyped,getApplicationContext());
            }
            else if(/*allowSearchScan==true &*/ isNumberMode==false & isDatabaseMode==false & isspMode==false & data== 14 & toggle==1 & EditMode.editMode)//Index + Middle ==> Exit Edit Mode
            {
                tts.speak("Exit Edit Mode ",TextToSpeech.QUEUE_FLUSH,null,null);
                toggle=0;
                allowSearchScan=false;
                EditMode.editMode=false;
            }
            else if(allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspMode==false & data == 2)//Index Finger to Navigate in Selected Word
            {
                edMode.edModeForwardNav(tts,tyString.alreadyTyped);
            }
            else if(allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspMode==false & data==8)//RIng Finger for Decision Making
            {
                tts.speak("Decision Navigations ",TextToSpeech.QUEUE_FLUSH,null,null);
                edMode.edModeDecisionNav(tts);
            }
            else if(allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspMode==false & data ==1) // Decision Selection in Edit Mode
            {
                edMode.edModeDecisionSelection(tts,tyString.alreadyTyped);
            }
            else if(allowSearchScan & isNumberMode==false & isDatabaseMode==false & isspMode==false & data==16)//Deletion in Edit Mode allowSearch Scan
            {
                tyString.alreadyTyped = edMode.edModeDeletion(tts,tyString.alreadyTyped);
            }
            /*
             *   #####Autosuggestions Mode
             * */

            /*
             *  ###########Database Mode Coding Starts Here
             * */

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
