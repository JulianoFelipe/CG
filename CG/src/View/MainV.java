/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Logging.BufferedPaneOutputStream;
import Logging.PaneHandler;
import Model.Aresta;
import Model.Poligono;
import Model.Vertice;
import Model.poligonosEsp.Circunferencia;
import Model.poligonosEsp.Nregular;
import Model.poligonosEsp.QuadrilateroRegular;
import Model.poligonosEsp.Triangulo;
import ioScene.InputScene;
import ioScene.OutputScene;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import utils.VMath;
import utils.VProperties;
/**
 *
 * @author Anderson
 */
public class MainV extends javax.swing.JFrame {
    private static final double DELETE_THRESHOLD = 3.9;
    private static final int SIDE_THRESHOLD = 30;
    private static final int CIRCUMFERENCE_CODE = Integer.MAX_VALUE;
    private static final int CIRCUMFERENCE_RADIUS_CODE = Integer.MAX_VALUE-1;
    private static final Logger LOG = Logger.getLogger("CG");
    private DrawablePanel panelCp;

    private int noPointsToCreate = -2;
    private boolean pendingCreating = false;
    private boolean regularSidedPolygon = false;
    private boolean regularSidedLock = false;
    
    private List<Vertice> temporaryList = new ArrayList();  
    
    private void resetPaint(){
        panelCp.nullTemps();
        panelCp.repaint();
    }
    
    private void resetDrawingState(){
        regularSidedLock = false;
        pendingCreating = false;
        noPointsToCreate = -1;
        unToggle();
        temporaryList = new ArrayList<>();
    }
    
