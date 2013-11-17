package de.meetwithfriends.security.util;

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

}
