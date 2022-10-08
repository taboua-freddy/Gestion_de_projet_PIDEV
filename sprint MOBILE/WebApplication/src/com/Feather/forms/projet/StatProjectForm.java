/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.forms.projet;

import com.Feather.forms.main.BaseForm;
import com.Feather.models.projet.Projet;
import com.Feather.services.projet.ProjectService;
import com.Feather.utils.Statics;
import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PieChart;
import com.codename1.components.ImageViewer;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.Resources;
import java.util.ArrayList;

/**
 *
 * @author Aymen
 */
class StatProjectForm extends BaseForm {

    Form current;
    private int id;
    int notstarted = 0;
    int inprogress = 0;
    int finished = 0;
    int onhold = 0;

    public Form getCurrent() {
        return current;
    }

    public void setCurrent(Form curren) {
        this.current = current;
    }
    private String pathAffiche = "";
    private ImageViewer l = new ImageViewer();
    private Projet projet = null;
    private String urlImage = Statics.BASE_URL + "/uploads/images/events/";

    public StatProjectForm(ProjectForm parent, Resources theme, Projet ev) {
        super(theme, "Project Statistics", new BorderLayout());

        ArrayList<Projet> listProjets = new ProjectService().getAllProjets();

        for (Projet projet : listProjets) {
            switch (projet.getStatus()) {
                case NOTSTARTED:
                    notstarted++;
                    break;
                case INPROGRESS:
                    inprogress++;
                    break;
                case FINISHED:
                    finished++;
                    break;
                default:
                    onhold++;
                    break;

            }
        }
        double[] values = new double[]{notstarted, inprogress, finished, onhold};

        int[] colors = new int[]{ColorUtil.GREEN, ColorUtil.BLUE, ColorUtil.MAGENTA, ColorUtil.BLACK};
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setZoomButtonsVisible(true);
        renderer.setZoomEnabled(true);
        renderer.setChartTitleTextSize(50);
        renderer.setDisplayValues(true);
        
        renderer.setShowLabels(true);
        SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
        r.setGradientEnabled(true);
        r.setGradientStart(0, ColorUtil.GREEN);
        r.setGradientStop(0, ColorUtil.GREEN);
        r.setHighlighted(true);

        PieChart chart = new PieChart(buildCategoryDataset("Status des Projets", values), renderer);

        ChartComponent c = new ChartComponent(chart);

        setTitle("Statistiques des Projets");
        setLayout(new BorderLayout());
        addComponent(BorderLayout.CENTER, c);
    
        this.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, (evt) -> {
            parent.showBack();
        });
        

    
                }

    private DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(50);
        renderer.setLegendTextSize(50);
        renderer.setMargins(new int[]{20, 30, 15, 0});
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    /**
     * Builds a category series using the provided values.
     *
     * @param titles the series titles
     * @param values the values
     * @return the category series
     */
    protected CategorySeries buildCategoryDataset(String title, double[] values) {
        CategorySeries series = new CategorySeries(title);
        int k = 0;
        series.add("Not Started " + "", values[0]);
        series.add("In Progreess " + "", values[1]);
        series.add("Finished " + "", values[2]);
        series.add("On Hold " + "", values[3]);
        return series;
    }

}
    