    private void addMouseListeners(){
        paneMs.addMouseListener(new java.awt.event.MouseAdapter() {           
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {               
                int x = evt.getX();
                int y = evt.getY();
                
                if(deleteBt.isSelected()){
                    List<Poligono> lista = panelCp.getListaPoligonos();
                    List<Integer> toRemove = new ArrayList();
                    
                    Vertice point = new Vertice((float) x, (float) y);
                    //System.out.println(point);
                    
                    for(int i=0; i<lista.size(); i++){
                        List<Vertice> vertices = lista.get(i).getVertices();
                        boolean toAdd = false;
                        //System.out.println("INNER: " + vertices.size());
                        innerFor: for(int j=0; j<vertices.size()-1; j++){
                            Vertice a = vertices.get(j);
                            Vertice b = vertices.get(j+1);
                            
                            //System.out.println(VMath.shortestDistance(a, b, point));
                            if (VMath.shortestDistance(a, b, point) < DELETE_THRESHOLD){
                                toAdd = true;
                                break innerFor;
                            }
                        }
                        
                        if (toAdd){
                            LOG.info("Removido o polígono.");
                            toRemove.add(i);
                        }
                    }
                    
                    for (Integer i : toRemove){
                        panelCp.removePoligono(i);
                    }
                    
                    return;
                }
                
                if(!pendingCreating){
                    int noTyped = getNumberOfSidesFromBtSelected();
                    if (noTyped == -1){
                        LOG.warning("Selecione um tipo de polígono para desenhar.");
                        return;
                    } else if (noTyped > SIDE_THRESHOLD && noTyped != CIRCUMFERENCE_CODE) {
                        LOG.info("Restrição estabelecida de lados é: " + SIDE_THRESHOLD + ".");
                        return; 
                    } else {
                        pendingCreating = true;
                        noPointsToCreate = noTyped;
                        temporaryList = new ArrayList<>();
                    }
                }

                
                if (regularSidedLock){   
                    Vertice radiusPnt = new Vertice((float) x, (float)y);
                    double pos = (x - temporaryList.get(0).getX())/100;
                    int dist = (int) VMath.distancia(temporaryList.get(0), radiusPnt);
                    panelCp.addPoligono(new Nregular(++noPointsToCreate, dist, temporaryList.get(0), pos));
                    panelCp.cleanTempRegular();
                    
                    resetDrawingState();
                    panelCp.repaint();
                    return ;
                }
                
                if (noPointsToCreate == CIRCUMFERENCE_RADIUS_CODE){
                    Vertice radiusPnt = new Vertice((float) x, (float)y);
                    double dist = VMath.distancia(temporaryList.get(0), radiusPnt);
                    panelCp.addPoligono(new Circunferencia(temporaryList.get(0), (int)Math.round(dist)));
                   
                    resetDrawingState();
                    resetPaint();
                } else if (noPointsToCreate == 1){
                    temporaryList.add(new Vertice((float) x, (float)y));
                    switch (temporaryList.size()) {
                        case 2:
                            panelCp.addPoligono(new QuadrilateroRegular(temporaryList.get(0), temporaryList.get(1)));
                            break;
                        case 3:
                            panelCp.addPoligono(new Triangulo(temporaryList.get(0), temporaryList.get(1), temporaryList.get(2)));
                            break;
                        default:
                            panelCp.addPoligono(new Poligono(temporaryList));
                            break;
                    }
                    
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
        });
        
        paneMs.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (temporaryList.size() < 1) return;
                if (!pendingCreating) return;
                //paneMs.repaint();
                
                int x = e.getX();
                int y = e.getY();
                
                if (noPointsToCreate == CIRCUMFERENCE_RADIUS_CODE){             
                    Vertice radiusPnt = new Vertice((float) x, (float)y);
                    int radius = (int) VMath.distancia(temporaryList.get(0), radiusPnt);
                    panelCp.setTempCirc(temporaryList.get(0), radius);
                    //panelCp.removeAll();
                } else {
                    if (regularSidedLock){
                        Vertice radiusPnt = new Vertice((float) x, (float)y);
                        double pos = (x - temporaryList.get(0).getX())/100;
                        int dist = (int) VMath.distancia(temporaryList.get(0), radiusPnt);
                        panelCp.setTempRegular(noPointsToCreate+1, dist, temporaryList.get(0), pos);
                    } else {
                        //panelCp.cleanTempoLines();
                        Vertice last = temporaryList.get(temporaryList.size()-1);
                        panelCp.setMovable(new Aresta(new Vertice((float) x, (float) y), last));
                    }
                } 
                panelCp.repaint();
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
        jPanel2 = new javax.swing.JPanel();
        selectBt = new javax.swing.JToggleButton();
        deleteBt = new javax.swing.JToggleButton();
        cancelBt = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtfLadosPrisma = new javax.swing.JTextField();
        quadrilateroBt = new javax.swing.JToggleButton();
        circBt = new javax.swing.JToggleButton();
        trianguloBt = new javax.swing.JToggleButton();
        irregularPoligonBt = new javax.swing.JToggleButton();
        regularNsided = new javax.swing.JToggleButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        consolePane = new javax.swing.JTextPane();
        paneMs = new javax.swing.JScrollPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        saveMenu = new javax.swing.JMenuItem();
        loadMenu = new javax.swing.JMenuItem();

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Ferramentas"));
        jPanel2.setToolTipText("");

        buttonGroup1.add(selectBt);
        selectBt.setText("Selecionar");
        selectBt.setEnabled(false);
        selectBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectBtActionPerformed(evt);
            }
        });

