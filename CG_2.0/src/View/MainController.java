/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import View.Config.ManualCamController;
import View.Options.PaintController;
import View.Options.RegularPolygonController;
import View.Options.RevBuildController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import m.Camera;
import m.Visao;
import m.Vista;
import m.World;
import m.poligonos.CGObject;
import m.poligonos.Poligono;
import m.poligonos.Vertice;
import resource.description.Ferramentas;
import resource.description.CriacaoPrevolucao;
import resource.description.Transformacoes;
import utils.ioScene.InputScene;
import utils.ioScene.OutputScene;
import utils.math.PMath;

/**
 *
 * @author JFPS
 */
public class MainController implements Initializable {
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    
    @FXML private MenuBar menu;
    @FXML private MenuItem salvar;
    @FXML private MenuItem carregar;
    @FXML private MenuItem limparCena;
    
    @FXML private TreeView<String> tools;
    @FXML private AnchorPane options;
    
    @FXML private Canvas frente;
    @FXML private Canvas topo;
    @FXML private Canvas lateral;
    @FXML private Canvas perspectiva;
    
    @FXML private MenuItem frenteCamParams;
    @FXML private MenuItem frenteCamAuto;
    @FXML private MenuItem lateralCamParams;
    @FXML private MenuItem lateralCamAuto;
    @FXML private MenuItem topoCamParams;
    @FXML private MenuItem topoCamAuto;
    @FXML private MenuItem persCamParams;
    @FXML private MenuItem persCamAuto;
    
    private World mundo;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mundo = World.getInstance();
        
        initializeTools(); //Listeners para a barra esquerda de ferramentas
        initializeMenuBar(); //Listeners para a barra de menu superior
        initializeViewToolbars(); //Listeners para as 4 barras de ferramentas das views
        initializeCanvases(); //Listeners para clicks nos canvas (Exceto perspectiva)
        
