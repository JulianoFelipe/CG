/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import View.Config.BackgroundColorsController;
import View.Config.ChangeFactorsController;
import View.Config.ManualCamController;
import View.Options.LuzAmbienteController;
import View.Options.LuzesPontuaisController;
import View.Options.TransformacaoController;
import View.Options.PolySelectController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
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
import m.Eixo;
import m.Visao;
import m.Vista;
import m.World;
import m.poligonos.CGObject;
import m.poligonos.Vertice;
import m.shader.AmbientLight;
import m.shader.Flat;
import m.shader.PointLight;
import m.shader.Wireframe;
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
    
    //<editor-fold defaultstate="collapsed" desc="GUI FXML ELEMENTS">
    @FXML private MenuBar  menu;
    @FXML private MenuItem salvar;
    @FXML private MenuItem carregar;
    @FXML private MenuItem limparCena;
    @FXML private CheckMenuItem showGrid;
    @FXML private CheckMenuItem showAxisIcon;
    @FXML private CheckMenuItem showAxis;
    @FXML private MenuItem factors;
    @FXML private MenuItem backgroundMenu;
    @FXML private Slider      gridThickness;
    @FXML private Slider      gridOpacity;
    @FXML private Slider      gridSize;
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
    
    @FXML private RadioMenuItem wireframeMenu;
    @FXML private RadioMenuItem flatMenu;
    @FXML private RadioMenuItem gouraudMenu;
