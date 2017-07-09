/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.visualizacao;

import Model.Vertice;
import utils.MMath;
import utils.VMath;

/**
 *
 * @author Juliano
 */
public class Camera {   
    private static final int MAXINSTANCES=1;
    private static Camera SINGLETON;
    private static long INSTANCES;
    private final long id;

    private final Viewport viewport;
    private final Window window;
    private Vertice vrp;
    private final Projecao proj;
    private final Vertice p;
    private Vertice y;
    
    private final float d;
    private final float far;
    private final float near;
    private final float zmin;
    
    public Camera() {
        if (INSTANCES > MAXINSTANCES) 
            throw new IllegalStateException("Limite de instâncias de "
                    +  this.getClass() + " excedido.");
        else{
            id = INSTANCES;
            INSTANCES++;
            
            viewport = Default.VIEWPORT;
            window = Default.WINDOW;
            vrp = Default.VRP;
            p = Default.direcaoCamera;
            y = Default.Y;
            proj = Default.projecao;
            d = Default.DIST_PLANO_PROJECAO;
            far = Default.FAR_PLANE;
            near = Default.NEAR_PLANE;
            zmin = near / far;
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters">
    public static Camera getCamera (){
        if (SINGLETON == null){
            return new Camera();
        } else {
            return SINGLETON;
        }
    }
    
    public long getId() {
        return id;
    }
    
    public Viewport getViewport() {
        return viewport;
    }
    
    public Window getWindow() {
        return window;
    }
    
    public Vertice getVrp() {
        return vrp;
    }
    
    public Projecao getProjecao() {
        return proj;
    }
//</editor-fold>
    
    public Vertice getVetorNormal() {
        float nx = p.getX() - vrp.getX();
        float ny = p.getY() - vrp.getY();
        float nz = p.getZ() - vrp.getZ();

        Vertice vetorN = new Vertice(nx, ny, nz);
        VMath.normalizar(vetorN);
        return vetorN;
    }
    
    public void init(){
        projecaoPerspectiva();
        projecaoOrtograficaLateral();
        projecaoOrtograficaTopo();
        projecaoOrtograficaFrontal();
    }
    
    public void projecaoPerspectiva(){
        Vertice normal = this.getVetorNormal();
        
        //v = y -(y * normal)* n
        float vx = y.getX() - (y.getX() * normal.getX() + y.getY() * normal.getY() + y.getZ() * normal.getZ()) * normal.getX();
        float vy = y.getY() - (y.getY() * normal.getX() + y.getY() * normal.getY() + y.getZ() * normal.getZ()) * normal.getY();
        float vz = y.getZ() - (y.getZ() * normal.getX() + y.getY() * normal.getY() + y.getZ() * normal.getZ()) * normal.getZ();

        proj.setV( new Vertice(vx, vy, vz) );
        VMath.normalizar(proj.getV());
        proj.setU( VMath.produtoVetorial(normal, proj.getV()) );
        VMath.normalizar( proj.getU() );

        float mCamera[][] = matrizTransformacaoCamera(normal);
        proj.setmCamera(mCamera);
        float mBasePerspectiva[][] = matrizTransformacaoBasePerspectiva();
        float mRecorte[][] = matrizTransformacaoRecorte();
        float mDispositivo[][] = matrizTransformacaoDispositivo();
        
        proj.setmPers( MMath.multiplicar(mDispositivo,
                MMath.multiplicar(mBasePerspectiva,
                MMath.multiplicar(mRecorte, mCamera)))
        );
    }
    
    public void projecaoOrtograficaFrontal(){
        Vertice vrpAnterior = new Vertice(vrp);
        vrp=new Vertice(0, 0, d);
        Vertice normal = this.getVetorNormal();

        //v = y -(y * normal)* n
        float vx = y.getX() - (y.getX() * normal.getX() + y.getY() * normal.getY() + y.getZ() * normal.getZ()) * normal.getX();
        float vy = y.getY() - (y.getY() * normal.getX() + y.getY() * normal.getY() + y.getZ() * normal.getZ()) * normal.getY();
        float vz = y.getZ() - (y.getZ() * normal.getX() + y.getY() * normal.getY() + y.getZ() * normal.getZ()) * normal.getZ();

        Vertice v = new Vertice(vx, vy, vz);
        VMath.normalizar(v);
        proj.setV(v);

        Vertice u = VMath.produtoVetorial(normal, v);
        VMath.normalizar(u);
        proj.setU(u);

        float mCamera[][] = matrizTransformacaoCamera(normal);
        proj.setmCamera(mCamera);
        float mRecorte[][] = matrizTransformacaoRecorte();
        float mDispositivo[][] = matrizTransformacaoDispositivo();
        float mBaseOrtografica[][] = matrizTransformacaoBaseOrtografica();
        
        proj.setmOrtograficaVistaFrontal( 
                MMath.multiplicar(mDispositivo,
                MMath.multiplicar(mBaseOrtografica, mCamera))
        );
        
        vrp = vrpAnterior;
    }
    
    public void projecaoOrtograficaLateral(){
        Vertice vrpAnterior = new Vertice(vrp);
        vrp=new Vertice(d, 0, 0);
        
        Vertice normal = this.getVetorNormal();

        //v = y -(y * normal)* n
        float vx = y.getX() - (y.getX() * normal.getX() + y.getY() * normal.getY() + y.getZ() * normal.getZ()) * normal.getX();
        float vy = y.getY() - (y.getY() * normal.getX() + y.getY() * normal.getY() + y.getZ() * normal.getZ()) * normal.getY();
        float vz = y.getZ() - (y.getZ() * normal.getX() + y.getY() * normal.getY() + y.getZ() * normal.getZ()) * normal.getZ();

        Vertice v = new Vertice(vx, vy, vz);
        VMath.normalizar(v);
        proj.setV(v);

        Vertice u = VMath.produtoVetorial(normal, v);
        VMath.normalizar(u);
        proj.setU(u);


        float mCamera[][] = matrizTransformacaoCamera(normal);
        proj.setmCamera(mCamera);
        float mBasePerspectiva[][] = matrizTransformacaoBasePerspectiva();
        float mRecorte[][] = matrizTransformacaoRecorte();
        float mDispositivo[][] = matrizTransformacaoDispositivo();
        float mBaseOrtografica[][] = matrizTransformacaoBaseOrtografica();
        
        proj.setmOrtograficaVistaLateral(
                MMath.multiplicar(mDispositivo,
                MMath.multiplicar(mBaseOrtografica, mCamera))
        );
        
        vrp = vrpAnterior;
    }
    
    public void projecaoOrtograficaTopo(){
        Vertice vrpAnterior = new Vertice(vrp);
        Vertice yAnterior = new Vertice(y);
        vrp=new Vertice(0, d, 0);
        y = new Vertice(0, 0, -1);
        
        Vertice normal = this.getVetorNormal();

        //v = y -(y * normal)* n
        float vx = y.getX() - (y.getX() * normal.getX() + y.getY() * normal.getY() + y.getZ() * normal.getZ()) * normal.getX();
        float vy = y.getY() - (y.getY() * normal.getX() + y.getY() * normal.getY() + y.getZ() * normal.getZ()) * normal.getY();
        float vz = y.getZ() - (y.getZ() * normal.getX() + y.getY() * normal.getY() + y.getZ() * normal.getZ()) * normal.getZ();

        Vertice v = new Vertice(vx, vy, vz);
        VMath.normalizar(v);
        proj.setV(v);

        Vertice u = VMath.produtoVetorial(normal, v);
        VMath.normalizar(u);
        proj.setU(u);

        float mCamera[][] = matrizTransformacaoCamera(normal);
        proj.setmCamera(mCamera);
        float mBasePerspectiva[][] = matrizTransformacaoBasePerspectiva();
        float mRecorte[][] = matrizTransformacaoRecorte();
        float mDispositivo[][] = matrizTransformacaoDispositivo();
        float mBaseOrtografica[][] = matrizTransformacaoBaseOrtografica();
        
        proj.setmOrtograficaVistaTopo(
                MMath.multiplicar(mDispositivo,
                MMath.multiplicar(mBaseOrtografica, mCamera))
        );

        vrp = vrpAnterior;
        y=yAnterior;
    }
    
    public float[][] matrizTransformacaoCamera(Vertice normal) {
        Vertice u = proj.getU();
        Vertice v = proj.getV();
        
        float mR[][] = {
            { u.getX(),      u.getY(),      u.getZ(),       0},
            { v.getX(),      v.getY(),      v.getZ(),       0},
            { normal.getX(), normal.getY(), normal.getZ(),  0},
            { 0,             0,             0,              1}
        };

        float mT[][] = {
            { 1, 0, 0, -vrp.getX()},
            { 0, 1, 0, -vrp.getY()},
            { 0, 0, 1, -vrp.getZ()},
            { 0, 0, 0, 1}
        };

        return MMath.multiplicar(mR, mT);
    }

    public float[][] matrizTransformacaoRecorte() {
        float cu = window.getCu();
        float cv = window.getCv();      
        float mD[][] = {
            { 1, 0, -cu/d, 0},
            { 0, 1, -cv/d, 0},
            { 0, 0, 1, 0},
            { 0, 0, 0, 1}
        };

        float su = window.getSu();
        float sv = window.getSv();
        float mE[][] = {
            { d/(su*far), 0,          0,     0},
            { 0,          d/(sv*far), 0,     0},
            { 0,          0,          1/far, 0},
            { 0,          0,          0,     1}
        };

        return MMath.multiplicar(mE, mD);
    }

    public float[][] matrizTransformacaoBasePerspectiva() {
        float mF[][] = {
            { 1, 0, 0, 0},
            { 0, 1, 0, 0},
            { 0, 0, 1, -zmin},
            { 0, 0, 0, 1}
        };

        float mG[][] = {
            { 1, 0, 0,          0},
            { 0, 1, 0,          0},
            { 0, 0, 1/(1-zmin), 0},
            { 0, 0, 0,          1}
        };

        float mH[][] = {
            { 1, 0, 0,             0},
            { 0, 1, 0,             0},
            { 0, 0, 1,             0},
            { 0, 0, (1-zmin)/zmin, 1}
        };

        float mI[][] = MMath.multiplicar(mH, MMath.multiplicar(mG, mF));

        float mJ[][] = new float[4][4];

        float escalar = 1 / zmin;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i < 3) {
                    mJ[i][j] = mI[i][j] * escalar;
                } else {
                    mJ[i][j] = mI[i][j];
                }
            }
        }
        
