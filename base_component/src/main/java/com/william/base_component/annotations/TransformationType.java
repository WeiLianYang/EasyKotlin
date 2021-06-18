/*
 * Copyright WeiLianYang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.william.base_component.annotations;

import androidx.annotation.IntDef;

import static com.william.base_component.image.ILoaderInterfaceKt.TYPE_BLURRED;
import static com.william.base_component.image.ILoaderInterfaceKt.TYPE_BLURRED_RADIUS;
import static com.william.base_component.image.ILoaderInterfaceKt.TYPE_CIRCLE;
import static com.william.base_component.image.ILoaderInterfaceKt.TYPE_CIRCLE_BORDER;
import static com.william.base_component.image.ILoaderInterfaceKt.TYPE_NORMAL;
import static com.william.base_component.image.ILoaderInterfaceKt.TYPE_RADIUS;
import static com.william.base_component.image.ILoaderInterfaceKt.TYPE_RADIUS_NOT_CROP;
import static com.william.base_component.image.ILoaderInterfaceKt.TYPE_ROUND_RECT;

/**
 * @author William
 * @date 2020/4/24 16:44
 * Class Comment：glide transformation type
 */

@IntDef({TYPE_NORMAL,
        TYPE_CIRCLE,
        TYPE_CIRCLE_BORDER,
        TYPE_ROUND_RECT,
        TYPE_RADIUS,
        TYPE_RADIUS_NOT_CROP,
        TYPE_BLURRED,
        TYPE_BLURRED_RADIUS})
public @interface TransformationType {
}
