package com.cdan003.annuaireelectronique;

import com.cdan003.logique.Contacts;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class FXMLController implements Initializable {
    
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfPrenom;
    @FXML
    private TextField tfRegroupement;
    @FXML
    private TextField tfLocalisation;
    @FXML
    private TextField tfanneUni;
    @FXML
    private Button btnAjout;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnAnnuler;
    @FXML
    private TextField tfRechercher;
    @FXML
    private TableView<Contacts> tvListe;
    @FXML
    private TableColumn<Contacts, String> colNom;
    @FXML
    private TableColumn<Contacts, String> colPrenom;
    @FXML
    private TableColumn<Contacts, String> colGenre;
    @FXML
    private TableColumn<Contacts, String> Colregroupement;
    @FXML
    private TableColumn<Contacts, String> colSecteur;
    @FXML
    private TableColumn<Contacts, String> colLocalisation;
    @FXML
    private TableColumn<Contacts, String> colAnneUniv;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnQuitter;
    
    private Contacts contacts;
    private int cle;
    
    Stage stage;
    @FXML
    private TableColumn<Contacts, Integer> colId;
    @FXML
    private Label labelStats;
    @FXML
    private Label labelWarnig;
    @FXML
    private Label labelNomInfo;
    @FXML
    private Label labelPrenomInfo;
    @FXML
    private Label labelGenreInfo;
    @FXML
    private Label labelRgpInfo;
    @FXML
    private Label labelSecteurInfo;
    @FXML
    private Label labelLocalisationInfo;
    @FXML
    private Label labelAnneUnivInfo;
    
    @FXML
    private ComboBox<String> comboBoxSecteur;
    @FXML
    private ComboBox<String> comboBoxGenre;
    
    String formatAnne = "^[0-9]{4}-[0-9]{2}$";
    boolean verification;
    boolean allsuccess;
    boolean isComboGenreCliked = false;
    boolean isCombosecteurCliked = false;
    @FXML
    private PieChart pichart;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnModifier.setDisable(true);
        btnSupprimer.setDisable(true);
        contacts = new Contacts();
        contacts.afficheContact(tvListe, colNom, colPrenom, colGenre, colSecteur, Colregroupement, colLocalisation, colAnneUniv, colId);
        labelStats.setText("Nombre(s) d'enregistrement(s) : " + String.valueOf(contacts.getStat()) + " élément(s)");
        comboBoxSecteur.getItems().addAll("Établissements privés", "Établissements publics");
        comboBoxGenre.getItems().addAll("Masculin", "Feminin");
        contacts.grapheStat(pichart);
    }
    
    @FXML
    private void actionAjout(ActionEvent event) {
        verification = !tfNom.getText().equals("") && 
                !tfPrenom.getText().equals("") && 
                !tfRegroupement.getText().equals("") && 
                !tfLocalisation.getText().equals("") && 
                !tfanneUni.getText().equals("");
        
        allsuccess = contacts.verification(tfPrenom, "*Prénom trop court", labelPrenomInfo, tfPrenom.getText().length() < 3) &&
                contacts.verification(tfNom, "*Nom trop court", labelNomInfo, tfNom.getText().length() < 3) &&
                contacts.verification(tfRegroupement, "*Doit être plus de 5 caractères", labelRgpInfo, tfRegroupement.getText().length() < 5) && 
                contacts.verification(tfLocalisation, "*Doit être plus de 5 caractères", labelLocalisationInfo, tfLocalisation.getText().length() < 4)&& 
                contacts.verification(tfanneUni, "*Format incorrect ex : (2001-02) ", labelAnneUnivInfo, !Pattern.compile(formatAnne).matcher(tfanneUni.getText()).find())&& 
                contacts.verification(comboBoxGenre, "*Choisissez (Féminin/Masculin)", labelGenreInfo, !isComboGenreCliked) &&
                contacts.verification(comboBoxSecteur, "*Choisissez (privé/public)", labelSecteurInfo, !isCombosecteurCliked);
        
        if (verification && allsuccess) {
            contacts.ajoutContact(tfNom, tfPrenom, comboBoxGenre, tfRegroupement, comboBoxSecteur, tfLocalisation, tfanneUni);
            contacts.afficheContact(tvListe, colNom, colPrenom, colGenre, colSecteur, Colregroupement, colLocalisation, colAnneUniv, colId);
            raz();
            contacts.grapheStat(pichart);
            labelWarnig.setText("Ajout réussie");
            labelWarnig.setTextFill(Paint.valueOf("green"));
        } else {
            contacts.verification(tfPrenom, "*Prenom trop court", labelPrenomInfo, tfPrenom.getText().length() < 3);
            contacts.verification(tfNom, "*Nom trop court", labelNomInfo, tfNom.getText().length() < 3);
            contacts.verification(tfanneUni, "*Format incorrect ex : (2001-02) ", labelAnneUnivInfo, !Pattern.compile(formatAnne).matcher(tfanneUni.getText()).find());
            contacts.verification(tfRegroupement, "*Doit être plus de 5 caractères", labelRgpInfo, tfRegroupement.getText().length() < 5);
            contacts.verification(tfLocalisation, "*Doit être plus de 5 caractères", labelLocalisationInfo, tfLocalisation.getText().length() < 4);
            contacts.verification(comboBoxGenre, "*Choisissez (Féminin/Masculin)", labelGenreInfo, !isComboGenreCliked);
            contacts.verification(comboBoxSecteur, "*Choisissez (privé/public)", labelSecteurInfo, !isCombosecteurCliked);
            labelWarnig.setText("Il y a une erreur dans la saisie du formulaire");
            labelWarnig.setTextFill(Paint.valueOf("red"));
        }
        labelStats.setText("Nombre(s) d'enregistrement(s) : " + String.valueOf(contacts.getStat()) + " élément(s)");
        
    }
    
    @FXML
    private void actionModifier(ActionEvent event) {
        verification = !tfNom.getText().equals("") && 
                !tfPrenom.getText().equals("") && 
                !tfRegroupement.getText().equals("") && 
                !tfLocalisation.getText().equals("") && 
                !tfanneUni.getText().equals("");
        
        allsuccess = contacts.verification(tfPrenom, "*Prénom trop court", labelPrenomInfo, tfPrenom.getText().length() < 3) &&
                contacts.verification(tfNom, "*Nom trop court", labelNomInfo, tfNom.getText().length() < 3) &&
                contacts.verification(tfRegroupement, "*Doit être plus de 5 caractères", labelRgpInfo, tfRegroupement.getText().length() < 5) && 
                contacts.verification(tfLocalisation, "*Doit être plus de 5 caractères", labelLocalisationInfo, tfLocalisation.getText().length() < 4)&& 
                contacts.verification(tfanneUni, "*Format incorrect ex : (2001-02) ", labelAnneUnivInfo, !Pattern.compile(formatAnne).matcher(tfanneUni.getText()).find());
        
        if (verification && allsuccess) {
            contacts.modifier( tfNom.getText(),tfPrenom.getText(), comboBoxGenre.getValue(), comboBoxSecteur.getValue(), tfRegroupement.getText(), tfLocalisation.getText(), tfanneUni.getText(), cle);
            contacts.afficheContact(tvListe, colNom, colPrenom, colGenre, colSecteur, Colregroupement, colLocalisation, colAnneUniv, colId);
            raz();
            labelWarnig.setText("Modification réussie");
            labelWarnig.setTextFill(Paint.valueOf("green"));
            btnModifier.setDisable(true);
            btnSupprimer.setDisable(true);
        }
        else {
            contacts.verification(tfPrenom, "*Prénom trop court", labelPrenomInfo, tfPrenom.getText().length() < 3);
            contacts.verification(tfNom, "*Nom trop court", labelNomInfo, tfNom.getText().length() < 3);
            contacts.verification(tfanneUni, "*Format incorrect ex : (2001-02) ", labelAnneUnivInfo, !Pattern.compile(formatAnne).matcher(tfanneUni.getText()).find());
            contacts.verification(tfRegroupement, "*Doit être plus de 5 caractères", labelRgpInfo, tfNom.getText().length() < 5);
            contacts.verification(tfanneUni, "*Doit être plus de 5 caractères", labelLocalisationInfo, tfNom.getText().length() < 4);
            labelWarnig.setText("Il y a une erreur dans la saisie du formulaire");
            labelWarnig.setTextFill(Paint.valueOf("red"));
            
        }
    }
    
    @FXML
    private void actionAnnuler(ActionEvent event) {
        raz();
    }
    
    @FXML
    private void afficherSelection(MouseEvent event) {
        raz();
        cle = colId.getCellData(tvListe.getSelectionModel().getFocusedIndex());
        tfNom.setText(colNom.getCellData(tvListe.getSelectionModel().getFocusedIndex()));
        System.out.println(cle);
        tfPrenom.setText(colPrenom.getCellData(tvListe.getSelectionModel().getFocusedIndex()));
        comboBoxGenre.setValue(colGenre.getCellData(tvListe.getSelectionModel().getFocusedIndex()));
        tfRegroupement.setText(Colregroupement.getCellData(tvListe.getSelectionModel().getFocusedIndex()));
        comboBoxSecteur.setValue(colSecteur.getCellData(tvListe.getSelectionModel().getFocusedIndex()));
        tfLocalisation.setText(colLocalisation.getCellData(tvListe.getSelectionModel().getFocusedIndex()));
        tfanneUni.setText(colAnneUniv.getCellData(tvListe.getSelectionModel().getFocusedIndex()));
        btnModifier.setDisable(false);
        btnSupprimer.setDisable(false);
    }
    
    @FXML
    private void actionSupprimer(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Annuaire Electronique");
        alert.setContentText("Voulez-vous vraiment supprimer ?");
        if(alert.showAndWait().get() == ButtonType.OK){
            contacts.supprimer(cle);
            contacts.afficheContact(tvListe, colNom, colPrenom, colGenre, colSecteur, Colregroupement, colLocalisation, colAnneUniv, colId);
            labelStats.setText("Nombre(s) d'enregistrement(s) : " + String.valueOf(contacts.getStat()) + " élément(s)");
            this.raz();
            contacts.grapheStat(pichart);
            labelWarnig.setText("Suppression réussie");
            btnModifier.setDisable(true);
            btnSupprimer.setDisable(true);
        }
    }
    
    @FXML
    public void actionquitter(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Annuaire Electronique");
        alert.setHeaderText("Vous êtes sur le point de quitter l'Annuaire.");
        alert.setContentText("Voulez-vous vraiment quitter ?");
        if(alert.showAndWait().get() == ButtonType.OK){
            stage = (Stage) AnchorPane.getScene().getWindow();
            stage.close();
        }
    }
    
    @FXML
    private void verificationPrenom(KeyEvent event) {
        contacts.verification(tfPrenom, "*Prénom trop court", labelPrenomInfo, tfPrenom.getText().length() < 3);
    }
    
    @FXML
    private void ActionRechercher(KeyEvent event) {
        contacts.rechercher(tfRechercher.getText(), tvListe, colNom, colPrenom, colGenre, colSecteur, Colregroupement, colLocalisation, colAnneUniv, colId);
        labelStats.setText("Nombre(s) d'enregistrement(s) : " + String.valueOf(contacts.getStat()) + " élément(s)");
    }
    
    @FXML
    private void verificationNom(KeyEvent event) {
        contacts.verification(tfNom, "*Nom trop court", labelNomInfo, tfNom.getText().length() < 3);
    }
    private void raz() {
        btnModifier.setDisable(true);
        btnSupprimer.setDisable(true);
        tfNom.setText("");
        tfPrenom.setText("");
        comboBoxGenre.setValue("Genre");
        tfRegroupement.setText("");
        comboBoxSecteur.setValue("Secteur");
        tfLocalisation.setText("");
        tfanneUni.setText("");
        cle = 1;
        
        labelNomInfo.setText("");
        labelPrenomInfo.setText("");
        labelGenreInfo.setText("");
        labelRgpInfo.setText("");
        labelSecteurInfo.setText("");
        labelLocalisationInfo.setText("");
        labelAnneUnivInfo.setText("");
        labelWarnig.setText("");
        
        cle = 1;
    }
    
    @FXML
    private void verificationanneUni(KeyEvent event) {
        contacts.verification(tfanneUni, "*Format incorrect ex : (2001-02) ", labelAnneUnivInfo, !Pattern.compile(formatAnne).matcher(tfanneUni.getText()).find());
    }
    
    @FXML
    private void afficheCBSecteur(MouseEvent event) {
        comboBoxSecteur.getItems();
        isCombosecteurCliked = true;
        contacts.verification(comboBoxSecteur, "*Choisissez (privé/public)", labelSecteurInfo, false);
    }
    
    @FXML
    private void afficheGenre(MouseEvent event) {
        comboBoxGenre.getItems();
        isComboGenreCliked = true;
        contacts.verification(comboBoxGenre, "*Choisissez (Féminin/Masculin)", labelGenreInfo, false);
    }

    @FXML
    private void verificationRgp(KeyEvent event) {
        contacts.verification(tfRegroupement, "*Doit être plus de 5 caractères", labelRgpInfo, tfRegroupement.getText().length() < 5);
    }

    @FXML
    private void verificationLocalisation(KeyEvent event) {
        contacts.verification(tfLocalisation, "*Doit être plus de 5 caractères", labelLocalisationInfo, tfLocalisation.getText().length() < 4);
    }

    
}  
