package com.polado.score;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by PolaDo on 8/14/2017.
 */

public class ReadWriteData {
    Context context;
    ArrayList<Player> players = new ArrayList<>();

    public ReadWriteData(Context context, ArrayList<Player> players) {
        this.context = context;
        this.players = players;
    }

    public void writeData() {
        String fileName = "data";

        String data = "";
        for (int i = 0; i < players.size(); i++) {
            data += players.get(i).getName() + "," + players.get(i).getScore() + ",";
        }

        Log.i("write data", data);

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void writeEmpty() {
        String fileName = "data";

        String data = "empty";

        Log.i("write data", data);

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public ArrayList<Player> readData() {
        String fileName = "data";
        String ret = "-1";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("home activity", "File not found: " + e.toString());
            ret = "-1";
        } catch (IOException e) {
            Log.e("home activity", "Can not read file: " + e.toString());
            ret = "-1";
        }

        Log.i("read data", ret);

        if (ret.equals("-1") || ret.equals("empty"))
            return null;

        String[] arr = ret.split(",");

        for (int i = 0; i < arr.length; i += 2) {
            players.add(new Player(arr[i], Integer.parseInt(arr[i + 1])));
        }

        return players;
    }
}
