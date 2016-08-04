# Tachie Transition
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)

使用 RenderScript 实现的立繪（Taichie）切换算法（常见于游戏中），原理是对每个像素进行源颜色和目标颜色的权重混合。

### Compare with the `ImageSwitcher`
![preview](art/cmp.gif)

`ImageSwitcher` 的切换实际上是借助两个 `ImageView` 完成的，对两个 `ImageView` 分别进行 FadeIn & FadeOut 动效实现图片间的切换。缺点在于像素的颜色并非是 **线性变化** 的，并且因为两个 `ImageView` 位置上处于前后的关系，所以第 `n` 和第 `n + 1` 次的切换效果并不能保持一致。

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
