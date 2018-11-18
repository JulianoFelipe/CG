/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import static View.MainController.FERRAMENTA_SEL;
import static View.MainController.NOTHING_SEL;
import static View.MainController.REVOLUCAO_SEL;
import static View.MainController.TRANSFORMACAO_SEL;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import m.Camera;
import m.Eixo;
import m.Visao;
import m.Vista;
import m.World;
import m.pipeline.CGPipeline;
import m.poligonos.CGObject;
import m.poligonos.Vertice;
import m.shader.CGShader;
import m.shader.PointLight;
import m.transformacoes.Cisalhamento;
import m.transformacoes.Escala;
import m.transformacoes.Rotacao;
import m.transformacoes.Translacao;
import resource.description.CriacaoPrevolucao;
import resource.description.Ferramentas;
import resource.description.Transformacoes;

/**
 *
 * @author JFPS
 */
public final class CGCanvas extends Canvas{
    //Está aqui para lembrar de futuras possíveis mudanças.
    //Tirar todas as variações do MainController e colocar aqui, de maneira similar
    //pra como há apenas uma classe "Vista", não 4
    
    private final MainController controller;
    private final World mundo;
    private final Visao visao;
    private final Vista vista;
    private CGShader shader;
    private Grid grid;
    
    private final ObjectProperty<CGObject> selectedObjProperty;
    private final StringProperty zoomProperty;
    private final BooleanProperty autoChangeActiveProperty;
    private final ObjectProperty<Color> backgroundColorProperty;
    //private final BooleanProperty updatePaintProperty;
    
    //Tools property
    private final ObjectProperty<Byte> selProperty;  
    private final ObjectProperty<Ferramentas> ferramentasProperty;  
    private final ObjectProperty<CriacaoPrevolucao> criacaoProperty;  
    private final ObjectProperty<Transformacoes> transformacoesProperty;  
    private final ObjectProperty<Eixo> axisOfOperationProperty;
    
    public CGCanvas(MainController controller, Vista vista, int width, int height, CGShader shader) {
        super(width, height);
        
        this.controller = controller;
        this.selectedObjProperty = new SimpleObjectProperty<>();
        this.vista = vista;
        this.visao = vista.getVisao();
        this.shader = shader;

        this.mundo = World.getInstance();
        
        this.zoomProperty = new SimpleStringProperty("x "+String.format(java.util.Locale.US,"%.2f", vista.getPipe().getProportions()));
        this.autoChangeActiveProperty = new SimpleBooleanProperty(false);
        
        selProperty = new SimpleObjectProperty<>(NOTHING_SEL);  
        ferramentasProperty = new SimpleObjectProperty<>(Ferramentas.LuzAmbiente);  
        criacaoProperty = new SimpleObjectProperty<>(CriacaoPrevolucao.gridSnap);  
        transformacoesProperty = new SimpleObjectProperty<>(Transformacoes.Rotacao);
        controller.bindToolsProperties(selProperty, ferramentasProperty, criacaoProperty, transformacoesProperty);
        
        ///////////////////////////////// SET MOUSE FUNCTIONS
        if (visao != Visao.Perspectiva){
            mouseOnClicked(); mouseOnMoved(); mouseOnPressed(); mouseOnDragged(); mouseOnReleased();
        }
        
        //updatePaintProperty = new SimpleBooleanProperty(false);
        axisOfOperationProperty = new SimpleObjectProperty<>(Eixo.Eixo_XY);
         
        backgroundColorProperty = new SimpleObjectProperty<>();
        backgroundColorProperty.addListener((ObservableValue<? extends Color> observable, Color oldValue, Color newValue) -> {
            if (newValue == null){
                super.getParent().setStyle("-fx-background-color: transparent");
            } else {
                String webColor = String.format( "#%02X%02X%02X",
                        (int)( newValue.getRed() * 255 ),
                        (int)( newValue.getGreen() * 255 ),
                        (int)( newValue.getBlue() * 255 ) );
                super.getParent().setStyle("-fx-background-color: " + webColor);
            }
        });
        
    }

    public void setGrid(Grid grid){
        this.grid = grid;
    }
    
    public void setShader(CGShader newShader){
        this.shader = newShader;
        vista.clearLights();
        List<PointLight> lights = newShader.getPointLights();
        
        for (PointLight light : lights){
            vista.addLight(light.getPosicao());
        }
        
        paint();
    }
    
