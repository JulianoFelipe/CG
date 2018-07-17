/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import static View.MainController.FERRAMENTA_SEL;
import static View.MainController.NOTHING_SEL;
import static View.MainController.REVOLUCAO_SEL;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import m.poligonos.Aresta;
import m.poligonos.ArestaEixo;
import m.poligonos.CGObject;
import m.poligonos.Vertice;
import m.poligonos.we_edge.HE_Poliedro;
import m.poligonos.we_edge.WE_Aresta;
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

    private final ObjectProperty<CGObject> selectedObjProperty;
    private final StringProperty zoomProperty;
    private final BooleanProperty autoChangeActiveProperty;
    
    //Tools property
    private final ObjectProperty<Byte> selProperty;  
    private final ObjectProperty<Ferramentas> ferramentasProperty;  
    private final ObjectProperty<CriacaoPrevolucao> criacaoProperty;  
    private final ObjectProperty<Transformacoes> transformacoesProperty;  
        
    public CGCanvas(MainController controller, Vista vista, int width, int height) {
        super(width, height);
        
        this.controller = controller;
        this.selectedObjProperty = new SimpleObjectProperty<>();
        this.vista = vista;
        this.visao = vista.getVisao();
        
        this.mundo = World.getInstance();
        
        this.zoomProperty = new SimpleStringProperty("x "+String.format(java.util.Locale.US,"%.2f", vista.getPipe().getProportions()));
        this.autoChangeActiveProperty = new SimpleBooleanProperty(false);
        
        selProperty = new SimpleObjectProperty<>(NOTHING_SEL);  
        ferramentasProperty = new SimpleObjectProperty<>(Ferramentas.Delete);  
        criacaoProperty = new SimpleObjectProperty<>(CriacaoPrevolucao.gridSnap);  
        transformacoesProperty = new SimpleObjectProperty<>(Transformacoes.Rotacao);
        controller.bindToolsProperties(selProperty, ferramentasProperty, criacaoProperty, transformacoesProperty);
        
        ///////////////////////////////// SET MOUSE FUNCTIONS
        if (visao != Visao.Perspectiva){
            mouseOnClicked(); mouseOnMoved(); mouseOnPressed(); mouseOnDragged();
        }
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
    
    //<editor-fold defaultstate="collapsed" desc="Pintura">
    public void paint(){
        clear();
        
        //////////////////////////////////////////// OBJS
        GraphicsContext graphs = this.getGraphicsContext2D();
        graphs.setFill(Color.BLACK);
        graphs.setStroke(Color.BLACK);
        graphs.setLineWidth(1);
        
        List<CGObject> objs = vista.get2Dobjects();
        objs.forEach((obj) -> {
            paintObject(graphs, obj);
        });
        
        //////////////////////////////////////////// TEMPS
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
    }
    
    /**
     * Seleciona qual função de pintura específica chamar
     * @param graphics
     * @param obj
     */
    private void paintObject(GraphicsContext graphs, CGObject obj){
        CGObject selected_obj = selectedObjProperty.get();
        
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
        } else if (obj instanceof ArestaEixo){
            ArestaEixo objA = (ArestaEixo) obj;
            graphs.setStroke(objA.getAxisColor());
            paintConectedPointList(graphs, obj.getPoints(), 0);
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
    
    public void clear(){
        GraphicsContext cl = this.getGraphicsContext2D();
        cl.clearRect(0, 0, this.getWidth(), this.getHeight());
    }
//</editor-fold>   
        
    public void setAutoCam(){
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
                    return;
            }
            
            if(changeCam)
                pipe.getCamera().set(new Camera(viewUp, vrp, p));
            paint();
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
            paint();
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
    
    private int previousX, previousY;
    private int draggedCount = 0;
    
    private void mouseOnClicked(){
        this.setOnMouseClicked((MouseEvent e) -> {
            Vertice clicked = new Vertice((float) e.getX(), (float) e.getY());
            System.out.println("Frente Clicked: " + e.getX() + ", " + e.getY() + ", " + e.getZ());
            if (selProperty.get() == REVOLUCAO_SEL){
                if (criacaoProperty.get() == CriacaoPrevolucao.free){
                    vista.getPipe().reverseConversion(clicked);
                    mundo.addTempPoint(clicked);

                    //Nregular newreg = new Nregular(6, 66, clicked);
                    //////getVistaFromVisao(Visao.Frontal).getPipe().reverseConversion(newreg);
                    //mundo.addObject(newreg);
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
                            paint();
                            return;
                        }
                    }

                    selectedObjProperty.set(null);
                    //selectController.objectProperty().set(null); //Not needed? MainController listens?
                }
            }
            paint();
        });
    }
    
    private void mouseOnMoved(){
        
    }
    
    private void mouseOnPressed(){
        this.setOnMousePressed((MouseEvent event) -> {
            draggedCount=0;
        });
    }
    
    private void mouseOnDragged(){
        this.setOnMouseDragged((MouseEvent event) -> {
            int newMouseX = (int) event.getX(),
                newMouseY = (int) event.getY();
            
            if (draggedCount == 0){
                previousX = newMouseX;
                previousY = newMouseY;
                ++draggedCount;
                return;
            }

            CGObject object = vista.getObject(selectedObjProperty.get());

            Translacao t = new Translacao(true);
            t.transladar(-(previousX-newMouseX), -(previousY-newMouseY), 0, object);
            vista.update(object);
            
            //problema: pega-se objeto com coords da visão, onde z=0 e seta elas no mundo, que propaga com z=0 para as outras, resultando em problemas
            //Fix: copiar a coordenada não alterada em cada vista ao invés de zerá-la, já que ela não influenciará no desenho e será utilizada na ocultação
            
            previousX = newMouseX;
            previousY = newMouseY;
            paint();
            ++draggedCount;
        });
    }
}
