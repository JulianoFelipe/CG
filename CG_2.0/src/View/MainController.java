/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import View.Options.CanvasPane;
import View.Options.PaintController;
import View.Options.RegularPolygonController;
import View.Options.RevBuildController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import resource.description.Ferramentas;
import resource.description.CriacaoPrevolucao;
import resource.description.Transformacoes;

/**
 *
 * @author JFPS
 */
public class MainController implements Initializable {
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    //https://www.youtube.com/watch?v=RY_Rb2UVQKQ
    
    @FXML
    private TreeView<String> tools;
    @FXML
    private MenuBar menu;
    @FXML
    private TextArea console;
    @FXML
    private AnchorPane options;
    @FXML
    private GridPane grid;
    
    @FXML
    private ImageView frente;
    @FXML
    private ImageView topo;
    @FXML
    private ImageView lateral;
    @FXML
    private ImageView perspectiva;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTools();
        initializeMenuBar();
        initializeConsole();
        
        //borderPane.setCenter( new CanvasPane(this) );
        
        //grid.add(new , NOTHING_SEL, NOTHING_SEL);
    }
    
    private void initializeTools(){
        TreeItem<String> root = new TreeItem<>("Root");
             
        TreeItem<String> ferramentas    = new TreeItem<>(Ferramentas.C_NAME);
        TreeItem<String> criacao        = new TreeItem<>(CriacaoPrevolucao.C_NAME);
        TreeItem<String> transformacoes = new TreeItem<>(Transformacoes.C_NAME);

        ferramentas.getChildren().addAll(
            new TreeItem<>(Ferramentas.Select.NAME, new ImageView(Ferramentas.Select.ICON)),
            new TreeItem<>(Ferramentas.Delete.NAME, new ImageView(Ferramentas.Delete.ICON)),
            new TreeItem<>(Ferramentas.Paint.NAME,  new ImageView(Ferramentas.Paint.ICON))
        );
        
        criacao.getChildren().addAll(
            new TreeItem<>(CriacaoPrevolucao.porPontos.NAME,   new ImageView(CriacaoPrevolucao.porPontos.ICON))//,
            //new TreeItem<>(CriacaoPrevolucao.porLinha.NAME, new ImageView(CriacaoPrevolucao.porLinha.ICON))
        );
        
        transformacoes.getChildren().addAll(
            new TreeItem<>(Transformacoes.Rotacao.NAME,      new ImageView(Transformacoes.Rotacao.ICON)),
            new TreeItem<>(Transformacoes.Escala.NAME,       new ImageView(Transformacoes.Escala.ICON)),
            new TreeItem<>(Transformacoes.Translacao.NAME,   new ImageView(Transformacoes.Translacao.ICON)),
            new TreeItem<>(Transformacoes.Cisalhamento.NAME, new ImageView(Transformacoes.Cisalhamento.ICON))
        );
        
        root.getChildren().addAll(ferramentas, criacao, transformacoes);
        
        tools.setRoot(root);
        tools.setShowRoot(false);
    }
    
    private void initializeMenuBar(){
        
    }
    
    private void initializeConsole(){
        
    }

    private void initViews(){
        
    }
    
    public static final byte NOTHING_SEL       = -1;
    public static final byte FERRAMENTA_SEL    = 0;
    public static final byte REVOLUCAO_SEL     = 1;
    public static final byte TRANSFORMACAO_SEL = 2;
    
    private byte CURRENT_SEL = NOTHING_SEL;
    private Ferramentas current_ferr;
    private CriacaoPrevolucao current_pol;
    private Transformacoes current_tra;
    
    private Parent paintOption;
    private PaintController paintControl;
    private Parent regularOption;
    private RegularPolygonController regularControl;
    private Parent revBuildOption;
    private RevBuildController revBuildController;
    
    @FXML //Clicar na árvore de ferramentas
    private void onMouseClickedToolsListener(MouseEvent e){
        Node node = e.getPickResult().getIntersectedNode();
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)){
            String name = (String) ((TreeItem)tools.getSelectionModel().getSelectedItem()).getValue();
            
            Ferramentas       ferr = Ferramentas.fromString(name);
            CriacaoPrevolucao  pol = CriacaoPrevolucao.fromString(name);
            Transformacoes     tra = Transformacoes.fromString(name);
            
            if (ferr != null){
                CURRENT_SEL = FERRAMENTA_SEL;
                current_ferr = ferr;
            } else if (pol != null){
                CURRENT_SEL = REVOLUCAO_SEL;
                current_pol = pol;
            } else if (tra != null){
                CURRENT_SEL = TRANSFORMACAO_SEL;
                current_tra = tra;
            } else {
                CURRENT_SEL = NOTHING_SEL;
            }
            
            handleSelectedTool();
        }
    }
    
    private void loadPaint(){
        try { 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Options/PaintOption.fxml"));
            loader.setController(paintControl);
            paintOption = loader.load();
        } catch (IOException ex) {  
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    /*private void loadRegular(){
        try { 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Options/RegularPolygonOption.fxml"));
            loader.setController(regularControl);
            regularOption = loader.load();
        } catch (IOException ex) {  
            LOG.log(Level.SEVERE, null, ex);
        }
    }*/
    
    private void loadRevPorPontos(){
        try { 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Options/RevBuildOption.fxml"));
            loader.setController(revBuildController);
            revBuildOption = loader.load();
        } catch (IOException ex) {  
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    //Adiciona opções conforme ferramentas
    private void handleSelectedTool(){ //BEWARE! FORSAKEN LAND!
        options.getChildren().clear();
        switch(CURRENT_SEL){
            case FERRAMENTA_SEL:
                if (null != current_ferr) switch (current_ferr) {
                    case Paint:
                        if (paintOption == null) loadPaint();
                        options.getChildren().add(paintOption);
                        break;
                    case Select:
                        break;
                    case Delete:
                        break;
                }
                break;
            case REVOLUCAO_SEL:
                if(null != current_pol && current_pol == CriacaoPrevolucao.porPontos){
                    if (revBuildOption == null) loadRevPorPontos();
                    options.getChildren().add(revBuildOption);
                }
                break;
                
            case TRANSFORMACAO_SEL:
                
                break;
        }
    }

    public PaintController getPaintControl() {
        return paintControl;
    }

    public RegularPolygonController getRegularControl() {
        return regularControl;
    }

    public byte getCurrentSelection() {
        return CURRENT_SEL;
    }

    public Ferramentas getCurrentFerramenta() {
        return current_ferr;
    }

    public CriacaoPrevolucao getCurrentTipoDePoligono() {
        return current_pol;
    }

    public Transformacoes getCurrentTrasformacao() {
        return current_tra;
    }
    
    
}
