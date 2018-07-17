/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import View.Config.ChangeFactorsController;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import m.CGViewport;
import m.Camera;
import m.Visao;
import m.Vista;
import m.World;
import m.poligonos.CGObject;
import m.poligonos.Vertice;
import resource.description.Ferramentas;
import resource.description.CriacaoPrevolucao;
import resource.description.Transformacoes;
import utils.config.StandardConfigWinView;
import utils.ioScene.InputScene;
import utils.ioScene.OutputScene;
import utils.math.PMath;

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
    
    @FXML private StackPane frentePane;
    @FXML private StackPane topoPane;
    @FXML private StackPane lateralPane;
    @FXML private StackPane perspectivaPane;
    
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
    private CGObject selectedObject = null;
    
    private CGCanvas frente;
    private CGCanvas topo;
    private CGCanvas lateral;
    private CGCanvas perspectiva;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mundo = World.getInstance();
           
        CGViewport viw = StandardConfigWinView.STD_VIEWPORT;
        int height = (int) viw.getDeltaV();
        int width  = (int) viw.getDeltaU();
        
        for (Vista v : mundo.getVistas()){
            switch (v.getVisao()) {
                case Frontal: frente  = new CGCanvas(this, v, width, height); break;
                case Lateral: lateral = new CGCanvas(this, v, width, height); break;
                case Topo:    topo    = new CGCanvas(this, v, width, height); break;
                case Perspectiva: perspectiva = new CGCanvas(this, v, width, height); break;
                default: throw new IllegalArgumentException("Visão não possui canvas equivalente adicionado.");
            }
        }

        frentePane     .getChildren().add(frente);
        lateralPane    .getChildren().add(lateral);
        topoPane       .getChildren().add(topo);
        perspectivaPane.getChildren().add(perspectiva);
        
        frente .zoomProperty().bindBidirectional(frenteZoom.textProperty());
        lateral.zoomProperty().bindBidirectional(lateralZoom.textProperty());
        topo   .zoomProperty().bindBidirectional(topoZoom.textProperty());
        perspectiva.zoomProperty().bindBidirectional(frenteZoom.textProperty());
        
        frente.selectedObjectProperty().addListener((ObservableValue<? extends CGObject> observable, CGObject oldValue, CGObject newValue) -> {
            selectedObject = newValue;
        });
        lateral.selectedObjectProperty().addListener((ObservableValue<? extends CGObject> observable, CGObject oldValue, CGObject newValue) -> {
            selectedObject = newValue;
        });
        topo.selectedObjectProperty().addListener((ObservableValue<? extends CGObject> observable, CGObject oldValue, CGObject newValue) -> {
            selectedObject = newValue;
        });
        perspectiva.selectedObjectProperty().addListener((ObservableValue<? extends CGObject> observable, CGObject oldValue, CGObject newValue) -> {
            selectedObject = newValue;
        });
        
        initializeTools(); //Listeners para a barra esquerda de ferramentas
        initializeMenuBar(); //Listeners para a barra de menu superior
        initializeViewToolbars(); //Listeners para as 4 barras de ferramentas das views
        initializeResizeAndCanvasHUD(); //Coloca Grids e listeners de resize
        
        paint();
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
            paint();
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
            paint();
        });
        
        factors.setOnAction((ActionEvent event) -> {
            Pane pane = null;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Config/ChangeFactors.fxml"));
            loader.setController(new ChangeFactorsController());
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
            dialog.setTitle("Fatores de alteração");
            dialog.show();
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
                    paint();
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        limparCena.setOnAction((ActionEvent event) -> {
            mundo.clearAll();
            selectedObject = null;
            if (selectController != null) selectController.objectProperty().set(null);
            paint();
            
        });
    }
    
    private void initializeViewToolbars(){
        //<editor-fold defaultstate="collapsed" desc="Frente">
        frenteCamParams.setOnAction((ActionEvent event) -> {
            ManualCamController controller = new ManualCamController(
                frente.getVista().getPipelineCamera(), Visao.Frontal
            );

            controller.camProperty().addListener((ObservableValue<? extends Camera> observable, Camera oldValue, Camera newValue) -> {
                frente.getVista().getPipelineCamera().set(newValue);
                paint();
            });

            final Stage dialog = getManualCamWindow(controller);
            dialog.setTitle("Câmera Frontal");
            dialog.show();
        });
        frenteCamAuto.setOnAction((ActionEvent event) -> {
            frente.autoChangeActiveProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                frenteGrid.showHotkeys(newValue);
            }); 
            frente.setAutoCam();
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Lateral">
        lateralCamParams.setOnAction((ActionEvent event) -> {
            ManualCamController controller = new ManualCamController(
                    lateral.getVista().getPipelineCamera(), Visao.Lateral
            );

            controller.camProperty().addListener((ObservableValue<? extends Camera> observable, Camera oldValue, Camera newValue) -> {
                lateral.getVista().getPipelineCamera().set(newValue);
                paint();
            });

            final Stage dialog = getManualCamWindow(controller);
            dialog.setTitle("Câmera Lateral");
            dialog.show();
        });
        lateralCamAuto.setOnAction((ActionEvent event) -> {
            //asdf
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Topo">
        topoCamParams.setOnAction((ActionEvent event) -> {
            ManualCamController controller = new ManualCamController(
                    topo.getVista().getPipelineCamera(), Visao.Topo
            );

            controller.camProperty().addListener((ObservableValue<? extends Camera> observable, Camera oldValue, Camera newValue) -> {
                topo.getVista().getPipelineCamera().set(newValue);
                paint();
            });

            final Stage dialog = getManualCamWindow(controller);
            dialog.setTitle("Câmera Topo");
            dialog.show();
        });
        topoCamAuto.setOnAction((ActionEvent event) -> {
            
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Perspectiva">
        persCamParams.setOnAction((ActionEvent event) -> {
            ManualCamController controller = new ManualCamController(
                perspectiva.getVista().getPipelineCamera(), Visao.Perspectiva
            );
            
            controller.camProperty().addListener((ObservableValue<? extends Camera> observable, Camera oldValue, Camera newValue) -> {
                perspectiva.getVista().getPipelineCamera().set(newValue);
                paint();
            });
                    
            final Stage dialog = getManualCamWindow(controller);
            dialog.setTitle("Câmera Perspectiva");
            dialog.show();
        });
        
        persCamAuto.setOnAction((ActionEvent event) -> {
            
        });
        //</editor-fold>
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
          
    public void cancelTempPoints(){
        mundo.clearTemp();
        paint();
    }
    
    public void paint(){
        frente     .paint();
        lateral    .paint();
        topo       .paint();
        perspectiva.paint();
    }
    
    public void finalizeTempPoints(){
        List<Vertice> lista = mundo.getTempPointsCopy();
        //Poligono pol = Poligono.build(lista);
        mundo.addObject(PMath.attemptBuildingFromPlanes(lista));
        mundo.clearTemp();
        paint();
    }
        
    //<editor-fold defaultstate="collapsed" desc="Seleção e carregamento da barra de ferramentas lateral esquerda">
    public void bindToolsProperties(ObjectProperty<Byte> current, ObjectProperty<Ferramentas> ferramenta, ObjectProperty<CriacaoPrevolucao> criacao, ObjectProperty<Transformacoes> transformacao){
        current.bind(CURRENT_SEL);
        ferramenta.bind(current_ferr);
        criacao.bind(current_pol);
        transformacao.bind(current_tra);
    }
    
    static final byte NOTHING_SEL       = -1;
    static final byte FERRAMENTA_SEL    = 0;
    static final byte REVOLUCAO_SEL     = 1;
    static final byte TRANSFORMACAO_SEL = 2;
    
    private final ObjectProperty<Byte> CURRENT_SEL = new SimpleObjectProperty(NOTHING_SEL);
    private final ObjectProperty<Ferramentas> current_ferr = new SimpleObjectProperty();
    private final ObjectProperty<CriacaoPrevolucao> current_pol= new SimpleObjectProperty();
    private final ObjectProperty<Transformacoes> current_tra= new SimpleObjectProperty();
    
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
                CURRENT_SEL.set(FERRAMENTA_SEL);
                current_ferr.set(ferr);
            } else if (pol != null){
                CURRENT_SEL.set(REVOLUCAO_SEL);
                current_pol.set(pol);
            } else if (tra != null){
                CURRENT_SEL.set(TRANSFORMACAO_SEL);
                current_tra.set(tra);
            } else {
                CURRENT_SEL.set(NOTHING_SEL);
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
            selectController = new PolySelectController(selectedObject);
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
        switch(CURRENT_SEL.get()){
            case FERRAMENTA_SEL:
                if (null != current_ferr.get()) switch (current_ferr.get()) {
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
                if(null != current_pol.get() && current_pol.get() == CriacaoPrevolucao.free){
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
//</editor-fold>
}
