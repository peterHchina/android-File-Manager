package com.example.managerfile.util;

import java.util.regex.Pattern;


import android.R.integer;


public class StringUtil {
    private static final byte[] CHARS = {
            65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86,
            87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111,
            112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55,
            56, 57
    };

    public static int getByteCnt(String paramString) 
    {
        if ((paramString == null) || (paramString.length() == 0))
        {
        	return 0;
        }

        int byteCnt = 0;
        int len = paramString.length();
        for(int index=0; index<len; index++)
        {
            int ch = Character.codePointAt(paramString, index);
            if ((ch >= 0) && (ch <= 255))
            {
            	byteCnt += 1;
            }
            else
            {
            	byteCnt += 2;
            }
        }
        
        return byteCnt;    
    }
    
    /**
     * 返回字符串paramString的前paramByteCnt个字节的字符个数。
     * @param paramString
     * @param paramByteCnt
     * @return
     */
    public static int getStringCnt(String paramString, int paramByteCnt)
    {
        if ((paramString == null) || (paramString.length() == 0))
        {
        	return 0;
        }
        
        int len = paramString.length();
        if(paramByteCnt <= 0)
        {
        	return len;
        }
        
        int byteCnt = 0;
        int strCnt=0; 
        int index = 0;
        for(index=0; index<len; index++)
        {
            int ch = Character.codePointAt(paramString, index);
            if ((ch >= 0) && (ch <= 255))
            {
            	byteCnt += 1;
            }
            else
            {
            	byteCnt += 2;
            }
            
            if(byteCnt == paramByteCnt)
            {
            	strCnt = index + 1;
            	break;
            }
            else if(byteCnt > paramByteCnt)
            {
            	strCnt = index;
            	break;
            }
        }
        
        if(index >= len)
        {
        	strCnt = len;
        }
        
        return strCnt;    
    }

//    public static String getRandomString(int paramInt) {
//        long l = System.currentTimeMillis();
//        Random localRandom = new Random(l);
//        byte[] arrayOfByte1 = new byte[paramInt];
//        int i = 0;
//        while (true) {
//            if (i >= paramInt)
//                return new String(arrayOfByte1);
//            byte[] arrayOfByte2 = CHARS;
//            int j = CHARS.length;
//            int k = localRandom.nextInt(j);
//            int m = arrayOfByte2[k];
//            arrayOfByte1[i] = m;
//            i += 1;
//        }
//    }

    public static boolean isBlank(String paramString) {
        if (paramString != null) 
        {
        	final int length = paramString.length();
        	for(int i = 0; i < length; i ++)
        	{
        		 if (!Character.isWhitespace(paramString.charAt(i)))
        			 return false;
        	}
        }
        return true;
    }

    public static boolean isEmpty(String paramString) {
        if ((paramString != null) && (paramString.trim().length() != 0)) {
            return false;
        }
        return true;
    }

    public static boolean isEquals(String paramString1, String paramString2) {
        boolean bool = false;
        if (paramString1 == paramString2)
            bool = true;
        else if (paramString1 != null)
        {
        	bool = paramString1.equals(paramString2);
        }
        return bool;
    }

    public static boolean isNotBlank(String paramString) {
       return !isBlank(paramString);
    }

    public static boolean isNotEmpty(String paramString) {
        return !isEmpty(paramString);
    }

//    public static String join(Object[] paramArrayOfObject, String paramString) {
//        if (paramArrayOfObject == null)
//            ;
//        int i;
//        for (String str = null;; str = join(paramArrayOfObject, paramString, 0, i)) {
//            return str;
//            i = paramArrayOfObject.length;
//        }
//    }

//    public static String join(Object[] paramArrayOfObject, String paramString, int paramInt1, int paramInt2)
//  {
//    if (paramArrayOfObject == null);
//    int i;
//    for (String str = null; ; str = "")
//    {
//      return str;
//      if (paramString == null)
//        paramString = "";
//      i = paramInt2 - paramInt1;
//      if (i > 0)
//        break;
//    }
//    int j = i * 16;
//    StringBuilder localStringBuilder1 = new StringBuilder(j);
//    int k = paramInt1;
//    while (true)
//    {
//      if (k >= paramInt2)
//      {
//        str = localStringBuilder1.toString();
//        break;
//      }
//      if (k > paramInt1)
//        StringBuilder localStringBuilder2 = localStringBuilder1.append(paramString);
//      if (paramArrayOfObject[k] != 0)
//      {
//        Object localObject = paramArrayOfObject[k];
//        StringBuilder localStringBuilder3 = localStringBuilder1.append(localObject);
//      }
//      k += 1;
//    }
//  }

    //guotao@add@截取string部分字符
    public static String subStringByByte(String paramString, int beginIndex, int endIndex) 
    {
        if ((paramString == null) || (paramString.length() == 0))
        {
        	return "";
        }
        
        if(beginIndex >= endIndex)
        {
        	return "";
        }
        
        int byteLength = 0;  
        String returnString = "";  
        for (int i = 0; i < paramString.length(); i++) 
        {  
            char c = paramString.charAt(i);
            byteLength += (0 <= c && c <= 255) ? 1 : 2;  
            
            if(byteLength > endIndex)
            {
            	break;
            }
            
            if (byteLength > beginIndex) 
            {
            	returnString += c; 
            }
        }  
      
        return returnString;  
    }
    
 
    
    public static boolean isNumeric(String str){ 
        Pattern pattern = Pattern.compile("[0-9]*"); 
        return pattern.matcher(str).matches();    
     } 


}
