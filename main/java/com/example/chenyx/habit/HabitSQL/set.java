package com.example.chenyx.habit.HabitSQL;

import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class set {

    private LineChartView lineChart;
    private List<SignData> signData_list;

    public set(LineChartView lineChart,List<SignData> signData_list){
        this.lineChart=lineChart;
        this.signData_list=signData_list;

    }

    ArrayList<Integer> data=new ArrayList<>();

    ArrayList<Integer> year=new ArrayList<>();
    ArrayList<Integer> month=new ArrayList<>();
    ArrayList<Integer> day=new ArrayList<>();
    int  calendars[][][]=new int[2][12][31];
    int  strength [][][]=new int[2][12][31];
    String [][][]Xline=new String[2][12][31];
    String [] Y={"5%","10%","20%","30%","40%","50%","60%","70%","80%","90%","100%"};
    int Y2[]={5,10,20,30,40,50,60,70,80,90,100};


    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    public void start()        //设置折线图
    {

        year.clear();
        month.clear();
        day.clear();
        mPointValues.clear();
        mAxisXValues.clear();

        readdata();
        setPoint();
        setXline();
        getAxisXLables();
        getAxisPoints();
        initLineChart();
    }


    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#009777"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平滑
        line.setStrokeWidth(3);//线条的粗细，默认是3
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(false);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data1 = new LineChartData();
        data1.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.RED);  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#D6D6D9"));//灰色

        //axisX.setName("");  //表格名称
        axisX.setTextSize(11);//设置字体大小
        //axisX.setMaxLabelChars(729); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data1.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(false); //x 轴分割线
        //axisX.setMaxLabelChars();



        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        axisY.setHasLines(true);
        //axisY.setMaxLabelChars(10);

        //axisY.setMaxLabelChars(20);//max label length, for example 60

        List<AxisValue> values = new ArrayList<>();

        for(int i = 0; i < Y.length; i++){
            AxisValue value = new AxisValue(Y2[i]);
            value.setLabel(Y[i]);
            values.add(value);
        }
        axisY.setValues(values);
       /* List<AxisValue> values=new ArrayList<>();
        for(int i=0;i<10;i=i+1)
        {
            AxisValue value=new AxisValue(i);
            value.setLabel(Y[i]);
            values.add(value);
        }
        axisY.setValues(values);*/
        axisY.setTextColor(Color.BLACK);
        axisY.setTextColor(Color.parseColor("#D6D6D9"));
        data1.setAxisYLeft(axisY);//Y轴设置在左边
        data1.setValueLabelsTextColor(Color.BLACK);

        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomEnabled(false);
        //lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        //lineChart.setMaxZoom((float)2);//缩放比例
        //lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data1);
        lineChart.setVisibility(View.VISIBLE);
        //lineChart.setScrollEnabled(true);
        //lineChart.setZoomEnabled(false);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 尼玛搞的老子好辛苦！！！见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         * 下面几句可以设置X轴数据的显示个数（x轴0-7个数据），当数据点个数小于（29）的时候，缩小到极致hellochart默认的是所有显示。当数据点个数大于（29）的时候，
         * 若不设置axisX.setMaxLabelChars(int count)这句话,则会自动适配X轴所能显示的尽量合适的数据个数。
         * 若设置axisX.setMaxLabelChars(int count)这句话,
         * 33个数据点测试，若 axisX.setMaxLabelChars(10);里面的10大于v.right= 7; 里面的7，则
         刚开始X轴显示7条数据，然后缩放的时候X轴的个数会保证大于7小于10
         若小于v.right= 7;中的7,反正我感觉是这两句都好像失效了的样子 - -!
         * 并且Y轴是根据数据的大小自动设置Y轴上限
         * 若这儿不设置 v.right= 7; 这句话，则图表刚开始就会尽可能的显示所有数据，交互性太差
         */
        Viewport v = new Viewport(0,lineChart.getMaximumViewport().height(),7,0);
        //
        lineChart.setCurrentViewport(v);


        Calendar f=Calendar.getInstance();
        int y=f.get(Calendar.YEAR);
        int m=f.get(Calendar.MONTH)+1;
        int d=f.get(Calendar.DATE);
        int z=(y-2018)*365+(m-1)*31+(d-1);
        lineChart.moveTo(z, 0);

    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables(){
        int x=0;
        for (int i = 0; i < 2; i++) {
            for (int j=0;j<12;j++)
            {
                for(int k=0;k<31;k++)
                {
                    x++;
                    mAxisXValues.add(new AxisValue(x).setLabel(Xline[i][j][k]));

                }
            }

        }
    }
    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(){
        int x=0;
        for (int i = 0; i < 2; i++) {
            for (int j=0;j<12;j++){
                for (int k=0;k<31;k++)
                {
                    x=x+1;
                    mPointValues.add(new PointValue(x, strength[i][j][k]));
                }
            }

        }
    }



    private void readdata(){

        SignData signData;

        for (int i=0;i<signData_list.size();i++)
        {
            signData=signData_list.get(i);
            year.add(signData.getYear());
            month.add(signData.getMonth());
            day.add(signData.getDay());
        }
    }

    private void setPoint(){
        int a,b,c;

        for(int i=0;i<2;i++)
        {
            for(int j=0;j<12;j++)
            {
                for(int k=0;k<31;k++)
                {
                    calendars[i][j][k]=0;
                    strength[i][j][k]=0;
                }
            }
        }
        for(int i=0;i<year.size();i++)
        {
            a=year.get(i)-2018;
            b=month.get(i)-1;
            c=day.get(i)-1;
            calendars[a][b][c]=1;
        }

        for(int i=0;i<2;i++)
        {
            for (int j=0;j<12;j++)
            {
                for(int k=0;k<31;k++)
                {
                    if(i==0&&j==0&&k==0)
                    {
                        if (calendars[0][0][0]==1)
                            strength[0][0][0]=6;
                        else
                            strength[0][0][0]=0;
                        break;
                    }
                    if(calendars[i][j][k]==1)
                    {
                        if(k==0&&(j==1||j==3||j==5||j==7||j==8||j==10))
                        {
                            strength[i][j][k]=strength[i][j-1][30]+6;
                            if (strength[i][j][k]>100)
                            {
                                strength[i][j][k]=100;
                            }
                        }

                        if (k==0&&(j==4||j==6||j==9||j==11))
                        {
                            strength[i][j][k]=strength[i][j-1][29]+6;
                            if (strength[i][j][k]>100)
                            {
                                strength[i][j][k]=100;
                            }
                        }

                        if (k==0&&j==2)
                        {
                            strength[i][j][k]=strength[i][j-1][27]+6;
                            if (strength[i][j][k]>100)
                            {
                                strength[i][j][k]=100;
                            }
                        }

                        if (k==0&&j==0)
                        {
                            strength[i][j][k]=strength[i-1][11][30]+6;
                            if (strength[i][j][k]>100)
                            {
                                strength[i][j][k]=100;
                            }
                        }

                        if (k!=0)
                        {
                            strength[i][j][k]=strength[i][j][k-1]+6;
                            if (strength[i][j][k]>100)
                            {
                                strength[i][j][k]=100;
                            }
                        }

                    }
                    if (calendars[i][j][k]==0)
                    {
                        if(k==0&&(j==1||j==3||j==5||j==7||j==8||j==10))
                        {
                            strength[i][j][k]=strength[i][j-1][30]-3;
                            if (strength[i][j][k]<0)
                            {
                                strength[i][j][k]=0;
                            }
                        }
                        if (k==0&&(j==4||j==6||j==9||j==11))
                        {
                            strength[i][j][k]=strength[i][j-1][29]-3;
                            if (strength[i][j][k]<0)
                            {
                                strength[i][j][k]=0;
                            }
                        }
                        if (k==0&&j==2)
                        {
                            strength[i][j][k]=strength[i][j-1][27]-3;
                            if (strength[i][j][k]<0)
                            {
                                strength[i][j][k]=0;
                            }
                        }
                        if (k==0&&j==0)
                        {
                            strength[i][j][k]=strength[i-1][j][30]-3;
                            if (strength[i][j][k]<0)
                            {
                                strength[i][j][k]=0;
                            }
                        }
                        if (k!=0)
                        {
                            strength[i][j][k]=strength[i][j][k-1]-3;
                            if (strength[i][j][k]<0)
                            {
                                strength[i][j][k]=0;
                            }
                        }
                    }

                }
            }
        }
    }

    private void setXline(){
        int i,j,k;
        String a,b,c;
        for(i=0;i<2;i++)
        {
            for(j=0;j<12;j++)
            {
                for(k=0;k<31;k++)
                {

                    Xline[i][j][k]=Integer.toString(j+1)+"/"+Integer.toString(k+1)+" ";
                    if (j==0&&k==0)
                    {
                        Xline[i][j][k]=Integer.toString(i+2018)+"/"+Integer.toString(j+1);
                    }
                }

            }

        }
    }
}

