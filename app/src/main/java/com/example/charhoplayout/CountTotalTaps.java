package com.example.charhoplayout;

import java.util.HashMap;

public class CountTotalTaps {

    private HashMap<String, Integer> counts; // To Count the Taps

    public CountTotalTaps(HashMap<String, Integer> counts) {

        this.counts = counts;

        this.counts.put("alModeForward", 0);
        this.counts.put("alModeBackward", 0);
        this.counts.put("alModeHopping", 0);
        this.counts.put("alModeSelection", 0);
        this.counts.put("alModeSpeakOut", 0);
        this.counts.put("alMOdeDeletion", 0);
        this.counts.put("alModeReset", 0);

        this.counts.put("nmModeEnter", 0);
        this.counts.put("nmModeExit", 0);
        this.counts.put("nmModeForward", 0);
        this.counts.put("nmModeBackward", 0);
        this.counts.put("nmModeSelection", 0);
        this.counts.put("nmModeDeletion", 0);

        this.counts.put("specialCharModeEnter", 0);
        this.counts.put("specialCharModeExit", 0);
        this.counts.put("specialCharModeForward", 0);
        this.counts.put("specialCharModeBackward", 0);
        this.counts.put("specialCharModeSelection", 0);

        this.counts.put("editModeEnter", 0);
        this.counts.put("editModeExit", 0);
        this.counts.put("editModeForwardNav", 0);
        this.counts.put("editModeDecisionNav", 0);
        this.counts.put("editModeDecisionSelection", 0);
        this.counts.put("editModeDeletion", 0);
    }

    public void performCounting(String val) {
        Integer i = counts.get(val);
        i++;
        counts.put(val, i);
    }

    public CountTotalTaps resetCounting()
    {
         CountTotalTaps countTotalTaps = new CountTotalTaps(new HashMap<String, Integer>());
         return  countTotalTaps;
    }

    public String displayTotalTapsCount()
    {
        String info = "";
        info = "Taps Info:\nAlphabetical Mode:\n\n"
                +"Forward Taps: "+counts.get("alModeForward")
                +"\nBackword Taps: "+counts.get("alModeBackward")
                +"\nSkipping Taps: "+counts.get("alModeHopping")
                +"\nSelection Taps: "+counts.get("alModeSelection")
                +"\nDeletion Taps: "+counts.get("alMOdeDeletion")
                +"\nSpeakOut Taps"+counts.get("alModeSpeakOut")
                +"\nStopping Taps: "+counts.get("alModeReset")
                +"\n\n"+"Number Mode Tapping Info:\n"
                +"Enter Number Mode Taps: "+counts.get("nmModeEnter")
                +"\nExit Number Mode Taps: "+counts.get("nmModeExit")
                +"Forward Taps: "+counts.get("nmModeForward")
                +"\nBackword Taps: "+counts.get("nmModeBackward")
                +"\nSelection Taps: "+counts.get("nmModeSelection")
                +"\nDeletion Taps: "+counts.get("nmModeDeletion")
                +"\n\n"+"Special Char Mode Tapping Info:\n"
                +"Enter Special Char Mode Taps: "+counts.get("specialCharModeEnter")
                +"\nExit Special Char Mode Taps: "+counts.get("specialCharModeExit")
                +"Forward Taps: "+counts.get("specialCharModeForward")
                +"\nBackword Taps: "+counts.get("specialCharModeBackward")
                +"\nSelection Taps: "+counts.get("specialCharModeSelection")
                +"\n\n"+"Edit Mode Tapping Info:\n"
                +"Enter Edit Mode Taps: "+counts.get("editModeEnter")
                +"\nExit Edit Mode Taps: "+counts.get("editModeExit")
                +"\nForward Nav Taps: "+counts.get("editModeForwardNav")
                +"\nEdit Options Nav Taps: "+counts.get("editModeDecisionNav")
                +"\nEdit Options Selection Taps: "+counts.get("editModeDecisionSelection")
                /*+"\nSelection (Edit/Replace) Edit Mode Taps: "+counts.get("Selection in Edit")
                +"\n Deletion in Edit Taps:  "+counts.get("Deletion in Edit")*/;
        int total_taps;

        total_taps = counts.get("alModeForward")
                + counts.get("alModeBackward")
                + counts.get("alModeHopping")
                + counts.get("alModeSelection")
                + counts.get("alMOdeDeletion")
                + counts.get("alModeSpeakOut")
                + counts.get("alModeReset")
                + counts.get("nmModeEnter")
                + counts.get("nmModeExit")
                + counts.get("nmModeForward")
                + counts.get("nmModeBackward")
                + counts.get("nmModeSelection")
                + counts.get("nmModeDeletion")
                + counts.get("specialCharModeEnter")
                + counts.get("specialCharModeExit")
                + counts.get("specialCharModeForward")
                + counts.get("specialCharModeBackward")
                + counts.get("specialCharModeSelection")
                + counts.get("editModeEnter")
                + counts.get("editModeExit")
                + counts.get("editModeForwardNav")
                + counts.get("editModeDecisionNav")
                + counts.get("editModeDecisionSelection");

        return info+"\n\n Total Number Of Taps: "+total_taps;
    }
}

