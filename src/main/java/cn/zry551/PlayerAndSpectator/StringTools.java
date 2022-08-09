package cn.zry551.PlayerAndSpectator;

public class StringTools {
    public static String ColorChar = "§";

    public static String HandleColorString(String NoColorStr){
        String Then = NoColorStr.replaceAll("#CC#",ColorChar);
        Then = Then.replaceAll("&",ColorChar);
        Then = Then.replaceAll("#AND#","&");
        return Then;



    }

    public static String ReplaceString(String NoColorStr,String PartName,String Strings){
        String Then = NoColorStr;
        Then = Then.replaceAll("@" + PartName + "@",Strings);
        return Then;
    }

    public static String ReplaceStringWithColor(String NoColorStr,String PartName,String Strings){
        String Then = HandleColorString(NoColorStr);
        Then = Then.replaceAll("@" + PartName + "@",Strings);
        return Then;
    }
}
