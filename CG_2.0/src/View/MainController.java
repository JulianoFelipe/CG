/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import View.Config.ManualCamController;
import View.Options.PaintController;
import View.Options.PolySelectController;
import View.Options.RegularPolygonController;
import View.Options.RevBuildController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import m.Camera;
import m.Eixo;
import m.Visao;
import m.Vista;
import m.World;
import m.poligonos.Aresta;
import m.poligonos.CGObject;
import m.poligonos.Movimento;
import m.poligonos.Vertice;
import m.poligonos.we_edge.HE_Poliedro;
import m.poligonos.we_edge.WE_Aresta;
import resource.description.Ferramentas;
import resource.description.CriacaoPrevolucao;
import resource.description.Transformacoes;
import utils.ioScene.InputScene;
import utils.ioScene.OutputScene;
import utils.math.PMath;
import utils.math.VMath;

/**
 *
 * @author JFPS
 */
public class MainController implements Initializable {
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    
    @FXML private MenuBar  menu;
    @FXML private MenuItem salvar;
    @FXML private MenuItem carregar;
    @FXML private MenuItem limparCena;
    @FXML private CheckMenuItem showGrid;
    @FXML private CheckMenuItem showAxisIcon;
    @FXML private CheckMenuItem showAxis;
    @FXML private MenuItem factors;
    @FXML private Slider      gridThickness;
    @FXML private Slider      gridOpacity;
    @FXML private ColorPicker gridColor;
    
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
    
    @FXML private Label frenteZoom;
    @FXML private Label lateralZoom;
    @FXML private Label topoZoom;
    @FXML private Label persZoom;
    
    private World mundo;
    private CGObject selected_obj = null;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mundo = World.getInstance();
                
        initializeTools(); //Listeners para a barra esquerda de ferramentas
        initializeMenuBar(); //Listeners para a barra de menu superior
        initializeViewToolbars(); //Listeners para as 4 barras de ferramentas das views
        initializeResizeAndCanvasHUD(); //Coloca Grids e listeners de resize
        initializeCanvases(); //Listeners para clicks nos canvas (Exceto perspectiva)
        
        //checkConfig();

        paintStuff();
    }
    
    //<editor-fold defaultstate="collapsed" desc="Alterações nos grids">
    
    private Grid frenteGrid;
    private Grid lateralGrid;
    private Grid topoGrid;
    private Grid persGrid;
    
    private void initializeResizeAndCanvasHUD(){
        StackPane parentPane = (StackPane) frente.getParent();
        parentPane.resize(frente.getWidth(), frente.getHeight());
        frente.widthProperty().bind(parentPane.widthProperty());
        frente.heightProperty().bind(parentPane.heightProperty());
        frenteGrid = new Grid(frente, Visao.Frontal);
        frenteGrid.setOpacity(0.35);
        /*grid.setFocusTraversable(true);*/ frenteGrid.setMouseTransparent(true);
        parentPane.getChildren().add(frenteGrid);
        
        parentPane = (StackPane) lateral.getParent();
        parentPane.resize(lateral.getWidth(), lateral.getHeight());
        lateral.widthProperty().bind(parentPane.widthProperty());
        lateral.heightProperty().bind(parentPane.heightProperty());
        lateralGrid = new Grid(lateral, Visao.Lateral);
        lateralGrid.setOpacity(0.35);
        /*grid.setFocusTraversable(true);*/ lateralGrid.setMouseTransparent(true);
        parentPane.getChildren().add(lateralGrid);
        
        parentPane = (StackPane) topo.getParent();
        parentPane.resize(topo.getWidth(), topo.getHeight());
        topo.widthProperty().bind(parentPane.widthProperty());
        topo.heightProperty().bind(parentPane.heightProperty());
        topoGrid = new Grid(topo, Visao.Topo);
        topoGrid.setOpacity(0.35);
        /*grid.setFocusTraversable(true);*/ topoGrid.setMouseTransparent(true);
        parentPane.getChildren().add(topoGrid);
        
        parentPane = (StackPane) perspectiva.getParent();
        parentPane.resize(perspectiva.getWidth(), perspectiva.getHeight());
        perspectiva.widthProperty().bind(parentPane.widthProperty());
        perspectiva.heightProperty().bind(parentPane.heightProperty());
        persGrid = new Grid(perspectiva, Visao.Perspectiva);
        persGrid.setOpacity(0.35);
        /*grid.setFocusTraversable(true); grid.setMouseTransparent(true);*/ //Sem click na perspectiva mesmo...
        parentPane.getChildren().add(persGrid);
        
        ////Atualização de pintura
        ChangeListener<Number> canvasSizeListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            paintStuff();
        };
        
        frente.widthProperty() .addListener(canvasSizeListener);
        frente.heightProperty().addListener(canvasSizeListener);
        lateral.widthProperty() .addListener(canvasSizeListener);
        lateral.heightProperty().addListener(canvasSizeListener);
        topo.widthProperty() .addListener(canvasSizeListener);
        topo.heightProperty().addListener(canvasSizeListener);
        perspectiva.widthProperty() .addListener(canvasSizeListener);
        perspectiva.heightProperty().addListener(canvasSizeListener);
        
        mundo.addAxis();
    }
