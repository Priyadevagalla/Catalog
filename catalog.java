import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.JSONObject;

public class SecretSharing {
    
    
    public static long baseToDecimal(String value, int base) {
        return Long.parseLong(value, base);
    }

   
    public static long lagrangeInterpolation(List<long[]> points) {
        long c = 0;
        int k = points.size();

        for (int i = 0; i < k; i++) {
            long xi = points.get(i)[0];
            long yi = points.get(i)[1];

            double term = yi;
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    long xj = points.get(j)[0];
                    term *= (-xj * 1.0) / (xi - xj);
                }
            }
            c += Math.round(term);
        }
        return c;
    }

    public static void main(String[] args) throws Exception {
       
        String content = new String(Files.readAllBytes(Paths.get("test.json")));
        JSONObject input = new JSONObject(content);

        int n = input.getJSONObject("keys").getInt("n");
        int k = input.getJSONObject("keys").getInt("k");

        List<long[]> points = new ArrayList<>();

       
        for (String key : input.keySet()) {
            if (key.equals("keys")) continue;

            int x = Integer.parseInt(key);
            JSONObject obj = input.getJSONObject(key);
            int base = Integer.parseInt(obj.getString("base"));
            String value = obj.getString("value");

            long y = baseToDecimal(value, base);
            points.add(new long[]{x, y});
        }

       
        points.sort(Comparator.comparingLong(a -> a[0]));
        points = points.subList(0, k);

       
        long secret = lagrangeInterpolation(points);
        
        System.out.println("Secret (Constant term C): " + secret);
    }
}
