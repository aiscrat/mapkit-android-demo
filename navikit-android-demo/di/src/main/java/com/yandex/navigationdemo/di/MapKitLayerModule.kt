package com.yandex.navigationdemo.di

import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.MapWindow
import com.yandex.mapkit.road_events_layer.RoadEventsLayer
import com.yandex.mapkit.road_events_layer.StyleProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class MapKitLayerModule {

    companion object {
        @Provides
        @ActivityScoped
        fun roadEventsLayer(
            mapWindow: MapWindow,
            styleProvider: StyleProvider
        ): RoadEventsLayer {
            return MapKitFactory.getInstance().createRouteRoadEventsLayer(
                mapWindow,
                styleProvider
            )
        }

        @Provides
        @ActivityScoped
        fun map(mapWindow: MapWindow): com.yandex.mapkit.map.Map = mapWindow.map
    }
}