        paintStuff();
    }
    
    //<editor-fold defaultstate="collapsed" desc="Listeners gerais para barra de menu e barras de ferramentas">
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
                new TreeItem<>(CriacaoPrevolucao.free.NAME,   new ImageView(CriacaoPrevolucao.free.ICON))//,
                //new TreeItem<>(CriacaoPrevolucao.gridSnap.NAME, new ImageView(CriacaoPrevolucao.gridSnap.ICON))
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
        salvar.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos de CG (*.jas)", "*.jas");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(menu.getScene().getWindow());
            
            if(file != null){
                try {
                    OutputScene.outputToFile(mundo.getObjectsCopy(), file);
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        carregar.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos de CG (*.jas)", "*.jas");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(menu.getScene().getWindow());
            
            if(file != null){
                try {
                    List<CGObject> objectsList = InputScene.getListFromFile(file);
                    mundo.clearAll();
                    //mundo.addObject(objectsList);
                    for (CGObject obj : objectsList){
                        mundo.addObject(obj);
                    }
                    paintStuff();
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        limparCena.setOnAction((ActionEvent event) -> {
            mundo.clearAll();
            paintStuff();
            
        });
    }
    
    private void initializeViewToolbars(){
        frenteCamParams.setOnAction((ActionEvent event) -> {
            System.out.println(getVistaFromVisao(Visao.Frontal).getPipelineCamera());
            ManualCamController controller = new ManualCamController(
                getVistaFromVisao(Visao.Frontal).getPipelineCamera(), Visao.Frontal
            );
            
            controller.camProperty().addListener((ObservableValue<? extends Camera> observable, Camera oldValue, Camera newValue) -> {
                //System.out.println("CAM MUDOU. PROPAGANDO.");
                getVistaFromVisao(Visao.Frontal).getPipelineCamera().set(newValue);
                paintStuff();
            });
            
            final Stage dialog = getManualCamWindow(controller);
            dialog.setTitle("Câmera Frontal");
            dialog.show();
        });
        frenteCamAuto.setOnAction((ActionEvent event) -> {
            throw new UnsupportedOperationException("Not supported yet.");
        });
        
        lateralCamParams.setOnAction((ActionEvent event) -> {
            ManualCamController controller = new ManualCamController(
                getVistaFromVisao(Visao.Lateral).getPipelineCamera(), Visao.Lateral
            );
            
            controller.camProperty().addListener((ObservableValue<? extends Camera> observable, Camera oldValue, Camera newValue) -> {
                getVistaFromVisao(Visao.Lateral).getPipelineCamera().set(newValue);
                paintStuff();
            });
            
            final Stage dialog = getManualCamWindow(controller);
            dialog.setTitle("Câmera Lateral");
            dialog.show();
        });
        lateralCamAuto.setOnAction((ActionEvent event) -> {
            throw new UnsupportedOperationException("Not supported yet.");
        });
        
        topoCamParams.setOnAction((ActionEvent event) -> {
            ManualCamController controller = new ManualCamController(
                getVistaFromVisao(Visao.Topo).getPipelineCamera(), Visao.Topo
            );
            
            controller.camProperty().addListener((ObservableValue<? extends Camera> observable, Camera oldValue, Camera newValue) -> {
                getVistaFromVisao(Visao.Topo).getPipelineCamera().set(newValue);
                paintStuff();
            });
            
            final Stage dialog = getManualCamWindow(controller);
            dialog.setTitle("Câmera Topo");
            dialog.show();
        });
        topoCamAuto.setOnAction((ActionEvent event) -> {
            throw new UnsupportedOperationException("Not supported yet.");
        });
        
        persCamParams.setOnAction((ActionEvent event) -> {
            ManualCamController controller = new ManualCamController(
                getVistaFromVisao(Visao.Perspectiva).getPipelineCamera(), Visao.Perspectiva
            );
            
            controller.camProperty().addListener((ObservableValue<? extends Camera> observable, Camera oldValue, Camera newValue) -> {
                getVistaFromVisao(Visao.Perspectiva).getPipelineCamera().set(newValue);
                paintStuff();
            });
            
            final Stage dialog = getManualCamWindow(controller);
            dialog.setTitle("Câmera Perspectiva");
            dialog.show();
        });
        persCamAuto.setOnAction((ActionEvent event) -> {
            throw new UnsupportedOperationException("Not supported yet.");
        });
    }
    
    private Stage getManualCamWindow(ManualCamController controller){
        Pane pane = null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Config/ManualCam.fxml"));
        loader.setController(controller);
        try {
            pane = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(menu.getScene().getWindow());
        Scene dialogScene = new Scene(pane);
        dialog.setResizable(false);
        dialog.setScene(dialogScene);

        return dialog;
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Operações em cima dos Canvas">
    private void initializeCanvases(){
        frente.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Frente Clicked: " + e.getX() + ", " + e.getY() + ", " + e.getZ());
            if (CURRENT_SEL == REVOLUCAO_SEL){
                if (current_pol == CriacaoPrevolucao.free){
                    //mundo.addTempPoint(new Vertice((float) -34, (float) 9));
                    //mundo.addTempPoint(new Vertice((float) e.getX(), (float) e.getY()));
                    //frente.getGraphicsContext2D().fillOval(e.getX(), e.getY(), 5, 5);
                    
                    Vertice newPoint = new Vertice((float) e.getX(), (float) e.getY());
                    getVistaFromVisao(Visao.Frontal).getPipe().reverseConversion(newPoint);
                    mundo.addTempPoint(newPoint);
                    
                    //Nregular newreg = new Nregular(6, 66, newPoint);
                    //getVistaFromVisao(Visao.Frontal).getPipe().reverseConversion(newreg);
                    //mundo.addObject(newreg);
                }
            }
            paintStuff();
        });
        
        lateral.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Lateral Clicked: " + e.getX() + ", " + e.getY() + ", " + e.getZ());
            if (CURRENT_SEL == REVOLUCAO_SEL){
                if (current_pol == CriacaoPrevolucao.free){
                    //mundo.addTempPoint(new Vertice(0, (float) e.getX(), (float) e.getY()));
                    //frente.getGraphicsContext2D().fillOval(e.getX(), e.getY(), 5, 5);
                    
                    Vertice newPoint = new Vertice((float) e.getX(), (float) e.getY());
                    //System.out.println("VERT LATERAL: " + newPoint);
                    getVistaFromVisao(Visao.Lateral).getPipe().reverseConversion(newPoint);
                    //System.out.println("VERT MUNDO: " + newPoint);
                    mundo.addTempPoint(newPoint);
                    
                    //Nregular newreg = new Nregular(6, 66, newPoint);
                    //getVistaFromVisao(Visao.Lateral).getPipe().reverseConversion(newreg);
                    //mundo.addObject(newreg);
                }
            }
            paintStuff();
        });
        
        topo.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Topo Clicked: " + e.getX() + ", " + e.getY() + ", " + e.getZ());
            if (CURRENT_SEL == REVOLUCAO_SEL){
                if (current_pol == CriacaoPrevolucao.free){
                    //mundo.addTempPoint(new Vertice((float) e.getX(), 0, (float) e.getY()));
                    //frente.getGraphicsContext2D().fillOval(e.getX(), e.getY(), 5, 5);
                    
                    Vertice newPoint = new Vertice((float) e.getX(), (float) e.getY());
                    //Vertice newPointCP = new Vertice(newPoint);
                    getVistaFromVisao(Visao.Topo).getPipe().reverseConversion(newPoint);
                    mundo.addTempPoint(newPoint);
                    
                    //Nregular newreg = new Nregular(6, 66, newPointCP);
                    //getVistaFromVisao(Visao.Topo).getPipe().reverseConversion(newreg);
                    //mundo.addObject(newreg);
                }
            }
            paintStuff();
        });
    }
    
    private void paintStuff(){
        System.out.println("PAINT");
        clearCanvases();
        
        for (Vista vista : mundo.getVistas()){
            GraphicsContext graphs = getCanvasFromView(vista).getGraphicsContext2D();
            graphs.setFill(Color.BLACK);
            graphs.setStroke(Color.BLACK);
            graphs.setLineWidth(1);
            
            List<CGObject> objs = vista.get2Dobjects();
            for (CGObject obj : objs){
                graphs.beginPath();
                
                Vertice point1 = obj.getPoint(0);
                //System.out.println("Vista: " + vista.getVisao() + ". Point: " + point1);
                Vertice point2 = null;
                for (int i=1; i<obj.getNumberOfPoints(); i++){
                    point2 = obj.getPoint(i);
                    graphs.strokeLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
                    //graphs.moveTo(point1.getX(), point.getY());
                    point1 = point2;
                }
                graphs.strokeLine(point1.getX(), point1.getY(), obj.getPoint(0).getX(),obj.getPoint(0).getY());
                //graphs.fill();
                graphs.closePath();
            }
        }
        
        for (Vista vista : mundo.getVistas()){
            GraphicsContext graphs = getCanvasFromView(vista).getGraphicsContext2D();
            graphs.setFill(Color.BLACK);
            graphs.setStroke(Color.BLACK);
            graphs.setLineWidth(1);
            
            List<Vertice> vertices = vista.getTempPoints();
            if (vertices.isEmpty())
                continue;
                
            graphs.beginPath();           
            Vertice point1 = vertices.get(0);
            graphs.fillOval(point1.getX(), point1.getY(), 5, 5);
            if (vista.getVisao().equals(Visao.Lateral))
                System.out.println("Point: " + point1);
            Vertice point2 = null;
            for (int i=1; i<vertices.size(); i++){
                point2 = vertices.get(i);
                graphs.fillOval(point2.getX(), point2.getY(), 5, 5);
                graphs.strokeLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
                point1 = point2;
            }
            //graphs.strokeLine(point1.getX(), point1.getY(), vertices.get(0).getX(), vertices.get(0).getY());
            graphs.closePath();
        }
    }
    
    private void clearCanvases(){
        GraphicsContext cl = frente.getGraphicsContext2D();
        cl.clearRect(0, 0, frente.getWidth(), frente.getHeight());
        
        cl = topo.getGraphicsContext2D();
        cl.clearRect(0, 0, topo.getWidth(), topo.getHeight());
        
        cl = lateral.getGraphicsContext2D();
        cl.clearRect(0, 0, lateral.getWidth(), lateral.getHeight());
        
        cl = perspectiva.getGraphicsContext2D();
        cl.clearRect(0, 0, perspectiva.getWidth(), perspectiva.getHeight());
    }
    
    private Canvas getCanvasFromView(Vista vista){
        switch (vista.getVisao()){
            case Frontal:
                return frente;
            case Lateral:
                return lateral;
            case Topo:
                return topo;
            case Perspectiva:
                return perspectiva;
            default:
                throw new IllegalArgumentException("Visão não prevista.");
                    
        }
    }
//</editor-fold>
    
    public void cancelTempPoints(){
        mundo.clearTemp();
        paintStuff();
    }
    
    public void finalizeTempPoints(){
        System.out.println("FINALIZE");
        List<Vertice> lista = mundo.getTempPointsCopy();
        //Poligono pol = Poligono.build(lista);
        mundo.addObject(PMath.attemptBuildingFromPlanes(lista));
        mundo.clearTemp();
        paintStuff();
    }
    
    private Vista getVistaFromVisao(Visao vis){
        for (Vista vista : mundo.getVistas()){
            if (vista.getVisao().equals(vis)){
                return vista;
            }
        }
        return null;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Seleção e carregamento da barra de ferramentas lateral esquerda">
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
    
    private void loadRegular(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Options/RegularPolygonOption.fxml"));
            loader.setController(regularControl);
            regularOption = loader.load();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
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
                if(null != current_pol && current_pol == CriacaoPrevolucao.free){
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
//</editor-fold>
}