        buttonGroup1.add(deleteBt);
        deleteBt.setText("Excluir");
        deleteBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtActionPerformed(evt);
            }
        });

        cancelBt.setText("Cancelar");
        cancelBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteBt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selectBt, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addComponent(cancelBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectBt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteBt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelBt)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Objetos"));

        jLabel2.setText("Quantidade de lados");

        jtfLadosPrisma.setText("5");
        jtfLadosPrisma.setToolTipText("Quantidade de lados para um polígono irregular");

        buttonGroup1.add(quadrilateroBt);
        quadrilateroBt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Quadrado.jpg"))); // NOI18N
        quadrilateroBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quadrilateroBtActionPerformed(evt);
            }
        });

        buttonGroup1.add(circBt);
        circBt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Circ.jpg"))); // NOI18N
        circBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                circBtActionPerformed(evt);
            }
        });

        buttonGroup1.add(trianguloBt);
        trianguloBt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Triangulo.jpg"))); // NOI18N
        trianguloBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trianguloBtActionPerformed(evt);
            }
        });

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

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.black, null, null));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(trianguloBt, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(quadrilateroBt, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(circBt, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(irregularPoligonBt, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(regularNsided, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtfLadosPrisma, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(quadrilateroBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(trianguloBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(circBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(7, 7, 7)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(irregularPoligonBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(regularNsided, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfLadosPrisma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
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

        jMenu1.setText("File");

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

        jMenuBar1.add(jMenu1);

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
                        .addComponent(paneMs, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(paneMs))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void circBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_circBtActionPerformed
        LOG.info("Clique na região para o centro da circunferência e depois em outra para ser calculado o raio.");
        pendingCreating = true;
        noPointsToCreate = CIRCUMFERENCE_CODE;
        temporaryList = new ArrayList<>();
    }//GEN-LAST:event_circBtActionPerformed

    private void trianguloBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trianguloBtActionPerformed
        LOG.info("Clique em três pontos para formar um triângulo.");
        pendingCreating = true;
        noPointsToCreate = 3;
        temporaryList = new ArrayList<>();
    }//GEN-LAST:event_trianguloBtActionPerformed

    private void quadrilateroBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quadrilateroBtActionPerformed
        LOG.info("Clique em dois pontos para formar um quadrilátero regular (Cantos).");
        pendingCreating = true;
        noPointsToCreate = 2;
        temporaryList = new ArrayList<>();
    }//GEN-LAST:event_quadrilateroBtActionPerformed

    private void irregularPoligonBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_irregularPoligonBtActionPerformed
        int noPointsTyped = getNumberOfSidesFromBtSelected();
        if (noPointsTyped <= 1){
            LOG.info("Só é possível criar polígonos com mais que 1 lado.");
        } else if (noPointsTyped > SIDE_THRESHOLD) {
            LOG.info("Restrição estabelecida de lados é: " + SIDE_THRESHOLD + ".");
        } else {
            LOG.info("Clique em " + noPointsTyped +  " pontos para formar um polígono.");
            pendingCreating = true;
            noPointsToCreate = noPointsTyped;
            temporaryList = new ArrayList<>();
        }
    }//GEN-LAST:event_irregularPoligonBtActionPerformed

    private void selectBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectBtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_selectBtActionPerformed

    private void deleteBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtActionPerformed
        LOG.info("Clique em alguma parte (Linha) dos polígonos que deseja excluir.");
    }//GEN-LAST:event_deleteBtActionPerformed

    private void cancelBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtActionPerformed
        resetDrawingState();
        resetPaint();
        LOG.info("Criação cancelada");
    }//GEN-LAST:event_cancelBtActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        List<Poligono> lista = panelCp.getListaPoligonos();
        paneMs.remove(panelCp);
        panelCp = new DrawablePanel(paneMs.getGraphics());
        panelCp.setSize(paneMs.getSize());
        panelCp.addAllPoligonos(lista);
        paneMs.add(panelCp);
        
        //paneMs.repaint();
        //panelCp.revalidate();
        //panelCp.repaint();
        resetPaint();
    }//GEN-LAST:event_formComponentResized

    private void regularNsidedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regularNsidedActionPerformed
        int noPointsTyped = getNumberOfSidesFromBtSelected();
        if (noPointsTyped <= 1){
            LOG.info("Só é possível criar polígonos com mais que 1 lado.");
        } else if (noPointsTyped > SIDE_THRESHOLD) {
            LOG.info("Restrição estabelecida de lados é: " + SIDE_THRESHOLD + ".");
        } else {
            LOG.info("Clique na região para o centro do polígono e depois em outra para ser calculado o raio.");
            pendingCreating = true;
            noPointsToCreate = noPointsTyped;
            temporaryList = new ArrayList<>();
        }
        //regularSidedPolygon = regularNsided.isSelected();
    }//GEN-LAST:event_regularNsidedActionPerformed

    private void saveMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Escolha um diretório para salvar");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            List<Poligono> toSave = panelCp.getListaPoligonos();
            System.out.println(toSave);
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
            
            Vertice max = VProperties.getMaxVertex(loaded);
            
            paneMs.remove(panelCp);
            panelCp = new DrawablePanel(paneMs.getGraphics());
            panelCp.setSize(new Dimension((int) max.getX(), (int) max.getY()));
            panelCp.addAllPoligonos(loaded);
            paneMs.add(panelCp);
            panelCp.revalidate();
            panelCp.repaint();
            
            LOG.info("Cena carregada!");
        }
    }//GEN-LAST:event_loadMenuActionPerformed

    private void formWindowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeiconified
        panelCp.repaint(0);
    }//GEN-LAST:event_formWindowDeiconified

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        resetPaint();
    }//GEN-LAST:event_formFocusGained

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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainV().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton cancelBt;
    private javax.swing.JToggleButton circBt;
    private javax.swing.JTextPane consolePane;
    private javax.swing.JToggleButton deleteBt;
    private javax.swing.JToggleButton irregularPoligonBt;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jtfLadosPrisma;
    private javax.swing.JMenuItem loadMenu;
    private javax.swing.JScrollPane paneMs;
    private javax.swing.JToggleButton quadrilateroBt;
    private javax.swing.JToggleButton regularNsided;
    private javax.swing.JMenuItem saveMenu;
    private javax.swing.JToggleButton selectBt;
    private javax.swing.JToggleButton trianguloBt;
    // End of variables declaration//GEN-END:variables
    
    private int getNumberOfSidesFromBtSelected(){
        if (trianguloBt.isSelected()){
            return 3;
        } else if (quadrilateroBt.isSelected()) {
            return 2;
        } else if (irregularPoligonBt.isSelected() || regularNsided.isSelected()){
            regularSidedPolygon = regularNsided.isSelected();
            return Integer.parseInt( jtfLadosPrisma.getText() );
        } else if (circBt.isSelected()){
            return CIRCUMFERENCE_CODE;
        } else {
            return -1;
        }
    }

    private void unToggle(){
        trianguloBt.setSelected(false);
        quadrilateroBt.setSelected(false);
        irregularPoligonBt.setSelected(false);
        circBt.setSelected(false);
    }
}

