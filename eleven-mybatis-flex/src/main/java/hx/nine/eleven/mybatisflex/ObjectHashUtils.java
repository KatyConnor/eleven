package hx.nine.eleven.mybatisflex;

public class ObjectHashUtils {

    public static void main(String[] args) {
        String s1 = "/f是/fs/方式af.exls";
        String s2 = "/sf/ww/fs.docx";
        String s3 = "/s12/gg/asd.text";
        System.out.println("s1 = "+ hashCode(s1));
        System.out.println("s2 = "+ hashCode(s2));
        System.out.println("s3 = "+ hashCode(s3));
    }

    public static String hashCode(String str) {
        char value[] = str.toCharArray();
        StringBuffer hash = new StringBuffer();
        for (int i = 0; i < value.length; i++) {
            int ch = value[i];
            System.out.println("----"+ch);
            hash.append(ch);
        }
        return hash.toString();
    }
}
