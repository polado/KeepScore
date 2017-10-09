package com.polado.score;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by PolaDo on 8/13/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<Player> players;
    private Context context;
    private static float duration;

    private DatabaseHandler databaseHandler;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView tvPlayerName, tvPlayerScore;
        private Button btnAdd10, btnAdd5, btnAdd1, btnMinus1, btnClearAll;
        private ImageButton btnDeletePlayer;

        private ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

            tvPlayerName = (TextView) itemView.findViewById(R.id.player_name_tv);
            tvPlayerScore = (TextView) itemView.findViewById(R.id.player_score_tv);

            btnAdd10 = (Button) itemView.findViewById(R.id.add_10_btn);
            btnAdd5 = (Button) itemView.findViewById(R.id.add_5_btn);
            btnAdd1 = (Button) itemView.findViewById(R.id.add_1_btn);
            btnMinus1 = (Button) itemView.findViewById(R.id.minus_1_btn);
            btnClearAll = (Button) itemView.findViewById(R.id.clear_score_btn);
            btnDeletePlayer = (ImageButton) itemView.findViewById(R.id.delete_player_btn);
        }
    }

    public Adapter(ArrayList players, Context context, float duration) {
        Log.i("Adapter", "Adapter");
        this.players = players;
        this.context = context;
        this.duration = duration;
        this.databaseHandler = new DatabaseHandler(this.context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("Adapter", "onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.i("Adapter", "onBindViewHolder");
        final Player player = players.get(position);

        holder.cardView.post(new Runnable() {
            @Override
            public void run() {
                slideToTop(holder.cardView);
            }
        });

        holder.tvPlayerName.setText(player.getName());
        holder.tvPlayerScore.setText(String.valueOf(player.getScore()));

        final int p = position;

        holder.btnAdd10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int score = Integer.parseInt(holder.tvPlayerScore.getText().toString());
                score += 10;
                holder.tvPlayerScore.setText(String.valueOf(score));
                update(p, score, player.getName(), holder.tvPlayerScore);
            }
        });

        holder.btnAdd5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int score = Integer.parseInt(holder.tvPlayerScore.getText().toString());
                score += 5;
                holder.tvPlayerScore.setText(String.valueOf(score));
                update(p, score, player.getName(), holder.tvPlayerScore);
            }
        });

        holder.btnAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int score = Integer.parseInt(holder.tvPlayerScore.getText().toString());
                score += 1;
                holder.tvPlayerScore.setText(String.valueOf(score));
                update(p, score, player.getName(), holder.tvPlayerScore);
            }
        });

        holder.btnMinus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int score = Integer.parseInt(holder.tvPlayerScore.getText().toString());
                score -= 1;
                holder.tvPlayerScore.setText(String.valueOf(score));
                update(p, score, player.getName(), holder.tvPlayerScore);
            }
        });

        holder.btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvPlayerScore.setText(String.valueOf(0));
                update(p, 0, player.getName(), holder.tvPlayerScore);
            }
        });

        holder.btnDeletePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete Player")
                        .setMessage("Are you sure you want to delete this player?")
                        .setPositiveButton("To Hell With It!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                slideToRight(holder.itemView, position);
                            }
                        })
                        .setNegativeButton("Keep em around", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            }
                        });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    private void update(int position, int score, String name, View v) {
        updateFile(position, score, name);
        updateView(v);
    }

    private void updateFile(int position, int score, String name) {
        Player player = this.players.get(position);
        player.setScore(score);
        databaseHandler.updatePlayer(player);

//        ArrayList<Player> players = this.players;
//
//        players.set(position, new Player(name, score));
//        new ReadWriteData(context, players).writeData();
    }

    private void updateView(final View v) {
        final int centerX = (v.getLeft() + v.getRight()) / 2;
        final int centerY = (v.getTop() + v.getBottom()) / 2;
        final float radius = Math.max(v.getWidth(), v.getHeight()) * 2.0f;

        ViewAnimationUtils.createCircularReveal(
                v, // View
                centerX, // centerX
                centerY, // centerY
                0, // startRadius
                radius // endRadius
        ).setDuration(1000).start();
    }

    private static void slideToTop(View view) {
        view.setVisibility(View.INVISIBLE);
        TranslateAnimation animate = new TranslateAnimation(0, 0, view.getHeight(), 0);
        animate.setDuration((long) (duration * 500));
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    public void slideToRight(View view, final int p) {
        Log.i("slide", p+" slide "+getItemCount());
        TranslateAnimation animate = new TranslateAnimation(0, view.getWidth(), 0, 0);
        animate.setDuration((long) (duration * 500));
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                databaseHandler.deletePlayer(players.get(p));

                players.remove(p);
                notifyItemRemoved(p);
                notifyItemRangeChanged(p, players.size());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
        view.setVisibility(View.INVISIBLE);
    }

}
