/*
 * Copyright (C) 2019 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package com.zhuzichu.mvvm.view.ribbon

import android.content.res.Resources

/** dp size to px size */
internal fun Float.dp2px(resources: Resources): Int {
    val scale = resources.displayMetrics.density
    return (this * scale * 0.5f).toInt()
}

/** sp size to px size */
internal fun Float.sp2px(resources: Resources): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (this * scale * 0.5f).toInt()
}
