package com.example.statisticsscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Map;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import retrofit2.CallAdapter;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Response;

import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static String TAG="Mariah";
    private int[] piechartData= {3,4,2,2,2};
   //private int[] yData= {70,210,140,60};
   PieChart pieChart;
    private static final String[] WEEKS = { "WEEK1", "WEEK2", "WEEK3", "WEEK4","WEEK5" };
    private static final String[] MONTHS= { "Jan", "FEB", "MAR", "APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC" };
    private static final String[] DAYS = { "DAY1", "DAY2", "DAY3", "DAY4","DAY5","DAY6","DAY7" };
    Button month ;
    Button year ;
    Button week ;
    ProgressBar donutProgress1,donutProgress2,donutProgress3;
    private BarChart barchart;
     private  BarDataSet set1,set2,set3,set4,set5;
     private BottomNavigationView bottomNavigationView;

            Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8099/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

            RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

    @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            getSupportActionBar().hide();
            Log.d(TAG,"ON CREATE");
            ///////////////////////////////////////////////
         month =(Button) findViewById(R.id.month_button);
         year =(Button) findViewById(R.id.year_button);
         week =(Button) findViewById(R.id.week_button);
        /////////////////////////////////
        pieChart=(PieChart) findViewById(R.id.PieChart4);
        pieChart.setRotationEnabled(true); // anable rotations
            pieChart.setHoleRadius(0f);//rdthe middle (not necessary)
            pieChart.setTransparentCircleAlpha(0);//Alpha goes from 0 to 255// pieChart.setCenterText("Total per category");
        Call <HashMap<String, Integer>>kidsactivebycategory=retrofitAPI.getActiveKidsOfCategory();
        kidsactivebycategory.enqueue(new Callback<HashMap<String, Integer>>() {
            @Override
            public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                int j =0;
                for (Integer i :response.body().values()) {
                    piechartData[j]=i.intValue();
                    j++;
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                Log.d(TAG,"PIECHART GET DATA FROM SPRING FAILLED"+t);

            }
        });


        addDataTothePieChart(piechartData);
            /// //////////////////////////////////////////////////////////////////
             donutProgress1 = (ProgressBar) findViewById(R.id.donut_progress1);
             donutProgress2 = (ProgressBar) findViewById(R.id.donut_progress2);
             donutProgress3 = (ProgressBar) findViewById(R.id.donut_progress3);
           addDataTotheparentprogress(2);
          addDataTothechildrenprogress (2);
          addDataTotheActivityrenprogress (2);
            //////////////////////////////////////////////////////////
        barchart =(BarChart) findViewById(R.id.CategoryBarChart);
        //barchart.setDescription("Annual trends");
        float groupSpace= 0.1f;
        float barSpace =0.02f;
        float barWidth = 0.1f;

        BarData data = createChartData();
        configureChartAppearance();
        prepareChartData(data);
        data.setBarWidth(barWidth);
        barchart.groupBars(1,groupSpace,barSpace);
        ///////////////////////////////////////////////////////////
       // final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
