# Tachie Transition
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)

使用 RenderScript 实现立繪切换算法（常见于游戏中），并与系统自带的 ImageSwitcher 的图片切换（两个 ImageView 相继 fadeIn、fadeOut）做比较。

### Comparison
![preview](art/cmp.gif)

### RenderScript
```c
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
```
