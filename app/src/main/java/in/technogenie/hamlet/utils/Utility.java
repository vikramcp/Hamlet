package in.technogenie.hamlet.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class Utility {

    private Context _context;

    // constructor
    public Utility(Context context) {
        this._context = context;
    }

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }

    public static String downloadHtml(String addr) {
        StringBuilder html = new StringBuilder();
        try {
            URL url = new URL(addr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d("Utility", "Connection :" + conn);
            if (conn != null) {
                conn.setConnectTimeout(100000);
                conn.setUseCaches(false);
                Log.d("Utility", "Response Code :" + conn.getResponseCode());
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("Utility", "Connection OK.. ");
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(),
                                    "utf-8"));
                    while (true) {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        html.append(line + "\n");
                    }
                    br.close();
                } else {
                    Log.d("LocateActivity",
                            "Connection Failure :" + conn.getResponseCode());
                }
                conn.disconnect();
            }
        } catch (Exception ex) {
        }
        return html.toString();
    }

    /*
     * public static String getURLEnv() { String url=null; String environment =
     * "local"; if (environment != null && environment.equals("local")) {
     * url="http://10.23.212.19:8080/rbiz/"; } else if(environment != null &&
     * environment.equals("test")) { url="http://rbiz.elasticbeanstalk.com/"; }
     * return url; }
     */

    // Reading file paths from SDCard
    public ArrayList<String> getFilePaths() {
        ArrayList<String> filePaths = new ArrayList<String>();

        File directory = new File(
                Environment.getExternalStorageDirectory()
                        + File.separator + Constants.PHOTO_ALBUM);

        // check for directory
        if (directory.isDirectory()) {
            // getting list of file paths
            File[] listFiles = directory.listFiles();

            // Check for count
            if (listFiles.length > 0) {

                // loop through all files
                for (int i = 0; i < listFiles.length; i++) {

                    // get file path
                    String filePath = listFiles[i].getAbsolutePath();

                    // check for supported file extension
                    if (IsSupportedFile(filePath)) {
                        // Add image path to array list
                        filePaths.add(filePath);
                    }
                }
            } else {
                // image directory is empty
                Toast.makeText(
                        _context,
                        Constants.PHOTO_ALBUM
                                + " is empty. Please load some images in it !",
                        Toast.LENGTH_LONG).show();
            }

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
            alert.setTitle("Error!");
            alert.setMessage(Constants.PHOTO_ALBUM
                    + " directory path is not valid! Please set the image directory name AppConstant.java class");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        return filePaths;
    }

    // Check supported file extensions
    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
                filePath.length());

        if (Constants.FILE_EXTN
                .contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;

    }

    /*
     * getting screen width
     */
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }


    public static String loadImageFromWeb(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/jcisec.better";

            System.out.println(path);
            File f = new File(path);
            //File myFile = new File(path, fileName);

            f.getParentFile().mkdirs();
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            try {

                byte[] b = new byte[100];
                int l = 0;
                while ((l = is.read(b)) != -1)
                    fos.write(b, 0, l);

            } catch (Exception e) {

            } finally {
                fos.close();
            }

            return f.getAbsolutePath();
        } catch (Exception e) {
            System.out.println("Exc=" + e);
            return null;

        }
    }


}
