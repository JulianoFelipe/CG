/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Logging.BufferedPaneOutputStream;
import Logging.PaneHandler;
import Model.Aresta;
import Model.Eixo;
import Model.Movimento;
import Model.Poligono;

//https://en.wikipedia.org/wiki/Orthographic_projection
//https://www.google.com.br/search?client=firefox-b-ab&dcr=0&ei=RUj-WffCIIOawAS5goyYCQ&q=projection+plane&oq=projection+plane&gs_l=psy-ab.3..0i19k1l10.1801.3934.0.4075.16.13.0.0.0.0.318.1788.0j5j3j1.9.0....0...1.1.64.psy-ab..7.9.1782...0j0i131k1j0i67k1j0i3k1j0i22i30k1j0i22i10i30k1.0.mhXAfFimwHE


import Model.Vertice;
import Model.Nregular;
import Model.Transformações.Cisalhamento;
import Model.Transformações.Escala;
import Model.Transformações.Rotacao;
import Model.Transformações.Translacao;
import ioScene.InputScene;
import ioScene.OutputScene;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import utils.PMath;
import utils.VMath;

/**
 *
 * @author Anderson
 */
public class MainV extends javax.swing.JFrame {
    private static final int MAX_LADOS = 20;
    private static final double LINE_PROXIMITY_THRESHOLD = 3.9;
    private static final Logger LOG = Logger.getLogger("CG");
    private final JToggleButton ghost = new JToggleButton();
    private DrawablePanel panelCp;

    private int noPointsToCreate = -2;
    private boolean pendingCreating = false;
    private boolean regularSidedPolygon = false;
    private boolean regularSidedLock = false;
    private List<Vertice> temporaryList = new ArrayList();  
    private Poligono selectedPolygon = null;
    
    private static final byte NO_ACTION        = 0;
    private static final byte ROTATE_ACTION    = 1;
    private static final byte TRANSLATE_ACTION = 2;
    private static final byte SHEAR_ACTION     = 3;
    private static final byte SCALE_ACTION     = 4;
    private byte currentAction = NO_ACTION;
    private Vertice previousDrag;
    private Vertice firstActionPoint = null;
    private boolean invertSlope = false;
    
    private boolean controlFLAG = false;
    private boolean shiftFLAG = false;
    
    private void resetPaint(){
        panelCp.repaint();
    }
    
    private void resetDrawingState(){
        regularSidedLock = false;
        pendingCreating = false;
        noPointsToCreate = -1;
        panelCp.nullTemps();
        //unToggle();
        temporaryList = new ArrayList<>();
    }
    
