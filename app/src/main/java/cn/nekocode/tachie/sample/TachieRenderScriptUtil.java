package cn.nekocode.tachie.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;

import cn.nekocode.rs.ScriptC_Tachie;

/**
 * Created by nekocode on 16/8/3.
 */
public final class TachieRenderScriptUtil {
    private TachieRenderScriptUtil(){
    }

    public static Bitmap trans(Bitmap source, Bitmap target, float alpha, Context context) {
        // Create renderscript
        RenderScript rs = RenderScript.create(context);

        // Create allocation from Bitmap
        Allocation allocationSource = Allocation.createFromBitmap(rs, source);
        Allocation allocationTarget = Allocation.createFromBitmap(rs, target);
        Allocation allocationOut = Allocation.createTyped(rs, allocationSource.getType());

        // Create script
        ScriptC_Tachie script = new ScriptC_Tachie(rs);

        // Set global parameters
        script.set_alpha(alpha);
        script.set_gTarget(allocationTarget);

        // Call script for output allocation
        script.forEach_fade(allocationSource, allocationOut);

        // Copy script result into bitmap
        Bitmap outBitmap = source.copy(source.getConfig(), true);
        allocationOut.copyTo(outBitmap);

        // Destroy everything to free memory
        allocationSource.destroy();
        allocationTarget.destroy();
        allocationOut.destroy();
        script.destroy();
        rs.destroy();

        return outBitmap;
    }
}
