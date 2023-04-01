package com.example.magic.screens;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.magic.GameApplication;
import com.example.magic.R;
import com.example.magic.services.MediaManager;

public class GameView extends FrameLayout {

    private static final int EDGE_MARGIN_X = 200;
    private static final int EDGE_MARGIN_Y = 50;

    private ImageView player;

    private TextView dialog;

    private int viewHeight;

    private int viewWidth;

    private EdgeCallback edgeCallback;

    private ViewPropertyAnimator movementAnimation;

    private ViewPropertyAnimator rotateAnimation;

    private FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

    private FrameLayout.LayoutParams lpDialog = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

    private MediaManager mediaManager;

    private Animator.AnimatorListener listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(@NonNull Animator animator) {
            mediaManager.startSteps();
        }

        @Override
        public void onAnimationEnd(@NonNull Animator animator) {
            mediaManager.stopSteps();
        }

        @Override
        public void onAnimationCancel(@NonNull Animator animator) {
            mediaManager.stopSteps();
        }

        @Override
        public void onAnimationRepeat(@NonNull Animator animator) {
            //
        }
    };

    public void setEdgeCallback(EdgeCallback edgeCallback) {
        this.edgeCallback = edgeCallback;
    }

    public GameView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mediaManager = ((GameApplication) context.getApplicationContext()).getMediaManager();
        player = new ImageView(context, attrs);
        player.setLayoutParams(lp);
        player.setImageResource(R.drawable.player);

        int playerHeight = Math.round(722 * 0.7f);
        int playerWidth = Math.round(324 * 0.7f);

        player.getLayoutParams().height = playerHeight;
        player.getLayoutParams().width = playerWidth;

        dialog = new TextView(context, attrs);
        dialog.setLayoutParams(lpDialog);
        dialog.setText("sokmsdomdsoimoisdmoidfsmnoisdfsok ");

        int padding = (int) context.getResources().getDimension(R.dimen.padding12);
        dialog.setMaxHeight(250);
        dialog.setMaxWidth(400);
        dialog.setBackground(context.getResources().getDrawable(R.drawable.bg_rounded, context.getTheme()));
        dialog.setPadding(padding, padding, padding, padding);

        addView(player);
        addView(dialog);
    }

    public void positionPlayerLeft() {
        cancelMovementAnimation();
        player.setX(EDGE_MARGIN_X);
        player.setY(viewHeight - player.getHeight() - EDGE_MARGIN_Y);

        smoothRotate(true);
        edgeCallback.leftEnabled(true);
        edgeCallback.rightEnabled(false);
    }

    public void positionPlayerRight() {
        cancelMovementAnimation();
        player.setX(viewWidth - EDGE_MARGIN_X - player.getWidth());
        player.setY(viewHeight - player.getHeight() - EDGE_MARGIN_Y);
        smoothRotate(false);
        edgeCallback.rightEnabled(true);
        edgeCallback.leftEnabled(false);
    }

    public void smoothRotate(boolean left) {
        float rotation = 0f;
        if (!left) {
            rotation = -180f;
        }

        cancelRotateAnimation();
        rotateAnimation = player.animate()
                .rotationY(rotation)
                .setDuration(100);
        rotateAnimation.start();
    }

    private void cancelRotateAnimation() {
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
        }
    }

    private void cancelMovementAnimation() {
        if (movementAnimation != null) {
            movementAnimation.cancel();
        }
    }

    float maxHeight;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewHeight = getHeight();
        viewWidth = getWidth();
        positionPlayerLeft();

        Log.d("TAG", "GameView: " + viewHeight);
        maxHeight = 0.90f * viewHeight;
        setOnTouchListener(
                new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            float y = motionEvent.getY();
                            if (y < maxHeight) {
                                y = maxHeight;
                            }

                            move(motionEvent.getX() - player.getWidth() / 2f, y - player.getHeight());

                            edgeCallback.rightEnabled(motionEvent.getX() + player.getWidth() >= viewWidth - EDGE_MARGIN_X * 1.5);

                            edgeCallback.leftEnabled(motionEvent.getX() <= EDGE_MARGIN_X * 1.5);

                        }
                        return false;
                    }
                }
        );
    }

    public void move(float x, float y) {
        float centerPlayer = player.getX() + player.getWidth() / 2f;

        boolean left = x >= centerPlayer;
        smoothRotate(left);

        movementAnimation = player.animate()
                .x(x)
                .y(y)
                .setDuration(1000);

        movementAnimation.setListener(listener);
        movementAnimation.start();
    }

    public void npcMove(float x, float y, float width, float height) {
        float centerPlayer = player.getX() + player.getWidth() / 2f;

        boolean left = x >= centerPlayer;

        smoothRotate(left);

        // Если ближайшая сторона npc - правая
        if (!left) {
            x += width;
        } else {
            x -= player.getWidth() - 25;
        }

        movementAnimation = player.animate()
                .x(x)
                .y(y)
                .setDuration(1000);

        movementAnimation.setListener(listener);
        movementAnimation.start();
    }
}