package com.example.magic.screens;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

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
import com.example.magic.models.action.Action;
import com.example.magic.models.action.AddToInventory;
import com.example.magic.models.action.NextLevelAction;
import com.example.magic.models.action.NpcMessage;
import com.example.magic.models.action.RemoveFromInventory;
import com.example.magic.models.action.UserMessage;
import com.example.magic.services.MediaManager;
import com.example.magic.services.StorageManager;

import java.util.ArrayList;
import java.util.List;

public class GameView extends FrameLayout {

    private static final int EDGE_MARGIN_X = 200;
    private static final int EDGE_MARGIN_Y = 50;

    private ImageView player;

    private boolean firstLayout = true;

    private TextView dialog;

    private int viewHeight;

    private int viewWidth;

    private ViewPropertyAnimator movementAnimation;

    private ViewPropertyAnimator rotateAnimation;

    private ViewPropertyAnimator dialogAnimation;

    private FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

    private FrameLayout.LayoutParams lpDialog = new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

    private MediaManager mediaManager;

    private StorageManager storageManager;

    private List<Action> actions = new ArrayList<>();

    private Runnable addToInventoryAction;

    public void setAddToInventoryAction(Runnable addToInventoryAction) {
        this.addToInventoryAction = addToInventoryAction;
    }

    public boolean isActionInProgress() {
        return currentAction < actions.size();
    }

    private int currentAction = 0;

    public GameView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mediaManager = ((GameApplication) context.getApplicationContext()).getMediaManager();
        storageManager = ((GameApplication) context.getApplicationContext()).getStorageManager();
        player = new ImageView(context, attrs);
        player.setLayoutParams(lp);
        player.setImageResource(R.drawable.player);

        int playerHeight = Math.round(722 * 0.7f);
        int playerWidth = Math.round(324 * 0.7f);

        player.getLayoutParams().height = playerHeight;
        player.getLayoutParams().width = playerWidth;

        dialog = new TextView(context, attrs);
        dialog.setLayoutParams(lpDialog);

        int padding = (int) context.getResources().getDimension(R.dimen.padding12);
        dialog.setMaxHeight(250);
        dialog.setMaxWidth(400);
        dialog.setBackground(context.getResources().getDrawable(R.drawable.bg_rounded, context.getTheme()));
        dialog.setPadding(padding, padding, padding, padding);
        dialog.setVisibility(GONE);

        addView(player);
        addView(dialog);
    }

    public void positionPlayerLeft() {
        cancelMovementAnimation();
        player.setX(EDGE_MARGIN_X);
        player.setY(viewHeight - player.getHeight() - EDGE_MARGIN_Y);

        smoothRotate(true);
    }

    public void moveToLeftLocation(Runnable onMoved) {
        move(0f + EDGE_MARGIN_X, player.getY(), onMoved);
    }

    public void moveToRightLocation(Runnable onMoved) {
        move(viewWidth - EDGE_MARGIN_X - player.getWidth(), player.getY(), onMoved);
    }

    public void positionPlayerRight() {
        cancelMovementAnimation();
        player.setX(viewWidth - EDGE_MARGIN_X - player.getWidth());
        player.setY(viewHeight - player.getHeight() - EDGE_MARGIN_Y);
        smoothRotate(false);
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
        rotateAnimation = null;
    }

    private void cancelMovementAnimation() {
        if (movementAnimation != null) {
            movementAnimation.cancel();
        }
        movementAnimation = null;
    }

    float maxHeight;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewHeight = getHeight();
        viewWidth = getWidth();
        if (firstLayout) {
            positionPlayerLeft();
        }
        firstLayout = false;

        maxHeight = 0.90f * viewHeight;
        setOnTouchListener(
                new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            if (isActionInProgress()) {
                                play();
                                return false;
                            }
                            actions = new ArrayList<>();
                            currentAction = 0;
                            dialog.setVisibility(GONE);
                            float y = motionEvent.getY();
                            if (y < maxHeight) {
                                y = maxHeight;
                            }

                            move(motionEvent.getX() - player.getWidth() / 2f, y - player.getHeight(), () -> {
                            });
                        }
                        return false;
                    }
                }
        );
    }

    public void setUpActions(List<Action> actions) {
        if (this.actions.isEmpty()) {
            this.actions = actions;
            currentAction = 0;
            play();
        }
    }

    private void play() {
        dialog.setVisibility(GONE);
        Action action = actions.get(currentAction);
        if (action instanceof UserMessage) {
            UserMessage message = ((UserMessage) action);
            displayPlayerMessage(message.getText(), message.getNpcCenter());
        } else if (action instanceof NpcMessage) {
            NpcMessage message = ((NpcMessage) action);
            displayNpcMessage(message.getText(), message.getX(), message.getY());
        } else if (action instanceof NextLevelAction) {
            // отображать надпись новый уровень
            storageManager.nextLevel();
        } else if (action instanceof AddToInventory) {
            storageManager.addToInventory(((AddToInventory) action).getItem());
            if (addToInventoryAction != null) {
                addToInventoryAction.run();
            }
        } else if (action instanceof RemoveFromInventory) {
            storageManager.removeForInventory(((RemoveFromInventory) action).getItem());
        }
        currentAction++;
    }

    public void displayPlayerMessage(String text, float npcX) {
        float x = player.getX() + player.getWidth();
        float y = player.getY();

        dialog.setText(text);
        dialog.setX(x);
        dialog.setY(y);

        if (dialogAnimation != null) {
            dialogAnimation.cancel();
        }

        dialog.setAlpha(0f);
        dialog.setVisibility(VISIBLE);
        dialogAnimation = dialog.animate()
                .setDuration(1000)
                .alpha(1f);
        dialogAnimation.start();
    }

    public void displayNpcMessage(String text, float x, float y) {
        dialog.setText(text);
        dialog.setX(x);
        dialog.setY(y);

        if (dialogAnimation != null) {
            dialogAnimation.cancel();
        }
        dialog.setAlpha(0f);
        dialog.setVisibility(VISIBLE);
        dialogAnimation = dialog.animate()
                .setDuration(1000)
                .alpha(1f);
        dialogAnimation.start();
    }

    public void move(float x, float y, Runnable onMoved) {
        Log.d("TAG", "move: ");
        if (dialogAnimation != null) {
            dialogAnimation.cancel();
        }
        float centerPlayer = player.getX() + player.getWidth() / 2f;

        boolean left = x >= centerPlayer;
        smoothRotate(left);

        movementAnimation = player.animate()
                .x(x)
                .y(y)
                .setDuration(1000)
                .withStartAction(() -> {
                    mediaManager.startSteps();
                })
                .withEndAction(() -> {
                    mediaManager.stopSteps();
                    onMoved.run();
                });
        movementAnimation.start();
    }

    public void npcMove(float x, float y, float width, float height, Runnable onMoved) {
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
                .setDuration(1000)
                .withStartAction(() -> {
                    mediaManager.startSteps();
                })
                .withEndAction(() -> {
                    mediaManager.stopSteps();
                    onMoved.run();
                });

        movementAnimation.start();
    }
}
