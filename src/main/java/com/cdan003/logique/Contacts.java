/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.cdan003.logique;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;

/**
 *
 * @author incli
 */
public class Contacts {
    String nom;
    String prenom;
    String genre;
    String regroupement;
    String secteur;
    String localisation;
    String anneUni;
    private int id;
    private int stat = 0;
    
    File file = new File("src/main/resources/fichiers/bd.txt");
    TreeMap<Integer, String> map = new TreeMap<>();
    Set<Integer> keys = map.keySet();
    String entete;
    
    public int getStat() {
        return stat;
    }
    
    public void setStat(int stat) {
        this.stat = stat;
    }
    
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getRegroupement() {
        return regroupement;
    }
    
    public void setRegroupement(String Regroupement) {
        this.regroupement = Regroupement;
    }
    
    public String getSecteur() {
        return secteur;
    }
    
    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }
    
    public String getLocalisation() {
        return localisation;
    }
    
    public void setLocalisation(String Localisation) {
        this.localisation = Localisation;
    }
    
    public String getAnneUni() {
        return anneUni;
    }
    
    public void setAnneUni(String anneUni) {
        this.anneUni = anneUni;
    }
    
    public Contacts () {
        this.lecture();
    }
    public Contacts (String nom, String prenom, String genre, String secteur ,String regroupement, String localisation, String anneUni, int id) {
        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
        this.regroupement = regroupement;
        this.secteur = secteur;
        this.localisation = localisation;
        this.anneUni = anneUni;
        this.id = id;
    }
    
    
    public void afficheContact(TableView table, TableColumn nom, TableColumn prenom,TableColumn genre, TableColumn secteur, TableColumn regroupement, TableColumn localisation, TableColumn anneUni, TableColumn id) {
        this.lecture();
        ObservableList<Contacts> list = FXCollections.observableArrayList();
        String[] data;
        for (Integer key : keys) {
            data = map.get(key).split("\t");
            list.add(new Contacts(data[5], data[6], data[4], data[3], data[2], data[1], data[0], key));
        }
        stat = map.size();
        table.setItems(list);
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        secteur.setCellValueFactory(new PropertyValueFactory<>("secteur"));
        regroupement.setCellValueFactory(new PropertyValueFactory<>("regroupement"));
        localisation.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        anneUni.setCellValueFactory(new PropertyValueFactory<>("anneUni"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
    }
    public void ajoutContact(TextField nom, TextField prenom, ComboBox comboBoxGenre, TextField regroupement, ComboBox comboBoxSecteur, TextField localisation, TextField anneUni){
        String contact = anneUni.getText() + "\t" + this.capitalize(localisation.getText().toLowerCase()) + "\t" +
                this.capitalize(regroupement.getText().toLowerCase()) + "\t" + comboBoxSecteur.getValue() + "\t" +comboBoxGenre.getValue() + "\t" + nom.getText().toUpperCase() + "\t" + this.capitalize(prenom.getText().toLowerCase());
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_16LE);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.newLine();
            bw.write(contact);
            bw.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("" + e.toString());
        }
    }
    
    public void modifier (String nom, String prenom, String genre, String secteur ,String regroupement, String localisation, String anneUni, int id) {
        try {
            String newContacts = anneUni + "\t" + this.capitalize(localisation.toLowerCase())+ "\t" +
                    this.capitalize(regroupement.toLowerCase()) + "\t" +  secteur + "\t" + genre + "\t" + nom.toUpperCase()  + "\t" +this.capitalize(prenom.toLowerCase());
            this.map.put(id, newContacts);
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_16LE);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(entete);
            for(Integer key: keys){
                bw.newLine();
                bw.write(map.get(key));
            }
            bw.close();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Contacts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void lecture() {
        try {
            BufferedReader bufferedReader = null;
            int cles = 0;
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_16LE));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                cles ++;
                if(cles==1)
                    entete=line;
                else
                    map.put(cles, line);
            }
            bufferedReader.close();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void supprimer(int cle) {
        try {
            int taille=map.size();
            map.remove(cle);
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_16LE);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(entete);
            for(Integer key: keys){
                bw.newLine();
                bw.write(map.get(key));
            }
            bw.close();
            writer.close();
            if(cle!=(taille+1)){
                map.remove(taille+1);
            }
            
        }   catch (IOException ex) {
            System.out.println("" + ex.toString());
        }
    }
    
    public void rechercher (String stm, TableView table, TableColumn nom, TableColumn prenom,TableColumn genre, TableColumn secteur, TableColumn regroupement, TableColumn localisation, TableColumn anneUni, TableColumn id) {
        ObservableList<Contacts> list = FXCollections.observableArrayList();
        String[] data;
        for(Integer key: keys){
            if (Pattern.compile(stm, Pattern.CASE_INSENSITIVE).matcher(map.get(key)).find()){
                data = map.get(key).split("\t");
                list.add(new Contacts(data[5], data[6], data[4], data[3], data[2], data[1], data[0], key));
            }
        }
        stat = list.size();
        table.setItems(list);
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        secteur.setCellValueFactory(new PropertyValueFactory<>("secteur"));
        regroupement.setCellValueFactory(new PropertyValueFactory<>("regroupement"));
        localisation.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        anneUni.setCellValueFactory(new PropertyValueFactory<>("anneUni"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        
    }
    
    public String capitalize(String str){
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    public boolean verification (TextField value , String warning, Label info, boolean condi) {
        boolean succes = false;
        if (condi){
            value.setStyle("-fx-border-color : red");
            value.setStyle("-fx-background-color : red, white");
            info.setText(warning);
            info.setTextFill(Paint.valueOf("red"));
        } else if (value.getText().equals("")) {
            info.setText("*Champs vide");
            info.setTextFill(Paint.valueOf("red"));
        }else {
            value.setStyle("-fx-border-color : green");
            value.setStyle("-fx-background-color : green, white");
            info.setText("");
            succes = true;
        }
        
        return succes;
    }
    
    public boolean verification (ComboBox value , String warning, Label info, boolean condi) {
        boolean succes = false;
        if (condi){
            value.setStyle("-fx-border-color : red");
            value.setStyle("-fx-background-color : red, white");
            info.setText(warning);
            info.setTextFill(Paint.valueOf("red"));
        } else {
            value.setStyle("-fx-border-color : green");
            value.setStyle("-fx-background-color : green, white");
            info.setText("");
            succes = true;
        }
        
        return succes;
    }

    
    public void grapheStat (PieChart stat) {
       stat.getData().clear();
       TreeMap<String, Integer>  etablisement = new TreeMap();
        Set<String> cles = etablisement.keySet();
        stat.setTitle("Etablissement");
        etablisement.clear();
        String[] data;
        for (Integer key : keys) {
            data = map.get(key).split("\t");
            etablisement.put(data[2], 0);
        }
        for (String cl : cles){
            for (Integer key : keys) {
                data = map.get(key).split("\t");
                if (cl.equals(data[2])) 
                   etablisement.put(cl,( 1 + etablisement.get(cl)));
            }
        }
        
        for (String c : cles){
           // if (etablisement.get(c) < 100) {
                stat.getData().add(new PieChart.Data(c, etablisement.get(c)));
                System.out.println(c + " : " + etablisement.get(c));
            //} 
        } 
    }
}
