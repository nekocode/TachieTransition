#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name(cn.nekocode.rs)

rs_allocation gTarget;
float alpha;

void init() {
}

/**
 * @author nekocode
 */
uchar4 __attribute__((kernel)) fade(uchar4 in, uint32_t x, uint32_t y) {
    //Convert input uchar4 to float4
    float4 inPixel = rsUnpackColor8888(in);
    float4 targetPixel = rsUnpackColor8888(rsGetElementAt_uchar4(gTarget, x, y));

    if (inPixel.a == 0) {
        inPixel.r = inPixel.g = inPixel.b = 0;
    }

    if (targetPixel.a == 0) {
        targetPixel.r = targetPixel.g = targetPixel.b = 0;
    }

    float4 outPixel = targetPixel * alpha + inPixel * (1.0f - alpha);
    return rsPackColorTo8888(outPixel);
}