        return MMath.multiplicar(mJ, zmin);
    }

    public float[][] matrizTransformacaoDispositivo() {
        float mK[][] = {
            { 0.5f, 0,    0, 0.5f},
            { 0,    0.5f, 0, 0.5f},
            { 0,    0,    1, 0},
            { 0,    0,    0, 1}
        };

        float dx = viewport.getDx();
        float dy = viewport.getDy();
        float dz = viewport.getDz();
        float Xmin = viewport.getXmin();
        float Ymin = viewport.getYmin();
        
        float mL[][] = {
            { dx, 0,  0,  Xmin},
            { 0,  dy, 0,  Ymin},
            { 0,  0,  dz, near},
            { 0,  0,  0,  1}
        };

        float mM[][] = {
            { 1, 0, 0, 0.5f},
            { 0, 1, 0, 0.5f},
            { 0, 0, 1, 0.5f},
            { 0, 0, 0, 1}
        };

        return MMath.multiplicar(mM, MMath.multiplicar(mL, mK));
    }

    public float[][] matrizTransformacaoBaseOrtografica() {	
        float mFo[][] = {
            { 1, 0, 0, 0},
            { 0, 1, 0, 0},
            { 0, 0, 1, -near},
            { 0, 0, 0, 1}
        };

        float su = window.getSu();
        float sv = window.getSv();
        float mGo[][] = {
            { 1/su, 0,    0,            0},
            { 0,    1/sv, 0,            0},
            { 0,    0,    1/(far-near), 0},
            { 0,    0,    0,            1}
        };
        
        return MMath.multiplicar(mGo, mFo);

    }
}