//</editor-fold>
    
    private World mundo;
    private ObjectProperty<CGObject> selectedObjectProperty;
    private ObjectProperty<Eixo> axisOfOperationProperty;
    
    private AmbientLight ambientLight;
    private List<PointLight> luzesPontuais;
    
    private CGCanvas frente;
    private CGCanvas topo;
    private CGCanvas lateral;
    private CGCanvas perspectiva;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mundo = World.getInstance();
        
        ambientLight = new AmbientLight(10);
        luzesPontuais = new ArrayList();
        luzesPontuais.add(new PointLight(new Vertice(0, 0, 0), Color.WHITE));
        
        CGViewport viw = StandardConfigWinView.STD_VIEWPORT;
        int height = (int) viw.getDeltaV();
        int width  = (int) viw.getDeltaU();
        
        for (Vista v : mundo.getVistas()){
            Vertice observer = v.getPipelineCamera().getVRP();
            switch (v.getVisao()) { 
                case Frontal: frente  = new CGCanvas(this, v, width, height, new Flat(observer, ambientLight, luzesPontuais)); break;
                case Lateral: lateral = new CGCanvas(this, v, width, height, new Flat(observer, ambientLight, luzesPontuais)); break;
                case Topo:    topo    = new CGCanvas(this, v, width, height, new Flat(observer, ambientLight, luzesPontuais)); break;
                case Perspectiva: perspectiva = new CGCanvas(this, v, width, height, new Flat(observer, ambientLight, luzesPontuais)); break;
                default: throw new IllegalArgumentException("Visão não possui canvas equivalente adicionado.");
            }
        }
        flatMenu.setSelected(true);

        frentePane     .getChildren().add(frente);
        lateralPane    .getChildren().add(lateral);
        topoPane       .getChildren().add(topo);
        perspectivaPane.getChildren().add(perspectiva);
        
        //<editor-fold defaultstate="collapsed" desc="Property Binding">
        ///Zoom
        frente .zoomProperty().bindBidirectional(frenteZoom.textProperty());
        lateral.zoomProperty().bindBidirectional(lateralZoom.textProperty());
        topo   .zoomProperty().bindBidirectional(topoZoom.textProperty());
        perspectiva.zoomProperty().bindBidirectional(persZoom.textProperty());

        ///Selected Object
        selectedObjectProperty = new SimpleObjectProperty();
        selectedObjectProperty.bindBidirectional(
            frente.selectedObjectProperty()
        );
        selectedObjectProperty.bindBidirectional(
            lateral.selectedObjectProperty()
        );
        selectedObjectProperty.bindBidirectional(
            topo.selectedObjectProperty()
        );
        selectedObjectProperty.bindBidirectional(
            perspectiva.selectedObjectProperty()
        );
        
        ///Axis of Operation
        axisOfOperationProperty = new SimpleObjectProperty<>(Eixo.Eixo_XY);
        axisOfOperationProperty.bindBidirectional(
            frente.axisOfOperationProperty()
        );
        axisOfOperationProperty.bindBidirectional(
            lateral.axisOfOperationProperty()
        );
        axisOfOperationProperty.bindBidirectional(
            topo.axisOfOperationProperty()
        );
        axisOfOperationProperty.bindBidirectional(
            perspectiva.axisOfOperationProperty()
        );
        //</editor-fold>
                
        initializeTools(); //Listeners para a barra esquerda de ferramentas
        initializeMenuBar(); //Listeners para a barra de menu superior
        initializeViewToolbars(); //Listeners para as 4 barras de ferramentas das views
        initializeResizeAndCanvasHUD(); //Coloca Grids e listeners de resize
        
        paint();
    }
    
    public void forceSelectedObjUpdate(){
        mundo.updateSelectedColors(selectedObjectProperty.get());
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
        frenteGrid.setOpacity(Fatores.DEFAULT_OPACITY);
        /*grid.setFocusTraversable(true);*/ frenteGrid.setMouseTransparent(true);
        parentPane.getChildren().add(frenteGrid);
        frente.setGrid(frenteGrid);
        
        parentPane = (StackPane) lateral.getParent();
        parentPane.resize(lateral.getWidth(), lateral.getHeight());
        lateral.widthProperty().bind(parentPane.widthProperty());
        lateral.heightProperty().bind(parentPane.heightProperty());
        lateralGrid = new Grid(lateral, Visao.Lateral);
        lateralGrid.setOpacity(Fatores.DEFAULT_OPACITY);
        /*grid.setFocusTraversable(true);*/ lateralGrid.setMouseTransparent(true);
        parentPane.getChildren().add(lateralGrid);
        lateral.setGrid(lateralGrid);
        
        parentPane = (StackPane) topo.getParent();
        parentPane.resize(topo.getWidth(), topo.getHeight());
        topo.widthProperty().bind(parentPane.widthProperty());
        topo.heightProperty().bind(parentPane.heightProperty());
        topoGrid = new Grid(topo, Visao.Topo);
        topoGrid.setOpacity(Fatores.DEFAULT_OPACITY);
        /*grid.setFocusTraversable(true);*/ topoGrid.setMouseTransparent(true);
        parentPane.getChildren().add(topoGrid);
        topo.setGrid(topoGrid);
        
        parentPane = (StackPane) perspectiva.getParent();
        parentPane.resize(perspectiva.getWidth(), perspectiva.getHeight());
        perspectiva.widthProperty().bind(parentPane.widthProperty());
        perspectiva.heightProperty().bind(parentPane.heightProperty());
        persGrid = new Grid(perspectiva, Visao.Perspectiva);
        persGrid.setOpacity(Fatores.DEFAULT_OPACITY);
        /*grid.setFocusTraversable(true); grid.setMouseTransparent(true);*/ //Sem click na perspectiva mesmo...
        parentPane.getChildren().add(persGrid);
        perspectiva.setGrid(persGrid);
        
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
            new TreeItem<>(Ferramentas.LuzAmbiente.NAME, new ImageView(Ferramentas.LuzAmbiente.ICON)),
            new TreeItem<>(Ferramentas.LuzPontual.NAME,  new ImageView(Ferramentas.LuzPontual.ICON))
        );
        
        criacao.getChildren().addAll(
            new TreeItem<>(CriacaoPrevolucao.free.NAME,     new ImageView(CriacaoPrevolucao.free.ICON)),
            new TreeItem<>(CriacaoPrevolucao.gridSnap.NAME, new ImageView(CriacaoPrevolucao.gridSnap.ICON))
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
        //<editor-fold defaultstate="collapsed" desc="Grid Flags">
        showGrid.setOnAction((ActionEvent event) -> {
            boolean show = showGrid.selectedProperty().get();
            frenteGrid.showGrid(show);
            lateralGrid.showGrid(show);
            topoGrid.showGrid(show);
            persGrid.showGrid(show);

            if (show){ //Hardcoding intensifies
                tools.getRoot().getChildren().get(1).getChildren().add(new TreeItem<>(CriacaoPrevolucao.gridSnap.NAME, new ImageView(CriacaoPrevolucao.gridSnap.ICON)));
            } else {
                current_pol.set(CriacaoPrevolucao.free); // Tee hee hee
                tools.getRoot().getChildren().get(1).getChildren().remove(1);
            }
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
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Factors and background">
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

        backgroundMenu.setOnAction((ActionEvent event) -> {
            Pane pane = null;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Config/BackgroundColors.fxml"));
            loader.setController(new BackgroundColorsController(
                    frente.backgroundColorProperty(), lateral.backgroundColorProperty(),
                    topo.backgroundColorProperty(), perspectiva.backgroundColorProperty())
            );

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
            dialog.setTitle("Cores de fundo");
            dialog.show();
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Grid Options">
        gridThickness.setOnMouseReleased((Event event) -> {
            int thickness = gridThickness.valueProperty().intValue();
            frenteGrid.setGridThickness(thickness);
            lateralGrid.setGridThickness(thickness);
            topoGrid.setGridThickness(thickness);
            persGrid.setGridThickness(thickness);
        });

        Tooltip opac = new Tooltip("30");
        opac.setAutoHide(true);
        gridOpacity.setTooltip(opac);
        gridOpacity.setOnMouseDragged((MouseEvent event) -> {
            opac.setText(String.format("%.2f", gridOpacity.valueProperty().doubleValue()));
            gridOpacity.getTooltip().show(menu.getScene().getWindow(), event.getScreenX()+10, event.getScreenY()+10);
        });
        gridOpacity.setOnMouseClicked((MouseEvent event) -> {
            opac.setText(String.format("%.2f", gridOpacity.valueProperty().doubleValue()));
            gridOpacity.getTooltip().show(menu.getScene().getWindow(), event.getScreenX()+10, event.getScreenY()+10);
        });
        gridOpacity.setOnMouseExited((MouseEvent event) -> { opac.hide(); });
        gridOpacity.setOnMouseReleased((Event event) -> {
            double opacity = gridOpacity.valueProperty().get();
            frenteGrid.setOpacity(opacity);
            lateralGrid.setOpacity(opacity);
            topoGrid.setOpacity(opacity);
            persGrid.setOpacity(opacity);
        });

        Tooltip tool = new Tooltip("30");
        tool.setAutoHide(true);
        gridSize.setTooltip(tool);
        gridSize.setOnMouseDragged((MouseEvent event) -> {
            tool.setText(String.valueOf(Math.round(gridSize.valueProperty().doubleValue())));
            gridSize.getTooltip().show(menu.getScene().getWindow(), event.getScreenX()+10, event.getScreenY()+10);
        });
        gridSize.setOnMouseExited((MouseEvent event) -> { tool.hide(); });
        gridSize.setOnMouseClicked((MouseEvent event) -> {
            tool.setText(String.valueOf(Math.round(gridSize.valueProperty().doubleValue())));
            gridSize.getTooltip().show(menu.getScene().getWindow(), event.getScreenX()+10, event.getScreenY()+10);
        });
        gridSize.setOnMouseReleased((MouseEvent event) -> {
            int cellSize = gridSize.valueProperty().intValue();

            frenteGrid.setCellSizePX(cellSize);
            lateralGrid.setCellSizePX(cellSize);
            topoGrid.setCellSizePX(cellSize);
            persGrid.setCellSizePX(cellSize);
        });

        gridColor.valueProperty().set(Color.LIGHTGRAY);
        gridColor.valueProperty().addListener((ObservableValue<? extends Color> observable, Color oldValue, Color newValue) -> {
            Color color = newValue;
            frenteGrid.setGridColor(color);
            lateralGrid.setGridColor(color);
            topoGrid.setGridColor(color);
            persGrid.setGridColor(color);
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Objetos na cena">
        salvar.setOnAction((ActionEvent event) -> {
            if (showAxis.selectedProperty().get()) mundo.removeAxis();

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

            if (showAxis.selectedProperty().get()) mundo.addAxis();
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
            //selectedObject = null;
            selectedObjectProperty.set(null);
            //if (selectController != null) selectController.objectProperty().set(null);
            paint();

        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Escolha de Shader">
        wireframeMenu.setOnAction((ActionEvent event) -> {
            frente.setShader(new Wireframe());
            lateral.setShader(new Wireframe());
            topo.setShader(new Wireframe());
            perspectiva.setShader(new Wireframe()); 
        });
        
        flatMenu.setOnAction((ActionEvent event) -> {
            frente.setShader(new Flat(frente.getVista().getPipelineCamera().getVRP(), ambientLight, luzesPontuais));
            lateral.setShader(new Flat(lateral.getVista().getPipelineCamera().getVRP(), ambientLight, luzesPontuais));
            topo.setShader(new Flat(topo.getVista().getPipelineCamera().getVRP(), ambientLight, luzesPontuais));
            perspectiva.setShader(new Flat(perspectiva.getVista().getPipelineCamera().getVRP(), ambientLight, luzesPontuais)); 
        });
        
        gouraudMenu.setOnAction((ActionEvent event) -> {
            /*frente.setShader(new Flat(frente.getVista().getPipelineCamera().getVRP(), ambientLight, luzesPontuais));
            lateral.setShader(new Flat(lateral.getVista().getPipelineCamera().getVRP(), ambientLight, luzesPontuais));
            topo.setShader(new Flat(topo.getVista().getPipelineCamera().getVRP(), ambientLight, luzesPontuais));
            perspectiva.setShader(new Flat(perspectiva.getVista().getPipelineCamera().getVRP(), ambientLight, luzesPontuais));  */
            throw new UnsupportedOperationException("Gouraud não suportado ainda.");
        });
        //</editor-fold>
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
            ChangeListener<Boolean> listener = (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                frenteGrid.showHotkeys(newValue);
            };
            frente.autoChangeActiveProperty().addListener(listener);
            frente.setAutoCam(listener);
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
            ChangeListener<Boolean> listener = (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                lateralGrid.showHotkeys(newValue);
            };
            lateral.autoChangeActiveProperty().addListener(listener);
            lateral.setAutoCam(listener);
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
            ChangeListener<Boolean> listener = (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                topoGrid.showHotkeys(newValue);
            };
            topo.autoChangeActiveProperty().addListener(listener);
            topo.setAutoCam(listener);
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
            ChangeListener<Boolean> listener = (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                persGrid.showHotkeys(newValue);
            };
            perspectiva.autoChangeActiveProperty().addListener(listener);
            perspectiva.setAutoCam(listener);
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
    
    private Object controller;
    private Parent option;
    
    private byte previous_sel;
    private Ferramentas previous_ferr;
    private CriacaoPrevolucao previous_pol;
    private Transformacoes previous_tra;
    private boolean loadPrevious = true;
    
    @FXML //Clicar na árvore de ferramentas
    private void onMouseClickedToolsListener(MouseEvent e){
        Node node = e.getPickResult().getIntersectedNode();
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)){
            String name = (String) ((TreeItem)tools.getSelectionModel().getSelectedItem()).getValue();
            
            previous_sel  = CURRENT_SEL .get();
            previous_ferr = current_ferr.get();
            previous_pol  = current_pol .get();
            previous_tra  = current_tra .get();
            
            Ferramentas       ferr = Ferramentas.fromString(name);
            CriacaoPrevolucao  pol = CriacaoPrevolucao.fromString(name);
            Transformacoes     tra = Transformacoes.fromString(name);
            
            if (ferr != null){
                CURRENT_SEL.set(FERRAMENTA_SEL);
                current_ferr.set(ferr);
                loadPrevious = (previous_sel == FERRAMENTA_SEL) && (previous_ferr==ferr);
            } else if (pol != null){
                CURRENT_SEL.set(REVOLUCAO_SEL);
                current_pol.set(pol);
                loadPrevious = false;
            } else if (tra != null){
                CURRENT_SEL.set(TRANSFORMACAO_SEL);
                current_tra.set(tra);
                loadPrevious = false;
            } else {
                CURRENT_SEL.set(NOTHING_SEL);
                loadPrevious = (previous_sel == FERRAMENTA_SEL) && (previous_ferr==Ferramentas.Select);
            }
            
            handleSelectedTool();
        }
    }
       
    private void load(String fxml, Object controller){
        this.controller = controller;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            if (controller != null)
                loader.setController(controller);
            option = loader.load();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    //Adiciona opções conforme ferramentas
    private void handleSelectedTool(){ //BEWARE! FORSAKEN LAND!
        //System.out.println("PREVIOUS: " + previous_sel);
        //System.out.println("PREVIOUS: " + previous_ferr);
        //System.out.println("PREVIOUS: " + previous_pol);
        //System.out.println("PREVIOUS: " + previous_tra);
        
        //System.out.println("CURRENT: " + CURRENT_SEL.get());
        //System.out.println("CURRENT: " + current_ferr.get());
        //System.out.println("CURRENT: " + current_pol.get());
        //System.out.println("CURRENT: " + current_tra.get());
        
        if (loadPrevious && options.getChildren().size()>0){
            //if (options!=null)
            //options.getChildren().clear();
            //options.getChildren().add(option);
            //System.out.println("Load: " + loadPrevious);
            return;
        }
        
        switch(CURRENT_SEL.get()){
            case FERRAMENTA_SEL:
                if (null != current_ferr.get()) switch (current_ferr.get()) {
                    case LuzPontual:
                        load("/View/Options/LuzesPontuais.fxml", new LuzesPontuaisController(this, luzesPontuais));
                        options.getChildren().clear();
                        options.getChildren().add(option);
                        break;
                    case Select:
                        load("/View/Options/PolySelect.fxml", new PolySelectController(selectedObjectProperty, this));
                        options.getChildren().clear();
                        options.getChildren().add(option);
                        break;
                    case LuzAmbiente:
                        load("/View/Options/LuzAmbiente.fxml", new LuzAmbienteController(this));
                        options.getChildren().clear();
                        options.getChildren().add(option);
                        break;
                }
                break;
            case REVOLUCAO_SEL:
                if(null != current_pol.get() && current_pol.get() == CriacaoPrevolucao.free){                    
                    load("/View/Options/RevBuild.fxml", null);
                    options.getChildren().clear();
                    options.getChildren().add(option);
                }
                break;
                
            case TRANSFORMACAO_SEL:
                if (null != current_tra.get()) switch (current_tra.get()) {
                    case Escala:
                        load("/View/Options/Transformacao.fxml", new TransformacaoController(axisOfOperationProperty, Transformacoes.Escala));
                        options.getChildren().clear();
                        options.getChildren().add(option);
                        break;
                    case Cisalhamento:
                        load("/View/Options/Transformacao.fxml", new TransformacaoController(axisOfOperationProperty, Transformacoes.Cisalhamento));
                        options.getChildren().clear();
                        options.getChildren().add(option);
                        break;
                    case Rotacao:
                        load("/View/Options/Transformacao.fxml", new TransformacaoController(axisOfOperationProperty, Transformacoes.Rotacao));
                        options.getChildren().clear();
                        options.getChildren().add(option);
                        break;
                }
                break;
                
            case NOTHING_SEL:
                options.getChildren().clear();
                break;
        }
        
        // NOT USED
        //load("/View/Options/RegularPolygonOption.fxml", new RegularPolygonController());
        //options.getChildren().add(option);
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Controle de luzes">
    public AmbientLight getAmbientLight(){
        return ambientLight;
    }
    
    public void setAmbientLight(AmbientLight newLight){
        ambientLight.update(newLight);
        paint();
    }
    
    public List<PointLight> getPointLights(){
        return luzesPontuais;
    }
    
    public void setPointLight(int i, PointLight newLight){
        luzesPontuais.get(i).update(newLight);
        paint();
    }
    
    public void addPointLight(PointLight newLight){
        luzesPontuais.add(newLight);
        paint();
    }
    
    public void removePointLight(int index){
        luzesPontuais.remove(index);
        paint();
    }
//</editor-fold>
}
