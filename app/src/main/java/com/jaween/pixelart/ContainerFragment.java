package com.jaween.pixelart;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaween.pixelart.tools.Tool;
import com.jaween.pixelart.ui.DrawingFragment;
import com.jaween.pixelart.ui.PaletteFragment;
import com.jaween.pixelart.ui.ToolboxFragment;

/**
 * Base container class for the main screen of the app: the drawing canvas and the tool panels.
 */
public class ContainerFragment extends Fragment implements
        PaletteFragment.OnColourSelectedListener,
        ToolboxFragment.OnToolSelectListener,
        DrawingFragment.OnDimensionsCalculatedListener {

    protected DrawingFragment drawingFragment;
    protected PaletteFragment paletteFragment;
    protected ToolboxFragment toolboxFragment;

    private static final String TAG_DRAWING_FRAGMENT = "drawing_fragment";
    private static final String TAG_PALETTE_FRAGMENT = "palette_fragment";
    private static final String TAG_TOOLBOX_FRAGMENT = "toolbox_fragment";

    private int colour;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_fragment, null);

        FragmentManager fragmentManager = getChildFragmentManager();
        drawingFragment = (DrawingFragment) fragmentManager.findFragmentByTag(TAG_DRAWING_FRAGMENT);
        paletteFragment = (PaletteFragment) fragmentManager.findFragmentByTag(TAG_PALETTE_FRAGMENT);
        toolboxFragment = (ToolboxFragment) fragmentManager.findFragmentByTag(TAG_TOOLBOX_FRAGMENT);

        if (drawingFragment == null | paletteFragment == null | toolboxFragment == null) {
            // Fragments don't yet exist, creates them
            drawingFragment = new DrawingFragment();
            paletteFragment = new PaletteFragment();
            toolboxFragment = new ToolboxFragment();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fl_container_drawing, drawingFragment, TAG_DRAWING_FRAGMENT);
            fragmentTransaction.add(R.id.fl_container_palette, paletteFragment, TAG_PALETTE_FRAGMENT);
            fragmentTransaction.add(R.id.fl_container_toolbox, toolboxFragment, TAG_TOOLBOX_FRAGMENT);
            fragmentTransaction.commit();
        }

        drawingFragment.setOnDimensionsCalculatedListener(this);
        toolboxFragment.setOnToolSelectListener(this);

        return view;
    }

    /**
     * Used to retrieve back-button presses
     * @return True if back consumed, false otherwise
     */
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onColourSelected(int colour, boolean done) {
        toolboxFragment.setColour(colour);
        this.colour = colour;
    }

    @Override
    public void onToolSelected(Tool tool, boolean done) {
        drawingFragment.setTool(tool);

        // When we switch tools, we must inform it of the current colour
        tool.getToolAttributes().getPaint().setColor(colour);
    }

    @Override
    public void onDimensionsCalculated(int width, int height) {
        toolboxFragment.setDimensions(width, height);
    }
}