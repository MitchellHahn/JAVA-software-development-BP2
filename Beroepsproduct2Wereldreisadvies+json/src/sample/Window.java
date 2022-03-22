package sample;

import com.sun.org.apache.bcel.internal.generic.LADD;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.JTextArea;
import org.json.simple.JSONArray;

import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;

public class Window {

    JSONParser parser = new JSONParser();
    ArrayList<Land> landen = new ArrayList<Land>();
    ArrayList<Land> HighRiskCountries = new ArrayList<Land>();
    ArrayList<Land> LowRiskCountries = new ArrayList<Land>();
    GridPane textPane = new GridPane();
    GridPane buttonPane = new GridPane();
    Stage primaryStage;

    public  Window(Stage _primaryStage) throws IOException {
        InitializeButtons();
        primaryStage = _primaryStage;
        GetData();
        textPane = new GridPane();
        for(int i = 0; i < landen.size(); i++)
        {
            Text text = new Text(10, 40, landen.get(i).name+ " - " + landen.get(i).threat+ " - " + landen.get(i).status);
            textPane.add(text, 0,i);
        }
        ShowScreen();
    }

    //Maak de Top buttons (home, veilige landen en gevaarlijke landen)
    void InitializeButtons(){
        buttonPane = new GridPane();
        Button homeButton = new Button("Home");
        Button veiligeLandenButton = new Button("Veiligste landen");
        Button GevaarlijkeLandenButton = new Button("Gevaarlijkste landen");
        homeButton.setOnAction(e -> {
            GetCountries();
        });
        veiligeLandenButton.setOnAction(e -> {
            GetLowRiskCountries();
        });
        GevaarlijkeLandenButton.setOnAction(e -> {
            GetHighRiskCountries();
        });
        //Selecteer knop toevoegen en positie
        buttonPane.add(homeButton, 0 , 0);
        buttonPane.add(veiligeLandenButton, 1 , 0);
        buttonPane.add(GevaarlijkeLandenButton, 2 , 0);
    }

    //Maak de window van de applicatie
    void ShowScreen(){

        GridPane rootPane = new GridPane();
        rootPane.addRow(0,buttonPane);
        rootPane.addRow(1, textPane);

        primaryStage.setTitle("wereld reisadvies");
        primaryStage.setScene(new Scene(rootPane, 1500, 1000));
        primaryStage.show();
    }

    //Do de API request naar de API en zet de results in de arrayList
    void GetData()throws IOException{
        try {
            URL uri = new URL("https://www.travel-advisory.info/api");
            URLConnection urlConnection = uri.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String input;
            String jsonString = "";
            while ((input = bufferedReader.readLine()) != null){
                jsonString+= input;
            }

            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
            JSONObject rawData = (JSONObject) jsonObject.get("data");
            System.out.println(rawData);

            for (Object key : rawData.keySet()) {
                //based on you key types
                String keyStr = (String)key;
                Object keyvalue = rawData.get(keyStr);
                JSONObject value = (JSONObject) rawData.get(keyStr);
                JSONObject advisory = (JSONObject) value.get("advisory");
                Land land = new Land(value.get("name").toString(), advisory.get("score").toString(), advisory.get("message").toString());
                landen.add(land);
            }


        } catch (MalformedURLException | ParseException e) {

        }
    }

    //Toon de landen in de standaard volgorde
    void GetCountries(){
        InitializeButtons();
        textPane = new GridPane();
        for(int i = 0; i < landen.size(); i++)
        {
            Text text = new Text(10, 40, landen.get(i).name+ " - " + landen.get(i).threat+ " - " + landen.get(i).status);
            textPane.add(text, 0,i);
        }
        ShowScreen();
    }

    //Toon de landen van High risk naar low risk
    void GetHighRiskCountries(){
        InitializeButtons();
        HighRiskCountries = (ArrayList) landen.clone();
        Collections.sort(HighRiskCountries, new HighestToLowest());
        textPane = new GridPane();
        for(int i = 0; i < HighRiskCountries.size(); i++)
        {
            Text text = new Text(10, 40, HighRiskCountries.get(i).name+ " - " + HighRiskCountries.get(i).threat+ " - " + HighRiskCountries.get(i).status);
            textPane.add(text, 0,i);
        }
        ShowScreen();
    }

    //Toon de landen van low risk naar high risk
    void GetLowRiskCountries(){
        InitializeButtons();
        LowRiskCountries = (ArrayList) landen.clone();
        Collections.sort(LowRiskCountries, new LowestToHighest());
        textPane = new GridPane();
        for(int i = 0; i < LowRiskCountries.size(); i++)
        {
            Text text = new Text(10, 40, LowRiskCountries.get(i).name+ " - " + LowRiskCountries.get(i).threat+ " - " + LowRiskCountries.get(i).status);
            textPane.add(text, 0,i);
        }
        ShowScreen();
    }

}
