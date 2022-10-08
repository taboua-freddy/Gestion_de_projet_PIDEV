/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML.controller.sprint;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import entity.Story;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;

import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import service.sprintService.StoryService;

/**
 *
 * @author Too
 */
public class HomeControllerSprint implements Initializable {

    //Table View Elements
    @FXML
    private TableView<Story> table_us;

    @FXML
    private TableColumn<Story, String> col_userstory;

    @FXML
    private TableColumn<Story, Integer> col_bv;

    @FXML
    private TableColumn<Story, Integer> col_cap;

    @FXML
    private TableColumn<Story, Float> col_comp;

    @FXML
    private TableColumn<Story, Float> col_roi;

    @FXML
    private TableColumn<Story, Integer> col_priority;
    @FXML
    private TableColumn<Story, String> col_desc;
    @FXML
    private TableColumn<Story,Object> col_update;


    //End Table View Elements
    //Start Boder Pane
    @FXML
    private BorderPane border_pane;
    //End Border Pane
    @FXML
    private TextField userstory;
    @FXML
    private TextField bv;
    @FXML
    private TextField cap;
    @FXML
    private TextField desc;
    @FXML
    private TextField comp;
    @FXML
    private TextField roi;
    @FXML
    private TextField priority;
    private TableView<Story> table;
    @FXML
    private Button submit;


    @FXML
    private void AddUserStory(ActionEvent event) throws SQLException {
        Story St = new Story(1,userstory.getText(),Integer.parseInt(bv.getText()),Integer.parseInt(cap.getText())
                ,Integer.parseInt(priority.getText()),
                Float.parseFloat(comp.getText()),desc.getText(),Integer.parseInt(roi.getText()));
        StoryService SS = new StoryService();
        SS.insert(St);
        System.out.println("Success!");
        loadStories();
        table_us.setVisible(false);
    }

    public void onEditChanged(TableColumn.CellEditEvent<Story,String> storyStringCellEditEvent){
        Story story = table_us.getSelectionModel().getSelectedItem();
        story.setNomStory(storyStringCellEditEvent.getNewValue());
        story.setBV(Integer.parseInt(storyStringCellEditEvent.getNewValue()));
        story.setCap(Integer.parseInt(storyStringCellEditEvent.getNewValue()));
        story.setComplexite(Float.parseFloat(storyStringCellEditEvent.getNewValue()));
        story.setPriorite(Integer.parseInt(storyStringCellEditEvent.getNewValue()));
        story.setDescription(storyStringCellEditEvent.getNewValue());
        story.setROI(Float.parseFloat(storyStringCellEditEvent.getNewValue()));


    }

    public void loadStories(){
        try {
            StoryService SS = new StoryService();
            List<Story> Stories = SS.displayAll();
            ObservableList<Story> StoryList = FXCollections.observableArrayList();

            for (Story st : Stories){
                StoryList.add(
                        new Story(st.getIdProjet(),st.getNomStory(),st.getBV(),st.getCap(),st.getPriorite(),st.getComplexite()
                                ,st.getDescription(),st.getROI(),new Button("Update")));


            }

            col_userstory.setCellValueFactory(new PropertyValueFactory<>("nomStory"));

            col_bv.setCellValueFactory(new PropertyValueFactory<>("BV"));
            col_cap.setCellValueFactory(new PropertyValueFactory<>("cap"));
            col_desc.setCellValueFactory(new PropertyValueFactory<>("description"));
            col_comp.setCellValueFactory(new PropertyValueFactory<>("complexite"));

            col_roi.setCellValueFactory(new PropertyValueFactory<>("ROI"));
            col_priority.setCellValueFactory(new PropertyValueFactory<>("priorite"));
            col_update.setCellValueFactory(new PropertyValueFactory<>("update"));




            editableCols();

            table_us.setItems(StoryList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAction() throws SQLException {

        ObservableList<Story> StoryList = table.getSelectionModel().getSelectedItems();
        for (Story story : StoryList){
            StoryService SS = new StoryService();
            SS.update(story);
            System.out.println("Updated successfully from TableView!");

        }

    }

    public void editableCols(){
        col_userstory.setCellFactory(TextFieldTableCell.forTableColumn());
        col_userstory.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setNomStory(e.getNewValue());
                }
        );
        col_bv.setCellFactory(TextFieldTableCell.<Story, Integer>forTableColumn(new IntegerStringConverter()));
        col_bv.setOnEditCommit(e->{
                    e.getTableView().getItems().get(e.getTablePosition().getRow()).setBV(e.getNewValue());
                }
        );
        col_cap.setCellFactory(TextFieldTableCell.<Story, Integer>forTableColumn(new IntegerStringConverter()));
        col_cap.setOnEditCommit(e->{
                    e.getTableView().getItems().get(e.getTablePosition().getRow()).setCap(e.getNewValue());
                }
        );
        col_priority.setCellFactory(TextFieldTableCell.<Story, Integer>forTableColumn(new IntegerStringConverter()));
        col_priority.setOnEditCommit(e->{
                    e.getTableView().getItems().get(e.getTablePosition().getRow()).setPriorite(e.getNewValue());
                }
        );
        col_comp.setCellFactory(TextFieldTableCell.<Story, Float>forTableColumn(new FloatStringConverter()));
        col_comp.setOnEditCommit(e->{
                    e.getTableView().getItems().get(e.getTablePosition().getRow()).setComplexite(e.getNewValue());
                }
        );
        col_desc.setCellFactory(TextFieldTableCell.forTableColumn());
        col_desc.setOnEditCommit(e->{
                    e.getTableView().getItems().get(e.getTablePosition().getRow()).setDescription(e.getNewValue());
                }
        );
        col_roi.setCellFactory(TextFieldTableCell.<Story, Float>forTableColumn(new FloatStringConverter()));
        col_roi.setOnEditCommit(e->{
                    e.getTableView().getItems().get(e.getTablePosition().getRow()).setROI(e.getNewValue());
                }
        );

        table_us.setEditable(true);

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //

        loadStories();

    }    
    
}