//</editor-fold>
    
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
        showGrid.setOnAction((ActionEvent event) -> {
            boolean show = showGrid.selectedProperty().get();
            frenteGrid.showGrid(show);
            lateralGrid.showGrid(show);
            topoGrid.showGrid(show);
            persGrid.showGrid(show);
        });
        
        showAxisIcon.setOnAction((ActionEvent event) -> {
            boolean show = showAxisIcon.selectedProperty().get();
            frenteGrid.showAxisIcon(show);
            lateralGrid.showAxisIcon(show);
            topoGrid.showAxisIcon(show);
            persGrid.showAxisIcon(show);
        });
        
        showAxis.setOnAction((ActionEvent event) -> {
            boolean show = showAxis.selectedProperty().get();
            if (show){
                mundo.addAxis();
            } else {
                mundo.removeAxis();
            }
            paintStuff();
        });
        
        factors.setOnAction((ActionEvent event) -> {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        });
        
        /*gridThickness.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("GRID THICK: OLD=" + oldValue + " New="+ newValue);
        });*/ //Muitos updates se não fazer um check com "valueChangingProperty()"
        gridThickness.setOnMouseReleased((Event event) -> {
            int thickness = gridThickness.valueProperty().intValue();
            frenteGrid.setGridThickness(thickness);
            lateralGrid.setGridThickness(thickness);
            topoGrid.setGridThickness(thickness);
            persGrid.setGridThickness(thickness);
        });
        
        gridOpacity.setOnMouseReleased((Event event) -> {
            double opacity = gridOpacity.valueProperty().get();
            frenteGrid.setOpacity(opacity);
            lateralGrid.setOpacity(opacity);
            topoGrid.setOpacity(opacity);
            persGrid.setOpacity(opacity);
        });
        
        gridColor.valueProperty().set(Color.RED);
        gridColor.valueProperty().addListener((ObservableValue<? extends Color> observable, Color oldValue, Color newValue) -> {
            Color color = newValue;
            frenteGrid.setGridColor(color);
            lateralGrid.setGridColor(color);
            topoGrid.setGridColor(color);
            persGrid.setGridColor(color);
        });
        
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
                    objectsList.forEach((obj) -> {
                        mundo.addObject(obj);
                    });
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
            selected_obj = null;
            if (selectController != null) selectController.objectProperty().set(null);
            paintStuff();
            
        });
    }
    
    private void initializeViewToolbars(){
        //<editor-fold defaultstate="collapsed" desc="Frente">
        float zoom = getVistaFromVisao(Visao.Frontal).getPipe().getProportions();
        frenteZoom.setText("x "+String.format(java.util.Locale.US,"%.1f", zoom));
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
            frente.getParent().getParent().setStyle("-fx-border-color: blue");
            frente.setFocusTraversable(true);
            frente.addEventFilter(MouseEvent.ANY, (e) -> frente.requestFocus());
            frente.setOnKeyPressed((KeyEvent event1) -> {

                KeyCode pressed = event1.getCode();
                Vista frenteVista = getVistaFromVisao(Visao.Frontal);
                Vertice vrp    = frenteVista.getPipelineCamera().getVRP();
                Vertice p      = frenteVista.getPipelineCamera().getP();
                Vertice viewUp = frenteVista.getPipelineCamera().getViewUp();
                switch (pressed){
                    case Z:
                        //vrp.setZ(vrp.getZ() - (float) 1);
                        //  p.setZ(  p.getZ() - (float) 1);
                        break;
                    case C:
                        //vrp.setZ(vrp.getZ() + (float) 1);
                        //  p.setZ(  p.getZ() + (float) 1);
                        break;
                    case W:
                        vrp.setY(vrp.getY() + (float) 1);
                          p.setY(  p.getY() + (float) 1);
                        break;
                    case A:
                        vrp.setX(vrp.getX() + (float) 1);
                          p.setX(  p.getX() + (float) 1);
                        break;
                    case S:
                        vrp.setY(vrp.getY() - (float) 1);
                          p.setY(  p.getY() - (float) 1);
                        break;
                    case D:
                        vrp.setX(vrp.getX() - (float) 1);
                          p.setX(  p.getX() - (float) 1);
                        break;
                    case ESCAPE:
                        frente.setOnKeyPressed(null);
                        frente.getParent().getParent().setStyle("-fx-border-color: black");
                        return;
                }
                frenteVista.getPipe().getCamera().set(new Camera(viewUp, vrp, p));
                paintStuff();
            });
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Lateral">
        zoom = getVistaFromVisao(Visao.Lateral).getPipe().getProportions();
        lateralZoom.setText("x "+String.format(java.util.Locale.US,"%.1f", zoom));
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
            lateral.getParent().getParent().setStyle("-fx-border-color: blue");
            lateral.setFocusTraversable(true);
            lateral.addEventFilter(MouseEvent.ANY, (e) -> lateral.requestFocus());
            lateral.setOnKeyPressed((KeyEvent event1) -> {

                KeyCode pressed = event1.getCode();
                Vista lateralVista = getVistaFromVisao(Visao.Lateral);
                Vertice vrp    = lateralVista.getPipelineCamera().getVRP();
                Vertice p      = lateralVista.getPipelineCamera().getP();
                Vertice viewUp = lateralVista.getPipelineCamera().getViewUp();
                switch (pressed){
                    case Z:
                        //vrp.setX(vrp.getX() - (float) 1);
                        //  p.setX(  p.getX() - (float) 1);
                        break;
                    case C:
                        //vrp.setX(vrp.getX() + (float) 1);
                        //  p.setX(  p.getX() + (float) 1);
                        break;
                    case W:
                        vrp.setZ(vrp.getZ() + (float) 1);
                          p.setZ(  p.getZ() + (float) 1);
                        break;
                    case A:
                        vrp.setY(vrp.getY() + (float) 1);
                          p.setY(  p.getY() + (float) 1);
                        break;
                    case S:
                        vrp.setZ(vrp.getZ() - (float) 1);
                          p.setZ(  p.getZ() - (float) 1);
                        break;
                    case D:
                        vrp.setY(vrp.getY() - (float) 1);
                          p.setY(  p.getY() - (float) 1);
                        break;
                    case ESCAPE:
                        lateral.setOnKeyPressed(null);
                        lateral.getParent().getParent().setStyle("-fx-border-color: black");
                        return;
                }
                lateralVista.getPipe().getCamera().set(new Camera(viewUp, vrp, p));
                paintStuff();
            });
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Topo">
        zoom = getVistaFromVisao(Visao.Topo).getPipe().getProportions();
        topoZoom.setText("x "+String.format(java.util.Locale.US,"%.1f", zoom));
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
            topo.getParent().getParent().setStyle("-fx-border-color: blue");
            topo.setFocusTraversable(true);
            topo.addEventFilter(MouseEvent.ANY, (e) -> topo.requestFocus());
            topo.setOnKeyPressed((KeyEvent event1) -> {

                KeyCode pressed = event1.getCode();
                Vista topVista = getVistaFromVisao(Visao.Topo);
                Vertice vrp    = topVista.getPipelineCamera().getVRP();
                Vertice p      = topVista.getPipelineCamera().getP();
                Vertice viewUp = topVista.getPipelineCamera().getViewUp();
                switch (pressed){
                    case Z:
                        //vrp.setY(vrp.getY() - (float) 1);
                        //  p.setY(  p.getY() - (float) 1);
                        break;
                    case C:
                        //vrp.setY(vrp.getY() + (float) 1);
                        //  p.setY(  p.getY() + (float) 1);
                        break;
                    case W:
                        vrp.setZ(vrp.getZ() + (float) 1);
                          p.setZ(  p.getZ() + (float) 1);
                        break;
                    case A:
                        vrp.setX(vrp.getX() + (float) 1);
                          p.setX(  p.getX() + (float) 1);
                        break;
                    case S:
                        vrp.setZ(vrp.getZ() - (float) 1);
                          p.setZ(  p.getZ() - (float) 1);
                        break;
                    case D:
                        vrp.setX(vrp.getX() - (float) 1);
                          p.setX(  p.getX() - (float) 1);
                        break;
                    case ESCAPE:
                        topo.setOnKeyPressed(null);
                        topo.getParent().getParent().setStyle("-fx-border-color: black");
                        return;
                }
                topVista.getPipe().getCamera().set(new Camera(viewUp, vrp, p));
                paintStuff();
            });
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Perspectiva">
        zoom = getVistaFromVisao(Visao.Perspectiva).getPipe().getProportions();
        persZoom.setText("x "+String.format(java.util.Locale.US,"%.1f", zoom));
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
            perspectiva.getParent().getParent().setStyle("-fx-border-color: blue");
            perspectiva.setFocusTraversable(true);
            perspectiva.addEventFilter(MouseEvent.ANY, (e) -> perspectiva.requestFocus());
            perspectiva.setOnKeyPressed((KeyEvent event1) -> {

                KeyCode pressed = event1.getCode();
                Vista pers = getVistaFromVisao(Visao.Perspectiva);
                Vertice vrp = pers.getPipelineCamera().getVRP();
                switch (pressed){
                    case Q:
                        vrp.setX(vrp.getX() - (float) 0.1);
                        break;
                    case E:
                        vrp.setX(vrp.getX() + (float) 0.1);
                        break;
                    case W:
                        vrp.setY(vrp.getY() - (float) 0.1);
                        break;
                    case A:
                        vrp.setZ(vrp.getZ() - (float) 0.1);
                        break;
                    case S:
                        vrp.setY(vrp.getY() + (float) 0.1);
                        break;
                    case D:
                        vrp.setZ(vrp.getZ() + (float) 0.1);
                        break;
                    case ESCAPE:
                        perspectiva.setOnKeyPressed(null);
                        perspectiva.setOnMouseDragged(null);
                        previousX = previousY = -1;
                        perspectiva.getParent().getParent().setStyle("-fx-border-color: black");
                        return;
                }
                pers.getPipe().getCamera().setVRP(vrp);
                paintStuff();
            });
            perspectiva.setOnMouseDragged((MouseEvent event1) -> {
                int currentX = (int) event1.getX(),
                    currentY = (int) event1.getY();
                
                if (previousX == -1){
                    previousX = currentX;
                    previousY = currentY;
                    return;
                }

                Vertice primeiro = new Vertice(previousX, previousY),
                        segundo  = new Vertice (currentX, currentY);
                
                Movimento vert = VMath.movimentoVertical(primeiro, segundo);
                Movimento hori = VMath.movimentoHorizontal(primeiro, segundo);
                
                Vista pers = getVistaFromVisao(Visao.Perspectiva);
                Vertice p = pers.getPipelineCamera().getP();
                float dp = pers.getPipe().getDP();
                
                if (vert == Movimento.Cima){
                    p.setY(p.getY() + (float)0.01);
                    //p.setZ(p.getZ() + (float)0.1);
                } else if (vert == Movimento.Baixo){
                    p.setY(p.getY() - (float)0.01);
                    //p.setZ(p.getZ() - (float)0.1);
                }
                
                if (hori == Movimento.Esquerda){
                    p.setX(p.getX() + (float)0.01);
                    //p.setZ(p.getZ() + (float)0.1);
                } else if (hori == Movimento.Direita){
                    p.setX(p.getX() - (float)0.01);
                    //p.setZ(p.getZ() - (float)0.1);
                }
                
                //double newZ = Math.sqrt((dp*dp)-(p.getY()*p.getY())-(p.getX()*p.getX()));
                //p.setZ((float) newZ);
                
                pers.getPipelineCamera().setP(p);
                paintStuff();
                
                previousX = currentX;
                previousY = currentY;
            });
        });
        //</editor-fold>
    }
    private int previousX=-1, previousY=-1;
    
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
            Vertice clicked = new Vertice((float) e.getX(), (float) e.getY());
            System.out.println("Frente Clicked: " + e.getX() + ", " + e.getY() + ", " + e.getZ());
            if (CURRENT_SEL == REVOLUCAO_SEL){
                if (current_pol == CriacaoPrevolucao.free){                    
                    getVistaFromVisao(Visao.Frontal).getPipe().reverseConversion(clicked);
                    mundo.addTempPoint(clicked);
                    
                    //Nregular newreg = new Nregular(6, 66, clicked);
                    //////getVistaFromVisao(Visao.Frontal).getPipe().reverseConversion(newreg);
                    //mundo.addObject(newreg);
                }
            } else if (CURRENT_SEL == FERRAMENTA_SEL){
                if (current_ferr == Ferramentas.Select){
                    for (CGObject obj : getVistaFromVisao(Visao.Frontal).get2Dobjects()){
                        /*if (PMath.proximoDeQualquerVerticeDoPoligono(obj, clicked)){
                            selected_obj = obj;
                            selectController.objectProperty().set(obj);
                            paintStuff();
                            return;
                        }*/
                        if (obj.contains(clicked.getX(), clicked.getY(), Eixo.Eixo_XY)){
                            selected_obj = obj;
                            selectController.objectProperty().set(obj);
                            paintStuff();
                            return;
                        }
                    }
                    
                    selected_obj = null;
                    selectController.objectProperty().set(null);
                }
            }
            paintStuff();
        });
        
        lateral.setOnMouseClicked((MouseEvent e) -> {
            Vertice clicked = new Vertice((float) e.getX(), (float) e.getY());
            System.out.println("Lateral Clicked: " + e.getX() + ", " + e.getY() + ", " + e.getZ());
            if (CURRENT_SEL == REVOLUCAO_SEL){
                if (current_pol == CriacaoPrevolucao.free){                   
                    getVistaFromVisao(Visao.Lateral).getPipe().reverseConversion(clicked);
                    mundo.addTempPoint(clicked);
                    
                    //Nregular newreg = new Nregular(6, 66, newPoint);
                    //getVistaFromVisao(Visao.Lateral).getPipe().reverseConversion(newreg);
                    //mundo.addObject(newreg);
                }  else if (CURRENT_SEL == FERRAMENTA_SEL){
                    if (current_ferr == Ferramentas.Select){
                        for (CGObject obj : getVistaFromVisao(Visao.Frontal).get2Dobjects()){
                            if (PMath.proximoDeQualquerVerticeDoPoligono(obj, clicked)){
                                selected_obj = obj;
                                selectController.objectProperty().set(obj);
                                paintStuff();
                                return;
                            }
                        }

                        selected_obj = null;
                        selectController.objectProperty().set(null);
                    }
                }
            }
            paintStuff();
        });
        
        topo.setOnMouseClicked((MouseEvent e) -> {
            Vertice clicked = new Vertice((float) e.getX(), (float) e.getY());
            System.out.println("Topo Clicked: " + e.getX() + ", " + e.getY() + ", " + e.getZ());
            if (CURRENT_SEL == REVOLUCAO_SEL){
                if (current_pol == CriacaoPrevolucao.free){
                    getVistaFromVisao(Visao.Topo).getPipe().reverseConversion(clicked);
                    mundo.addTempPoint(clicked);
                    
                    //Nregular newreg = new Nregular(6, 66, newPointCP);
                    //getVistaFromVisao(Visao.Topo).getPipe().reverseConversion(newreg);
                    //mundo.addObject(newreg);
                } else if (CURRENT_SEL == FERRAMENTA_SEL){
                    if (current_ferr == Ferramentas.Select){
                        for (CGObject obj : getVistaFromVisao(Visao.Frontal).get2Dobjects()){
                            if (PMath.proximoDeQualquerVerticeDoPoligono(obj, clicked)){
                                selected_obj = obj;
                                selectController.objectProperty().set(obj);
                                paintStuff();
                                return;
                            }
                        }

                        selected_obj = null;
                        selectController.objectProperty().set(null);
                    }
                }
            }
            paintStuff();
        });
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
    
    //<editor-fold defaultstate="collapsed" desc="Funções de pintura">
    private void paintStuff(){
        clearCanvases();
        
        mundo.getVistas().forEach((vista) -> {
            GraphicsContext graphs = getCanvasFromView(vista).getGraphicsContext2D();
            graphs.setFill(Color.BLACK);
            graphs.setStroke(Color.BLACK);
            graphs.setLineWidth(1);
            
            List<CGObject> objs = vista.get2Dobjects();
            objs.forEach((obj) -> {
                paintObject(graphs, obj);
            });
        });
        
        mundo.getVistas().forEach((vista) -> {
            GraphicsContext graphs = getCanvasFromView(vista).getGraphicsContext2D();
            graphs.setFill(Color.BLACK);
            graphs.setStroke(Color.BLACK);
            graphs.setLineWidth(1);
            List<Vertice> vertices = vista.getTempPoints();
            if (!(vertices.isEmpty())) {
                int radius=5;
                graphs.beginPath();
                Vertice point1 = vertices.get(0);
                graphs.fillOval(point1.getX(), point1.getY(), 5, 5);
                
                Vertice point2 = null;
                for (int i=1; i<vertices.size(); i++){
                    point2 = vertices.get(i);
                    graphs.fillOval(point2.getX()-(radius/2), point2.getY()-(radius/2), radius, radius);
                    graphs.strokeLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
                    point1 = point2;
                }
                //graphs.strokeLine(point1.getX(), point1.getY(), vertices.get(0).getX(), vertices.get(0).getY());
                graphs.closePath();
            }
        });
    }
    
    /**
     * Seleciona qual função de pintura específica chamar
     * @param graphics
     * @param obj 
     */
    private void paintObject(GraphicsContext graphs, CGObject obj){
        if (selected_obj!=null && obj.getID()==(selected_obj.getID())){
            graphs.setStroke(Color.RED);
        } else {
            graphs.setStroke(Color.BLACK);
        }
        
        if (obj instanceof HE_Poliedro){
            List<List<WE_Aresta>> faces = ((HE_Poliedro) obj).getVisibleFaces();
            faces.forEach((face) -> {
                paintArestasConectadas(graphs, face);
            });
        } else {
            paintConectedPointList(graphs, obj.getPoints(), 0);
        }
    }
    
    private void paintArestasConectadas(GraphicsContext graphs, List<? extends Aresta> lista){
        graphs.beginPath();
             
        lista.forEach((aresta) -> {
            Vertice ini = aresta.getvInicial();
            Vertice fin = aresta.getvFinal();
            graphs.strokeLine(ini.getX(), ini.getY(), fin.getX(), fin.getY());
        });
        
        graphs.closePath();
    }
    
    /**
     * Pinta os pontos como contínuos
     * @param graphs
     * @param lista 
     */
    private void paintConectedPointList(GraphicsContext graphs, List<? extends Vertice> lista, int pointRadius){
        graphs.beginPath();
                
        Vertice point1 = lista.get(0);
        //System.out.println("Vista: " + vista.getVisao() + ". Point: " + point1);
        Vertice point2 = null;
        for (int i=1; i<lista.size(); i++){
            point2 = lista.get(i);
            graphs.strokeLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
            
            if (pointRadius > 0){
                graphs.fillOval(point1.getX()-(pointRadius/2), point1.getY()-(pointRadius/2), pointRadius, pointRadius);
            }
            
            point1 = point2;
        }
        graphs.strokeLine(point1.getX(), point1.getY(), lista.get(0).getX(),lista.get(0).getY());
        
        
        
        graphs.closePath();
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
//</editor-fold>
    
    public void cancelTempPoints(){
        mundo.clearTemp();
        paintStuff();
    }
    
    public void finalizeTempPoints(){
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
    private Parent selectOption;
    private PolySelectController selectController;
    
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
    
    private void loadSelect(){
        if (selectController == null){
            selectController = new PolySelectController(selected_obj);
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Options/PolySelect.fxml"));
            loader.setController(selectController);
            selectOption = loader.load();
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
                        if (selectOption == null) loadSelect();
                        options.getChildren().add(selectOption);
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
