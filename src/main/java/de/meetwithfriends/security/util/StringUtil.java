package de.meetwithfriends.security.util;

import java.util.Random;

public class StringUtil
{
    public static String convertToHex(byte[] data)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++)
        {
            sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static String getRandomHexString(final int numchars){
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.substring(0, numchars);
    }
}
