import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class Main
{
    private static final int capA = 65;
    private static final int capZ = 90;
    private static final int lowA = 97;
    private static final int lowZ = 122;
    private static final String outPut = "data/outPut.txt";

    public static void main(String[]args) throws IOException
    {
        TreeMap<String, String> hashMap = new TreeMap<>();
        for(;;)
        {
            System.out.println("Start system");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            String[] r = command.split("\\-");

            for (int i = 0; i < r.length; i++)
            {
                if (r[i].contains("mode")) {
                    hashMap.put("mode", r[i].substring(4).trim());
                    continue;
                }
                if (r[i].contains("key")) {
                    hashMap.put("key", r[i].substring(4).trim());
                    continue;
                }
                if (r[i].contains("dta")) {
                    hashMap.put("dta", r[i].substring(4).trim());
                    continue;
                }
                if (r[i].contains("alg")) {
                    hashMap.put("alg", r[i].substring(4).trim());
                    continue;
                }
                if (r[i].contains("in")) {
                    hashMap.put("in", r[i].substring(3).trim());
                    continue;
                }
                if (r[i].contains("out")) {
                    hashMap.put("out", r[i].substring(4).trim());
                }
            }

            boolean key = hashMap.containsKey("key");
            boolean mode = hashMap.containsKey("mode");
            boolean dta = hashMap.containsKey("dta");
            boolean in = hashMap.containsKey("in");
            boolean dataAndIn = (dta && in);
            boolean out = hashMap.containsKey("out");

            boolean enc = Objects.equals(hashMap.get("mode"), "enc");
            boolean dec = Objects.equals(hashMap.get("mode"), "dec");

//            boolean alg = hashMap.containsKey("alg");
//            boolean unicode = hashMap.containsValue("unicode");
//            boolean shift = hashMap.containsValue("shift");

            String output = hashMap.get("out");

            if (key)
            {
                int count = Integer.parseInt(hashMap.get("key"));
                if (dta || dataAndIn)
                {
                    String dataString = hashMap.get("dta");
                    if (!mode || (mode && enc))
                    {
                        StringBuilder stringBuilder = encryption(dataString, count);
                        writer(out, stringBuilder, output);
                    }
                    if (mode && dec)
                    {
                        StringBuilder stringBuilder = decryption(dataString, count);
                        writer(out, stringBuilder, output);
                    }
                    continue;
                }
                if (in)
                {
                    String input = hashMap.get("in");
                    if (input != null)
                    {
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(input));
                        StringBuilder stringBuilderIn = new StringBuilder();
                        for (; ; )
                        {
                            String line = bufferedReader.readLine();
                            if (line == null)
                            {
                                break;
                            }
                            stringBuilderIn.append(line);
                        }

                        String dataString = stringBuilderIn.toString();
                        if (!mode || enc)
                        {
                            StringBuilder stringBuilder = encryption(dataString, count);
                            writer(out, stringBuilder, output);
                        }
                        if (mode && dec)
                        {
                            StringBuilder stringBuilder = decryption(dataString, count);
                            writer(out, stringBuilder, output);
                        }
                    }
                    System.out.println("Error!");
                }
            }
        }
    }

    public static void writer(boolean b, StringBuilder builder, String output) throws FileNotFoundException
    {
        PrintWriter printWriter;
        if (b)
        {
            printWriter = new PrintWriter(output);
        } else
        {
            printWriter = new PrintWriter(outPut);
        }
        printWriter.write(builder.toString());
        printWriter.flush();
        printWriter.close();
    }

    public static StringBuilder encryption(String string, int count)
    {
        StringBuilder stringBuilder = new StringBuilder();

        IntStream.range(0, string.length()).forEach(i -> {
            int y = string.charAt(i);
            int r = y + count;

            if (correctCap(y))
            {
                if (r > capZ)
                {
                    int s = lowA + r - lowZ - 1;
                    stringBuilder.append((char) s);
                } else {
                    stringBuilder.append((char) r);
                }
            } else if (correctLow(y))
            {
                if (r > lowZ)
                {
                    int u = lowA + r - lowZ - 1;
                    stringBuilder.append((char) u);
                } else {
                    stringBuilder.append((char) r);
                }
            } else {
                stringBuilder.append(string.charAt(i));
            }
        });
        return stringBuilder;
    }

    public static StringBuilder decryption(String string, int count)
    {
        StringBuilder stringBuilder = new StringBuilder();

        IntStream.range(0, string.length()).forEach(i ->
        {
            int y = string.charAt(i);
            int o = y - count;

            if (correctCap(y))
            {
                if (o < capA)
                {
                    int s = capZ + o - capA + 1;
                    stringBuilder.append((char) s);
                } else {
                    stringBuilder.append((char) o);
                }
            } else if (correctLow(y))
            {
                if (o < lowA)
                {
                    int r = lowZ + o - lowA + 1;
                    stringBuilder.append((char) r);
                } else {
                    stringBuilder.append((char) o);
                }
            } else {
                stringBuilder.append(string.charAt(i));
            }
        });
        return stringBuilder;
    }

    private static boolean correctCap(int i)
    {
        boolean b = (i >= capA && i <= capZ);
        return b;
    }

    private static boolean correctLow(int i)
    {
        boolean b = (i >= lowA && i <= lowZ);
        return b;
    }

    private static int correctDigits(int i)
    {
        int k = 0;
        if (i > 25)
        {
            k = i % 25;
            return k;
        }
        return i;
    }
}