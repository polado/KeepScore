package com.polado.score;

import android.content.DialogInterface;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;

    private Adapter adapter;

    private ArrayList<Player> players = new ArrayList<>();

    private ReadWriteData readWriteData;

    //    SQLite
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        readWriteData = new ReadWriteData(this, this.players);

//        SQLite
        databaseHandler = new DatabaseHandler(this);

        this.players = databaseHandler.getAllPlayers();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        float duration = Settings.Global.getFloat(getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE, 1.0f);
        adapter = new Adapter(players, this, duration);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

//                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                LayoutInflater layoutInflater = LayoutInflater.from(Home.this);
                final View editText = layoutInflater.inflate(R.layout.player_name, null);

                final EditText playerName = (EditText) editText.findViewById(R.id.player_name_et);

                final AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);
                dialog.setTitle("Add New Player")
                        .setMessage("Enter Player Name")
                        .setView(editText)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                                Player player = new Player(playerName.getText().toString().trim(), 0);

//                                SQLite
                                Player player = databaseHandler.addPlayer(new Player(playerName.getText().toString().trim(), 0));

                                players.add(player);

                                Log.i("Home", "add player size " + players.size());

                                adapter.notifyItemInserted(players.size());
                                recyclerView.scrollToPosition(players.size());
                                recyclerView.smoothScrollToPosition(players.size());
                                Log.i("Home", "2 add player size " + players.size());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                Snackbar.make(view, "Canceled", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                            }
                        });
//                dialog.show();
                final AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                playerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    }
                });
                playerName.requestFocus();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (players.size() > 0)
//            readWriteData.writeData();
//        else
//            readWriteData.writeEmpty();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        SQLite
//        this.players = databaseHandler.getAllPlayers();
//        adapter.notifyDataSetChanged();

//        if (players.size() == 0) {
//            ArrayList<Player> players = readWriteData.readData();
//            if (players != null) {
//                this.players = players;
//                adapter.notifyDataSetChanged();
//            }
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_clear_all:
                final AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);
                dialog.setTitle("Clear All")
                        .setMessage("Are you sure you want to clear all players? ")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                                for(int i =0;i<adapter.getItemCount(); i++) {
//                                    Adapter.ViewHolder viewHolder = (Adapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
//                                    adapter.slideToRight(viewHolder.itemView, i);
//                                }

//                                SQLite
                                for (Player p : players) {
                                    databaseHandler.deletePlayer(p);
                                }

                                players.clear();
//                                deleteFile("data");
                                adapter.notifyDataSetChanged();
                                Snackbar.make(getCurrentFocus(), "Cleared", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                Snackbar.make(getCurrentFocus(), "Canceled", Snackbar.LENGTH_LONG).show();
                            }
                        });
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
