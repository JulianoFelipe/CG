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
import Model.Nregular;
import ioScene.InputScene;
import ioScene.OutputScene;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import utils.VMath;
import utils.VProperties;
/**
 *
 * @author Anderson
 */
public class MainV extends javax.swing.JFrame {
    private static final int MAX_LADOS = 20;
    private static final double DELETE_THRESHOLD = 3.9;
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
    
    //https://en.wikipedia.org/wiki/Z-order_curve // Para armazenar "texture maps"?
    private void addMouseListeners(){
        paneMs.addMouseListener(new java.awt.event.MouseAdapter() {           
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {                               
                int x = evt.getX();
                int y = evt.getY();
                
                //<editor-fold defaultstate="collapsed" desc="Delete Action">
                if(deleteBt.isSelected()){
                    List<Poligono> lista = panelCp.getListaPoligonos();
                    List<Integer> toRemove = new ArrayList();

                    Vertice point = new Vertice((float) x, (float) y);

                    for(int i=0; i<lista.size(); i++){
                        List<Vertice> vertices = lista.get(i).getVertices();
                        boolean toAdd = false;

                        innerFor: for(int j=0; j<vertices.size(); j++){
                            Vertice a = vertices.get(j), b;
                            if (j == vertices.size()-1) b=vertices.get(0);
                            else b=vertices.get(j+1);
                            
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

                    /*for (Integer i : toRemove){
                        panelCp.removePoligono(i);
                    }*/

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
                    panelCp.addPoligono(new Nregular(jSlider1.getValue(), dist, temporaryList.get(0), 0.));
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
                        panelCp.addPoligono(new Poligono(temporaryList));                    
                        resetDrawingState();
                        panelCp.cleanTempoLines();
                    }
                } else if (noPointsToCreate == 1){
                    temporaryList.add(new Vertice((float) x, (float)y));
                    panelCp.addPoligono(new Poligono(temporaryList));                    
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
                
                if (regularSidedLock){
                    Vertice radiusPnt = new Vertice((float) x, (float)y);
                    int dist = (int) VMath.distancia(temporaryList.get(0), radiusPnt);
                    panelCp.setTempRegular(jSlider1.getValue(), dist, temporaryList.get(0), 0.);
                 } else {
                    //panelCp.cleanTempoLines();
                    Vertice last = temporaryList.get(temporaryList.size()-1);
                    panelCp.setMovable(new Aresta(new Vertice((float) x, (float) y), last));
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
        LOG.info("O botão \"Cancelar\" pode servir para forçar a atualização da pintura da cena.");
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
        translateBt = new javax.swing.JToggleButton();
        rotateBt = new javax.swing.JToggleButton();
        shearBt = new javax.swing.JToggleButton();
        scaleBt = new javax.swing.JToggleButton();
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
        deleteBt = new javax.swing.JToggleButton();
        cancelBt = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        saveMenu = new javax.swing.JMenuItem();
        loadMenu = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        javaFillRadio = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Transformações"));
        jPanel2.setToolTipText("");

        buttonGroup1.add(translateBt);
        translateBt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/translacao.png"))); // NOI18N
        translateBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                translateBtActionPerformed(evt);
            }
        });

        buttonGroup1.add(rotateBt);
        rotateBt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/rotacao.png"))); // NOI18N
        rotateBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotateBtActionPerformed(evt);
            }
        });

        buttonGroup1.add(shearBt);
        shearBt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/cisalhamento.png"))); // NOI18N
        shearBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shearBtActionPerformed(evt);
            }
        });

        buttonGroup1.add(scaleBt);
        scaleBt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/escala.png"))); // NOI18N
        scaleBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scaleBtActionPerformed(evt);
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
                        .addComponent(rotateBt, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(translateBt, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(scaleBt, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(shearBt, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rotateBt)
                    .addComponent(translateBt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(shearBt)
                    .addComponent(scaleBt)))
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
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
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

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Ferramentas"));
        jPanel5.setToolTipText("");

        buttonGroup1.add(selectBt);
        selectBt.setText("Selecionar");
        selectBt.setEnabled(false);
        selectBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectBtActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteBt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selectBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectBt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteBt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelBt)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        javaFillRadio.setSelected(true);
        javaFillRadio.setText("Fill Java");
        javaFillRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javaFillRadioActionPerformed(evt);
            }
        });
        jMenu2.add(javaFillRadio);

        buttonGroup2.add(jRadioButtonMenuItem2);
        jRadioButtonMenuItem2.setText("Fill CG (Manual)");
        jRadioButtonMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jRadioButtonMenuItem2);

        jMenuBar1.add(jMenu2);

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
                        .addComponent(paneMs, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(2, 2, 2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(paneMs))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void irregularPoligonBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_irregularPoligonBtActionPerformed
        LOG.info("Clique em no máximo " + MAX_LADOS +  " e no mínimo 3 pontos para formar um polígono. O botão direito do mouse finaliza criação.");
        pendingCreating = true;
        noPointsToCreate = getNumberOfSidesFromBtSelected();
        temporaryList = new ArrayList<>();
    }//GEN-LAST:event_irregularPoligonBtActionPerformed

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
        LOG.info("Clique na região para o centro do polígono e depois em outra para ser calculado o raio.");
        pendingCreating = true;
        noPointsToCreate = getNumberOfSidesFromBtSelected();
        temporaryList = new ArrayList<>();
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

    private void translateBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_translateBtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_translateBtActionPerformed

    private void rotateBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateBtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rotateBtActionPerformed

    private void shearBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shearBtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_shearBtActionPerformed

    private void scaleBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scaleBtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scaleBtActionPerformed

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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton cancelBt;
    private javax.swing.JTextPane consolePane;
    private javax.swing.JToggleButton deleteBt;
    private javax.swing.JToggleButton irregularPoligonBt;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JRadioButtonMenuItem javaFillRadio;
    private javax.swing.JMenuItem loadMenu;
    private javax.swing.JScrollPane paneMs;
    private javax.swing.JToggleButton regularNsided;
    private javax.swing.JToggleButton rotateBt;
    private javax.swing.JMenuItem saveMenu;
    private javax.swing.JToggleButton scaleBt;
    private javax.swing.JToggleButton selectBt;
    private javax.swing.JToggleButton shearBt;
    private javax.swing.JToggleButton translateBt;
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
        irregularPoligonBt.setSelected(false);
    }
}