/*private void vFrenteMouseReleased(java.awt.event.MouseEvent evt) {                                      
        vi = null;
    }                                     

    private void vFrenteMouseClicked(java.awt.event.MouseEvent evt) {                                     
        /*if (poligonoSelecinado != null) { // caso haja um poligono selecionado então tira o seu destaque.
        CPintura.PintarBordasPoligono(poligonoSelecinado);
        }
        // pegando ponto do clique
        Vertice v = new Vertice();
        v.setX(evt.getPoint().x);
        v.setY(evt.getPoint().y);
        // pega o poligono q foi selecionado
        poligonoSelecinado = CFuncoes.getPoligonoSelecionadoVistaFrontal(v);
        // Destaca o poligono que foi selecionado
        if (poligonoSelecinado != null) {
        CPintura.PintarBordasPoligono(poligonoSelecinado);
        }*/
        /*Point point = evt.getPoint();
        panelCp.addPoligono(new QuadrilateroRegular(new Vertice((float)point.x, (float)point.x), new Vertice((float)point.x+1, (float)point.x+1)));
        vFrente = panelCp;
        vFrente.repaint();
        System.out.println("ÇLKAJFÇL");
    }                                    

    private void vFrenteMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {                                        
        
    }                                       

    private void vFrenteMouseDragged(java.awt.event.MouseEvent evt) {                                     
        // Aperta e Arrasta
        if (selectBt.isSelected() && poligonoSelecinado != null) {
            
        }
    }*/
