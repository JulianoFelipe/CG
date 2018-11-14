/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader;

import View.Fatores;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public abstract class CGShader implements Shader{

    protected final Vertice observador;
    protected final AmbientLight luzAmbiente;
    protected final List<PointLight> luzesPontuais;

    protected CGShader(Vertice observador, AmbientLight luzAmbiente) {
        this.observador = observador;
        this.luzAmbiente = luzAmbiente;
        this.luzesPontuais = new ArrayList();
    }
    
    protected CGShader(Vertice observador, AmbientLight luzAmbiente, List<PointLight> luzesPontuais) {
        this.observador = observador;
        this.luzAmbiente = luzAmbiente;
        this.luzesPontuais = luzesPontuais;
    }

    public void setAmbientLight(AmbientLight light){
        luzAmbiente.update(light);
    }
    
    public void updatePointLight(int index, PointLight light){
        luzesPontuais.get(index).update(light);
    }

    @Override
    public void paintTemporaryPoints(List<? extends Vertice> tempPoints, GraphicsContext graphs){
        //////////////////////////////////////////// TEMPS
        graphs.setFill(Color.BLACK);
        graphs.setStroke(Color.BLACK);
        graphs.setLineWidth(1);

        if (!(tempPoints.isEmpty())) {
            int radius=Fatores.POINT_RADIUS;
            graphs.beginPath();
            Vertice point1 = tempPoints.get(0);
            graphs.fillOval(point1.getX()-(radius/2), point1.getY()-(radius/2), radius, radius);
            
            Vertice point2 = null;
            for (int i=1; i<tempPoints.size(); i++){
                point2 = tempPoints.get(i);
                graphs.fillOval(point2.getX()-(radius/2), point2.getY()-(radius/2), radius, radius);
                graphs.strokeLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
                point1 = point2;
            }
            //graphs.strokeLine(point1.getX(), point1.getY(), vertices.get(0).getX(), vertices.get(0).getY());
            graphs.closePath();
        }
    }
        
    public void paintPoints(List<? extends Vertice> tempPoints, GraphicsContext graphs){
        if (!(tempPoints.isEmpty())) {
            int radius=Fatores.POINT_RADIUS;
            graphs.beginPath();
            Vertice point1;
           
            for (int i=0; i<tempPoints.size(); i++){
                point1 = tempPoints.get(i);
                graphs.fillOval(point1.getX()-(radius/2), point1.getY()-(radius/2), radius, radius);
            }
            //graphs.strokeLine(point1.getX(), point1.getY(), vertices.get(0).getX(), vertices.get(0).getY());
            graphs.closePath();
        }
    }
}
