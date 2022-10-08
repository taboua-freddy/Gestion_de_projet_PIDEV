/*
 * Copyright (c) 2016, Codename One
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions 
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */

package com.Feather.forms.sprint;

import com.Feather.forms.main.BaseForm;
import com.Feather.services.sprint.StoryService;
import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PieChart;
import com.codename1.components.InfiniteProgress;
import com.codename1.messaging.Message;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;


/**
 * The newsfeed form
 *
 * @author Shai Almog
 */
public class StatStory extends BaseForm {
  
    StoryService ss = new StoryService();
    private Resources resource;
    Container ct = new Container();
  private DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(40);
        renderer.setLegendTextSize(40);
        //renderer.setMargins(new int[]{20, 30, 15, 0});
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
    protected CategorySeries buildCategoryDataset() {
        CategorySeries series = new CategorySeries("Stataistique des Stories");
        int doing = ss.getCount("doing");;
        int done = ss.getCount("done");;
        int notStarted= ss.getCount("not started");;

        series.add("not started", notStarted);
        series.add("doing", doing);
         series.add("done", done);
        return series;
    }
    
    public StatStory(Resources res) {
        super(res,"Story Statistiques", BoxLayout.y());
        Dialog ip = new InfiniteProgress().showInifiniteBlocking();
        resource = res;
      int[] colors = new int[]{ColorUtil.BLUE, ColorUtil.GREEN, ColorUtil.MAGENTA, ColorUtil.YELLOW, ColorUtil.CYAN};
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setZoomButtonsVisible(true);
        renderer.setZoomEnabled(true);
        renderer.setChartTitleTextSize(5);
        renderer.setLabelsColor(ColorUtil.BLACK);
        renderer.setDisplayValues(true);
        renderer.setShowLabels(true);
        SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
        
        r.setHighlighted(true);
            
        // Create the chart ... pass the values and renderer to the chart object.
       PieChart chart = new PieChart(buildCategoryDataset(), renderer);

        // Wrap the chart in a Component so we can add it to a form
        ChartComponent c = new ChartComponent(chart);
        Button contact = new Button("Send mail");
        
      
        
        add(c);
        contact.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) 
            {
                Message mes = new Message("Enter message");
                Display.getInstance().sendMessage(new String[] {"Sprintmanagmentpidev@gmail.com"}, "Enter Subject", mes);
            }
        });
        Container content = BoxLayout.encloseY(
                contact
        );
        add(content);
       
   }
     
  
}