    //https://en.wikipedia.org/wiki/Z-order_curve // Para armazenar "texture maps"?
    private void addMouseListeners(){
        paneMs.addMouseListener(new java.awt.event.MouseAdapter() {           
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {                               
                int x = evt.getX();
                int y = evt.getY();
                
                if (currentAction != NO_ACTION){
                    previousDrag = new Vertice(x, y);
                    return;
                }

                //<editor-fold defaultstate="collapsed" desc="Select Action">
                if(selectBt.isSelected()){
                    List<Poligono> lista = panelCp.getListaPoligonos();
                    Vertice point = new Vertice(x, y);

                    for(int i=0; i<lista.size(); i++){
                        List<Vertice> vertices = lista.get(i).getVertices();
                        boolean toAdd = false;

                        innerFor: for(int j=0; j<vertices.size(); j++){
                            Vertice a = vertices.get(j), b;
                            if (j == vertices.size()-1) b=vertices.get(0);
                            else b=vertices.get(j+1);
                            
                            //System.out.println(VMath.shortestDistance(a, b, point));
                            if (VMath.shortestDistance(a, b, point) < LINE_PROXIMITY_THRESHOLD){
                                toAdd = true;
                                break innerFor;
                            }
                        }

                        if (toAdd){
                            LOG.info("Polígono selecionado.");
                            selectedPolygon = lista.get(i);
                            panelCp.setSelectedPolygon(selectedPolygon);
                            resetPaint();
                            return;
                        }
                    }
                    return;
                }
                //</editor-fold>
                
                if(!pendingCreating){
                    int noTyped = getNumberOfSidesFromBtSelected();
                    if (noTyped == -1){
                        LOG.warning("Selecione um tipo de polígono para desenhar.");
                        return;
                    } else {
                        pendingCreating = true;
                        noPointsToCreate = noTyped;
                        temporaryList = new ArrayList<>();
                    }
                }

                if (regularSidedLock){   
                    Vertice radiusPnt = new Vertice((float) x, (float)y);
                    int dist = (int) VMath.distancia(temporaryList.get(0), radiusPnt);
                    panelCp.addPoligono(localPolygonBuilder(dist));  //new Nregular(jSlider1.getValue(), dist, temporaryList.get(0), 0.));
                    panelCp.cleanTempRegular();
                    
                    resetDrawingState();
                    panelCp.repaint();
                    regularSidedLock = false;
                    regularSidedPolygon = false;
                    return ;
                }
                
                if (SwingUtilities.isRightMouseButton(evt)){
                    if (temporaryList.size() < 3){
                        temporaryList.clear();
                        resetDrawingState();
                        panelCp.cleanTempoLines();
                    } else {
                        panelCp.addPoligono(localPolygonBuilder()); ///new Poligono(temporaryList));          
                        resetDrawingState();
                        panelCp.cleanTempoLines();
                    }
                } else if (noPointsToCreate == 1){
                    temporaryList.add(new Vertice((float) x, (float)y));
                    panelCp.addPoligono(localPolygonBuilder()); ///new Poligono(temporaryList));        
                    resetDrawingState();
                    panelCp.cleanTempoLines();
                } else {
                    temporaryList.add(new Vertice((float) x, (float)y));
                    --noPointsToCreate;
                    if (temporaryList.size() >= 2){
                        int size = temporaryList.size();
                        panelCp.addTempoLine(new Aresta(temporaryList.get(size-2), temporaryList.get(size-1)));
                    }    
                    
                    if (regularSidedPolygon){
                        regularSidedLock = true;
                    }
                    LOG.info("Ponto capturado.");
                }
                panelCp.repaint();
            } 

            @Override
            public void mouseReleased(MouseEvent e) {
                panelCp.setAnchor(null);
                resetPaint();
            }            
        });
        
        paneMs.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                
                if (currentAction != NO_ACTION){
                    Vertice v = new Vertice((float) x, (float)y);
                    //<editor-fold defaultstate="collapsed" desc="Escala e Cisalhamento Move">
                    if (currentAction==SHEAR_ACTION || currentAction==SCALE_ACTION){
                        int selSize = selectedPolygon.getVertices().size();
                        boolean toAdd=false;
                        Aresta closeLine = null;
                        for(int j=0; j<selSize; j++){
                            Vertice a = selectedPolygon.getVertices().get(j), b;
                            if (j == selSize-1) b=selectedPolygon.getVertices().get(0);
                            else b=selectedPolygon.getVertices().get(j+1);

                            if (VMath.shortestDistance(a, b, v) < LINE_PROXIMITY_THRESHOLD){
                                toAdd = true;
                                closeLine = new Aresta(a,b);
                                break;
                            }
                        }
                        if (toAdd){
                            /*boolean isVert=VMath.isLineVertical(closeLine), isHori=VMath.isLineHorizontal(closeLine);
                                
                            if (isVert && !isHori && controlFLAG){
                                paneMs.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                            } else if (isHori && !isVert && controlFLAG){
                                paneMs.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
                            } else if (!controlFLAG){
                                paneMs.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                            } else {
                                if (Math.abs(VMath.lineSlope(closeLine)) == 1.0)
                                    paneMs.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                                else
                                    paneMs.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            }*/
                                 if ( controlFLAG && !shiftFLAG) paneMs.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                            else if (!controlFLAG &&  shiftFLAG) paneMs.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
                            //else if ( controlFLAG &&  shiftFLAG) paneMs.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                            else                                 paneMs.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                        } else {
                            paneMs.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Resto Move">
                    } else {
                        Vertice prox = PMath.verticeProximoDeQualquerVerticeDoPoligono(selectedPolygon, v);
                        if (prox != null){
                            paneMs.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            firstActionPoint = prox;
                        } else
                            paneMs.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                    //</editor-fold>
                }
                
                if (temporaryList.size() < 1) return;
                if (!pendingCreating) return;
                //paneMs.repaint();
                               
                if (regularSidedLock){
                    Vertice radiusPnt = new Vertice((float) x, (float)y);
                    int dist = (int) VMath.distancia(temporaryList.get(0), radiusPnt);
                    panelCp.setTempRegular(localPolygonBuilder(dist));
                } else {
                    //panelCp.cleanTempoLines();
                    Vertice last = temporaryList.get(temporaryList.size()-1);
                    panelCp.setMovable(new Aresta(new Vertice((float) x, (float) y), last));
                }
                panelCp.repaint();
                
                //paneMs.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                Vertice curr = new Vertice(e.getX(), e.getY());
                if (selectedPolygon!=null && currentAction!=NO_ACTION){
                    boolean anchor = centerAnchorBox.isSelected();
                    Vertice center = null;
                    if (anchor){
                        center = selectedPolygon.getCentroideDaMedia();
                        panelCp.setAnchor(center);
                    }
                    //<editor-fold defaultstate="collapsed" desc="Cisalhamento Drag">
                    if (currentAction == SHEAR_ACTION){
                        Cisalhamento c = new Cisalhamento();
                        Movimento v = VMath.movimentoVertical(previousDrag, curr);
                        Movimento h = VMath.movimentoHorizontal(previousDrag, curr);
                            
                        switch (paneMs.getCursor().getType()) {   //Invertido!!!!!
                            case Cursor.N_RESIZE_CURSOR: //Vertical
                                if (v == Movimento.Cima) 
                                    selectedPolygon = c.cisalhamento(Eixo.Eixo_Y,  0.01, selectedPolygon, center);
                                else if (v == Movimento.Baixo)
                                    selectedPolygon = c.cisalhamento(Eixo.Eixo_Y, -0.01, selectedPolygon, center);
                                break;
                            case Cursor.E_RESIZE_CURSOR: //Horizontal
                                if (h == Movimento.Direita)
                                    selectedPolygon = c.cisalhamento(Eixo.Eixo_X,  0.01, selectedPolygon, center);
                                else if (h == Movimento.Esquerda)
                                    selectedPolygon = c.cisalhamento(Eixo.Eixo_X, -0.01, selectedPolygon, center);
                                break;
                            case Cursor.MOVE_CURSOR:
                                if (h == Movimento.Direita)
                                    selectedPolygon = c.cisalhamento(Eixo.Eixo_X,  0.01, selectedPolygon, center);
                                else if (h == Movimento.Esquerda)
                                    selectedPolygon = c.cisalhamento(Eixo.Eixo_X, -0.01, selectedPolygon, center);
                                if (v == Movimento.Cima) 
                                    selectedPolygon = c.cisalhamento(Eixo.Eixo_Y,  0.01, selectedPolygon, center);
                                else if (v == Movimento.Baixo)
                                    selectedPolygon = c.cisalhamento(Eixo.Eixo_Y, -0.01, selectedPolygon, center);
                                break;
                        }
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Translação Drag">
                        } else if (currentAction == TRANSLATE_ACTION){
                            Translacao t = new Translacao();
                            selectedPolygon = t.transladar((int) -(previousDrag.getX() - curr.getX()),
                                                           (int) -(previousDrag.getY() - curr.getY()),
                                                           0, selectedPolygon);
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Escala Drag">
                    } else if (currentAction == SCALE_ACTION){
                        Escala sc = new Escala();
                        Movimento v = VMath.movimentoVertical(previousDrag, curr);
                        Movimento h = VMath.movimentoHorizontal(previousDrag, curr);
                        
                        switch (paneMs.getCursor().getType()) {   //Invertido!!!!!
                            case Cursor.N_RESIZE_CURSOR: //Vertical
                                if (v == Movimento.Cima)
                                    selectedPolygon = sc.escala(Eixo.Eixo_Y, 1.01, selectedPolygon, center);
                                else if (v == Movimento.Baixo)
                                    selectedPolygon = sc.escala(Eixo.Eixo_Y, 0.99, selectedPolygon, center);
                                break;
                            case Cursor.E_RESIZE_CURSOR: //Horizontal
                                if (h == Movimento.Esquerda)
                                    selectedPolygon = sc.escala(Eixo.Eixo_X, 1.01, selectedPolygon, center);
                                else if (h == Movimento.Direita)
                                    selectedPolygon = sc.escala(Eixo.Eixo_X, 0.99, selectedPolygon, center);
                                break;
                            case Cursor.MOVE_CURSOR:
                                if (v == Movimento.Cima)
                                    selectedPolygon = sc.escala(Eixo.Eixo_Y, 1.01, selectedPolygon, center);
                                else if (v == Movimento.Baixo)
                                    selectedPolygon = sc.escala(Eixo.Eixo_Y, 0.99, selectedPolygon, center);
                                if (h == Movimento.Esquerda)
                                    selectedPolygon = sc.escala(Eixo.Eixo_X, 1.01, selectedPolygon, center);
                                else if (h == Movimento.Direita)
                                    selectedPolygon = sc.escala(Eixo.Eixo_X, 0.99, selectedPolygon, center);
                                break;
                        }
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Rotação Drag">
                    } else if (currentAction == ROTATE_ACTION){
                        Vertice rotAxis = null;
                        if (center != null) rotAxis = center;
                        else rotAxis = new Vertice(firstActionPoint);
                        Movimento m = VMath.movimentoHorizontal(previousDrag, curr);
                        Rotacao r = new Rotacao();
                        if (m == Movimento.Direita)
                            selectedPolygon = r.rotacao(0.05, selectedPolygon, rotAxis);
                        else if (m == Movimento.Esquerda)
                            selectedPolygon = r.rotacao(-0.05, selectedPolygon, rotAxis);
                    }
                    //</editor-fold>
                }
                panelCp.setSelectedPolygon(selectedPolygon);
                resetPaint();
                previousDrag = curr;
            }
        });
    }
    
    /**
     * Creates new form Principal
     */
    public MainV() {  
        //Toolkit.getDefaultToolkit().setDynamicLayout(false);
        initComponents();
        panelCp = new DrawablePanel(paneMs.getGraphics());
        paneMs.add(panelCp);
        addMouseListeners();

        consolePane.setEditable(false);
        BufferedPaneOutputStream oStream = new BufferedPaneOutputStream(consolePane);
        LOG.addHandler(new PaneHandler(oStream));
        LOG.setLevel(Level.FINE);
        LOG.log(Level.INFO, "Cena inicializada...");
        LOG.info("O botão \"Cancelar\" pode servir para forçar a atualização da pintura da cena.");
        buttonGroup1.add(ghost); //Para "deselecionar" os botões
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    controlFLAG = e.isControlDown();
                    shiftFLAG = e.isShiftDown();
                  return false;
                }
          });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        paintBt = new javax.swing.JButton();
        bordaColor = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        bordaBox = new javax.swing.JCheckBox();
        fundoBox = new javax.swing.JCheckBox();
        fundoColor = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        irregularPoligonBt = new javax.swing.JToggleButton();
        regularNsided = new javax.swing.JToggleButton();
        jSlider1 = new javax.swing.JSlider();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        consolePane = new javax.swing.JTextPane();
        paneMs = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        selectBt = new javax.swing.JToggleButton();
        cancelBt = new javax.swing.JButton();
        deleteBt = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        translateBt1 = new javax.swing.JToggleButton();
        rotateBt1 = new javax.swing.JToggleButton();
        shearBt1 = new javax.swing.JToggleButton();
        scaleBt1 = new javax.swing.JToggleButton();
        centerAnchorBox = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        saveMenu = new javax.swing.JMenuItem();
        loadMenu = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        javaFillRadio = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        jMenu3 = new javax.swing.JMenu();
        defaultColorRadio = new javax.swing.JRadioButtonMenuItem();
        setColorRadio = new javax.swing.JRadioButtonMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                formWindowDeiconified(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Pintura"));
        jPanel2.setToolTipText("");

        paintBt.setText("Pintar");
        paintBt.setToolTipText("Pintar polígono selecionado na cor exibida");
        paintBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paintBtActionPerformed(evt);
            }
        });

        bordaColor.setBackground(new java.awt.Color(0, 0, 0));
        bordaColor.setForeground(new java.awt.Color(0, 0, 0));
        bordaColor.setToolTipText("Seleção de cores");
        bordaColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bordaColorActionPerformed(evt);
            }
        });

        jButton1.setText("Transparente");
        jButton1.setToolTipText("Selecionar cor transparente");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        bordaBox.setText("Borda");
        bordaBox.setToolTipText("");
        bordaBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bordaBoxActionPerformed(evt);
            }
        });

        fundoBox.setText("Fundo");

        fundoColor.setText("null");
        fundoColor.setToolTipText("Seleção de cores");
        fundoColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fundoColorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bordaBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bordaColor, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(fundoBox, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fundoColor, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(paintBt, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paintBt)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bordaBox)
                    .addComponent(fundoBox))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(bordaColor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(fundoColor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Objetos"));

        jLabel2.setText("Lados para polígono regular");

        buttonGroup1.add(irregularPoligonBt);
        irregularPoligonBt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Irregular.jpg"))); // NOI18N
        irregularPoligonBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irregularPoligonBtActionPerformed(evt);
            }
        });

        buttonGroup1.add(regularNsided);
        regularNsided.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/regularPolygon.png"))); // NOI18N
        regularNsided.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regularNsidedActionPerformed(evt);
            }
        });

        jSlider1.setMajorTickSpacing(3);
        jSlider1.setMaximum(20);
        jSlider1.setMinimum(3);
        jSlider1.setMinorTickSpacing(1);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setToolTipText("");
        jSlider1.setValue(3);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(irregularPoligonBt, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(regularNsided, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(irregularPoligonBt)
                    .addComponent(regularNsided))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Console"));

        jScrollPane1.setViewportView(consolePane);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
        );

        paneMs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                paneMsKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paneMsKeyReleased(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Ferramentas"));
        jPanel5.setToolTipText("");

        buttonGroup1.add(selectBt);
        selectBt.setText("Selecionar");
        selectBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectBtActionPerformed(evt);
            }
        });

        cancelBt.setText("Cancelar");
        cancelBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtActionPerformed(evt);
            }
        });

        deleteBt.setText("Excluir");
        deleteBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(selectBt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteBt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelBt))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Transformações"));
        jPanel4.setToolTipText("");

        buttonGroup1.add(translateBt1);
        translateBt1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/translacao.png"))); // NOI18N
        translateBt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                translateBt1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(rotateBt1);
        rotateBt1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/rotacao.png"))); // NOI18N
        rotateBt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotateBt1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(shearBt1);
        shearBt1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cisalhamento.png"))); // NOI18N
        shearBt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shearBt1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(scaleBt1);
        scaleBt1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/escala.png"))); // NOI18N
        scaleBt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scaleBt1ActionPerformed(evt);
            }
        });

        centerAnchorBox.setSelected(true);
        centerAnchorBox.setText("Ancorar centróide");
        centerAnchorBox.setToolTipText("Torna invariante em relação ao centróide.");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(centerAnchorBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rotateBt1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scaleBt1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(translateBt1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shearBt1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(translateBt1)
                    .addComponent(rotateBt1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scaleBt1)
                    .addComponent(shearBt1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(centerAnchorBox)
                .addContainerGap())
        );

        jMenu1.setText("Arquivo");

        saveMenu.setText("Salvar");
        saveMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuActionPerformed(evt);
            }
        });
        jMenu1.add(saveMenu);

        loadMenu.setText("Carregar");
        loadMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMenuActionPerformed(evt);
            }
        });
        jMenu1.add(loadMenu);

        jMenuItem1.setText("Nova cena");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Algoritmo de Preenchimento");

        buttonGroup2.add(javaFillRadio);
        javaFillRadio.setText("Fill Java");
        javaFillRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javaFillRadioActionPerformed(evt);
            }
        });
        jMenu2.add(javaFillRadio);

        buttonGroup2.add(jRadioButtonMenuItem2);
        jRadioButtonMenuItem2.setSelected(true);
        jRadioButtonMenuItem2.setText("Fill CG (Manual)");
        jRadioButtonMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jRadioButtonMenuItem2);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Pintura");

        buttonGroup4.add(defaultColorRadio);
        defaultColorRadio.setText("Criar polígonos com cor padrão");
        jMenu3.add(defaultColorRadio);

        buttonGroup4.add(setColorRadio);
        setColorRadio.setSelected(true);
        setColorRadio.setText("Criar polígonos com cor da área de pintura");
        jMenu3.add(setColorRadio);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Ajuda");

        jMenuItem2.setText("Controles");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(paneMs, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(2, 2, 2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(paneMs))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void irregularPoligonBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_irregularPoligonBtActionPerformed
        LOG.info("Clique em no máximo " + MAX_LADOS +  " e no mínimo 3 pontos para formar um polígono. O botão direito do mouse finaliza criação.");
        pendingCreating = true;
        noPointsToCreate = getNumberOfSidesFromBtSelected();
        temporaryList = new ArrayList<>();
        selectedPolygon = null;
        panelCp.setSelectedPolygon(selectedPolygon);
        currentAction = NO_ACTION;
        resetPaint();
    }//GEN-LAST:event_irregularPoligonBtActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        List<Poligono> lista = panelCp.getListaPoligonos();
        paneMs.remove(panelCp);
        panelCp = new DrawablePanel(paneMs.getGraphics());
        panelCp.setSize(paneMs.getSize());
        panelCp.addAllPoligonos(lista);
        panelCp.setSelectedPolygon(selectedPolygon);
        ///panelCp.addTempoLine(aresta); //Problema ser der resize no meio da operação
        paneMs.add(panelCp);
        
        //paneMs.repaint();
        //panelCp.revalidate();
        //panelCp.repaint();
        resetPaint();
    }//GEN-LAST:event_formComponentResized

    private void regularNsidedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regularNsidedActionPerformed
        LOG.info("Clique na região para o centro do polígono e depois em outra para ser calculado o raio.");
        pendingCreating = true;
        noPointsToCreate = getNumberOfSidesFromBtSelected();
        temporaryList = new ArrayList<>();
        //regularSidedPolygon = regularNsided.isSelected();
        selectedPolygon = null;
        panelCp.setSelectedPolygon(selectedPolygon);
        currentAction = NO_ACTION;
        resetPaint();
    }//GEN-LAST:event_regularNsidedActionPerformed

    private void saveMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Escolha um caminho");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
                       
            List<Poligono> toSave = panelCp.getListaPoligonos();
            //System.out.println(toSave);
            try {
                OutputScene.outputToFile(toSave, selectedFile);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            
            LOG.info("Cena salva!");
        }
    }//GEN-LAST:event_saveMenuActionPerformed

    private void loadMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMenuActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Escolha um arquivo ." + OutputScene.FILE_EXTENSION + " para abrir");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            List<Poligono> loaded = null;
            try {
                loaded = InputScene.getListFromFile(selectedFile);
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            
            //Vertice max = VProperties.getMaxVertex(loaded);
            
            paneMs.remove(panelCp);
            panelCp = new DrawablePanel(paneMs.getGraphics());
            //panelCp.setSize(new Dimension((int) max.getX(), (int) max.getY()));
            panelCp.setSize(paneMs.getSize());
            panelCp.addAllPoligonos(loaded);
            paneMs.add(panelCp);
            resetPaint();
            
            LOG.info("Cena carregada!");
        }
    }//GEN-LAST:event_loadMenuActionPerformed

    private void formWindowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeiconified
        resetPaint();
    }//GEN-LAST:event_formWindowDeiconified

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        resetPaint();
    }//GEN-LAST:event_formFocusGained

    private void selectBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectBtActionPerformed
        LOG.info("Clique em alguma parte (Linha) dos polígonos que deseja selecionar.");
        currentAction = NO_ACTION;
    }//GEN-LAST:event_selectBtActionPerformed

    private void cancelBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtActionPerformed
        selectedPolygon = null;
        panelCp.setSelectedPolygon(null);
        resetDrawingState();
        resetPaint();
        LOG.info("Criação cancelada");
        unToggle();
        currentAction = NO_ACTION;
    }//GEN-LAST:event_cancelBtActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        panelCp.clear();
        resetDrawingState();
        resetPaint();
        LOG.info("Cena limpa");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jRadioButtonMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem2ActionPerformed
        if (javaFillRadio.isSelected()){
            panelCp.setUseJavaFill(true);
        } else {
            panelCp.setUseJavaFill(false);
        }
        repaint();
    }//GEN-LAST:event_jRadioButtonMenuItem2ActionPerformed

    private void javaFillRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javaFillRadioActionPerformed
        if (javaFillRadio.isSelected()){
            panelCp.setUseJavaFill(true);
        } else {
            panelCp.setUseJavaFill(false);
        }
        repaint();
    }//GEN-LAST:event_javaFillRadioActionPerformed

    private void deleteBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtActionPerformed
        if (selectedPolygon == null){
            LOG.info("Selecione um polígono com a ferramenta de seleção para poder excluir.");
            unToggle();
            //selectBt.doClick();
        } else {
            panelCp.remove(selectedPolygon);
            panelCp.setSelectedPolygon(null);
            repaint();
            cancelBt.doClick();
        }
    }//GEN-LAST:event_deleteBtActionPerformed

    private void translateBt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_translateBt1ActionPerformed
        if (selectedPolygon == null){
            LOG.info("Selecione um polígono com a ferramenta de seleção para poder transladar.");
            unToggle();
            //selectBt.doClick();
        } else {
            currentAction = TRANSLATE_ACTION;
            LOG.warning("Arraste um vértice para transladar.");
        }
    }//GEN-LAST:event_translateBt1ActionPerformed

    private void rotateBt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateBt1ActionPerformed
        if (selectedPolygon == null){
            LOG.info("Selecione um polígono com a ferramenta de seleção para poder rotacionar.");
            unToggle();
            //selectBt.doClick();
        } else {
            LOG.warning("Arraste um vértice para rotacionar. A rotação é baseada somente no movimento horizontal do cursor.");
            //Movimentação do ponto devido à erros de arredondamento.
            currentAction = ROTATE_ACTION;
        }
    }//GEN-LAST:event_rotateBt1ActionPerformed

    private void shearBt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shearBt1ActionPerformed
        if (selectedPolygon == null){
            LOG.info("Selecione um polígono com a ferramenta de seleção para poder realizar o cisalhamento.");
            unToggle();
            //selectBt.doClick();
        } else {
            currentAction = SHEAR_ACTION;
            LOG.warning("Arraste uma aresta para realizar o cisalhamento.");
        }
    }//GEN-LAST:event_shearBt1ActionPerformed

    private void scaleBt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scaleBt1ActionPerformed
        if (selectedPolygon == null){
            LOG.info("Selecione um polígono com a ferramenta de seleção para poder alterar a escala.");
            unToggle();
            //selectBt.doClick();
        } else {
            currentAction = SCALE_ACTION;
            LOG.warning("Arraste uma aresta para alterar a escala.");
        }
    }//GEN-LAST:event_scaleBt1ActionPerformed

    private Color currentBorda = Color.BLACK;
    private Color currentFundo = null;
    
    private void bordaColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bordaColorActionPerformed
        Color newColor = JColorChooser.showDialog(null, "Escolha uma cor", currentBorda);
        if(newColor != null){
            currentBorda = newColor;
            bordaColor.setBackground(currentBorda);
            bordaColor.setForeground(currentBorda);
            bordaColor.setText("");
        }
    }//GEN-LAST:event_bordaColorActionPerformed

    private void paintBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paintBtActionPerformed
        if (selectedPolygon == null){
            LOG.info("Selecione um polígono com a ferramenta de seleção para poder pintar.");
        } else {
            if (bordaBox.isSelected()){
                selectedPolygon.setCorBorda(currentBorda);
                if (currentBorda != null) LOG.info("Borda do polígono pintada.");
            } 
            
            if (fundoBox.isSelected()){
                selectedPolygon.setCorFundo(currentFundo);
                LOG.info("Fundo do polígono pintado.");
            }
            
            resetPaint();
        }
    }//GEN-LAST:event_paintBtActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        /*if (bordaBox.isSelected()){
            currentBorda = null;
            bordaColor.setBackground(currentBorda);
            bordaColor.setForeground(currentBorda);
            bordaColor.setText("null");
        }*/ //Borda não pode ser transparente mesmo...
        
        //if (fundoBox.isSelected()){
            currentFundo = null;
            fundoColor.setBackground(currentFundo);
            fundoColor.setForeground(currentFundo);
            fundoColor.setText("null");
        //}
        
        /*if (bordaBox.isSelected() && fundoBox.isSelected()){
            LOG.warning("Selecione um campo para colocar a cor como transparente (Fundo ou Borda)");
        }*/
    }//GEN-LAST:event_jButton1ActionPerformed

    private void fundoColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fundoColorActionPerformed
        Color newColor = JColorChooser.showDialog(null, "Escolha uma cor", currentFundo);
        if(newColor != null){
            currentFundo = newColor;
            fundoColor.setBackground(currentFundo);
            fundoColor.setForeground(currentFundo);
            fundoColor.setText("");
        }
    }//GEN-LAST:event_fundoColorActionPerformed

    private void paneMsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paneMsKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_CONTROL){
            controlFLAG=true;
            System.out.println("INN");
        }
        System.out.println("CONTROL");
    }//GEN-LAST:event_paneMsKeyPressed

    private void paneMsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paneMsKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_CONTROL){
            controlFLAG=true;
            System.out.println("OUTTTTT");
        }
        System.out.println("REEEEEEE");
    }//GEN-LAST:event_paneMsKeyReleased

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.isControlDown()){
            controlFLAG = true;
            System.out.println("CONTROL");
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        if (!evt.isControlDown()){
            controlFLAG = false;
        }
    }//GEN-LAST:event_formKeyReleased

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        
        ControlsSplash splash = new ControlsSplash();
        splash.setVisible(true);
        this.setEnabled(false);
        //splash.
        this.setEnabled(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void bordaBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bordaBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bordaBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainV.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainV().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox bordaBox;
    private javax.swing.JButton bordaColor;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JButton cancelBt;
    private javax.swing.JCheckBox centerAnchorBox;
    private javax.swing.JTextPane consolePane;
    private javax.swing.JRadioButtonMenuItem defaultColorRadio;
    private javax.swing.JButton deleteBt;
    private javax.swing.JCheckBox fundoBox;
    private javax.swing.JButton fundoColor;
    private javax.swing.JToggleButton irregularPoligonBt;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JRadioButtonMenuItem javaFillRadio;
    private javax.swing.JMenuItem loadMenu;
    private javax.swing.JButton paintBt;
    private javax.swing.JScrollPane paneMs;
    private javax.swing.JToggleButton regularNsided;
    private javax.swing.JToggleButton rotateBt1;
    private javax.swing.JMenuItem saveMenu;
    private javax.swing.JToggleButton scaleBt1;
    private javax.swing.JToggleButton selectBt;
    private javax.swing.JRadioButtonMenuItem setColorRadio;
    private javax.swing.JToggleButton shearBt1;
    private javax.swing.JToggleButton translateBt1;
    // End of variables declaration//GEN-END:variables
    
    private int getNumberOfSidesFromBtSelected(){
        if (regularNsided.isSelected()){
            regularSidedPolygon = true;
            return 2;
        } else if (irregularPoligonBt.isSelected()) {
            return MAX_LADOS;
        } else {
            return -1;
        }
    }

    private void unToggle(){
        /*irregularPoligonBt.setSelected(false);
        selectBt.setSelected(false);
        regularNsided.setSelected(false);
        rotateBt1.setSelected(false);
        translateBt1.setSelected(false);
        shearBt1.setSelected(false);
        scaleBt1.setSelected(false);*/
        ghost.doClick(); //Botão que não faz nada para atualizar a seleção de todos os botões no mesmo grupo
        currentAction = NO_ACTION;
    }
    
    private Poligono localPolygonBuilder(){
        if (defaultColorRadio.isSelected()){
            return new Poligono(temporaryList);
        } else {
            Color borda=Poligono.DEFAULT_BORDA, fundo=null;
            if (bordaBox.isSelected() && currentBorda!=null)
                borda = currentBorda;
            if (fundoBox.isSelected())
                fundo = currentFundo; 
            
            return new Poligono(temporaryList, borda, fundo);
        }
    }
    
    private Nregular localPolygonBuilder(int radius){
        //new Nregular(jSlider1.getValue(), dist, temporaryList.get(0), 0.));
        if (defaultColorRadio.isSelected()){
           return new Nregular(jSlider1.getValue(), radius, temporaryList.get(0)); 
        } else {
            Color borda=Poligono.DEFAULT_BORDA, fundo=null;
            if (bordaBox.isSelected() && currentBorda!=null)
                borda = currentBorda;
            if (fundoBox.isSelected())
                fundo = currentFundo; 
            return new Nregular(jSlider1.getValue(), radius, temporaryList.get(0), borda, fundo);
        }
    }
}