    public void updatePointLight(int index, PointLight pointLight){
        PointLight newL = new PointLight(pointLight);
        vista.getPipe().convert2D(newL.getPosicao());
        shader.updatePointLight(index, newL);
    }
    
    public void addPointLight(PointLight pointLight){
        PointLight newL = new PointLight(pointLight);
        vista.addLight(newL.getPosicao());
        shader.addPointLight(newL);
    }
    
    public void removePointLight(int index){
        vista.removeLight(index);
        shader.removePointLight(index);
    }
    
    public CGShader getShader(){
        return shader;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters & Properties">
    public ObjectProperty<Color> backgroundColorProperty(){
        return backgroundColorProperty;
    }
    
    public Vista getVista() {
        return vista;
    }
    
    public StringProperty zoomProperty() {
        return zoomProperty;
    }
    
    public ObjectProperty<CGObject> selectedObjectProperty(){
        return selectedObjProperty;
    }
    
    public BooleanProperty autoChangeActiveProperty(){
        return autoChangeActiveProperty;
    }
    
    public ObjectProperty<Eixo> axisOfOperationProperty(){
        return axisOfOperationProperty;
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Pintura">
    public void paint(){
        clear();
        
        CGObject selObj = selectedObjProperty.get();
        long id = (selObj == null ? -1 : selObj.getID());
        
        if (backgroundColorProperty.get() != null)
            shader.shade(vista.get2Dobjects(), this.getGraphicsContext2D(), id, backgroundColorProperty.get().invert());
        else
            shader.shade(vista.get2Dobjects(), this.getGraphicsContext2D(), id, Color.BLACK);
        
        shader.paintTemporaryPoints(vista.getTempPoints(), this.getGraphicsContext2D());
    }
    
    public void clear(){
        GraphicsContext cl = this.getGraphicsContext2D();
        cl.clearRect(0, 0, this.getWidth(), this.getHeight());
        //cl.setStroke(Color.BLACK);
        //cl.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
//</editor-fold>   
      
    //<editor-fold defaultstate="collapsed" desc="Listeners para alterações de Câmera/Window">
    private ChangeListener change;
    
    public void setAutoCam(ChangeListener listener){
        this.change = listener;
        autoChangeActiveProperty.set(true);
        if (visao == Visao.Perspectiva){
            setAutoCamPerspectiva();
        } else {
            setAutoCamOrtografica();
        }
    }
    
    private void setAutoCamOrtografica(){
        this.getParent().getParent().setStyle("-fx-border-color: blue");
        this.setFocusTraversable(true);
        this.addEventFilter(MouseEvent.ANY, (e) -> this.requestFocus());
        this.setOnKeyPressed((KeyEvent event1) -> {
            
            KeyCode pressed = event1.getCode();
            CGPipeline pipe = vista.getPipe();
            Vertice vrp    = pipe.getCamera().getVRP();
            Vertice p      = pipe.getCamera().getP();
            Vertice viewUp = pipe.getCamera().getViewUp();
            boolean changeCam = false;
            switch (pressed){
                case Z:
                    pipe.zoom(+Fatores.fator_zoom);
                    zoomProperty.set("x "+String.format(java.util.Locale.US,"%.2f", pipe.getProportions()));
                    break;
                case C:
                    pipe.zoom(-Fatores.fator_zoom);
                    zoomProperty.set("x "+String.format(java.util.Locale.US,"%.2f", pipe.getProportions()));
                    break;
                case W:
                    if (visao == Visao.Frontal){
                        vrp.setY(vrp.getY() + Fatores.fator_movimento_ort);
                        p.setY(  p.getY() + Fatores.fator_movimento_ort);
                    } else {
                        vrp.setZ(vrp.getZ() + Fatores.fator_movimento_ort); //Lateral e Topo tem Z no mesmo eixo
                        p.setZ(  p.getZ() + Fatores.fator_movimento_ort);
                    }
                    changeCam = true;
                    break;
                case A:
                    if (visao == Visao.Lateral){
                        vrp.setY(vrp.getY() + Fatores.fator_movimento_ort);
                        p.setY(  p.getY() + Fatores.fator_movimento_ort);
                    } else {
                        vrp.setX(vrp.getX() + Fatores.fator_movimento_ort); //Frontal e Topo tem X no mesmo eixo
                        p.setX(  p.getX() + Fatores.fator_movimento_ort);
                    }
                    changeCam = true;
                    break;
                case S:
                    if (visao == Visao.Frontal){
                        vrp.setY(vrp.getY() - Fatores.fator_movimento_ort);
                        p.setY(  p.getY() - Fatores.fator_movimento_ort);
                    } else {
                        vrp.setZ(vrp.getZ() - Fatores.fator_movimento_ort); //Lateral e Topo tem Z no mesmo eixo
                        p.setZ(  p.getZ() - Fatores.fator_movimento_ort);
                    }
                    changeCam = true;
                    break;
                case D:
                    if (visao == Visao.Lateral){
                        vrp.setY(vrp.getY() - Fatores.fator_movimento_ort);
                        p.setY(  p.getY() - Fatores.fator_movimento_ort);
                    } else {
                        vrp.setX(vrp.getX() - Fatores.fator_movimento_ort); //Frontal e Topo tem X no mesmo eixo
                        p.setX(  p.getX() - Fatores.fator_movimento_ort);
                    }
                    changeCam = true;
                    break;
                case ESCAPE:
                    this.setOnKeyPressed(null);
                    this.getParent().getParent().setStyle("-fx-border-color: black");
                    autoChangeActiveProperty.set(false);
                    autoChangeActiveProperty.removeListener(change);
                    return;
            }
            
            if(changeCam){
                pipe.getCamera().set(new Camera(viewUp, vrp, p));
                //System.out.println("SET NEW");
                for (int i=0; i<controller.getPointLights().size(); i++){
                    this.updatePointLight(i, controller.getPointLights().get(i));
                }
            }
                
            controller.paint();
        });
    }
    
    private void setAutoCamPerspectiva(){
        this.getParent().getParent().setStyle("-fx-border-color: blue");
        this.setFocusTraversable(true);
        this.addEventFilter(MouseEvent.ANY, (e) -> this.requestFocus());
        this.setOnKeyPressed((KeyEvent event1) -> {
            
            KeyCode pressed = event1.getCode();
            CGPipeline persPipe = vista.getPipe();
            Vertice vrp = persPipe.getCamera().getVRP();
            boolean changeCam = false;
            switch (pressed){
                case Z:
                    persPipe.zoom(+Fatores.fator_zoom);
                    zoomProperty.set("x "+String.format(java.util.Locale.US,"%.2f", persPipe.getProportions()));
                    break;
                case C:
                    persPipe.zoom(-Fatores.fator_zoom);
                    zoomProperty.set("x "+String.format(java.util.Locale.US,"%.2f", persPipe.getProportions()));
                    break;
                case Q:
                    vrp.setX(vrp.getX() - Fatores.fator_movimento_pers);
                    changeCam = true;
                    break;
                case E:
                    vrp.setX(vrp.getX() + Fatores.fator_movimento_pers);
                    changeCam = true;
                    break;
                case W:
                    vrp.setY(vrp.getY() - Fatores.fator_movimento_pers);
                    changeCam = true;
                    break;
                case A:
                    vrp.setZ(vrp.getZ() - Fatores.fator_movimento_pers);
                    changeCam = true;
                    break;
                case S:
                    vrp.setY(vrp.getY() + Fatores.fator_movimento_pers);
                    changeCam = true;
                    break;
                case D:
                    vrp.setZ(vrp.getZ() + Fatores.fator_movimento_pers);
                    changeCam = true;
                    break;
                case ESCAPE:
                    this.setOnKeyPressed(null);
                    this.setOnMouseDragged(null);
                    previousX = previousY = -1;
                    this.getParent().getParent().setStyle("-fx-border-color: black");
                    autoChangeActiveProperty.set(false);
                    return;
            }
            if(changeCam)
                persPipe.getCamera().setVRP(vrp);
            controller.paint();
        });
        /*perspectiva.setOnMouseDragged((MouseEvent event1) -> {
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
        
        Vista pers = perspectiva.getVista();
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
        paint();
        
        previousX = currentX;
        previousY = currentY;
        });*/
    }
//</editor-fold>
    
    private int previousX, previousY;
    private int draggedCount = 0;
    
    private void mouseOnClicked(){
        this.setOnMouseClicked((MouseEvent e) -> {
            Vertice clicked = new Vertice((float) e.getX(), (float) e.getY());
            //System.out.println(visao + " Clicked: " + e.getX() + ", " + e.getY() + ", " + e.getZ());
            if (selProperty.get() == REVOLUCAO_SEL){
                if (criacaoProperty.get() == CriacaoPrevolucao.free){
                    vista.getPipe().reverseConversion(clicked);
                    mundo.addTempPoint(clicked);

                    //Nregular newreg = new Nregular(6, 66, clicked);
                    //////getVistaFromVisao(Visao.Frontal).getPipe().reverseConversion(newreg);
                    //mundo.addObject(newreg);
                } else if (criacaoProperty.get() == CriacaoPrevolucao.gridSnap){
                    float[] closestIntercept = grid.closestIntercept((int) e.getX(), (int) e.getY(), Fatores.fator_threshold);
                    if (closestIntercept == null) return;
                    else{
                        clicked.setX(closestIntercept[0]);
                        clicked.setY(closestIntercept[1]);
                        vista.getPipe().reverseConversion(clicked);
                        mundo.addTempPoint(clicked);
                    }
                }
            } else if (selProperty.get() == FERRAMENTA_SEL){
                if (ferramentasProperty.get() == Ferramentas.Select){
                    for (CGObject obj : vista.get2Dobjects()){
                        /*if (PMath.proximoDeQualquerVerticeDoPoligono(obj, clicked)){
                        selected_obj = obj;
                        selectController.objectProperty().set(obj);
                        paint();
                        return;
                        }*/
                        if (obj.contains(clicked.getX(), clicked.getY(), Eixo.Eixo_XY)){
                            selectedObjProperty.set(obj);
                            //selectController.objectProperty().set(obj);
                            controller.paint();
                            return;
                        }
                    }

                    selectedObjProperty.set(null);
                    //selectController.objectProperty().set(null); //Not needed? MainController listens?
                }
            }
            controller.paint();
        });
    }
    
    private void mouseOnMoved(){
        this.setOnMouseMoved((Event event) -> {
            if (selProperty.get() == TRANSFORMACAO_SEL){
                
                Transformacoes t = transformacoesProperty.get();
                if (t == Transformacoes.Cisalhamento || t == Transformacoes.Escala){
                    
                    if (axisOfOperationProperty.get() == Eixo.Eixo_X)
                        this.setCursor(Cursor.H_RESIZE);   
                    else if (axisOfOperationProperty.get() == Eixo.Eixo_Y)
                        this.setCursor(Cursor.V_RESIZE);
                    else
                        this.setCursor(Cursor.MOVE);
                    
                } else if (t == Transformacoes.Translacao){
                    this.setCursor(Cursor.OPEN_HAND);
                } else {
                    this.setCursor(Cursor.HAND);
                }
                
            } else {
                this.setCursor(Cursor.DEFAULT);
            }
        });
    }
    
    private void mouseOnReleased(){
        this.setOnMouseReleased((MouseEvent event) -> {
            draggedCount = 0;
            if (selProperty.get() == TRANSFORMACAO_SEL){
                
                Transformacoes t = transformacoesProperty.get();
                if (t == Transformacoes.Cisalhamento || t == Transformacoes.Escala){
                    
                    if (axisOfOperationProperty.get() == Eixo.Eixo_X)
                        this.setCursor(Cursor.H_RESIZE);   
                    else if (axisOfOperationProperty.get() == Eixo.Eixo_Y)
                        this.setCursor(Cursor.V_RESIZE);
                    else
                        this.setCursor(Cursor.MOVE);
                    
                } else if (t == Transformacoes.Translacao){
                    this.setCursor(Cursor.OPEN_HAND);
                } else {
                    this.setCursor(Cursor.DEFAULT);
                }
                
            } else
                this.setCursor(Cursor.DEFAULT);
        });
    }
    
    private void mouseOnPressed(){
        this.setOnMousePressed((MouseEvent event) -> {
            draggedCount=0;
            if (selProperty.get() == TRANSFORMACAO_SEL){
                if (transformacoesProperty.get() == Transformacoes.Translacao)
                    this.setCursor(Cursor.CLOSED_HAND);
            } else
                this.setCursor(Cursor.DEFAULT);
        });
    }
    
    private void mouseOnDragged(){
        this.setOnMouseDragged((MouseEvent event) -> {
            if (selectedObjProperty.get() == null) return;
            
            if (selProperty.get() == TRANSFORMACAO_SEL){
                int newMouseX = (int) event.getX(),
                    newMouseY = (int) event.getY();
            
                if (draggedCount == 0){
                    previousX = newMouseX;
                    previousY = newMouseY;
                    ++draggedCount;
                    return;
                }

                CGObject object = vista.getObject(selectedObjProperty.get());
                              
                float signalX=-1, signalY=-1, signalZ=1;
                
                Eixo axisOfOp = axisOfOperationProperty.get();
                
                if (transformacoesProperty.get() == Transformacoes.Cisalhamento){
                    
                    if (axisOfOp == Eixo.Eixo_X || axisOfOp == Eixo.Eixo_XY)
                        signalX = (previousX > newMouseX ? Fatores.getFatorCisalhamentoMinus() : (previousX < newMouseX ? Fatores.getFatorCisalhamentoPlus(): 0));
                   else signalX = 0;

                   if (axisOfOp == Eixo.Eixo_Y || axisOfOp == Eixo.Eixo_XY)
                        signalY = (previousY > newMouseY ? Fatores.getFatorCisalhamentoMinus() : (previousY < newMouseY ? Fatores.getFatorCisalhamentoPlus() : 0));
                   else signalY = 0; 
                   
                } else if (transformacoesProperty.get() == Transformacoes.Escala){
                    
                    if (axisOfOp == Eixo.Eixo_X || axisOfOp == Eixo.Eixo_XY || axisOfOp == Eixo.Eixo_XYZ)
                         signalX = (previousX > newMouseX ? Fatores.getFatorEscalaMinus() : (previousX < newMouseX ? Fatores.getFatorEscalaPlus() : 1));
                    else signalX = 1;

                    if (axisOfOp == Eixo.Eixo_Y || axisOfOp == Eixo.Eixo_XY || axisOfOp == Eixo.Eixo_XYZ)
                         signalY = (previousY > newMouseY ? Fatores.getFatorEscalaPlus() : (previousY < newMouseY ? Fatores.getFatorEscalaMinus() : 1));
                    else signalY = 1; 
                    
                    if (axisOfOp == Eixo.Eixo_XYZ){
                             if (signalX != 1) signalZ = signalY = signalX;
                        else if (signalY != 1) signalZ = signalX = signalY;
                    }

                } else if (transformacoesProperty.get() == Transformacoes.Rotacao){
                    
                    signalX = (previousX > newMouseX ? -Fatores.fator_rotacao : (previousX < newMouseX ? Fatores.fator_rotacao : 0));
                    //signalY = (previousY > newMouseY ? -Fatores.fator_rotacao : (previousY < newMouseY ? Fatores.fator_rotacao : 0));
                    
                }
                
                switch (transformacoesProperty.get()){
                    case Cisalhamento:
                        Cisalhamento c = new Cisalhamento(true);
                        c.cisalhamento(signalX, signalY, object, object.getCentroide());
                        break;
                    case Rotacao:
                        Rotacao r = new Rotacao(true);
                             if (visao == Visao.Frontal) r.rotacaoZ(signalX, object, object.getCentroide());
                        else if (visao == Visao.Lateral) r.rotacaoX(signalX, object, object.getCentroide());
                        else if (visao == Visao.Topo)    r.rotacaoY(signalX, object, object.getCentroide());
                        break;
                    case Escala:
                        Escala e = new Escala(true);
                        e.escala(signalX, signalY, signalZ, object, object.getCentroide());
                        break;
                    case Translacao:
                        Translacao t = new Translacao(true);
                        t.transladar(-(previousX-newMouseX), -(previousY-newMouseY), 0, object);
                        break;
                    default: throw new IllegalArgumentException("Transformação não prevista para evento Drag.");
                }
                
                vista.update(object);

                //problema: pega-se objeto com coords da visão, onde z=0 e seta elas no mundo, que propaga com z=0 para as outras, resultando em problemas
                //Fix: copiar a coordenada não alterada em cada vista ao invés de zerá-la, já que ela não influenciará no desenho e será utilizada na ocultação

                previousX = newMouseX;
                previousY = newMouseY;
                controller.paint();
                ++draggedCount;
            }
        });
    }
}