//        final Fragment fragment1 = new Users();
//        final Fragment fragment2 = new Leaders();
//        final Fragment fragment3 = new CoursesFragment();
//        final Fragment fragment4 = new HomeFragment();
//        final Fragment fragment5 = new MoreFragment();


        BottomNavigationView bnv = findViewById(R.id.bottomNav);
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem item){
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.homeFragment:
                        fragment = new HomeFragment();
                        break;

                    case R.id.usersFragment:
                        fragment = new UsersFragment();
                        break;


                    case R.id.leadersFragment:
                        fragment = new LeadersFragment();
                        break;

                    case R.id.coursesFragment:
                        fragment = new CoursesFragment();
                        break;

                    case R.id.moreFragment:
                        fragment = new MoreFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                //same as above:
                //getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                return true;
            }
        });

        }

    private void prepareChartData(BarData data) {
        data.setValueTextSize(0f);
        barchart.setData(data);
        barchart.invalidate();
    }

    private void configureChartAppearance() {
        barchart.getDescription().setEnabled(true);
        barchart.setDrawValueAboveBar(false);

        XAxis xAxis = barchart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(WEEKS));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis axisLeft = barchart.getAxisLeft();
        axisLeft.setGranularity(10f);
        axisLeft.setAxisMinimum(0);

        YAxis axisRight = barchart.getAxisRight();
        axisRight.setGranularity(10f);
        axisRight.setAxisMinimum(0);
    }

    private BarData createChartData() {
         int MAX_X_VALUE = 4;
         int MAX_Y_VALUE = 50;
         int MIN_Y_VALUE = 5;
         int j=5;
        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        ArrayList<BarEntry> values3 = new ArrayList<>();
        ArrayList<BarEntry> values4 = new ArrayList<>();
        ArrayList<BarEntry> values5 = new ArrayList<>();

         for(float i=0 ; i<WEEKS.length;i++)
         {
             values1.add(new BarEntry(i, j));
             values2.add(new BarEntry(i, 2*j));
             values3.add(new BarEntry(i, 3*j));
             values4.add(new BarEntry(i, 4*j));
             values5.add(new BarEntry(i, 5*j));
            j++;
         }
        set1 = new BarDataSet(values1, "CATEGORY1");
        set1.setColor(Color.GREEN);
        set2 = new BarDataSet(values2, "CATEGORY2");
        set2.setColor(Color.BLUE);

        set3 = new BarDataSet(values3, "CATEGORY3");
        set3.setColor(Color.YELLOW);

        set4 = new BarDataSet(values4, "CATEGORY4");
        set4.setColor(Color.RED);

        set5 = new BarDataSet(values5, "CATEGORY5");
        set5.setColor(Color.GRAY);

        BarData data = new BarData(set1,set2,set3,set4,set5);

        return data;
    }
   // ADD DATA TO THE PIECHART
    private void addDataTothePieChart(int [] yData)
    {
        Log.d(TAG,"addDataTothePieChart");
        ArrayList<PieEntry> yDataLine = new ArrayList<>();
        //now we will enter the data
        for(int i=0 ; i<yData.length;i++)
        {
            yDataLine.add (new PieEntry(yData[i],i));

        }
        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yDataLine,"Total per category ");
        pieDataSet.setSliceSpace(5);// the size of the space between the categories
        pieDataSet.setValueTextSize(20);
       pieDataSet.setColors(Color.RED, Color.GREEN, Color.BLUE,Color.GRAY,Color.YELLOW);
        // ADD LEGEND
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
//         legend.setPosition(Legend.LegendPosition.LEFT_TO_CHART);
        //create pie data set
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
    // ACTIVITIES PROGRESS BAR
    private void addDataTotheActivityrenprogress(int number)
    {
        TextView activitytext = (TextView) findViewById( R.id.ACTIVITYPrograsbartext);
       // if number =1 so it is per week
        if(number==1)
        {
            Call <HashMap<String, Integer>> activitybyweek = retrofitAPI.getActivitiesInHoursPerWeek();
            activitybyweek.enqueue(new Callback<HashMap<String, Integer>>() {
                double totalActivity,activeActivity;
                @Override
                public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                    totalActivity=response.body().get("totalTime").doubleValue();
                    activeActivity=response.body().get("activityTime").doubleValue();
                    activitytext.setText(response.body().get("activityTime").toString()+" "+(int)((activeActivity/totalActivity)*100)+"%");
                    donutProgress1.setProgress ((int)((activeActivity/totalActivity)*100));

                }

                @Override
                public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                    Log.d(TAG,"ACTIVITY NUMBER PER WEEK FAILLED");

                }
            });

        }
        // if number =2 so it is per MONTH
        if(number==2)
        {
            Call <HashMap<String, Integer>> activitybymonth = retrofitAPI.getActivitiesInHoursPerMonth();
            activitybymonth.enqueue(new Callback<HashMap<String, Integer>>() {
                double totalActivity,activeActivity;
                @Override
                public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                    totalActivity=response.body().get("totalTime").doubleValue();
                    activeActivity=response.body().get("activityTime").doubleValue();
                    activitytext.setText(response.body().get("activityTime").toString()+"  "+(int)((activeActivity/totalActivity)*100)+"%");
                    donutProgress1.setProgress ((int)((activeActivity/totalActivity)*100));

                }

                @Override
                public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                    Log.d(TAG,"ACTIVITY NUMBER PER MONTH FAILLED");

                }
            });

        }
        // if number =3 so it is per YEAR
        if(number==3)
        {
            Call <HashMap<String, Integer>> activitybyyear = retrofitAPI.getActivitiesInHoursPerYear();
            activitybyyear.enqueue(new Callback<HashMap<String, Integer>>() {
                double totalActivity,activeActivity;
                @Override
                public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                    totalActivity=response.body().get("totalTime").doubleValue();
                    activeActivity=response.body().get("activityTime").doubleValue();
                    activitytext.setText(response.body().get("activityTime").toString()+"  "+(int)((activeActivity/totalActivity)*100)+"%");
                    donutProgress1.setProgress ((int)((activeActivity/totalActivity)*100));
                }

                @Override
                public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                    Log.d(TAG,"ACTIVITY NUMBER PER MONTH FAILLED");

                }
            });

        }
    }
    // PARENT PROGRASS
    private void addDataTotheparentprogress(int number)
    {
        TextView parenttext = (TextView) findViewById(R.id.parentPrograsbartext);
        // number =1 so we are on week
        if(number==1)
        {     Call <HashMap<String, Integer>> parentbyweek = retrofitAPI.getAllActiveParentsByWeek();
            parentbyweek.enqueue(new Callback<HashMap<String, Integer>>() {
                @Override
                public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                    String text =response.body().get("New Parents").toString();
                    Call <Integer>parentpecentbyweek=retrofitAPI. getPercentActiveParentsByWeek();
                    parentpecentbyweek.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            donutProgress2.setProgress(response.body().intValue());
                            parenttext.setText(text+ " "+ response.body().intValue()+ "%");

                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                           Log.d(TAG,"PARENT PRECENT PER WEEK FAILLED");
                        }
                    });
                }

                @Override
                public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                    Log.d(TAG,"PARENT NUMBER PER WEEK FAILLED");

                }
            });
        }
        // if number =2 so it is per month
        if(number==2)
        {
            Call <HashMap<String, Integer>> parentbymonth = retrofitAPI.getAllActiveParentsByMonth();
            parentbymonth.enqueue(new Callback<HashMap<String, Integer>>() {
                @Override
                public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                    String text =response.body().get("New Parents").toString();
                    Call <Integer>parentpecentbymonth=retrofitAPI. getPercentActiveParentsByMonth();
                    parentpecentbymonth.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            donutProgress2.setProgress(response.body().intValue());
                            parenttext.setText(text+ " "+ response.body().intValue()+ "%");
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.d(TAG,"PARENT PRECENT PER MONTH FAILLED");

                        }
                    });
                }

                @Override
                public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                    Log.d(TAG,"PARENT NUMBER PER MONTH FAILLED");

                }
            });
        }
        // IF NUMBER =3 SO IT IS PER YEAR
        if(number==3)
        {
            Call <HashMap<String, Integer>> parentbyyear = retrofitAPI.getAllActiveParentsByYear();
            parentbyyear.enqueue(new Callback<HashMap<String, Integer>>() {
                @Override
                public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                    String text =response.body().get("New Parents").toString();
                    Call <Integer>parentpecentbyyear=retrofitAPI. getPercentActiveParentsByYear();
                    parentpecentbyyear.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            donutProgress2.setProgress(response.body().intValue());
                            parenttext.setText(text+ " "+ response.body().intValue()+ "%");

                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.d(TAG,"PARENT PERCENT PER YEAR FAILLED");

                        }
                    });
                }

                @Override
                public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                    Log.d(TAG,"PARENT NUMBER PER YEAR FAILLED");

                }
            });

        }
    }
    // CHILDREN PROGRASS BAR
    private void addDataTothechildrenprogress(int number)
    {
        TextView childrenprograsstext =(TextView) findViewById(R.id.childrenPrograsbartext);
        // if number =1 so it is per week
        if(number ==1) {
            Call<HashMap<String, Integer>> childrenbyweek = retrofitAPI.getAllActiveKidsByWeek();
            childrenbyweek.enqueue(new Callback<HashMap<String, Integer>>() {
                @Override
                public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                    String text=response.body().get("New Kids").toString();
                    Call<Integer> childrenpecentbyweek = retrofitAPI.getPercentActiveKidsByWeek();
                    childrenpecentbyweek.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            donutProgress3.setProgress(response.body().intValue());
                            childrenprograsstext.setText(text+"  "+response.body().intValue()+"%");
                            Log.d(TAG,response.body().toString());

                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.d(TAG, "CHILDREN PERCENT PER WEEK FAILLED");

                        }
                    });


                }

                @Override
                public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                    Log.d(TAG, "CHILDREN NUMBER PER WEEK FAILLED");

                }
            });
        }
            // IF NUMBER = 2 SO IT IS PER MONTH
            if(number==2)
            {
                Call<HashMap<String, Integer>> childrenbymonth = retrofitAPI.getAllActiveKidsByMonth();
                childrenbymonth.enqueue(new Callback<HashMap<String, Integer>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                        String text=response.body().get("New Kids").toString();
                        Call<Integer> childrenpecentbymonth = retrofitAPI.getPercentActiveKidsPerMonth();
                        childrenpecentbymonth.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                donutProgress3.setProgress(response.body().intValue());
                                childrenprograsstext.setText(text+"  "+response.body().intValue()+"%");


                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Log.d(TAG, "CHILDREN PERCENT PER MONTH FAILLED");

                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                        Log.d(TAG, "CHILDREN NUMBER PER MONTH FAILLED");

                    }
                });

            }
            // IF NUMBER = 3 SO IT IS PER YEAR
        if (number ==3)
        {
            Call<HashMap<String, Integer>> childrenbyyear = retrofitAPI.getAllActiveKidsByYear();
            childrenbyyear.enqueue(new Callback<HashMap<String, Integer>>() {
                @Override
                public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                    String text=response.body().get("New Kids").toString();
                    Call<Integer> childrenpecentbyyear = retrofitAPI.getPercentActiveKidsByYear();
                    childrenpecentbyyear.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            donutProgress3.setProgress(response.body().intValue());
                            childrenprograsstext.setText(text+"  "+response.body().intValue()+"%");


                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.d(TAG, "CHILDREN PERCENT PER YEAR FAILLED");

                        }
                    });

                }

                @Override
                public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                    Log.d(TAG, "CHILDREN NUMBER PER YEAR FAILLED");

                }
            });

        }
    }
    public void monthonClick(View view) {
        addDataTotheparentprogress(2);
        addDataTothechildrenprogress (2);
        addDataTotheActivityrenprogress (2);
        //PIECHART
//          int[] piechartData= new int [5];
//        Call <HashMap<String, Integer>> Activities = retrofitAPI.getActiveKidsOfCategoryByMonth();
//        Activities.enqueue(new Callback<HashMap<String, Integer>>() {
//            @Override
//            public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
//                int j =0;
//                for (Integer i :response.body().values()) {
//                    piechartData[j]=i.intValue();
//                    j++;
//                }
//                addDataTothePieChart(piechartData);
//
//            }
//
//            @Override
//            public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
//                 Log.d(TAG ,"ACTIVTIES PER MONTH FAILLED");
//            }
//        });

    }

    public void weekonClick(View view)
    {
        addDataTotheparentprogress(1);
        addDataTothechildrenprogress(1);
        addDataTotheActivityrenprogress(1);
        //PIECHART
//        int[] piechartData= new int [5];
//        Call <HashMap<String, Integer>> Activities = retrofitAPI.getActiveKidsOfCategoryByWeek();
//        Activities.enqueue(new Callback<HashMap<String, Integer>>() {
//            @Override
//            public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
//                int j =0;
//                for (Integer i :response.body().values()) {
//                    piechartData[j]=i.intValue();
//                    j++;
//                }
//                addDataTothePieChart(piechartData);
//
//            }
//
//            @Override
//            public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
//                Log.d(TAG ,"ACTIVTIES PER WEEK FAILLED");
//            }
//        });
    }

    public void yearonClick(View view) {
        addDataTotheparentprogress(3);
        addDataTothechildrenprogress(3);
        addDataTotheActivityrenprogress(3);
        //PIECHART
//        int[] piechartData= new int [5];
//        Call <HashMap<String, Integer>> Activities = retrofitAPI.getActiveKidsOfCategoryByYear();
//        Activities.enqueue(new Callback<HashMap<String, Integer>>() {
//            @Override
//            public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
//                int j =0;
//                for (Integer i :response.body().values()) {
//                    piechartData[j]=i.intValue();
//                    j++;
//                }
//                addDataTothePieChart(piechartData);
//
//            }
//
//            @Override
//            public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
//                Log.d(TAG ,"ACTIVTIES PER YEAR FAILLED");
//            }
//        });

    }

}